package club.lavaer.lavalocationmanager.GUI;

import club.lavaer.lavalocationmanager.LMWarp;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

import static club.lavaer.lavalocationmanager.LavaLocationManager.DataBase;

public class MenuListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();
        InventoryView inv = player.getOpenInventory();

        if (inv.getTitle().contains(Menu.TITLE)){
            e.setCancelled(true);

            int i = Integer.parseInt(inv.getTitle().substring(inv.getTitle().length()-1));

            if (e.getRawSlot() < 0 || e.getRawSlot() > e.getInventory().getSize() || e.getCurrentItem() == null)
                return;

            ItemStack clickedItem = e.getCurrentItem();
            String name = clickedItem.getItemMeta().getDisplayName();

            if(Objects.equals(name, "Next Page"))
                new Menu(player,i+1).open();
            else if(Objects.equals(name, "Last Page"))
                new Menu(player,i-1).open();

            if(DataBase.readWarp(name) == null){
                return;
            }
                LMWarp lmWarp = DataBase.readWarp(name);
                if(e.isLeftClick()){
                    player.teleport(lmWarp.getLocation());
                }else if(e.isRightClick()){
                    if(!DataBase.LikeWarp(name,player.getUniqueId())){
                        player.sendMessage(ChatColor.RED + "你已经点过赞了！");
                        return;
                    }

                    player.sendMessage(ChatColor.GREEN + "点赞成功！当前本传送点赞数：" + ChatColor.WHITE + lmWarp.getLikeCount());
                    new Menu(player,1).open();
                }

        }
    }
}
