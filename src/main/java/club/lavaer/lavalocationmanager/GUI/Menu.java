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
    public int pageCount;
    //创建文本
    public static final String TITLE = "地标菜单";

    //注册物品的方法
    public ItemStack ItemReg(Material material, String name, ArrayList<String> lorearr){
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(lorearr);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    public ArrayList<String> makeLore(String str){
        ArrayList<String> arr = new ArrayList<>();
        arr.add(str);
        return arr;
    }

    //构造函数，并将物品注册进菜单栏
    public Menu(Player player,int page) {
        components = Bukkit.createInventory(player, 54, (TITLE+" Page: "+page));
        owner = player;

        //注册物品
        int[] ph = new int[]{0, 1, 2, 3, 5, 6, 7, 8, 46, 47, 48, 49, 50, 51, 52};
        for(int i : ph){
            components.setItem(i, ItemReg(Material.STAINED_GLASS_PANE," ",null));
        }

        ArrayList<LMWarp> AllWarps = mg.readAllWarps();
        this.pageCount = (AllWarps.size()/36) + 1;
        if(page == 1){
            components.setItem(45, ItemReg(Material.BARRIER," ",null));
        }else{
            components.setItem(45, ItemReg(Material.ARROW,"Last Page",makeLore(page + "/" + pageCount)));
        }

        if(page == pageCount){
            components.setItem(53, ItemReg(Material.BARRIER," ",null));
        }else{
            components.setItem(53, ItemReg(Material.ARROW,"Next Page",makeLore(page + "/" + pageCount)));
        }

        DecimalFormat df = new DecimalFormat("0.00");
        int index = 0;
        for(int i = (page-1)*36; i<=page*36-1; i++){
            LMWarp warp = null;
            try {
                warp = AllWarps.get(i);
            }catch (IndexOutOfBoundsException e){
                break;
            }

            index ++;
            StringBuilder coopsInformation = new StringBuilder();

            coopsInformation.append("| ");
            for(UUID j : warp.coops){
                coopsInformation.append(Bukkit.getOfflinePlayer(j).getName()).append(" | ");
            }

            ArrayList<String> lore = new ArrayList<>();
            lore.add(" ");
            lore.add("------------------------");
            lore.add("创建者: "+ Bukkit.getOfflinePlayer(warp.author).getName() +"");
            lore.add("协作者: " + coopsInformation +"");
            lore.add("获赞数: "+ warp.getLikeCount() +"");
            lore.add("坐标: "+df.format(warp.location.getX()) + " / " + df.format(warp.location.getY()) + " / " + df.format(warp.location.getZ()) + "");
            lore.add("创建时间: "+ warp.date +"");
            lore.add("------------------------");
            lore.add(" ");
            lore.add("左键传送");
            lore.add("右键点赞");
            ItemStack itemStack = ItemReg(Material.EYE_OF_ENDER, warp.name, lore);
            components.setItem((8+index), itemStack);
        }

        Collections.sort(AllWarps);
        ArrayList<String> Dlore = new ArrayList<String>();
        Dlore.add("---------点赞排行---------");
        int tmp = Math.min(AllWarps.size(), 10);
        for(int i = 0; i <= (tmp-1); i++){
            Dlore.add("  " + (i+1) + ". " + AllWarps.get(i).name + "  by " + Bukkit.getOfflinePlayer(AllWarps.get(i).author).getName() + "  点赞数:"+ AllWarps.get(i).getLikeCount() );
        }

        Dlore.add("------------------------");
        components.setItem(4, ItemReg(Material.DIAMOND_BLOCK,"传送点点赞数排行榜",Dlore));

    }

    //打开菜单
    public void open() {
        owner.openInventory(components);
    }
}
