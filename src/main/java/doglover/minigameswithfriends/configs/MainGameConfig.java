package doglover.minigameswithfriends.configs;

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

    private final RandomizerConfig randomizerConfig = new RandomizerConfig();

    public RandomizerConfig getRandomizerConfig() {
        return randomizerConfig;
    }

    private final DeathmatchConfig deathMatchConfig = new DeathmatchConfig();

    public DeathmatchConfig getDeathMatchConfig() {
        return deathMatchConfig;
    }

    private final WouldYouRatherConfig wouldYouRatherConfig = new WouldYouRatherConfig();

    public WouldYouRatherConfig getWouldYouRatherConfig() {
        return wouldYouRatherConfig;
    }

    public MainGameConfig() {
        super("MainGame");
        registerConfigValue("PointsToWin", Integer.class, 1);
        registerConfigValue("PointsPerDeathmatchWin", Integer.class, 1);
        registerConfigValue("KeepInventoryOnDeath", Boolean.class, false);
        registerConfigValue("SetToDayOnStart", Boolean.class, true);
        registerConfigValue("ResetAdvancementsOnGameStart", Boolean.class, false);
        registerConfigValue("TeleportPlayersToWorldSpawnOnGameStart", Boolean.class, true);
    }


    public GamemodeConfig getGamemodeConfigFromName(String name) {
        return switch (name.toLowerCase()) {
            case "dimensionswap" -> getDimensionSwapConfig();
            case "deathswap" -> getDeathSwapConfig();
            case "deathmatch" -> getDeathMatchConfig();
            case "randomizer" -> getRandomizerConfig();
            case "blockshuffle" -> getBlockShuffleConfig();
            case "wouldyourather" -> getWouldYouRatherConfig();
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

    public boolean shouldResetAdvancementsOnGameStart() {
        return getBoolean("ResetAdvancementsOnGameStart");
    }

    public boolean shouldTeleportPlayersToWorldSpawnOnGameStart() {
        return getBoolean("TeleportPlayersToWorldSpawnOnGameStart");
    }
}
