package doglover.minigameswithfriends.gamemodes;

public class DeathmatchGamemode extends TimeEventBasedGamemode {

    @Override
    public void tick() {
        super.tick();

        this.getGame().addScoreboardContributution("§dDeathmatch in: §b" + getFormattedTimeRemaining());

    }
    @Override
    public void onGameStart() {
        this.setMinTicks(20 * this.getGame().getConfig().getDeathMatchConfig().getMinimumSecondsBeforeDeathMatch());
        this.setMaxTicks(20 * this.getGame().getConfig().getDeathMatchConfig().getMaximumSecondsBeforeDeathMatch());
        super.onGameStart();
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
