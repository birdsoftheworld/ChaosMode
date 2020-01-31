package birds.chaosMode.ChaosMode.modes.chaosModes;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.IntervalMode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class TntRain extends IntervalMode {
    public TntRain(ChaosMode chaosMode) {
        super(chaosMode, "TNT Rain");
        setIcon(Material.TNT, ChatColor.RESET.toString() + getName(), "Click to activation");
        this.setInterval(40);
    }

    @Override
    public void enable() {
        super.enable();
        runInterval();
    }

    @Override
    public void disable() {
        super.disable();
        stopInterval();
    }

    @Override
    public void intervalFunction() {
        Bukkit.getLogger().info("Does a different thing!");
    }
}
