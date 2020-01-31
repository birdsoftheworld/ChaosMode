package birds.chaosMode.ChaosMode.modes.chaosModes;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.IntervalMode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class TntRain extends IntervalMode {
    public TntRain(ChaosMode chaosMode) {
        super(chaosMode, "TNT Rain");
        setIcon(Material.TNT, ChatColor.RESET.toString() + getName(), "Click to activation");
        this.setInterval(40);
    }

    @Override
    public void enable() {
        super.enable();
        startInterval();
    }

    @Override
    public void disable() {
        super.disable();
        stopInterval();
    }

    @Override
    public void intervalFunction() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.getWorld().spawnEntity(onlinePlayer.getLocation().add(0.0, 10.0, 0.0), EntityType.PRIMED_TNT);
            Bukkit.getLogger().info("Spawned TNT on player " + onlinePlayer.getName());
        }
    }
}
