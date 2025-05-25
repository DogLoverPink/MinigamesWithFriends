package doglover.dimensionSwap.configs;

public class DeathSwapConfig extends GamemodeConfig {

    public DeathSwapConfig() {
        super("DeathSwap");
        registerConfigValue("MinimumSecondsBeforeSwap", Integer.class, 60);
        registerConfigValue("MaximumSecondsBeforeSwap", Integer.class, 180);
        registerConfigValue("PointsPerImpressiveKill", Integer.class, 2);
        registerConfigValue("PointsPerLameKill", Integer.class, 1);
    }


    public int getMinimumSecondsBeforeSwap() {
       return getInt("MinimumSecondsBeforeSwap");
    }

    public int getMaximumSecondsBeforeSwap() {
        return getInt("MaximumSecondsBeforeSwap");
    }

    public int getPointsPerImpressiveDeath() {
        return getInt("PointsPerImpressiveKill");
    }

    public int getPointsPerLameDeath() {
        return getInt("PointsPerLameKill");
    }
}
