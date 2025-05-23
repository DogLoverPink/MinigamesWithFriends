package doglover.dimensionSwap.gamemodes;

import doglover.dimensionSwap.Game;

public abstract class Gamemode {

    public abstract void tick();

    public abstract void onGameEnd();

    public abstract void onGameStart();

    private Game game;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
