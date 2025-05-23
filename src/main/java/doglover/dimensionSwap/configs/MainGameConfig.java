package doglover.dimensionSwap.configs;

public class MainGameConfig extends GamemodeConfig {

    private DimensionSwapConfig dimensionSwapConfig;

    public DimensionSwapConfig getDimensionSwapConfig() {
        return dimensionSwapConfig;
    }

    public void setDimensionSwapConfig(DimensionSwapConfig dimensionSwapConfig) {
        this.dimensionSwapConfig = dimensionSwapConfig;
    }

    public MainGameConfig() {
        super("minigames");
        registerConfigValue("pointsToWin", Integer.class, 1);
    }

    public GamemodeConfig getGamemodeConfigFromName(String name) {
        return switch (name.toLowerCase()) {
            case "dimensionswap" -> getDimensionSwapConfig();
            default -> null;
        };
    }

    public int getPointsToWin() {
        return getInt("pointsToWin");
    }
}
