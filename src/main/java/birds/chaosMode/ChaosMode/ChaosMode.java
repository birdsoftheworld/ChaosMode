package birds.chaosMode.ChaosMode;

import birds.chaosMode.ChaosMode.commands.ChaosCommand;
import birds.chaosMode.ChaosMode.modes.Mode;
import birds.chaosMode.ChaosMode.modes.chaosModes.*;
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
        modes.add(new PotionEffects(this, usables));
        modes.add(new EternalNight(this));
        modes.add(new BlockCollapse(this));
        this.getCommand("chaos").setExecutor(new ChaosCommand(this)); // open menu hub
    }
}
