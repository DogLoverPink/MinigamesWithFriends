package doglover.minigameswithfriends.configs;

public class WouldYouRatherConfig extends GamemodeConfig {

    public WouldYouRatherConfig() {
        super("WouldYouRather");
        registerConfigValue("MinimumSecondsBeforeNewChoice", Integer.class, 60);
        registerConfigValue("MaximumSecondsBeforeNewChoice", Integer.class, 180);
        registerConfigValue("AllocatedSecondsForChoosingOption", Integer.class, 25);
        registerConfigValue("PreventMovingDuringChoiceSelection", Boolean.class, true);
        registerConfigValue("ApplyDamageImmunityDuringChoiceSelection", Boolean.class, true);
        registerConfigValue("StartGameWithAChoicePrompt", Boolean.class, true);
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

    public boolean shouldStartGameWithAChoicePrompt() {
        return getBoolean("StartGameWithAChoicePrompt");
    }

    public boolean shouldPreventMovingDuringChoiceSelection() {
        return getBoolean("PreventMovingDuringChoiceSelection");
    }

    public boolean shouldApplyDamageImmunityDuringChoiceSelection() {
        return getBoolean("ApplyDamageImmunityDuringChoiceSelection");
    }


}
