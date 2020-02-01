package birds.chaosMode.ChaosMode;

import birds.chaosMode.ChaosMode.commands.ChaosCommand;
import birds.chaosMode.ChaosMode.modes.Mode;
import birds.chaosMode.ChaosMode.modes.chaosModes.BlockToBedrock;
import birds.chaosMode.ChaosMode.modes.chaosModes.MoonGravity;
import birds.chaosMode.ChaosMode.modes.chaosModes.TntDrop;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class ChaosMode extends JavaPlugin {

    public List<Mode> getModes() {
        return modes;
    }

    private List<Mode> modes;

    @Override
    public void onEnable() {
        modes = new ArrayList<>();
        modes.add(new MoonGravity(this));
        modes.add(new TntDrop(this));
        this.getCommand("chaos").setExecutor(new ChaosCommand(this)); // open menu hub
    }
}
