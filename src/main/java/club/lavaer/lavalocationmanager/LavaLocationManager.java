package club.lavaer.lavalocationmanager;

import club.lavaer.lavalocationmanager.GUI.MenuListener;
import club.lavaer.lavalocationmanager.commands.LocationManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.logging.Logger;

public final class LavaLocationManager extends JavaPlugin {
    private static final Logger log = Logger.getLogger("Minecraft");
    public static JavaPlugin instance;

    public static MonGoDB mg = new MonGoDB();   //数据库

    @Override
    public void onEnable() {
        instance = this;

        mg.connect("localhost", 27017);     //连接数据库
        this.getCommand("LocationManager").setExecutor(new LocationManager());
        Bukkit.getPluginManager().registerEvents(new MenuListener(), this);

        log.info("[LavaLocationManager] LavaLocationManager loaded.");
    }

    @Override
    public void onDisable() {
        log.info("[LavaLocationManager] LavaLocationManager disabled.");
    }
}
