package club.lavaer.lavalocationmanager;

import club.lavaer.lavalocationmanager.GUI.MenuListener;
import club.lavaer.lavalocationmanager.commands.LocationManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class LavaLocationManager extends JavaPlugin {

    private static final Logger log = Logger.getLogger("Minecraft");
    public static JavaPlugin instance;
    public static MongoDataBase DataBase = new MongoDataBase();   //数据库

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getLogger().warning("Could not find PlaceholderAPI! This plugin is required.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        if(!DataBase.connect("localhost", 27017, "LMC", "warps")){
            getLogger().warning("DataBaseError");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        instance = this;
        this.getCommand("LocationManager").setExecutor(new LocationManager());
        Bukkit.getPluginManager().registerEvents(new MenuListener(), this);
        new LmPapiExpansion(this).register();

        log.info("[LavaLocationManager] LavaLocationManager loaded.");
    }

    @Override
    public void onDisable() {
        log.info("[LavaLocationManager] LavaLocationManager disabled.");
    }
}
