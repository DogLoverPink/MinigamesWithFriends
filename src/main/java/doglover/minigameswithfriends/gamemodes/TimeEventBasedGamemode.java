package doglover.minigameswithfriends.gamemodes;

import java.util.Random;

public abstract class TimeEventBasedGamemode extends Gamemode {

    private int minTicks;

    private static final Random random = new Random();

    public int getMaxTicks() {
        return maxTicks;
    }

    public int getTickGoal() {
        return tickGoal;
    }

    public void setTickGoal(int tickGoal) {
        this.tickGoal = tickGoal;
    }

    public abstract void onGameEnd();

    @Override
    public void onGameStart() {
        tickGoal = getNextComputedTime();
    }

    public void setMaxTicks(int maxTicks) {
        this.maxTicks = maxTicks;
    }

    public int getMinTicks() {
        return minTicks;
    }

    public void setMinTicks(int minTicks) {
        this.minTicks = minTicks;
    }

    private int maxTicks;

    int getNextComputedTime() {
        return random.nextInt(minTicks, maxTicks + 1);
    }

    private int tickGoal;





    @Override
    public void tick() {
        tickGoal--;
        if (tickGoal <= 0) {
            tickGoal = getNextComputedTime();
            onTimeEventTrigger();
        }

    }

    public abstract void onTimeEventTrigger();

    String getFormattedTimeRemaining() {
        int seconds = tickGoal / 20;
        int minutes = seconds / 60;
        seconds = seconds - (minutes * 60);
        return minutes + ":" + String.format("%02d", seconds + 1);
    }



}
