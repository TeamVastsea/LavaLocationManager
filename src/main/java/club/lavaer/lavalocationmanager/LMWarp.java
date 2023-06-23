package club.lavaer.lavalocationmanager;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.UUID;


public class LMWarp implements Comparable<LMWarp>{
    public String name;
    public Location location;
    public String date;
    public ArrayList<UUID> stars;
    public ArrayList<UUID> coops;
    public UUID author;

    public LMWarp(String name, Location location, String date, ArrayList<UUID> stars,ArrayList<UUID> coops, UUID author) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.stars = stars;
        this.coops = coops;
        this.author = author;
    }
    @Override
    public int compareTo(LMWarp o) {
        if (getLikeCount() > o.getLikeCount()) {
            return -1;
        } else if (getLikeCount() < o.getLikeCount()) {
            return 1;
        }
        return 0;
    }
    public boolean Like(UUID uuid){
        for (UUID i : this.stars) {
            if(i.equals(uuid)){
                return false;
            }
        }
        this.stars.add(uuid);
        return true;
    }
    public int getLikeCount(){
        int index = 0;
        for (UUID i : this.stars) {
            index++;
        }
        return index;
    }
}
