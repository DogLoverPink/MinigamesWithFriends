package doglover.minigameswithfriends.configs.gamemodeconfigs;

import doglover.minigameswithfriends.configs.GameModuleConfig;

public class DimensionSwapConfig extends GameModuleConfig {

    public DimensionSwapConfig() {
        super("DimensionSwap");
        registerConfigValue("MinimumSecondsBeforeSwap", Integer.class, 60);
        registerConfigValue("MaximumSecondsBeforeSwap", Integer.class, 180);
        registerConfigValue("NumberOfSwaps", Integer.class, 2);
        registerConfigValue("SendPlayersToMainWorldAfterGameEnds", Boolean.class, false);
    }

    public boolean shouldSendPlayersToMainWorldAfterGameEnds() {
        return getBoolean("SendPlayersToMainWorldAfterGameEnds");
    }

    public int getMinimumSecondsBeforeSwap() {
       return getInt("MinimumSecondsBeforeSwap");
    }

    public int getMaximumSecondsBeforeSwap() {
        return getInt("MaximumSecondsBeforeSwap");
    }

    public int getNumberOfSwaps() {
        return getInt("NumberOfSwaps");
    }
}
