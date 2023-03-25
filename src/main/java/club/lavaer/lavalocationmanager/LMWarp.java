package club.lavaer.lavalocationmanager;

import org.bukkit.Location;
import java.util.UUID;

public class LMWarp {
    public String name;
    public Location location;
    public String date;
    public int stars;
    public UUID author;

    public LMWarp(String name, Location location, String date, int stars, UUID author) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.stars = stars;
        this.author = author;
    }
}
