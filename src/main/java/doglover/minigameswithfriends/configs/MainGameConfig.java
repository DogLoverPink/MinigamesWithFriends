package doglover.minigameswithfriends.configs;

public class MainGameConfig extends GameModuleConfig {

    public MainGameConfig() {
        super("MainGame");
        registerConfigValue("PointsToWin", Integer.class, 1);
        registerConfigValue("PointsPerDeathmatchWin", Integer.class, 1);
        registerConfigValue("KeepInventoryOnDeath", Boolean.class, false);
        registerConfigValue("SetToDayOnGameStart", Boolean.class, true);
        registerConfigValue("ResetAdvancementsOnGameStart", Boolean.class, true);
        registerConfigValue("TeleportPlayersToWorldSpawnOnGameStart", Boolean.class, true);
    }


    public boolean shouldSetToDayOnGameStart() {
        return getBoolean("SetToDayOnGameStart");
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
