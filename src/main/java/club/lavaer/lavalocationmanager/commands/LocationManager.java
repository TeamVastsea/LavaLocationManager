package club.lavaer.lavalocationmanager.commands;

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
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import static club.lavaer.lavalocationmanager.LavaLocationManager.mg;

public class LocationManager implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length >= 1) {
                switch (args[0]) {
                    case "?":
                    case "help": {
                        //帮助
                        player.sendMessage("------LM help------ \n" +
                                "打开gui: /lm \n" +
                                "创建地标: /lm setwarp <Name> [warpCo-Author1[,warpCo-Author2,.., warpCo-AuthorN]] \n" +
                                "编辑已有地标: /lm edit <warpSerial> \n" +
                                "传送玩家至地标: /lm tp <player> <warpSerial> \n" +
                                "重设地标位置：/lm resetloc <warpSerial> \n" +
                                "列出所有可用地标：/lm list \n" +
                                "添加协作者: /lm addco <warpSerial> <player> \n" +
                                "移除地标 /lm rmwarp <warpSerial> \n" +
                                "移除协作者 /lm rmco <player> \n" +
                                "确认 /lm confirm \n"
                        );
                        break;
                    }

                    case "setwarp": {
                        //设置地标
                        if (args.length >= 2) {
                            String WarpName = args[1];
                            //查重
                            if(mg.readWarp(WarpName) != null){
                                player.sendMessage(ChatColor.RED + "地标重复");
                                break;
                            }

                            //临时格式化器
                            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            DecimalFormat df = new DecimalFormat("0.00");

                            //存储时间
                            Date dNow = new Date();
                            String date = ft.format(dNow);


                            //将地标存储到数据库
                            Location location = player.getLocation();
                            LMWarp warp = new LMWarp(WarpName,location,date,0,player.getUniqueId());
                            mg.storeWarp(warp.name, warp);

                            player.sendMessage("\n" +
                                    "------------------------ \n" +
                                    "您创建了一个地标 \n" +
                                    "创建者: " + player.getName() + " \n" +
                                    "协作者:  warp Co-Author list \n" +
                                    "名称: " + WarpName + " \n" +
                                    "坐标: " + df.format(location.getX()) + " / " + df.format(location.getY()) + " / " + df.format(location.getZ()) + " \n" +
                                    "创建时间 " + date + " \n" +
                                    "------------------------ \n" +
                                    "\n"
                            );
                        } else {
                            player.sendMessage(ChatColor.RED + "地标名不得为空");
                        }
                        break;
                    }
                    case "ls":
                    case "list": {
                        int index = 0;

                        player.sendMessage("正在为您查找所有可用地标");
                        for(LMWarp warp : mg.readAllWarps()){
                            index ++;
                            player.sendMessage(Bukkit.getOfflinePlayer(warp.author).getName() +" 的 "+ warp.name +"\n");
                        }
                        player.sendMessage("以上, 共" + index + "个");
                        break;
                    }

                    case "resetloc": {
                        if (args.length >= 2) {
                            Location location = player.getLocation();
                            String WarpName = args[1];
                            LMWarp lmWarp = mg.readWarp(WarpName);

                            if(lmWarp != null){
                                mg.updateWarpLoc(WarpName,location);
                            }else{
                                player.sendMessage(ChatColor.RED + "地标名不存在");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "地标名不得为空");
                        }
                        break;
                    }
                    case "edit": {

                        player.sendMessage("edit");
                        break;
                    }

                    case "tp":
                    case "teleport": {
                        //传送到地标

                        if (args.length >= 2) {
                            String WarpName = args[1];
                            LMWarp lmWarp = mg.readWarp(WarpName);

                            if(lmWarp != null){
                                player.teleport(lmWarp.location);
                            }else{
                                player.sendMessage(ChatColor.RED + "地标名不存在");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "地标名不得为空");
                        }
                        break;

                    }
                }
            }else{
                //打开GUI
            }


        } else {
            sender.sendMessage(ChatColor.DARK_RED + "只有玩家才能执行此命令！");
        }
        return true;
    }
}
