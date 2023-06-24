package club.lavaer.lavalocationmanager;

import com.mongodb.*;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.UUID;

public class MonGoDB {
    private DBCollection warps;
    private DB mcserverdb;
    private MongoClient client;

    public boolean connect(String ip, int port) {
        //连接到指定的IP和端口
        //默认为localhost, 27017
        try {
            client = new MongoClient(ip, port);
        } catch (NullPointerException e) {
            System.out.println("无法连接至数据库!");
            e.printStackTrace();
            return false;
        }
        //获取名为 "LMC" 的数据库
        //如果它不存在，将被自动创建, 一旦你在其中保存了一些东西
        mcserverdb = client.getDB("LMC");
        //获取数据库中的集合。
        //相当于MySQL中的表，你可以在这里存储对象
        warps = mcserverdb.getCollection("warps");

        return true;
    }
    public boolean updateWarpLoc(UUID uniqueId, String name, Location location) {

        DBObject r = new BasicDBObject("warp", name);
        DBObject found = warps.findOne(r);
        if (found == null || !DBOtoLMW(found).author.equals(uniqueId)){
            return false;
        }
        BasicDBObject set = new BasicDBObject("$set", r);
        set.append("$set", new BasicDBObject("location", location.getBlockX() + "/" + location.getBlockY() + "/" + location.getBlockZ() + "/" + location.getWorld().getName()));
        warps.update(found, set);
        return true;
    }
    public void storeWarp(String name, LMWarp lmWarp){

        Location location = lmWarp.location;

        DBObject obj = new BasicDBObject("warp", name);
        obj.put("name", lmWarp.name);
        obj.put("location", location.getBlockX() + "/" + location.getBlockY() + "/" + location.getBlockZ() + "/" + location.getWorld().getName() );
        obj.put("date", lmWarp.date);
        obj.put("stars", lmWarp.stars);
        obj.put("coops", lmWarp.coops);
        obj.put("author", lmWarp.author.toString());

        warps.insert(obj);
    }
    public boolean LikeWarp(String name, UUID uuid){
        LMWarp lmWarp = readWarp(name);

        if(!lmWarp.Like(uuid)){
            return false;
        }

        Location location = lmWarp.location;
        DBObject objOld = new BasicDBObject("warp", name);
        DBObject obj = new BasicDBObject("warp", name);
        obj.put("name", lmWarp.name);
        obj.put("location", location.getBlockX() + "/" + location.getBlockY() + "/" + location.getBlockZ() + "/" + location.getWorld().getName() );
        obj.put("date", lmWarp.date);
        obj.put("stars", lmWarp.stars);
        obj.put("coops", lmWarp.coops);
        obj.put("author", lmWarp.author.toString());

        warps.update(objOld,obj);
        return true;
    }
    public boolean changeCoop(UUID operator,String name, boolean flag,UUID uuid){
        LMWarp lmWarp = readWarp(name);

        if(!operator.equals(lmWarp.author) || !lmWarp.changeCoop(uuid,flag)){
            return false;
        }

        Location location = lmWarp.location;
        DBObject objOld = new BasicDBObject("warp", name);
        DBObject obj = new BasicDBObject("warp", name);
        obj.put("name", lmWarp.name);
        obj.put("location", location.getBlockX() + "/" + location.getBlockY() + "/" + location.getBlockZ() + "/" + location.getWorld().getName() );
        obj.put("date", lmWarp.date);
        obj.put("stars", lmWarp.stars);
        obj.put("coops", lmWarp.coops);
        obj.put("author", lmWarp.author.toString());

        warps.update(objOld,obj);
        return true;
    }
    public boolean DelWarp(UUID uniqueId,String name){
        DBObject r = new BasicDBObject("warp", name);
        DBObject found = warps.findOne(r);
        if (found == null || !DBOtoLMW(found).author.equals(uniqueId)){
            return false;
        }
        warps.remove(found);
        return true;
    }
    public LMWarp readWarp(String name){
        DBObject obj = new BasicDBObject("warp", name);
        DBObject found = warps.findOne(obj);
        if(found == null){
            return null;
        }
        return DBOtoLMW(found) ;
    }
    public ArrayList<LMWarp> readAllWarps(){
        ArrayList<LMWarp> Warps = new ArrayList<>();
        //DBObject r = new BasicDBObject("warp", name);
        DBCursor cursor = warps.find();

        while(cursor.hasNext()){
            Warps.add(DBOtoLMW(cursor.next()));
        }
        return Warps;
    }
    public static LMWarp DBOtoLMW(DBObject found){
        String LocStr = (String) found.get("location");
        String[] parts = LocStr.split("/");
        Location location = new Location(Bukkit.getServer().getWorld(parts[3]), Integer.parseInt(parts[0]),
                Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
        return new LMWarp((String)found.get("name"),location,(String)found.get("date"),(ArrayList<UUID>) found.get("stars"),(ArrayList<UUID>) found.get("coops"),UUID.fromString((String)found.get("author"))) ;
    }
}
