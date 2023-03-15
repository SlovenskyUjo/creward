package sk.matiuss.creward.CReward;

import sk.matiuss.creward.CReward.managers.RewardManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter {

    List<String> arguments = new ArrayList<>(Arrays.asList("reload","reset"));

    private RewardManager rewardManager;

    public TabCompleter(RewardManager rewardManager){
        this.rewardManager = rewardManager;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> result = new ArrayList<>();
        if(args.length == 1) {
            for (String s : arguments) {
                if (s.toLowerCase().startsWith(args[0].toLowerCase())) {
                    result.add(s);
                }
            }
            return result;
        }
        if(args.length == 2) {
            for(Reward reward : rewardManager.getRewards()){
                if(reward.getName().toLowerCase().startsWith(args[1].toLowerCase())){
                    result.add(reward.getName());
                }
            }
            return result;
        }
        return null;
    }
}