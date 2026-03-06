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
            case "DeathSwap", "DimensionSwap", "Randomizer", "BlockShuffle", "WouldYouRather", "Deathmatch" -> true;
            default -> false;
        };
    }

    public abstract void updateConfig();

    public static Gamemode getGamemodeFromName(String gamemodeName) {
        return switch (gamemodeName) {
            case "DeathSwap" -> new DeathSwapGamemode();
            case "DimensionSwap" -> new DimensionSwapGamemode();
            case "Randomizer" -> new RandomizerGamemode();
            case "BlockShuffle" -> new BlockShuffleGamemode();
            case "WouldYouRather" -> new WouldYouRatherGamemode();
            case "Deathmatch" -> new DeathmatchGamemode();
            default -> null;
        };
    }

    public static String getGamemodeListString() {
        return "DeathSwap, DimensionSwap, Randomizer, BlockShuffle, WouldYouRather, Deathmatch";
    }

    public static List<String> getGamemodeList() {
        return List.of("DeathSwap", "DimensionSwap", "Randomizer", "BlockShuffle", "WouldYouRather", "Deathmatch");
    }

    public static List<Class<? extends Gamemode>> getGamemodeClassList() {
        return List.of(DeathSwapGamemode.class, DimensionSwapGamemode.class, RandomizerGamemode.class, BlockShuffleGamemode.class, WouldYouRatherGamemode.class, DeathmatchGamemode.class);
    }

    public static String getGamemodeNameFromClass(Class<? extends Gamemode> gamemodeClass) {
        if (gamemodeClass == null) {
            return "";
        }
        return gamemodeClass.getSimpleName().replace("Gamemode", "");
    }
}
