package birds.chaosMode.ChaosMode.modes;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.options.IntegerOption;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Boss;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public abstract class ProgressBar {

    private ProgressBar progressBar;
    private BukkitRunnable runnable;
    private ChaosMode chaosMode;

    public ProgressBar(ChaosMode chaosMode) {
        this.chaosMode = chaosMode;
        runnable = null;
    }

    public BossBar getBar(String title, BarColor color, BarStyle style) {
        BossBar bar = Bukkit.createBossBar(title, color, style);
        return bar;
    }

    public abstract BukkitRunnable getRunnable();

    public void startInterval(ChaosMode chaosMode, IntegerOption interval) {
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
