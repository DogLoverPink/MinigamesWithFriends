package doglover.minigameswithfriends.configs;

public class RandomizerConfig extends GamemodeConfig{

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
