package birds.chaosMode.ChaosMode.utility;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.options.IntegerOption;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class ProgressBar {

    private BukkitRunnable runnable;

    protected ProgressBar() {
        runnable = null;
    }

    public BossBar getBar(String title, BarColor color, BarStyle style) {
        return Bukkit.createBossBar(title, color, style);
    }

    public abstract BukkitRunnable getRunnable();

    public void startInterval(ChaosMode chaosMode) {
        runnable = getRunnable();
        runnable.runTaskTimer(chaosMode, 0, 1);
    }

    public void stopInterval() {
        if (runnable != null && !runnable.isCancelled()) {
            runnable.cancel();
            runnable = null;
        }
    }

    public void incrementBar(BossBar bar, int increment) {
        bar.setProgress(bar.getProgress() + increment);
    }
}
