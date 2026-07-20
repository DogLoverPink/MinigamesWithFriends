package doglover.minigameswithfriends.configs.gamemodeconfigs;

import doglover.minigameswithfriends.configs.GameModuleConfig;

public class DeathSwapConfig extends GameModuleConfig {

    public DeathSwapConfig() {
        super("DeathSwap");
        registerConfigValue("MinimumSecondsBeforeSwap", Integer.class, 60);
        registerConfigValue("MaximumSecondsBeforeSwap", Integer.class, 180);
        registerConfigValue("PointsPerImpressiveKill", Integer.class, 2);
        registerConfigValue("PointsPerLameKill", Integer.class, 1);
        registerConfigValue("KeepInventoryOnSwapRelatedDeath", Boolean.class, true);
    }


    public int getMinimumSecondsBeforeSwap() {
       return getInt("MinimumSecondsBeforeSwap");
    }

    public int getMaximumSecondsBeforeSwap() {
        return getInt("MaximumSecondsBeforeSwap");
    }

    public int getPointsPerImpressiveDeath() {
        return getInt("PointsPerImpressiveKill");
    }

    public boolean shouldKeepInventoryOnSwapRelatedDeath() {
        return getBoolean("KeepInventoryOnSwapRelatedDeath");
    }

    public int getPointsPerLameDeath() {
        return getInt("PointsPerLameKill");
    }
}
