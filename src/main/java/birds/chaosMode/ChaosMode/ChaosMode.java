package birds.chaosMode.ChaosMode;

import birds.chaosMode.ChaosMode.commands.ChaosCommand;
import birds.chaosMode.ChaosMode.modes.Mode;
import birds.chaosMode.ChaosMode.modes.chaosModes.BlockToBedrock;
import birds.chaosMode.ChaosMode.modes.chaosModes.MoonGravity;
import birds.chaosMode.ChaosMode.modes.chaosModes.RaytraceTest;
import birds.chaosMode.ChaosMode.modes.chaosModes.TntRain;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class ChaosMode extends JavaPlugin {

    public Mode[] getModes() {
        return modes;
    }

    private Mode[] modes;

    @Override
    public void onEnable() {
        modes = new Mode[3];
        modes[0] = new MoonGravity(this);
        modes[1] = new TntRain(this);
        modes[2] = new RaytraceTest(this);
        this.getCommand("chaos").setExecutor(new ChaosCommand(this)); // open menu hub
    }
}
