package doglover.minigameswithfriends.configs;

public class WouldYouRatherConfig extends GamemodeConfig {

    public WouldYouRatherConfig() {
        super("WouldYouRather");
        registerConfigValue("MinimumSecondsBeforeNewChoice", Integer.class, 60);
        registerConfigValue("MaximumSecondsBeforeNewChoice", Integer.class, 180);
        registerConfigValue("AllocatedSecondsForChoosingOption", Integer.class, 15);
        registerConfigValue("MinimumSecondsBeforeDeathMatch", Integer.class, 180);
        registerConfigValue("MaximumSecondsBeforeDeathMatch", Integer.class, 180);
    }


    public int getMinimumSecondsBeforeNewChoice() {
       return getInt("MinimumSecondsBeforeNewChoice");
    }

    public int getMaximumSecondsBeforeNewChoice() {
        return getInt("MaximumSecondsBeforeNewChoice");
    }

    public int getAllocatedSecondsForChoosingOption() {
        return getInt("AllocatedSecondsForChoosingOption");
    }

    public int getMinimumSecondsBeforeDeathMatch() {
        return getInt("MinimumSecondsBeforeDeathMatch");
    }

    public int getMaximumSecondsBeforeDeathMatch() {
        return getInt("MaximumSecondsBeforeDeathMatch");
    }

}
