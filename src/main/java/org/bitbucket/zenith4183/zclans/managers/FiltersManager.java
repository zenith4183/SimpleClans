package org.bitbucket.zenith4183.zclans.managers;

import org.bitbucket.zenith4183.zclans.zClans;
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

    private zClans plugin;
    private File filtersConfigFile = null;
    private YamlConfiguration filtersConfig = null;

    public FiltersManager() {
        plugin = zClans.getInstance();

        saveDefaultFiltersConfig();
    }

    public List<String> getFilters() {
        YamlConfiguration filtersConfig = (YamlConfiguration) getFiltersConfig();
        return filtersConfig.getStringList("filters");
    }

    private void saveDefaultFiltersConfig() {
        if (filtersConfigFile == null) {
            filtersConfigFile = new File(plugin.getDataFolder(), "filters.yml");
        }
        if (!filtersConfigFile.exists()) {
            plugin.saveResource("filters.yml", false);
        }
    }

    private void reloadFiltersConfig() {
        if (filtersConfigFile == null) {
            filtersConfigFile = new File(plugin.getDataFolder(), "filters.yml");
        }
        filtersConfig = YamlConfiguration.loadConfiguration(filtersConfigFile);

        try {
            Reader defFiltersConfigStream = new InputStreamReader(plugin.getResource("filters.yml"), "UTF8");
            YamlConfiguration defFiltersConfig =  YamlConfiguration.loadConfiguration(defFiltersConfigStream);
            filtersConfig.setDefaults(defFiltersConfig);
        } catch (UnsupportedEncodingException e) {
            plugin.getLogger().info("Failed to set filters config");
        }
    }

    private FileConfiguration getFiltersConfig() {
        if (filtersConfig == null) {
            reloadFiltersConfig();
        }
        return filtersConfig;
    }

}
