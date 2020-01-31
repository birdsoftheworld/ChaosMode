package birds.chaosMode.ChaosMode.modes;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskRunner extends BukkitRunnable {
    public void run() {
        Bukkit.getLogger().info("Does the thing.");
    }
}
