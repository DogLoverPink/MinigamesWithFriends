package doglover.minigameswithfriends.configs.modifierconfigs;

import doglover.minigameswithfriends.configs.GameModuleConfig;

public class RandomizerConfig extends GameModuleConfig {

    public RandomizerConfig() {
        super("Randomizer");
        registerConfigValue("RerandomizeAfterDeathMatch", Boolean.class, false);

        registerConfigValue("RandomlyEnchantGear", Boolean.class, true);
    }

    public boolean isRerandomizeAfterDeathMatch() {
        return getBoolean("RerandomizeAfterDeathMatch");
    }

    public boolean isRandomlyEnchantingGear() {
        return getBoolean("RandomlyEnchantGear");
    }


}
