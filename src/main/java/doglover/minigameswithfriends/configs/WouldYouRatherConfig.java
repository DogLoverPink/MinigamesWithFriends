package doglover.minigameswithfriends.configs;

public class WouldYouRatherConfig extends GamemodeConfig {

    public WouldYouRatherConfig() {
        super("WouldYouRather");
        registerConfigValue("MinimumSecondsBeforeNewChoice", Integer.class, 60);
        registerConfigValue("MaximumSecondsBeforeNewChoice", Integer.class, 180);
    }


    public int getMinimumSecondsBeforeNewChoice() {
       return getInt("MinimumSecondsBeforeNewChoice");
    }

    public int getMaximumSecondsBeforeNewChoice() {
        return getInt("MaximumSecondsBeforeNewChoice");
    }

}
