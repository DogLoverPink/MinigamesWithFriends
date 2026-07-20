package doglover.minigameswithfriends.gamemodes;

import doglover.minigameswithfriends.configs.gamemodeconfigs.DeathmatchConfig;

public class DeathmatchGamemode extends TimeEventBasedGamemode {

    private static final DeathmatchConfig CONFIG = new DeathmatchConfig();

    static {
        Gamemode.register("Deathmatch", DeathmatchGamemode.class, DeathmatchGamemode::new, CONFIG);
    }

    public static DeathmatchConfig config() {
        return CONFIG;
    }

    @Override
    public DeathmatchConfig getConfig() {
        return CONFIG;
    }

    @Override
    public void tick() {
        super.tick();

        this.getGame().addScoreboardContribution("§dDeathmatch in: §b" + getFormattedTimeRemaining());

    }
    @Override
    public void onGameStart() {
        updateConfig();
        super.onGameStart();
    }

    @Override
    public void updateConfig() {
        this.setMinTicks(20 * getConfig().getMinimumSecondsBeforeDeathMatch());
        this.setMaxTicks(20 * getConfig().getMaximumSecondsBeforeDeathMatch());
    }

    @Override
    public void onTimeEventTrigger() {
        getGame().startDeathMatch();

    }


    @Override
    public void onGameEnd() {

    }

    @Override
    public String toString() {
        return "Deathmatch";
    }
}
