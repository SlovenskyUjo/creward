package sk.matiuss.creward.CReward.events;

import sk.matiuss.creward.CReward.GUI;
import sk.matiuss.creward.CReward.Main;
import sk.matiuss.creward.CReward.Reward;
import sk.matiuss.creward.CReward.managers.PlayerManager;
import sk.matiuss.creward.CReward.managers.RewardManager;
import sk.matiuss.creward.CReward.managers.configurations.PluginConfig;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class Events implements Listener {

    private PluginConfig pluginConfig;
    private PlayerManager playerManager;
    private RewardManager rewardManager;

    public Events(PluginConfig pluginConfig, PlayerManager playerManager, RewardManager rewardManager){
        this.pluginConfig = pluginConfig;
        this.playerManager = playerManager;
        this.rewardManager = rewardManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        UUID uuid = p.getUniqueId();
        String title = pluginConfig.getGuiTitle();
        if(Main.getPlugin().isPapiEnabled()){
            title = PlaceholderAPI.setPlaceholders(p,title);
        }
        if(p.getOpenInventory().getTitle().equals(title)){
            e.setCancelled(true);
            if(e.getCurrentItem() == null){
                return;
            }
            ItemStack item = e.getCurrentItem();
            if(item.getItemMeta()==null){
                return;
            }
            Reward reward = rewardManager.getReward(e.getSlot());
            if(reward == null){
                return;
            }
            playerManager.claim(reward,uuid);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        Main.getPlugin().getServer().getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {
            @Override
            public void run() {
                if(playerManager.claimable(p.getUniqueId()) > 0) {
                    if (p.hasPermission(pluginConfig.getAutoPickupPerm())) {
                        if(pluginConfig.isAutoPickup()) {
                            int i = playerManager.claimAll(p.getUniqueId());
                            p.sendMessage(pluginConfig.getPrefix() + pluginConfig.getAutoClaim().replace("%rewards%", i + ""));
                            return;
                        }
                    }
                    if(pluginConfig.getNotify().equalsIgnoreCase("false") || pluginConfig.getNotify() == null){
                        return;
                    }
                    String msg = pluginConfig.getNotify();
                    p.sendMessage(pluginConfig.getPrefix() + Main.getPlugin().setPlaceholders(msg,p));
                }
            }
        }, 20L * 3);

    }
}
