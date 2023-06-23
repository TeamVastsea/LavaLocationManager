package club.lavaer.lavalocationmanager.GUI;

import club.lavaer.lavalocationmanager.LMWarp;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.*;

import static club.lavaer.lavalocationmanager.LavaLocationManager.mg;

//菜单
public class Menu {
    public Inventory components;
    public Player owner;
    //创建文本
    public static final String TITLE = "地标菜单";

    //注册物品的方法
    public ItemStack ItemReg(Material material, String name, String loreqw){
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        ArrayList<String> arr =  new ArrayList<String>();
        arr.add(loreqw);
        System.out.println(arr);
        itemMeta.setLore(arr);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    //构造函数，并将物品注册进菜单栏
    public Menu(Player player) {
        components = Bukkit.createInventory(player, 54, TITLE);
        owner = player;
        //注册物品
        int[] ph = new int[]{0, 1, 2, 3, 5, 6, 7, 8, 46, 47, 48, 50, 51, 52};
        for(int i : ph){
            components.setItem(i, ItemReg(Material.STAINED_GLASS_PANE," "," 123\n 123"));
        }
        DecimalFormat df = new DecimalFormat("0.00");

        int index = 0;
        for(LMWarp warp : mg.readAllWarps()){
            index ++;


            Iterator<UUID> iterate = warp.coops.iterator();
            StringBuilder coopsInformation = new StringBuilder();

            //使用Iterator的方法访问元素
            coopsInformation.append("| ");
            while(iterate.hasNext()){
                coopsInformation.append(Bukkit.getOfflinePlayer(iterate.next()).getName()).append(" | ");
            }


            String lorere = "\n" +
                    "------------------------\n" +
                    "创建者: "+ Bukkit.getOfflinePlayer(warp.author).getName() +"\n" +
                    "协作者: " + coopsInformation +"\n" +
                    "获赞数: "+ warp.getLikeCount() +"\n" +
                    "坐标: "+df.format(warp.location.getX()) + " / " + df.format(warp.location.getY()) + " / " + df.format(warp.location.getZ()) + "\n" +
                    "创建时间: "+ warp.date +"\n" +
                    "------------------------\n" +
                    "\n" +
                    "左键传送\n" +
                    "右键点赞\n";
            ItemStack itemStack = ItemReg(Material.EYE_OF_ENDER, warp.name, lorere);
            components.setItem((8+index), itemStack);
        }

    }

    //打开菜单
    public void open() {
        owner.openInventory(components);
    }
}
