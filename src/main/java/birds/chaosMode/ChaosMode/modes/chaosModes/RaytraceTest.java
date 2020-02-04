package birds.chaosMode.ChaosMode.modes.chaosModes;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.IntervalMode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;

public abstract class RaytraceTest extends IntervalMode {

    public RaytraceTest(ChaosMode chaosMode) {
        super(chaosMode, "Raytracing Test");
        interval.setValue(20);
        interval.setDefaultValue(20);
        this.setInterval(20);
        setIcon(Material.GLASS, ChatColor.RESET.toString() + getName(), "Click to activation");
    }

    @Override
    public BukkitRunnable getRunnable() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (onlinePlayer == null) continue;

                    for (Player onlinePlayer2 : Bukkit.getOnlinePlayers()) {
                        if (onlinePlayer.equals(onlinePlayer2)) continue;
                        RayTraceResult result = onlinePlayer2.getBoundingBox().rayTrace(onlinePlayer.getLocation().toVector(), onlinePlayer.getEyeLocation().getDirection(), 10.0);
                        if (result == null) continue;
                        Bukkit.getLogger().info(onlinePlayer2.getDisplayName());
                        onlinePlayer2.sendMessage("You have been spotted by " + onlinePlayer.getDisplayName());
                    }
                }
            }
        };
    }
}
