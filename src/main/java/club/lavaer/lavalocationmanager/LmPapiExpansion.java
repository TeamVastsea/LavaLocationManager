package club.lavaer.lavalocationmanager;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.jetbrains.annotations.NotNull;

import static club.lavaer.lavalocationmanager.LavaLocationManager.mg;

public class LmPapiExpansion extends PlaceholderExpansion{
    private final LavaLocationManager plugin;

    public LmPapiExpansion(LavaLocationManager plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getAuthor() {
        return "Lavander";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "lmwarp";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(params.equalsIgnoreCase("all_likes")){
            return (mg.getAllLikes() + "");
        }
        if(params.equalsIgnoreCase("player_all_likes")){
            return (mg.getPlayerAllLikes(player.getUniqueId()) + "");
        }
        if(params.equalsIgnoreCase("player_rank")){
            return (mg.getRank(player.getUniqueId()) + "");
        }
        return null;
    }
}
