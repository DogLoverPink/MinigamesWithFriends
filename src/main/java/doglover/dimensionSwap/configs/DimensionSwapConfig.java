package doglover.dimensionSwap.configs;

public class DimensionSwapConfig extends GamemodeConfig {

    public DimensionSwapConfig() {
        super("DimensionSwap");
        registerConfigValue("CanVisitSameWorldTwice", Boolean.class, false);
        registerConfigValue("MinimumSecondsBeforeSwap", Integer.class, 60);
        registerConfigValue("MaximumSecondsBeforeSwap", Integer.class, 180);
        registerConfigValue("NumberOfSwaps", Integer.class, 2);
        registerConfigValue("SendPlayersToMainWorldAfterGameEnds", Boolean.class, false);
    }

    public boolean shouldSendPlayersToMainWorldAfterGameEnds() {
        return getBoolean("SendPlayersToMainWorldAfterGameEnds");
    }

    public boolean isCanVisitSameWorldTwice() {
        return getBoolean("CanVisitSameWorldTwice");
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
