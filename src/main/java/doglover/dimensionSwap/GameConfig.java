package doglover.dimensionSwap;

public class GameConfig {

    private boolean canVisitSameWorldTwice;
    private int minimumSecondsBeforeSwap;
    private int maximumSecondsBeforeSwap;
    private int numbersOfSwaps;

    public static GameConfig loadGameConfig() {

        boolean canVisitSameWorldTwice = DimensionSwap.getGamePlugin().getConfig().getBoolean("canVisitSameWorldTwice");
        int minimumSecondsBeforeSwap = DimensionSwap.getGamePlugin().getConfig().getInt("minimumSecondsBeforeSwap");
        int maximumSecondsBeforeSwap = DimensionSwap.getGamePlugin().getConfig().getInt("maximumSecondsBeforeSwap");
        int numbersOfSwaps = DimensionSwap.getGamePlugin().getConfig().getInt("numbersOfSwaps");

        return new GameConfig(canVisitSameWorldTwice, minimumSecondsBeforeSwap, maximumSecondsBeforeSwap, numbersOfSwaps);
    }

    public GameConfig(boolean canVisitSameWorldTwice, int minimumSecondsBeforeSwap, int maximumSecondsBeforeSwap, int numbersOfSwaps) {
        this.canVisitSameWorldTwice = canVisitSameWorldTwice;
        this.minimumSecondsBeforeSwap = minimumSecondsBeforeSwap;
        this.maximumSecondsBeforeSwap = maximumSecondsBeforeSwap;
        this.numbersOfSwaps = numbersOfSwaps;
    }
    public GameConfig() {
        this.canVisitSameWorldTwice = false;
        this.minimumSecondsBeforeSwap = 60;
        this.maximumSecondsBeforeSwap = 180;
        this.numbersOfSwaps = 3;
    }

    public void setConfigValue(String key, String value) {
        switch (key) {
            case "canVisitSameWorldTwice":
                setCanVisitSameWorldTwice(Boolean.parseBoolean(value));
                break;
            case "minimumSecondsBeforeSwap":
                setMinimumSecondsBeforeSwap(Integer.parseInt(value));
                break;
            case "maximumSecondsBeforeSwap":
                setMaximumSecondsBeforeSwap(Integer.parseInt(value));
                break;
            case "numbersOfSwaps":
                setNumbersOfSwaps(Integer.parseInt(value));
                break;
            default:
                throw new IllegalArgumentException("Invalid config key: " + key);
        }
    }

    public String getConfigValue(String key) {
        String value;
        switch (key) {
            case "canVisitSameWorldTwice":
                value = String.valueOf(isCanVisitSameWorldTwice());
                break;
            case "minimumSecondsBeforeSwap":
                value = String.valueOf(getMinimumSecondsBeforeSwap());
                break;
            case "maximumSecondsBeforeSwap":
                value = String.valueOf(getMaximumSecondsBeforeSwap());
                break;
            case "numbersOfSwaps":
                value = String.valueOf(getNumbersOfSwaps());
                break;
            default:
                throw new IllegalArgumentException("Invalid config key: " + key);
        }
        return value;
    }

    public void saveConfig() {
        DimensionSwap.getGamePlugin().getConfig().set("canVisitSameWorldTwice", canVisitSameWorldTwice);
        DimensionSwap.getGamePlugin().getConfig().set("minimumSecondsBeforeSwap", minimumSecondsBeforeSwap);
        DimensionSwap.getGamePlugin().getConfig().set("maximumSecondsBeforeSwap", maximumSecondsBeforeSwap);
        DimensionSwap.getGamePlugin().getConfig().set("numbersOfSwaps", numbersOfSwaps);
        DimensionSwap.getGamePlugin().saveConfig();

    }

    public boolean isCanVisitSameWorldTwice() {
        return canVisitSameWorldTwice;
    }

    public void setCanVisitSameWorldTwice(boolean canVisitSameWorldTwice) {
        this.canVisitSameWorldTwice = canVisitSameWorldTwice;
    }

    public int getMinimumSecondsBeforeSwap() {
        return minimumSecondsBeforeSwap;
    }

    public void setMinimumSecondsBeforeSwap(int minimumSecondsBeforeSwap) {
        this.minimumSecondsBeforeSwap = minimumSecondsBeforeSwap;
    }

    public int getMaximumSecondsBeforeSwap() {
        return maximumSecondsBeforeSwap;
    }

    public void setMaximumSecondsBeforeSwap(int maximumSecondsBeforeSwap) {
        this.maximumSecondsBeforeSwap = maximumSecondsBeforeSwap;
    }

    public int getNumbersOfSwaps() {
        return numbersOfSwaps;
    }

    public void setNumbersOfSwaps(int numbersOfSwaps) {
        this.numbersOfSwaps = numbersOfSwaps;
    }


}
