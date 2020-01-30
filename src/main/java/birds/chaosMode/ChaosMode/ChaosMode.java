package birds.chaosMode.ChaosMode;

import birds.chaosMode.ChaosMode.commands.ChaosCommand;
import birds.chaosMode.ChaosMode.modes.Mode;
import birds.chaosMode.ChaosMode.modes.chaosModes.MoonGravity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class ChaosMode extends JavaPlugin {

    public Mode[] getModes() {
        return modes;
    }

    private Mode[] modes;

    @Override
    public void onEnable() {
        modes = new Mode[1];
        modes[0] = new MoonGravity(this);
        this.getCommand("chaos").setExecutor(new ChaosCommand(this)); // open menu hub
    }
}
