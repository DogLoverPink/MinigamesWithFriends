package doglover.dimensionSwap.configs;

public class DimensionSwapConfig extends GamemodeConfig {

    public DimensionSwapConfig() {
        super("dimensionSwap");
        registerConfigValue("canVisitSameWorldTwice", Boolean.class, false);
        registerConfigValue("minimumSecondsBeforeSwap", Integer.class, 60);
        registerConfigValue("maximumSecondsBeforeSwap", Integer.class, 180);
        registerConfigValue("numberOfSwaps", Integer.class, 2);
    }

    public boolean isCanVisitSameWorldTwice() {
        return getBoolean("canVisitSameWorldTwice");
    }

    public int getMinimumSecondsBeforeSwap() {
       return getInt("minimumSecondsBeforeSwap");
    }

    public int getMaximumSecondsBeforeSwap() {
        return getInt("maximumSecondsBeforeSwap");
    }

    public int getNumberOfSwaps() {
        return getInt("numberOfSwaps");
    }
}
