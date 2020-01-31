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

    public abstract void intervalFunction();

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

    public void startInterval() {
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                intervalFunction();
            }
        };
        runnable.runTaskTimer(chaosMode, 0, this.getInterval());
    }

    public void stopInterval() {
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
}
