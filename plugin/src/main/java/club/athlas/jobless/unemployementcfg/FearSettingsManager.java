package club.athlas.jobless.unemployementcfg;

import club.athlas.jobless.Jobless;
import club.athlas.jobless.unemployementcfg.constantfear.UnemploymentSetting;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public class FearSettingsManager {

    private final Jobless plugin;

    private FileConfiguration config;

    public FearSettingsManager(Jobless plugin) {
        this.plugin = plugin;
    }

    public FearSettingsManager load() {
        plugin.saveDefaultConfig();
        config = plugin.getConfig();

        return this;
    }

    public void reassessLifeChoices() {
        plugin.reloadConfig();
        config = plugin.getConfig();
    }

    public String getString(@NotNull UnemploymentSetting unemploymentSetting, String defaultValue) {
        String s = config.getString(unemploymentSetting.getPath());

        if (s == null || s.isEmpty()) {
            printInvalidValueWarning(unemploymentSetting, String.valueOf(defaultValue), true);
            return defaultValue;
        }

        return s;
    }

    public boolean getBool(@NotNull UnemploymentSetting unemploymentSetting, boolean defaultValue) {
        if (!config.isSet(unemploymentSetting.getPath())) {
            printInvalidValueWarning(unemploymentSetting, String.valueOf(defaultValue), true);
            return defaultValue;
        }

        String stringValue = config.getString(unemploymentSetting.getPath());
        if (!(stringValue != null && (stringValue.equalsIgnoreCase("true") || stringValue.equalsIgnoreCase("false")))) {
            printInvalidValueWarning(unemploymentSetting, String.valueOf(defaultValue), false);
            return defaultValue;
        }

        return config.getBoolean(unemploymentSetting.getPath());
    }

    public long getLong(@NotNull UnemploymentSetting unemploymentSetting, long defaultValue) {
        if (config.get(unemploymentSetting.getPath()) == null) {
            printInvalidValueWarning(unemploymentSetting, String.valueOf(defaultValue), true);
            return defaultValue;
        }

        try {
            String stringValue = config.getString(unemploymentSetting.getPath());
            if (stringValue == null) {
                printInvalidValueWarning(unemploymentSetting, String.valueOf(defaultValue), true);
                return defaultValue;
            }

            Long.parseLong(stringValue);
            return config.getLong(unemploymentSetting.getPath());
        } catch (NumberFormatException e) {
            return defaultValue;
        }

    }

    public <E extends Enum<E>> E getEnum(@NotNull Class<E> enumClass, @NotNull UnemploymentSetting setting, @NotNull E defaultValue) {
        if (!config.isSet(setting.getPath())) {
            printInvalidValueWarning(setting, defaultValue.name(), true);
            return defaultValue;
        }

        String stringValue = config.getString(setting.getPath());
        if (stringValue == null) {
            printInvalidValueWarning(setting, defaultValue.name(), false);
            return defaultValue;
        }

        try {
            return Enum.valueOf(enumClass, stringValue.toUpperCase());
        } catch (IllegalArgumentException e) {
            printInvalidValueWarning(setting, defaultValue.name(), false);
            return defaultValue;
        }
    }

    private void printInvalidValueWarning(@NotNull UnemploymentSetting key, String defaultValue, boolean empty) {
        plugin.getLogger().warning("[Config] " + (empty ? "Empty" : "Invalid") + " value found in " + key.getPath() +" in config.yml! Using the default one -> " + defaultValue);
    }

}
