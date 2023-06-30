package club.lavaer.lavalocationmanager;

import org.bukkit.OfflinePlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.jetbrains.annotations.NotNull;

import static club.lavaer.lavalocationmanager.LavaLocationManager.DataBase;

public class LmPapiExpansion extends PlaceholderExpansion{
    private final LavaLocationManager plugin;

    public LmPapiExpansion(LavaLocationManager plugin) { this.plugin = plugin; }

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
    public boolean persist() { return true; }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        switch (params.toLowerCase()){
            case "all_likes":
                return DataBase.getAllLikes() + "";
            case "player_all_likes":
                return DataBase.getPlayerAllLikes(player.getUniqueId()) + "";
            case "player_rank" :
                return DataBase.getRank(player.getUniqueId()) + "";
        }
        return null;
    }
}
