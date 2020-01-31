package birds.chaosMode.ChaosMode.modes;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.options.IntegerOption;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class IntervalMode extends Mode {

    private IntegerOption interval = new IntegerOption();
    private ChaosMode chaosMode;
    private BukkitRunnable runnable;

    public IntervalMode(ChaosMode chaosMode, String name) {
        super(name);
        this.chaosMode = chaosMode;
        runnable = null;
    }

    public abstract BukkitRunnable getRunnable();

    protected void startInterval() {
        runnable = getRunnable();
        runnable.runTaskTimer(chaosMode, 0, this.getInterval());
    }

    protected void stopInterval() {
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
}
