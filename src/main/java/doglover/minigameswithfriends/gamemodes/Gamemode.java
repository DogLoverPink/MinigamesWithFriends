package doglover.minigameswithfriends.gamemodes;

import doglover.minigameswithfriends.configs.GameModuleConfig;

import java.util.function.Supplier;

public abstract class Gamemode extends GameModule {

    public static void register(String name, Class<? extends Gamemode> moduleClass,
                                Supplier<? extends Gamemode> factory, GameModuleConfig config) {
        registerModule(name, moduleClass, factory, config, Type.GAMEMODE);
    }

    @Override
    public final Type getType() {
        return Type.GAMEMODE;
    }
}
