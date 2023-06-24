package club.lavaer.lavalocationmanager;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.jetbrains.annotations.NotNull;

public class LmPapiExpansion extends PlaceholderExpansion{
    private final LavaLocationManager plugin;

    public LmPapiExpansion(LavaLocationManager plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getAuthor() {
        return "Lavander";
    }

    @Override
    public String getIdentifier() {
        return "LM";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(params.equalsIgnoreCase("placeholder1")){
            return plugin.getConfig().getString("placeholders.placeholder1", "default1");
        }
        if(params.equalsIgnoreCase("test")){
            return "233";
        }
        if(params.equalsIgnoreCase("placeholder2")) {
            return plugin.getConfig().getString("placeholders.placeholder2", "default2");
        }

        return null; // Placeholder is unknown by the Expansion
    }
}
