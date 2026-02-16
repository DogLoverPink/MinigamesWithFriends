package doglover.minigameswithfriends.configs;

public class DeathmatchConfig extends GamemodeConfig {

    public DeathmatchConfig() {
        super("Deathmatch");
        registerConfigValue("MinimumSecondsBeforeDeathMatch", Integer.class, 300);
        registerConfigValue("MaximumSecondsBeforeDeathMatch", Integer.class, 300);
        registerConfigValue("DeathmatchAreaRadiusBlocks", Integer.class, 35);
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

    public int getDeathmatchAreaRadiusBlocks() {
        return getInt("DeathmatchAreaRadiusBlocks");
    }



}
