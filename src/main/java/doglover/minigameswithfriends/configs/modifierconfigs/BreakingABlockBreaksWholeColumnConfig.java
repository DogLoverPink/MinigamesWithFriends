package doglover.minigameswithfriends.configs.modifierconfigs;

import doglover.minigameswithfriends.configs.GameModuleConfig;

public class BreakingABlockBreaksWholeColumnConfig extends GameModuleConfig {

    public BreakingABlockBreaksWholeColumnConfig() {
        super("BreakingABlockBreaksWholeColumn");
        registerConfigValue("AnimationMode", StringEnum.class, "None");
        registerStringEnum("AnimationMode", "None", "RapidBreaking", "FallingBlocks");
        registerConfigValue("BreakDirection", StringEnum.class, "AboveAndBelow");
        registerStringEnum("BreakDirection", "Below", "AboveAndBelow");
        registerConfigValue("DropItems", Boolean.class, true);
    }

    public boolean shouldDropItems() {
        return getBoolean("DropItems");
    }

    public String getAnimationMode() {
        return getString("AnimationMode");
    }

    public String getBreakDirection() {
        return getString("BreakDirection");
    }
}
