package doglover.minigameswithfriends.gamemodes;

import doglover.minigameswithfriends.Game;

import java.util.List;

public abstract class Gamemode {

    public abstract void tick();

    public abstract void onGameEnd();

    public abstract void onGameStart();

    public void onDeathMatchEnd() {

    }

    private Game game;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public static boolean isValidGamemode(String gamemodeName) {
        return switch (gamemodeName) {
            case "DeathSwap", "DimensionSwap", "Randomizer", "BlockShuffle" -> true;
            default -> false;
        };
    }

    public static Gamemode getGamemodeFromName(String gamemodeName) {
        return switch (gamemodeName) {
            case "DeathSwap" -> new DeathSwapGamemode();
            case "DimensionSwap" -> new DimensionSwapGamemode();
            case "Randomizer" -> new RandomizerGamemode();
            case "BlockShuffle" -> new BlockShuffleGamemode();
            default -> null;
        };
    }

    public static String getGamemodeListString() {
        return "DeathSwap, DimensionSwap, Randomizer, BlockShuffle";
    }

    public static List<String> getGamemodeList() {
        return List.of("DeathSwap", "DimensionSwap", "Randomizer", "BlockShuffle");
    }
}
