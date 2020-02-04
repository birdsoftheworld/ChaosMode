package birds.chaosMode.ChaosMode.utility;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.IntervalMode;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitRunnable;

public class ProgressBar {

    private BukkitRunnable runnable;
    private BossBar bossBar = Bukkit.createBossBar("Time Left", BarColor.RED, BarStyle.SOLID);
    private ChaosMode chaosMode;

    protected ProgressBar(ChaosMode chaosMode) {
        this.chaosMode = chaosMode;
        runnable = null;
    }

    private void startInterval(final IntervalMode intervalMode) {
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (bossBar.getProgress() + (1.0 / intervalMode.getInterval()) < 1) {
                    bossBar.setProgress(bossBar.getProgress() + (1.0 / intervalMode.getInterval()));
                } else {
                    bossBar.setProgress(0);
                }
            }
        };
        runnable.runTaskTimer(chaosMode, 0, 1);
    }

    private void stopInterval() {
        if (runnable != null && !runnable.isCancelled()) {
            runnable.cancel();
            runnable = null;
        }
    }

    public void stop() {
        stopInterval();
        bossBar.setVisible(false);
    }

    public void start(IntervalMode intervalMode) {
        startInterval(intervalMode);
        bossBar.setVisible(true);
    }

    public void reset() {
        bossBar.setProgress(0);
    }

    public void setBossBarStyle(String title, BarColor color, BarStyle style) {
        this.bossBar = Bukkit.createBossBar(title, color, style);
    }

    public void incrementBar(BossBar bar, int increment) {
        bar.setProgress(bar.getProgress() + increment);
    }
}
