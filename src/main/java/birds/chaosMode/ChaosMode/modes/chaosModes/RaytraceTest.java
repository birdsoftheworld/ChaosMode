package birds.chaosMode.ChaosMode.modes.chaosModes;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.IntervalMode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;

public class RaytraceTest extends IntervalMode {

    public RaytraceTest(ChaosMode chaosMode) {
        super(chaosMode, "Raytracing Test");
        setIcon(Material.GLASS, ChatColor.RESET.toString() + getName(), "Click to activation");
    }

    @Override
    public BukkitRunnable getRunnable() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (onlinePlayer == null) return;

                    for (Entity nearbyEntity : onlinePlayer.getNearbyEntities(10.0, 10.0, 10.0)) {
                        RayTraceResult result = nearbyEntity.getBoundingBox().rayTrace(onlinePlayer.getLocation().toVector(), onlinePlayer.getEyeLocation().getDirection(), 10.0);
                        Bukkit.getLogger().info(result.getHitEntity().getName().toString());
                    }
                }
            }
        };
    }
}
