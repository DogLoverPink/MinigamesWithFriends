package doglover.minigameswithfriends.gamemodes;

import doglover.minigameswithfriends.configs.GameModuleConfig;

import java.util.function.Supplier;

public abstract class Modifier extends GameModule {

    public static void register(String name, Class<? extends Modifier> moduleClass,
                                Supplier<? extends Modifier> factory, GameModuleConfig config) {
        registerModule(name, moduleClass, factory, config, Type.MODIFIER);
    }

    @Override
    public final Type getType() {
        return Type.MODIFIER;
    }
}
