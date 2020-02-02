package birds.chaosMode.ChaosMode.modes;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.options.IntChangeEvent;
import birds.chaosMode.ChaosMode.modes.options.IntegerOption;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class IntervalMode extends Mode {

    protected IntegerOption interval = new IntegerOption(20, 1, Integer.MAX_VALUE);
    private ChaosMode chaosMode;
    private BukkitRunnable runnable;

    public IntervalMode(ChaosMode chaosMode, String name) {
        super(name);
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
        runnable = null;
        addOption(interval);
    }

    public abstract BukkitRunnable getRunnable();

    private void startInterval() {
        runnable = getRunnable();
        runnable.runTaskTimer(chaosMode, 0, this.getInterval());
    }

    private void stopInterval() {
        if (runnable != null && !runnable.isCancelled()) {
            runnable.cancel();
            runnable = null;
        }
    }

    public int getInterval() {
        return interval.getValue();
    }

    public void setInterval(int value) {
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
    }

    @Override
    public void disable() {
        super.disable();
        stopInterval();
    }
}
