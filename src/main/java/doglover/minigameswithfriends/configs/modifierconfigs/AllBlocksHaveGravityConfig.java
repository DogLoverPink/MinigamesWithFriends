package doglover.minigameswithfriends.configs.modifierconfigs;

import doglover.minigameswithfriends.configs.GameModuleConfig;

public class AllBlocksHaveGravityConfig extends GameModuleConfig {

    public AllBlocksHaveGravityConfig() {
        super("AllBlocksHaveGravity");
        registerConfigValue("HorizontalEffectRadius", Integer.class, 10);
        registerConfigValue("VerticalEffectRadius", Integer.class, 40);
        registerConfigValue("EnableInvisibleBlockCulling", Boolean.class, true);
        registerConfigValue("MaxBlockVisibilityDistance", Integer.class, 48);
        registerConfigValue("AlwaysVisibleRadiusBlocks", Integer.class, 6);
        registerConfigValue("MaxConcurrentFallingBlockEntities", Integer.class, 5000);
        registerConfigValue("MaxDroppedItemsPerWorld", Integer.class, 500);
        registerConfigValue("ApplyPhysicsUpdatesToFallingBlocks", Boolean.class, false);
    }


    public int getHorizontalEffectRadius() {
        return Math.max(0, getInt("HorizontalEffectRadius"));
    }

    public int getVerticalEffectRadius() {
        return Math.max(0, getInt("VerticalEffectRadius"));
    }
    public boolean isInvisibleBlockCullingEnabled() {
        return getBoolean("EnableInvisibleBlockCulling");
    }

    /**
     * Blocks further than this are never rendered, but the logic for them falling still happens
     * */
    public int getMaxBlockVisibilityDistanceBlocks() {
        return Math.max(1, getInt("MaxBlockVisibilityDistance"));
    }

    /**
     * Blocks this close to a player are always rendered, regardless of whether or not they could be culled
     */
    public int getAlwaysVisibleRadiusBlocks() {
        return Math.max(0, getInt("AlwaysVisibleRadiusBlocks"));
    }


    public int getMaxConcurrentFallingBlockEntities() {
        return Math.max(0, getInt("MaxConcurrentFallingBlockEntities"));
    }

    public int getMaxDroppedItemsPerWorld() {
        return Math.max(0, getInt("MaxDroppedItemsPerWorld"));
    }

    public boolean shouldApplyPhysicsUpdatesToFallingBlocks() {
        return getBoolean("ApplyPhysicsUpdatesToFallingBlocks");
    }
}
