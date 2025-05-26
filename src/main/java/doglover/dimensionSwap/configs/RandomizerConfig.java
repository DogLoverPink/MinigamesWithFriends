package doglover.dimensionSwap.configs;

public class RandomizerConfig extends GamemodeConfig{

    public RandomizerConfig() {
        super("Randomizer");
        registerConfigValue("RerandomizeAfterDeathMatch", Boolean.class, false);
        registerConfigValue("EnableDeathMatches", Boolean.class, true);
        registerConfigValue("MinimumSecondsBeforeDeathMatch", Integer.class, 180);
        registerConfigValue("MaximumSecondsBeforeDeathMatch", Integer.class, 180);
        registerConfigValue("RandomlyEnchantGear", Boolean.class, false);
    }

    public boolean isRerandomizeAfterDeathMatch() {
        return getBoolean("RerandomizeAfterDeathMatch");
    }

    public boolean isEnableDeathMatches() {
        return getBoolean("EnableDeathMatches");
    }

    public int getMinimumSecondsBeforeDeathMatch() {
       return getInt("MinimumSecondsBeforeDeathMatch");
    }

    public int getMaximumSecondsBeforeDeathMatch() {
        return getInt("MaximumSecondsBeforeDeathMatch");
    }

    public boolean isRandomlyEnchantingGear() {
        return getBoolean("RandomlyEnchantGear");
    }


}
