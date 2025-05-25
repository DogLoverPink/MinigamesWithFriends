package doglover.dimensionSwap.configs;

import org.bukkit.Material;

public class BlockShuffleConfig extends GamemodeConfig {

    public BlockShuffleConfig() {
        super("BlockShuffle");
        registerConfigValue("MinimumSecondsBeforeShuffle", Integer.class, 60);
        registerConfigValue("MaximumSecondsBeforeShuffle", Integer.class, 180);
        registerConfigValue("PointsPerSuccessfulBlockStep", Integer.class, 2);
        registerConfigValue("ShuffleBlocksPerPlayer", Boolean.class, false);
        registerConfigValue("GivePointsAtEndOfRound", Boolean.class, false);
        registerConfigValue("AllowNetherBlocks", Boolean.class, false);
    }


    public boolean shouldAllowNetherBlocks() {
        return getBoolean("AllowNetherBlocks");
    }

    public int getMinimumSecondsBeforeShuffle() {
       return getInt("MinimumSecondsBeforeShuffle");
    }

    public int getMaximumSecondsBeforeShuffle() {
        return getInt("MaximumSecondsBeforeShuffle");
    }

    public int getPointsPerSuccessfulBlockStep() {
        return getInt("PointsPerSuccessfulBlockStep");
    }

    public boolean shouldShuffleBlocksPerPlayer() {
        return getBoolean("ShuffleBlocksPerPlayer");
    }

    public boolean shouldGivePointsAtEndOfRound() {
        return getBoolean("GivePointsAtEndOfRound");
    }
}
