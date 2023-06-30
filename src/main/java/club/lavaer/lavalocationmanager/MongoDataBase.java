package club.lavaer.lavalocationmanager;

import com.mongodb.*;

import com.mongodb.client.MongoClients;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.*;

import static club.lavaer.lavalocationmanager.LavaLocationManager.DataBase;

public class MongoDataBase {
    private DBCollection WarpCollection;
    private Map LikesMap = new HashMap();
    private List<Map.Entry<UUID, Integer>> SortedLikeList;

    public boolean connect(String ConnectionString, String DataBase, String Collection) {
        MongoClient client;

        try{
            client = new MongoClient(new MongoClientURI(ConnectionString));
        } catch (NullPointerException e) {
            System.out.println("无法连接至数据库!");
            return false;
        }

        DB mcserverdb = client.getDB(DataBase);
        WarpCollection = mcserverdb.getCollection(Collection);
        updateLikesMap();

        return true;
    }

    public boolean updateWarpLoc(UUID uniqueId, String name, Location location) {

        DBObject r = new BasicDBObject("warp", name);
        DBObject found = WarpCollection.findOne(r);
        if (found == null || !DBOtoLMW(found).getAuthor().equals(uniqueId)){
            return false;
        }
        BasicDBObject set = new BasicDBObject("$set", r);
        set.append("$set", new BasicDBObject("location", location.getBlockX() + "/" + location.getBlockY() + "/" + location.getBlockZ() + "/" + location.getWorld().getName()));
        WarpCollection.update(found, set);
        return true;
    }
    public int getAllLikes(){
        ArrayList<LMWarp> AllWarps = DataBase.readAllWarps();

        int sum = 0;
        for(LMWarp i : AllWarps) sum += i.getLikeCount();

        return sum;
    }
    public int getPlayerAllLikes(UUID uuid){
        ArrayList<LMWarp> AllWarps = DataBase.readAllWarps();

        int sum = 0;
        for(LMWarp i : AllWarps) if(i.getAuthor().equals(uuid)) sum += i.getLikeCount();

        return sum;
    }
    private void updateLikesMap(){
        ArrayList<LMWarp> AllWarps = DataBase.readAllWarps();
        for(LMWarp i : AllWarps)
            if(!LikesMap.containsKey(i.getAuthor())) LikesMap.put(i.getAuthor(), getPlayerAllLikes(i.getAuthor()));

        SortedLikeList = new ArrayList<Map.Entry<UUID, Integer>>(LikesMap.entrySet()); //转换为list
        SortedLikeList.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
    }
    public int getRank(UUID uuid){
        for(int i = 0; i < SortedLikeList.size(); i++)
            if(SortedLikeList.get(i).getKey().equals(uuid))
                return (i+1);

        return -1;
    }

    public void storeWarp(String name, LMWarp lmWarp){

        DBObject obj = new BasicDBObject("warp", name);
        Location location = lmWarp.getLocation();

        obj.put("name", lmWarp.getName());
        obj.put("location", location.getBlockX() + "/" + location.getBlockY() + "/" + location.getBlockZ() + "/" + location.getWorld().getName() );
        obj.put("date", lmWarp.getDate());
        obj.put("stars", lmWarp.getStars());
        obj.put("coops", lmWarp.getCoops());
        obj.put("author", lmWarp.getAuthor().toString());

        WarpCollection.insert(obj);
        updateLikesMap();
    }
    public boolean LikeWarp(String name, UUID uuid){
        LMWarp lmWarp = readWarp(name);

        if(!lmWarp.Like(uuid)){
            return false;
        }

        Location location = lmWarp.getLocation();
        DBObject objOld = new BasicDBObject("warp", name);
        DBObject obj = new BasicDBObject("warp", name);
        obj.put("name", lmWarp.getName());
        obj.put("location", location.getBlockX() + "/" + location.getBlockY() + "/" + location.getBlockZ() + "/" + location.getWorld().getName() );
        obj.put("date", lmWarp.getDate());
        obj.put("stars", lmWarp.getStars());
        obj.put("coops", lmWarp.getCoops());
        obj.put("author", lmWarp.getAuthor().toString());

        WarpCollection.update(objOld,obj);
        updateLikesMap();
        return true;
    }
    public boolean changeCoop(UUID operator,String name, boolean flag,UUID uuid){
        LMWarp lmWarp = readWarp(name);

        if(!operator.equals(lmWarp.getAuthor()) || !lmWarp.changeCoop(uuid,flag)){
            return false;
        }

        Location location = lmWarp.getLocation();
        DBObject objOld = new BasicDBObject("warp", name);
        DBObject obj = new BasicDBObject("warp", name);
        obj.put("name", lmWarp.getName());
        obj.put("location", location.getBlockX() + "/" + location.getBlockY() + "/" + location.getBlockZ() + "/" + location.getWorld().getName() );
        obj.put("date", lmWarp.getDate());
        obj.put("stars", lmWarp.getStars());
        obj.put("coops", lmWarp.getCoops());
        obj.put("author", lmWarp.getAuthor().toString());

        WarpCollection.update(objOld,obj);
        updateLikesMap();
        return true;
    }
    public boolean DelWarp(UUID uniqueId,String name){
        DBObject found = WarpCollection.findOne(new BasicDBObject("warp", name));
        if (found == null || !DBOtoLMW(found).getAuthor().equals(uniqueId))
            return false;

        WarpCollection.remove(found);
        updateLikesMap();
        return true;
    }

    public LMWarp readWarp(String name){
        return DBOtoLMW(WarpCollection.findOne(new BasicDBObject("warp", name)));
    }

    public ArrayList<LMWarp> readAllWarps(){
        ArrayList<LMWarp> Warps = new ArrayList<>();
        DBCursor cursor = WarpCollection.find();

        while(cursor.hasNext())
            Warps.add(DBOtoLMW(cursor.next()));

        return Warps;
    }

    public static LMWarp DBOtoLMW(DBObject found){
        if(found == null)
            return null;

        String[] parts = ((String)found.get("location")).split("/");
        Location location = new Location(Bukkit.getServer().getWorld(parts[3]), Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));

        return new LMWarp((String)found.get("name"),location,(String)found.get("date"),(ArrayList<UUID>) found.get("stars"),(ArrayList<UUID>) found.get("coops"),UUID.fromString((String)found.get("author"))) ;
    }

    public String getRankedPlayer(int i) {
        return (SortedLikeList.get(i-1) != null) ? Bukkit.getOfflinePlayer(SortedLikeList.get(i-1).getKey()).getName() : null;
    }
}
