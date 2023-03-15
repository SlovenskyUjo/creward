package sk.matiuss.creward.CReward.managers;

import sk.matiuss.creward.CReward.*;
import sk.matiuss.creward.CReward.commands.Commands;
import sk.matiuss.creward.CReward.events.Events;
import sk.matiuss.creward.CReward.managers.configurations.DataConfig;
import sk.matiuss.creward.CReward.managers.configurations.PluginConfig;
import org.bukkit.Bukkit;

public class ClassManager {

    private PluginConfig pluginConfig;
    private DataConfig dataConfig;
    private RewardManager rewardManager;
    private DBManager dbManager;
    private GUI gui;
    private PlayerManager playerManager;
    private PlaceholderAPI papi;

    public ClassManager(){
        pluginConfig = new PluginConfig();
        dataConfig = new DataConfig();
        dbManager = new DBManager(pluginConfig);
        rewardManager = new RewardManager(pluginConfig,dbManager);
        playerManager = new PlayerManager(dataConfig, pluginConfig, rewardManager,dbManager);
        gui = new GUI(pluginConfig, rewardManager,playerManager);
        Main.getPlugin().getCommand("creward").setExecutor(new Commands(gui, pluginConfig, playerManager, rewardManager));
        Main.getPlugin().getServer().getPluginManager().registerEvents(new Events(pluginConfig, playerManager, rewardManager), Main.getPlugin());
        Main.getPlugin().getCommand("creward").setTabCompleter(new TabCompleter(rewardManager));
        if(Main.getPlugin().isPapiEnabled()) {
            papi = new PlaceholderAPI(playerManager, rewardManager, pluginConfig);
            papi.register();
        }
    }

    public RewardManager getRewardManager() {
        return rewardManager;
    }

    public DataConfig getDataConfig() {
        return dataConfig;
    }

    public PluginConfig getPluginConfig() {
        return pluginConfig;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public GUI getGui() {
        return gui;
    }

    public DBManager getDbManager() {
        return dbManager;
    }
}