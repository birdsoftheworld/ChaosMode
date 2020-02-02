package birds.chaosMode.ChaosMode.modes.chaosModes;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.IntervalMode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TntDrop extends IntervalMode {

    private ChaosMode chaosMode;

    public TntDrop(ChaosMode chaosMode) {
        super(chaosMode, "TNT Drop");
        this.chaosMode = chaosMode;
        interval.setValue(600);
        interval.setDefaultValue(600);
        setIcon(Material.TNT, ChatColor.RESET.toString() + getName(), "Click to change settings");
        this.setInterval(600);
    }

    @Override
    public BukkitRunnable getRunnable() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    // spawn tnt on the player
                    onlinePlayer.getWorld().spawnEntity(onlinePlayer.getLocation(), EntityType.PRIMED_TNT);
                }
            }
        };
    }
}
