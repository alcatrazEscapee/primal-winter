package com.alcatrazescapee.primalwinter;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import com.alcatrazescapee.primalwinter.platform.ForgeConfig;
import com.alcatrazescapee.primalwinter.platform.NetworkSetupCallback;
import com.alcatrazescapee.primalwinter.platform.XPlatform;
import com.alcatrazescapee.primalwinter.util.EventHandler;
import com.alcatrazescapee.primalwinter.world.PrimalWinterFeatures;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.world.BiomeGenerationSettingsBuilder;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ClimateSettingsBuilder;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

@Mod(PrimalWinter.MOD_ID)
public final class ForgePrimalWinter
{
    public static final ModContainer MOD = ModList.get()
        .getModContainerById(PrimalWinter.MOD_ID)
        .orElseThrow();
    public static final IEventBus EVENT_BUS = Objects.requireNonNull(MOD.getEventBus());
    public static final ForgeConfig CONFIG = (ForgeConfig) XPlatform.INSTANCE.config();

    public static final DeferredRegister<MapCodec<? extends BiomeModifier>> BIOME_MODIFIERS = DeferredRegister.create(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, PrimalWinter.MOD_ID);
    public static final Supplier<MapCodec<? extends Instance>> CODEC = BIOME_MODIFIERS.register("instance", () -> RecordCodecBuilder.mapCodec(instance -> instance.group(
        PlacedFeature.LIST_CODEC.fieldOf("surface_structures").forGetter(c -> c.surfaceStructures),
        PlacedFeature.LIST_CODEC.fieldOf("top_layer_modification").forGetter(c -> c.topLayerModification)
    ).apply(instance, Instance::new)));

    public ForgePrimalWinter()
    {
        PrimalWinter.earlySetup();

        EVENT_BUS.addListener((FMLCommonSetupEvent event) -> PrimalWinter.lateSetup());
        EVENT_BUS.addListener((RegisterPayloadHandlersEvent event) -> {
            final PayloadRegistrar register = event.registrar(ModList.get().getModFileById(PrimalWinter.MOD_ID).versionString());
            PrimalWinter.networkingSetup(new NetworkSetupCallback() {
                @Override
                public <T extends CustomPacketPayload> void registerS2C(CustomPacketPayload.Type<T> type, StreamCodec<ByteBuf, T> codec, Consumer<T> handler)
                {
                    register.playToClient(type, codec, (payload, context) -> context.enqueueWork(() -> handler.accept(payload)));
                }
            });
        });
        EVENT_BUS.addListener((ModConfigEvent.Loading event) -> CONFIG.updateCaches());
        EVENT_BUS.addListener((ModConfigEvent.Reloading event) -> CONFIG.updateCaches());
        BIOME_MODIFIERS.register(EVENT_BUS);

        NeoForge.EVENT_BUS.addListener((RegisterCommandsEvent event) -> EventHandler.registerCommands(event.getDispatcher()));
        NeoForge.EVENT_BUS.addListener((LevelEvent.Load event) -> EventHandler.setLevelToThunder(event.getLevel()));
        NeoForge.EVENT_BUS.addListener((PlayerEvent.PlayerLoggedInEvent event) -> {
            if (event.getEntity() instanceof ServerPlayer player)
            {
                EventHandler.onPlayerJoinWorld(player);
            }
        });

        final ForgeConfig config = (ForgeConfig) XPlatform.INSTANCE.config();

        MOD.registerConfig(ModConfig.Type.COMMON, config.common);
        MOD.registerConfig(ModConfig.Type.CLIENT, config.client);

        if (FMLLoader.getDist() == Dist.CLIENT)
        {
            ForgePrimalWinterClient.setupClient();
        }
    }

    public record Instance(
        HolderSet<PlacedFeature> surfaceStructures,
        HolderSet<PlacedFeature> topLayerModification
    )
        implements BiomeModifier
    {
        @Override
        public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder)
        {
            if (biome.unwrapKey().filter(XPlatform.INSTANCE.config()::isWinterBiome).isEmpty() || phase != Phase.MODIFY)
            {
                return;
            }

            final ClimateSettingsBuilder climate = builder.getClimateSettings();
            climate.setHasPrecipitation(true);
            climate.setTemperature(-0.5f);
            climate.setTemperatureModifier(Biome.TemperatureModifier.NONE);

            builder.getSpecialEffects()
                .waterColor(0x3938C9)
                .waterFogColor(0x050533);

            final BiomeGenerationSettingsBuilder settings = builder.getGenerationSettings();
            for (Holder<PlacedFeature> feature : surfaceStructures)
            {
                settings.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, feature);
            }

            settings.getFeatures(GenerationStep.Decoration.TOP_LAYER_MODIFICATION)
                .removeIf(holder -> holder.unwrapKey().map(key -> key == MiscOverworldPlacements.FREEZE_TOP_LAYER).orElse(false));
            for (Holder<PlacedFeature> feature : topLayerModification)
            {
                settings.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, feature);
            }

            PrimalWinterFeatures.addSpawns(builder.getMobSpawnSettings()::addSpawn);
        }

        @Override
        public MapCodec<? extends BiomeModifier> codec()
        {
            return CODEC.get();
        }
    }
}
