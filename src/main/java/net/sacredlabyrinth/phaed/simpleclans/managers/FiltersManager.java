package net.sacredlabyrinth.phaed.simpleclans.managers;

import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by zenith4183 on 5/22/16.
 */
public final class FiltersManager {

    private SimpleClans plugin;
    private File filtersConfigFile = null;
    private YamlConfiguration filtersConfig = null;

    public FiltersManager() {
        plugin = SimpleClans.getInstance();

        saveDefaultFiltersConfig();
    }

    public List<String> getFilters() {
        YamlConfiguration filtersConfig = (YamlConfiguration) getFiltersConfig();
        List<String> filters = filtersConfig.getStringList("filters");

        return filters;

    }

    public void saveDefaultFiltersConfig() {
        if (filtersConfigFile == null) {
            filtersConfigFile = new File(plugin.getDataFolder(), "filters.yml");
        }
        if (!filtersConfigFile.exists()) {
            plugin.saveResource("filters.yml", false);
        }
    }

    public void reloadFiltersConfig() {
        if (filtersConfigFile == null) {
            filtersConfigFile = new File(plugin.getDataFolder(), "filters.yml");
        }
        filtersConfig = YamlConfiguration.loadConfiguration(filtersConfigFile);

        try {
            Reader defFiltersConfigStream = new InputStreamReader(plugin.getResource("filters.yml"), "UTF8");
            if (defFiltersConfigStream != null) {
                YamlConfiguration defFiltersConfig =  YamlConfiguration.loadConfiguration(defFiltersConfigStream);
                filtersConfig.setDefaults(defFiltersConfig);
            }
        } catch (UnsupportedEncodingException e) {
            plugin.getLogger().info("Failed to set filters config");
        }
    }

    public FileConfiguration getFiltersConfig() {
        if (filtersConfig == null) {
            reloadFiltersConfig();
        }
        return filtersConfig;
    }

}
