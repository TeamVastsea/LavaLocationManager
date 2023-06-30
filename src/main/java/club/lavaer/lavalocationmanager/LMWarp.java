package club.lavaer.lavalocationmanager;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.UUID;


public class LMWarp implements Comparable<LMWarp>{
    private String name;
    private Location location;
    private String date;
    private ArrayList<UUID> stars;
    private ArrayList<UUID> coops;
    private UUID author;

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
        if (getLikeCount() > o.getLikeCount())
            return -1;
        else if (getLikeCount() < o.getLikeCount())
            return 1;
        return 0;
    }

    public boolean Like(UUID uuid){
        for (UUID i : this.stars)
            if(i.equals(uuid))
                return false;

        this.stars.add(uuid);
        return true;
    }
    public boolean changeCoop(UUID uuid, boolean flag){
        if(flag){

            for (UUID i : this.coops)
                if(i.equals(uuid))
                    return false;

            this.coops.add(uuid);
            return true;

        }else{

            for (UUID i : this.coops)
                if(i.equals(uuid)){
                    this.coops.remove(uuid);
                    return true;
                }

        }
        return false;
    }

    public int getLikeCount(){
        return this.stars.size();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public UUID getAuthor() {
        return author;
    }

    public void setAuthor(UUID author) {
        this.author = author;
    }

    public ArrayList<UUID> getStars(){
        return stars;
    }

    public void setStars(ArrayList<UUID> stars){
        this.stars = stars;
    }

    public ArrayList<UUID> getCoops(){
        return coops;
    }
    public void setCoops(ArrayList<UUID> coops){
        this.coops = coops;
    }
}
