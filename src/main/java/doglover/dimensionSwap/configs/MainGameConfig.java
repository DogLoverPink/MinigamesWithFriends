package doglover.dimensionSwap.configs;

public class MainGameConfig extends GamemodeConfig {

    private final DimensionSwapConfig dimensionSwapConfig = new DimensionSwapConfig();

    public DimensionSwapConfig getDimensionSwapConfig() {
        return dimensionSwapConfig;
    }

    private final DeathSwapConfig deathSwapConfig = new DeathSwapConfig();

    public DeathSwapConfig getDeathSwapConfig() {
        return deathSwapConfig;
    }

    private final BlockShuffleConfig blockShuffleConfig = new BlockShuffleConfig();

    public BlockShuffleConfig getBlockShuffleConfig() {
        return blockShuffleConfig;
    }

    public final RandomizerConfig randomizerConfig = new RandomizerConfig();

    public RandomizerConfig getRandomizerConfig() {
        return randomizerConfig;
    }

    public MainGameConfig() {
        super("MainGame");
        registerConfigValue("PointsToWin", Integer.class, 1);
        registerConfigValue("PointsPerDeathmatchWin", Integer.class, 1);
        registerConfigValue("KeepInventoryOnDeath", Boolean.class, false);
        registerConfigValue("SetToDayOnStart", Boolean.class, true);
    }


    public GamemodeConfig getGamemodeConfigFromName(String name) {
        return switch (name.toLowerCase()) {
            case "dimensionswap" -> getDimensionSwapConfig();
            case "deathswap" -> getDeathSwapConfig();
            case "randomizer" -> getRandomizerConfig();
            case "blockshuffle" -> getBlockShuffleConfig();
            case "maingame" -> this;
            default -> null;
        };
    }

    public boolean shouldSetToDayOnStart() {
        return getBoolean("SetToDayOnStart");
    }

    public int getPointsPerDeathmatchWin() {
        return getInt("PointsPerDeathmatchWin");
    }

    public boolean shouldKeepInventoryOnDeath() {
        return getBoolean("KeepInventoryOnDeath");
    }

    public int getPointsToWin() {
        return getInt("PointsToWin");
    }
}
