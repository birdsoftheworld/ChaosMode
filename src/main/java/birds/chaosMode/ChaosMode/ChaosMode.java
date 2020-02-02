package birds.chaosMode.ChaosMode;

import birds.chaosMode.ChaosMode.commands.ChaosCommand;
import birds.chaosMode.ChaosMode.modes.Mode;
import birds.chaosMode.ChaosMode.modes.chaosModes.Corruption;
import birds.chaosMode.ChaosMode.modes.chaosModes.MoonGravity;
import birds.chaosMode.ChaosMode.modes.chaosModes.TntDrop;
import birds.chaosMode.ChaosMode.utility.Usables;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class ChaosMode extends JavaPlugin {

    public ArrayList<Mode> getModes() {
        return modes;
    }

    private ArrayList<Mode> modes = new ArrayList<>();
    private Usables usables;

    @Override
    public void onEnable() {
        usables = new Usables();
        modes.add(new MoonGravity(this));
        modes.add(new TntDrop(this));
        modes.add(new Corruption(this, usables));
        this.getCommand("chaos").setExecutor(new ChaosCommand(this)); // open menu hub
    }
}
