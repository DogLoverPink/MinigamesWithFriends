package doglover.dimensionSwap.configs;

import doglover.dimensionSwap.DimensionSwap;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class GamemodeConfig {

    Map<String, Class<?>> configValues = new HashMap<>();
    FileConfiguration config = DimensionSwap.getGamePlugin().getConfig();

    public String getPrefix() {
        return prefix;
    }

    public Map<String, Class<?>> getConfigValues() {
        return configValues;
    }


    protected void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    String prefix;


    protected void registerConfigValue(String key, Class<?> type, Object defaultValue) {
        configValues.put(key, type);
        config.addDefault(prefix + "." + key, defaultValue);
    }

    public GamemodeConfig(String prefix) {
        this.prefix = prefix;
    }

    public boolean validateAndSetValue(String key, String value) {
        Class<?> type = configValues.get(key);
        if (type == null) {
            return false;
        }
        if (type == Integer.class) {
            try {
                int intValue = Integer.parseInt(value);
                set(key, intValue);
            } catch (NumberFormatException e) {
                return false;
            }
        } else if (type == Boolean.class) {
            if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
                return false;
            }
            boolean boolValue = Boolean.parseBoolean(value);
            set(key, boolValue);
        } else if (type == Double.class) {
            try {
                double doubleValue = Double.parseDouble(value);
                set(key, doubleValue);
            } catch (NumberFormatException e) {
                return false;
            }
        } else if (type == Material.class) {
            Material material = Material.getMaterial(value.toUpperCase());
            if (material == null) {
                return false;
            }
            set(key, material);
        } else if (type == String.class) {
            set(key, value);
        } else {
            return false;
        }
        DimensionSwap.getGamePlugin().saveConfig();
        return true;
    }

    protected void set(String key, Object value) {
        config.set(prefix + "." + key, value);
        DimensionSwap.getGamePlugin().saveConfig();
    }

    public String getString(String key) {
        return config.getString(prefix + "." + key);
    }
    protected int getInt(String key) {
        return config.getInt(prefix + "." + key);
    }
    protected boolean getBoolean(String key) {
        return config.getBoolean(prefix + "." + key);
    }
    protected double getDouble(String key) {
        return config.getDouble(prefix + "." + key);
    }


}
