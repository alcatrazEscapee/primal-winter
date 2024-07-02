package com.alcatrazescapee.primalwinter.data;

import java.util.function.Function;
import java.util.function.Supplier;
import com.alcatrazescapee.primalwinter.PrimalWinter;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import static com.alcatrazescapee.primalwinter.blocks.PrimalWinterBlocks.*;

public final class BuiltinModels extends BlockStateProvider
{
    public BuiltinModels(GatherDataEvent event)
    {
        super(event.getGenerator().getPackOutput(), PrimalWinter.MOD_ID, event.getExistingFileHelper());
    }

    @Override
    protected void registerStatesAndModels()
    {
        snowyBlock(SNOWY_DIRT);
        snowyBlock(SNOWY_COARSE_DIRT);
        snowyBlock(SNOWY_SAND);
        snowyBlock(SNOWY_RED_SAND);
        snowyBlock(SNOWY_GRAVEL);
        snowyBlock(SNOWY_MUD);
        snowyBlock(SNOWY_STONE);
        snowyBlock(SNOWY_GRANITE);
        snowyBlock(SNOWY_ANDESITE);
        snowyBlock(SNOWY_DIORITE);
        snowyBlock(SNOWY_WHITE_TERRACOTTA);
        snowyBlock(SNOWY_ORANGE_TERRACOTTA);
        snowyBlock(SNOWY_TERRACOTTA);
        snowyBlock(SNOWY_YELLOW_TERRACOTTA);
        snowyBlock(SNOWY_BROWN_TERRACOTTA);
        snowyBlock(SNOWY_RED_TERRACOTTA);
        snowyBlock(SNOWY_LIGHT_GRAY_TERRACOTTA);
        simpleBlockWithItem(SNOWY_DIRT_PATH, models()
            .withExistingParent(name(SNOWY_DIRT_PATH), "block/dirt_path")
            .texture("side", mcLoc("block/snow"))
            .texture("top", modLoc("block/snowy_dirt_path_side")),
            model -> ConfiguredModel.allYRotations(model, 0, false));
        logBlock(SNOWY_OAK_LOG);
        logBlock(SNOWY_BIRCH_LOG);
        logBlock(SNOWY_SPRUCE_LOG);
        logBlock(SNOWY_JUNGLE_LOG);
        logBlock(SNOWY_DARK_OAK_LOG);
        logBlock(SNOWY_ACACIA_LOG);
        logBlock(SNOWY_CHERRY_LOG);
        logBlock(SNOWY_MANGROVE_LOG);
        leavesBlock(SNOWY_OAK_LEAVES);
        leavesBlock(SNOWY_BIRCH_LEAVES);
        leavesBlock(SNOWY_SPRUCE_LEAVES);
        leavesBlock(SNOWY_JUNGLE_LEAVES);
        leavesBlock(SNOWY_DARK_OAK_LEAVES);
        leavesBlock(SNOWY_ACACIA_LEAVES);
        leavesBlock(SNOWY_CHERRY_LEAVES);
        leavesBlock(SNOWY_MANGROVE_LEAVES);
        simpleBlockWithItem(SNOWY_MANGROVE_ROOTS.get(), models()
            .withExistingParent(name(SNOWY_MANGROVE_ROOTS), "block/mangrove_roots")
            .texture("top", modLoc("block/snowy_mangrove_roots_top"))
            .texture("side", modLoc("block/snowy_mangrove_roots_side")));
        simpleBlockWithItem(SNOWY_MUDDY_MANGROVE_ROOTS.get(), models().cubeColumn(name(SNOWY_MUDDY_MANGROVE_ROOTS),
            modLoc("block/snowy_muddy_mangrove_roots_side"),
            modLoc("block/snowy_muddy_mangrove_roots_top")));
        vineBlock(SNOWY_VINE);
        simpleBlock(SNOWY_SUGAR_CANE.get(), models()
            .withExistingParent(name(SNOWY_SUGAR_CANE), modLoc("block/overlay_tinted_cross"))
            .texture("cross", modLoc("block/snowy_sugar_cane"))
            .texture("overlay", modLoc("block/snowy_sugar_cane_overlay")));
        itemModels().basicItem(SNOWY_SUGAR_CANE.id());
        simpleBlockWithItem(SNOWY_CACTUS.get(), models()
            .withExistingParent(name(SNOWY_CACTUS), mcLoc("block/cactus"))
            .texture("bottom", modLoc("block/snowy_cactus_bottom"))
            .texture("top", modLoc("block/snowy_cactus_top"))
            .texture("side", modLoc("block/snowy_cactus_side")));
    }

    void snowyBlock(Supplier<Block> block)
    {
        final String name = nonSnowyName(block);
        simpleBlockWithItem(block.get(), models()
            .cubeBottomTop(name, modLoc("block/snowy_" + name), mcLoc("block/" + name), mcLoc("block/snow")));
    }

    void logBlock(Supplier<RotatedPillarBlock> block)
    {
        final String name = name(block);
        final ModelFile model = models().cubeColumn(name, modLoc("block/" + name), modLoc("block/" + name + "_top"));
        getVariantBuilder(block.get())
            .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.X).modelForState().rotationX(90).rotationY(90).modelFile(model).addModel()
            .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y).modelForState().modelFile(model).addModel()
            .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Z).modelForState().modelFile(model).rotationX(90).addModel();
        simpleBlockItem(block.get(), model);
    }

    void leavesBlock(Supplier<Block> block)
    {
        simpleBlockWithItem(block.get(), models()
            .withExistingParent(name(block), modLoc("block/snowy_leaves"))
            .texture("all", mcLoc("block/" + nonSnowyName(block)))
            .texture("overlay", modLoc("block/snowy_leaves_overlay")));
    }

    void vineBlock(Supplier<Block> block)
    {
        record Part(BooleanProperty property, int xRot, int yRot) {}

        final ModelFile model = models().getExistingFile(modLoc("block/snowy_vine"));
        final MultiPartBlockStateBuilder builder = getMultipartBuilder(block.get());
        for (Part part : new Part[] {
            new Part(VineBlock.UP, 270, 0),
            new Part(VineBlock.NORTH, 0, 0),
            new Part(VineBlock.EAST, 0, 90),
            new Part(VineBlock.SOUTH, 0, 180),
            new Part(VineBlock.WEST, 0, 270)
        }) {
            builder.part().modelFile(model)
                .uvLock(true)
                .rotationX(part.xRot).rotationY(part.yRot)
                .addModel()
                .condition(part.property, true);
            builder.part().modelFile(model)
                .uvLock(true)
                .rotationX(part.xRot).rotationX(part.yRot)
                .addModel()
                .condition(VineBlock.UP, false)
                .condition(VineBlock.NORTH, false)
                .condition(VineBlock.EAST, false)
                .condition(VineBlock.SOUTH, false)
                .condition(VineBlock.WEST, false);
        }

        itemModels().getBuilder(name(block)).parent(model);
    }

    void simpleBlockWithItem(Supplier<Block> block, ModelFile model, Function<ModelFile, ConfiguredModel[]> models)
    {
        simpleBlock(block.get(), models.apply(model));
        simpleBlockItem(block.get(), model);
    }

    String nonSnowyName(Supplier<? extends Block> block)
    {
        return name(block).replace("snowy_", "");
    }

    String name(Supplier<? extends Block> block)
    {
        return BuiltInRegistries.BLOCK.getKey(block.get()).getPath();
    }
}
