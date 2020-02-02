package birds.chaosMode.ChaosMode.modes;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.options.IntChangeEvent;
import birds.chaosMode.ChaosMode.modes.options.IntegerOption;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Boss;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class IntervalMode extends Mode {

    protected IntegerOption interval = new IntegerOption(20, 1, Integer.MAX_VALUE);
    private ChaosMode chaosMode;
    private BukkitRunnable runnable;
    public BossBar bossBar;

    public IntervalMode(ChaosMode chaosMode, String name) {
        super(name);
        final BossBar bossBar = cooldownBar.getBar(getName(), BarColor.RED, BarStyle.SOLID);
        interval.setIcon(Material.CLOCK, ChatColor.RESET.toString() + "Interval (ticks)", ChatColor.RESET.toString() + "Interval (ticks)");
        interval.setChangeEvent(new IntChangeEvent() {
            @Override
            public void change(int oldValue, int newValue) {
                if (runnable != null) {
                    stopInterval();
                    startInterval();
                }
            }
        });
        this.chaosMode = chaosMode;
        this.bossBar = bossBar;
        runnable = null;
        addOption(interval);
    }

    private ProgressBar cooldownBar = new ProgressBar(chaosMode) {
        @Override
        public BukkitRunnable getRunnable() {
            return new BukkitRunnable() {
                @Override
                public void run() {
                    if (bossBar.getProgress() + (1.0 / getInterval()) < 1) {
                        bossBar.setProgress(bossBar.getProgress() + (1.0 / getInterval()));
                    } else {
                        bossBar.setProgress(0);
                    }
                }
            };
        }
    };

    public abstract BukkitRunnable getRunnable();

    private void startInterval() {
        runnable = getRunnable();
        runnable.runTaskTimer(chaosMode, 0, this.getInterval());
        bossBar.setProgress(0);
    }

    private void stopInterval() {
        if (runnable != null && !runnable.isCancelled()) {
            runnable.cancel();
            runnable = null;
        }
    }

    private int getInterval() {
        return interval.getValue();
    }

    protected void setInterval(int value) {
        this.interval.setValue(value);
        if (runnable != null) {
            stopInterval();
            startInterval();
        }
    }

    @Override
    public void enable() {
        super.enable();
        startInterval();
        cooldownBar.startInterval(chaosMode, interval);
    }

    @Override
    public void disable() {
        super.disable();
        stopInterval();
        cooldownBar.stopInterval();
    }
}
