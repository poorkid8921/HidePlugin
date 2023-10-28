package org.hide;

import net.skinsrestorer.api.SkinsRestorer;
import net.skinsrestorer.api.SkinsRestorerProvider;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Hide extends JavaPlugin {
    public static Hide plugin;
    static FileConfiguration config;
    public static String hidden;
    public static String nolongerhidden;
    public static String name;
    public static String skin;
    public static SkinsRestorer skinsRestorerAPI;

    @Override
    public void onEnable() {
        config = this.getConfig();
        config.addDefault("Hidden",
                "&aYou are now hidden.");
        config.addDefault("No_Longer_Hidden",
                "&aYou are no longer hidden.");
        config.addDefault("Name",
                "&#¤&#¤");
        config.addDefault("Hidden_skin",
                "FelixSculks");
        config.options().copyDefaults(true);
        saveConfig();

        skinsRestorerAPI = SkinsRestorerProvider.get();
        plugin = this;
        load();
        this.getCommand("hide").setExecutor(new comm());
        this.getCommand("ahide").setExecutor(new comm());
    }

    public static void load() {
        hidden = ChatColor.translateAlternateColorCodes('&',
                config.getString("Hidden"));
        nolongerhidden = ChatColor.translateAlternateColorCodes('&',
                config.getString("No_Longer_Hidden"));
        name = ChatColor.translateAlternateColorCodes('&',
                config.getString("Name"));
        skin = config.getString("Hidden_skin");
    }

    @Override
    public void onDisable() {
    }
}
