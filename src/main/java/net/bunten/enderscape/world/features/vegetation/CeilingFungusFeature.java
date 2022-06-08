package net.bunten.enderscape.world.features.vegetation;

import com.mojang.serialization.Codec;

import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.util.MathUtil;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class CeilingFungusFeature extends Feature<DefaultFeatureConfig> {
    private static final BlockState STEM = EnderscapeBlocks.CELESTIAL_STEM.getDefaultState();
    private static final BlockState CAP = EnderscapeBlocks.CELESTIAL_CAP.getDefaultState();
    public CeilingFungusFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    protected static void place(WorldAccess world, BlockPos pos, BlockState state) {
        world.setBlockState(pos, state, 3);
    }

    private static void generateCap(WorldAccess world, BlockPos pos, int height) {
        int radius = height / 4;
        for (int x = -radius + 1; x < radius; x++) {
            for (int z = -radius + 1; z < radius; z++) {
                double distance = MathUtil.sqrt(x * x + z * z);
                if (distance <= radius) {
                    place(world, pos.add(x, 0, z), CAP);
                }
            }
        }
    }

    private static void generate(WorldAccess world, Random random, BlockPos pos, int height) {
        for (int y = 0; y < height; y++) {
            place(world, pos.down(y), STEM);
        }
        generateCap(world, pos.down(height), height);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        BlockPos pos = context.getOrigin();

        if (!world.isAir(pos)) {
            return false;
        } else {
            if (world.getBlockState(pos.up()).isOpaque()) {
                generate(world, random, pos, MathUtil.nextBetween(random, 5, 30));
                return true;
            } else {
                return false;
            }
        }
    }
}