package sk.matiuss.creward.CReward.commands;

import sk.matiuss.creward.CReward.GUI;
import sk.matiuss.creward.CReward.Main;
import sk.matiuss.creward.CReward.Reward;
import sk.matiuss.creward.CReward.Utilities;
import sk.matiuss.creward.CReward.managers.PlayerManager;
import sk.matiuss.creward.CReward.managers.RewardManager;
import sk.matiuss.creward.CReward.managers.configurations.PluginConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    private GUI gui;
    private PluginConfig pluginConfig;
    private PlayerManager playerManager;
    private RewardManager rewardManager;

    public Commands(GUI gui, PluginConfig pluginConfig, PlayerManager playerManager, RewardManager rewardManager){
        this.gui = gui;
        this.pluginConfig = pluginConfig;
        this.playerManager = playerManager;
        this.rewardManager = rewardManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equalsIgnoreCase("creward") || label.equalsIgnoreCase("cr")){
            if(args.length == 0){
                if(sender instanceof Player){
                    Player p = (Player) sender;
                    gui.openInventory(p);
                }
            }if(args.length == 1){
                if(args[0].equalsIgnoreCase("reload")){
                    if (sender.hasPermission("cr.reload")) {
                        pluginConfig.reloadConfig();
                        rewardManager.loadRewards();
                        if(Main.getPlugin().isDisabled()){
                            sender.sendMessage(pluginConfig.getPrefix() + Utilities.Color("&cErrors occured, check console"));
                        }else {
                            sender.sendMessage(pluginConfig.getPrefix() + pluginConfig.getConfigReload());
                        }
                    }else{
                        Player p = (Player) sender;
                        String msg = pluginConfig.getNoPerm();
                        sender.sendMessage(pluginConfig.getPrefix() + Main.getPlugin().setPlaceholders(msg,p));
                        return false;
                    }
                }if(args[0].equalsIgnoreCase("help")){
                    sender.sendMessage(Utilities.Color("&8&m--------------------------------"));
                    sender.sendMessage(Utilities.Color("&2/cr | /creward &7- Shows the GUI"));
                    sender.sendMessage(Utilities.Color("&2/cr reload &7- Reloads the plugin"));
                    sender.sendMessage(Utilities.Color("&2/cr reset <reward> <player> &7- Reloads the player's reward time"));
                    sender.sendMessage(Utilities.Color("&2/cr help &7- Shows this message"));
                    sender.sendMessage(Utilities.Color("&8&m--------------------------------"));
                }
            }if(args.length == 3){
                if(args[0].equalsIgnoreCase("reset")){
                    if(sender.hasPermission("cr.reset")){
                        Reward reward = rewardManager.getReward(args[1]);
                        Player p = Bukkit.getPlayer(args[2]);
                        if(reward == null){
                            return false;
                        }
                        if(p == null){
                            return false;
                        }
                        playerManager.reset(reward,p.getUniqueId());
                        String msg = pluginConfig.getReset();
                        sender.sendMessage(pluginConfig.getPrefix() + Main.getPlugin().setPlaceholders(msg,p));
                    }else{
                        Player p = (Player) sender;
                        String msg = pluginConfig.getNoPerm();
                        sender.sendMessage(pluginConfig.getPrefix() + Main.getPlugin().setPlaceholders(msg,p));
                        return false;
                    }
                }
            }
        }
        return false;
    }
}