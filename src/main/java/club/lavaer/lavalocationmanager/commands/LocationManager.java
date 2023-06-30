package club.lavaer.lavalocationmanager.commands;

import club.lavaer.lavalocationmanager.GUI.Menu;
import club.lavaer.lavalocationmanager.LMWarp;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static club.lavaer.lavalocationmanager.LavaLocationManager.DataBase;

public class LocationManager implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "只有玩家才能执行此命令！");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            new Menu(player, 1).open();
            return true;
        }

        switch (args[0]) {
            case "?":
            case "help": {
                String HELP = "------LM help------ \n" +
                        "打开gui: /lm \n" +
                        "创建地标: /lm setwarp <Name> [warpCo-Author1[,warpCo-Author2,.., warpCo-AuthorN]] \n" +
                        "编辑已有地标: /lm edit <warpSerial> \n" +
                        "传送玩家至地标: /lm tp <player> <warpSerial> \n" +
                        "重设地标位置：/lm resetloc <warpSerial> \n" +
                        "列出所有可用地标：/lm list \n" +
                        "添加协作者: /lm addco <warpSerial> <player> \n" +
                        "移除地标 /lm rmwarp <warpSerial> \n" +
                        "移除协作者 /lm rmco <warpSerial> <player> \n" +
                        "点赞 /lm like <warpSerial> \n" +
                        "确认 /lm confirm \n";
                player.sendMessage(HELP);
                break;
            }

            case "like": {
                if (args.length < 2) {
                    player.sendMessage(ChatColor.RED + "参数错误 地标名不得为空");
                    break;
                }
                if (!DataBase.LikeWarp(args[1], player.getUniqueId())) {
                    player.sendMessage(ChatColor.RED + "你已经点过赞了！");
                    break;
                }
                player.sendMessage(ChatColor.GREEN + "点赞成功！当前本传送点赞数： " + ChatColor.WHITE + DataBase.readWarp(args[1]).getLikeCount());
                break;
            }
            case "setwarp": {
                if (args.length < 2) {
                    player.sendMessage(ChatColor.RED + "地标名不得为空");
                    break;
                }

                String WarpName = args[1];

                if (DataBase.readWarp(WarpName) != null) {
                    player.sendMessage(ChatColor.RED + "地标重复");
                    break;
                }

                SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                DecimalFormat df = new DecimalFormat("0.00");
                Date dNow = new Date();
                String date = ft.format(dNow);

                int index = 1;
                ArrayList<UUID> coops = new ArrayList<>();
                while (args.length >= (index + 2)) {
                    coops.add(Bukkit.getOfflinePlayer(args[index + 1]).getUniqueId());
                    index++;
                }

                Location location = player.getLocation();

                LMWarp warp = new LMWarp(WarpName, location, date, new ArrayList<>(), coops, player.getUniqueId());
                DataBase.storeWarp(warp.getName(), warp);

                StringBuilder coopsInformation = new StringBuilder().append("| ");
                for(UUID uuid : coops) coopsInformation.append(Bukkit.getOfflinePlayer(uuid).getName()).append(" | ");

                String Success = "\n" +
                        "------------------------ \n" +
                        "您创建了一个地标 \n" +
                        "创建者: " + player.getName() + " \n" +
                        "协作者:  " + coopsInformation + " \n" +
                        "名称: " + WarpName + " \n" +
                        "坐标: " + df.format(location.getX()) + " / " + df.format(location.getY()) + " / " + df.format(location.getZ()) + " \n" +
                        "创建时间 " + date + " \n" +
                        "------------------------ \n" +
                        "\n";

                player.sendMessage(Success);
                break;
            }

            case "ls":
            case "list": {

                player.sendMessage("正在为您查找所有可用地标");

                ArrayList<LMWarp> AllWarps = DataBase.readAllWarps();
                for (LMWarp warp : AllWarps)
                    player.sendMessage(Bukkit.getOfflinePlayer(warp.getAuthor()).getName() + " 的 " + warp.getName() + "\n");

                player.sendMessage("以上共" + AllWarps.size() + "个,更多信息请查询GUI");
                break;
            }

            case "resetloc": {
                if (args.length < 2) {
                    player.sendMessage(ChatColor.RED + "地标名不得为空");
                    break;
                }

                Location location = player.getLocation();
                String WarpName = args[1];
                LMWarp lmWarp = DataBase.readWarp(WarpName);

                if (lmWarp == null) {
                    player.sendMessage(ChatColor.RED + "地标名不存在");
                    break;
                }

                if (DataBase.updateWarpLoc(player.getUniqueId(), WarpName, location))
                    player.sendMessage(ChatColor.GREEN + "操作成功");
                else
                    player.sendMessage(ChatColor.RED + "操作失败");

                break;
            }
            case "edit": {
                player.sendMessage("edit");
                break;
            }

            case "tp":
            case "teleport": {
                if (args.length < 2) {
                    player.sendMessage(ChatColor.RED + "参数错误 地标名不得为空");
                    break;
                }

                String WarpName = args[1];
                LMWarp lmWarp = DataBase.readWarp(WarpName);

                if (lmWarp == null) {
                    player.sendMessage(ChatColor.RED + "地标名不存在");
                    break;
                }

                player.teleport(lmWarp.getLocation());
                break;

            }
            case "addco": {
                if (args.length < 3) {
                    player.sendMessage(ChatColor.RED + "参数错误");
                    break;
                }

                UUID uuid = Bukkit.getOfflinePlayer(args[2]).getUniqueId();
                String WarpName = args[1];
                LMWarp lmWarp = DataBase.readWarp(WarpName);

                if (lmWarp == null) {
                    player.sendMessage(ChatColor.RED + "地标名不存在");
                    break;
                }

                if (DataBase.changeCoop(player.getUniqueId(), WarpName, true, uuid))
                    player.sendMessage(ChatColor.GREEN + "操作成功");
                else
                    player.sendMessage(ChatColor.RED + "操作失败");

                break;
            }
            case "rmco": {
                if (args.length < 3) {
                    player.sendMessage(ChatColor.RED + "参数错误");
                    break;
                }

                UUID uuid = Bukkit.getOfflinePlayer(args[2]).getUniqueId();
                String WarpName = args[1];
                LMWarp lmWarp = DataBase.readWarp(WarpName);

                if (lmWarp == null) {
                    player.sendMessage(ChatColor.RED + "地标名不存在");
                    break;
                }

                if (DataBase.changeCoop(player.getUniqueId(), WarpName, false, uuid))
                    player.sendMessage(ChatColor.GREEN + "操作成功");
                else
                    player.sendMessage(ChatColor.RED + "操作失败");

                break;
            }
            case "rmwarp": {
                if (args.length < 2) {
                    player.sendMessage(ChatColor.RED + "参数错误 地标名不得为空");
                    break;
                }

                String WarpName = args[1];
                LMWarp lmWarp = DataBase.readWarp(WarpName);

                if (lmWarp == null) {
                    player.sendMessage(ChatColor.RED + "地标名不存在");
                    break;
                }

                if (DataBase.DelWarp(player.getUniqueId(), WarpName))
                    player.sendMessage(ChatColor.GREEN + "操作成功");
                else
                    player.sendMessage(ChatColor.RED + "操作失败");

                break;
            }
            default:{
                player.sendMessage(ChatColor.RED + "未知指令");
                break;
            }
        }
        return true;
    }
}
