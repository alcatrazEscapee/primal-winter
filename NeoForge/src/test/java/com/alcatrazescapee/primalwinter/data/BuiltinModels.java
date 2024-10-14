package com.alcatrazescapee.primalwinter.data;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import com.alcatrazescapee.primalwinter.PrimalWinter;
import com.alcatrazescapee.primalwinter.platform.RegistryHolder;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.BambooStalkBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.properties.BambooLeaves;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
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
            .texture("top", mcLoc("block/snow"))
            .texture("side", modLoc("block/snowy_dirt_path_side")),
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
        leavesBlock(SNOWY_AZALEA_LEAVES);
        leavesBlock(SNOWY_FLOWERING_AZALEA_LEAVES);
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
        bambooBlock(SNOWY_BAMBOO);
        lilyPadBlock(SNOWY_LILY_PAD);
        mushroomBlock(SNOWY_BROWN_MUSHROOM_BLOCK);
        mushroomBlock(SNOWY_RED_MUSHROOM_BLOCK);
        mushroomBlock(SNOWY_MUSHROOM_STEM);
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

    void bambooBlock(RegistryHolder<Block> block)
    {
        final MultiPartBlockStateBuilder builder = getMultipartBuilder(block.get());
        for (int age = 0; age <= 1; age++)
        {
            var part = builder.part();
            for (int i = 1; i <= 4; i++)
            {
                part.modelFile(models()
                    .withExistingParent("bamboo%d_age%d".formatted(i, age), mcLoc("block/bamboo%d_age%d".formatted(i, age)))
                    .texture("all", modLoc("block/snowy_bamboo_stalk"))
                    .texture("particle", modLoc("block/snowy_bamboo_stalk")));
                if (i != 4) part = part.nextModel();
            }
            part.addModel().condition(BambooStalkBlock.AGE, age);
        }
        for (BambooLeaves leaves : List.of(BambooLeaves.SMALL, BambooLeaves.LARGE))
        {
            final String leavesName = "bamboo_%s_leaves".formatted(leaves.getSerializedName());
            builder.part()
                .modelFile(models()
                    .withExistingParent(leavesName, mcLoc("block/" + leavesName))
                    .texture("texture", modLoc("block/snowy_" + leavesName))
                    .texture("particle", modLoc("block/snowy_" + leavesName)))
                .addModel()
                .condition(BambooStalkBlock.LEAVES, leaves);
        }
        itemModels().basicItem(block.id());
    }

    void lilyPadBlock(RegistryHolder<Block> block)
    {
        final BlockModelBuilder model = models().withExistingParent(name(block), mcLoc("block/lily_pad"))
            .texture("particle", modLoc("block/snowy_lily_pad"))
            .texture("texture", modLoc("block/snowy_lily_pad"));
        getVariantBuilder(block.get())
            .partialState()
            .modelForState()
            .modelFile(model).nextModel()
            .modelFile(model).rotationY(90).nextModel()
            .modelFile(model).rotationY(180).nextModel()
            .modelFile(model).rotationY(270).addModel();
        itemModels().getBuilder(block.id().toString())
            .parent(new ModelFile.UncheckedModelFile("item/generated"))
            .texture("layer0", modLoc("block/snowy_lily_pad"));
    }

    void mushroomBlock(Supplier<Block> block)
    {
        final String name = name(block);
        final ModelFile outside = models().singleTexture(name, mcLoc("block/template_single_face"), modLoc("block/" + name));
        final ModelFile inside = models().getExistingFile(mcLoc("block/mushroom_block_inside"));
        final MultiPartBlockStateBuilder builder = getMultipartBuilder(block.get());

        record Part(BooleanProperty property, int xRot, int yRot) {}

        for (Part part : new Part[] {
            new Part(BlockStateProperties.NORTH, 0, 0),
            new Part(BlockStateProperties.EAST, 0, 90),
            new Part(BlockStateProperties.SOUTH, 0, 180),
            new Part(BlockStateProperties.WEST, 0, 270),
            new Part(BlockStateProperties.UP, 270, 0),
            new Part(BlockStateProperties.DOWN, 90, 0),
        }) {
            builder.part().modelFile(outside)
                .uvLock(part.property != BlockStateProperties.NORTH)
                .rotationX(part.xRot).rotationY(part.yRot)
                .addModel()
                .condition(part.property, true);
            builder.part().modelFile(inside)
                .uvLock(false)
                .rotationX(part.xRot).rotationY(part.yRot)
                .addModel()
                .condition(part.property, false);
        }
        simpleBlockItem(block.get(), models().cubeAll(name + "_inventory", modLoc("block/" + name)));
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
