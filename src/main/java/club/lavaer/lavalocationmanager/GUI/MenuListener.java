package club.lavaer.lavalocationmanager.GUI;

import club.lavaer.lavalocationmanager.LMWarp;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import static club.lavaer.lavalocationmanager.LavaLocationManager.mg;

public class MenuListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();
        InventoryView inv = player.getOpenInventory();

        if (inv.getTitle().equals(Menu.TITLE)){
            e.setCancelled(true);
            if (e.getRawSlot() < 0 || e.getRawSlot() > e.getInventory().getSize()) {
                return;
            }

            ItemStack clickedItem = e.getCurrentItem();
            String name = clickedItem.getItemMeta().getDisplayName();
            if(mg.readWarp(name) != null){
                LMWarp lmWarp = mg.readWarp(name);
                if(e.isLeftClick()){
                    player.teleport(lmWarp.location);
                }else if(e.isRightClick()){
                    if(mg.LikeWarp(name,player.getUniqueId())){
                        player.sendMessage(ChatColor.GREEN + "点赞成功！当前本传送点赞数：" + ChatColor.WHITE + lmWarp.getLikeCount());
                        new Menu(player).open();
                    }else{
                        player.sendMessage(ChatColor.RED + "你已经点过赞了！");
                    }
                }
            }
        }
    }
}
