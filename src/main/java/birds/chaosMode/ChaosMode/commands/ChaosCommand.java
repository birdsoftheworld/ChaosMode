package birds.chaosMode.ChaosMode.commands;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.menu.OptionsHub;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChaosCommand implements CommandExecutor {
    private OptionsHub hub;

    public ChaosCommand(ChaosMode chaosMode) {
        hub = new OptionsHub(chaosMode);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED.toString() + "Only players can run this command.");
            return true;
        }

        // show the options GUI
        hub.showInventory((Player) commandSender);
        return true;
    }
}
