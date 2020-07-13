package birds.chaosMode.ChaosMode.commands;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.menu.OptionsHub;
import birds.chaosMode.ChaosMode.modes.Mode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChaosCommand implements CommandExecutor {
    private OptionsHub hub;
    private ChaosMode chaosMode;

    public ChaosCommand(ChaosMode chaosMode) {
        this.chaosMode = chaosMode;
        hub = new OptionsHub(chaosMode);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED.toString() + "Only players can run this command.");
            return true;
        }

        if (args.length == 2) {
            if (args[0].equals("toggle")) {
                String modeName = args[1];
                if (modeName == null) return false;
                for (Mode mode : chaosMode.getModes()) {
                    String currentModeName = mode.getInternalName();
                    if (currentModeName.equals(modeName)) {
                        commandSender.sendMessage(ChatColor.GOLD + "Toggled " + mode.getName());
                        mode.setEnabled(!mode.isEnabled());
                        break;
                    }
                }
            }
        } else {
            // show the options GUI
            hub.showInventory((Player) commandSender);
        }
        return true;
    }
}
