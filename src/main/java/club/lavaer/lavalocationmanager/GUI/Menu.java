package club.lavaer.lavalocationmanager.GUI;

import club.lavaer.lavalocationmanager.LMWarp;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.*;

import static club.lavaer.lavalocationmanager.LavaLocationManager.DataBase;

//菜单
public class Menu {
    public Inventory inventory;
    public Player owner;
    public int pageCount;


    //创建文本
    public static final String TITLE = "地标菜单";
    public static final int SLOTS_COUNT = 36;

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
    public Menu(Player player, int page) {
        inventory = Bukkit.createInventory(player, 54, (TITLE+" Page: "+page));
        owner = player;

        //注册物品
        int[] ph = new int[]{0, 1, 2, 3, 5, 6, 7, 8, 46, 47, 48, 49, 50, 51, 52};
        for(int i : ph)
            inventory.setItem(i, ItemReg(Material.STAINED_GLASS_PANE," ",null));


        ArrayList<LMWarp> AllWarps = DataBase.readAllWarps();
        this.pageCount = (AllWarps.size()/SLOTS_COUNT) + 1;

        if(page == 1)
            inventory.setItem(45, ItemReg(Material.BARRIER," ",null));
        else
            inventory.setItem(45, ItemReg(Material.ARROW,"Last Page",makeLore(page + "/" + pageCount)));

        if(page == pageCount)
            inventory.setItem(53, ItemReg(Material.BARRIER," ",null));
        else
            inventory.setItem(53, ItemReg(Material.ARROW,"Next Page",makeLore(page + "/" + pageCount)));


        DecimalFormat df = new DecimalFormat("0.00");
        int index = 0;

        for(int i = (page-1)*SLOTS_COUNT ; i<= page*SLOTS_COUNT-1 ; i++){
            LMWarp warp;
            try {
                warp = AllWarps.get(i);
            }catch (IndexOutOfBoundsException e){
                break;
            }

            index ++;
            StringBuilder coopsInformation = new StringBuilder().append("| ");
            for(UUID j : warp.getCoops())
                coopsInformation.append(Bukkit.getOfflinePlayer(j).getName()).append(" | ");


            ArrayList<String> lore = new ArrayList<>();
            lore.add(" ");
            lore.add("------------------------");
            lore.add("创建者: "+ Bukkit.getOfflinePlayer(warp.getAuthor()).getName() +"");
            lore.add("协作者: " + coopsInformation +"");
            lore.add("获赞数: "+ warp.getLikeCount() +"");
            lore.add("坐标: "+df.format(warp.getLocation().getX()) + " / " + df.format(warp.getLocation().getY()) + " / " + df.format(warp.getLocation().getZ()) + "");
            lore.add("创建时间: "+ warp.getDate() +"");
            lore.add("------------------------");
            lore.add(" ");
            lore.add("左键传送");
            lore.add("右键点赞");
            ItemStack itemStack = ItemReg(Material.EYE_OF_ENDER, warp.getName(), lore);
            inventory.setItem((8+index), itemStack);
        }

        Collections.sort(AllWarps);
        ArrayList<String> Dlore = new ArrayList<>();
        Dlore.add("---------点赞排行---------");
        int tmp = Math.min(AllWarps.size(), 10);
        for(int i = 0; i <= (tmp-1); i++){
            Dlore.add("  " + (i+1) + ". " + AllWarps.get(i).getName() + "  by " + Bukkit.getOfflinePlayer(AllWarps.get(i).getAuthor()).getName() + "  点赞数:"+ AllWarps.get(i).getLikeCount() );
        }

        Dlore.add("------------------------");
        inventory.setItem(4, ItemReg(Material.DIAMOND_BLOCK,"传送点点赞数排行榜",Dlore));

    }

    //打开菜单
    public void open() {
        owner.openInventory(inventory);
    }
}
