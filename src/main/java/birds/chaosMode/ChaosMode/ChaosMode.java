package birds.chaosMode.ChaosMode;

import birds.chaosMode.ChaosMode.commands.ChaosCommand;
import birds.chaosMode.ChaosMode.modes.Mode;
import birds.chaosMode.ChaosMode.modes.chaosModes.MoonGravity;
import birds.chaosMode.ChaosMode.modes.chaosModes.TntDrop;
import org.bukkit.plugin.java.JavaPlugin;

public class ChaosMode extends JavaPlugin {

    public Mode[] getModes() {
        return modes;
    }

    private Mode[] modes;

    @Override
    public void onEnable() {
        modes = new Mode[2];
        modes[0] = new MoonGravity(this);
        modes[1] = new TntDrop(this);
        this.getCommand("chaos").setExecutor(new ChaosCommand(this)); // open menu hub
    }
}
