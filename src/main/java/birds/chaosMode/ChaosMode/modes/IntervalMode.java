package birds.chaosMode.ChaosMode.modes;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.options.IntegerOption;
import javafx.concurrent.Task;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

public abstract class IntervalMode extends Mode {

    private IntegerOption interval = new IntegerOption();
    private ChaosMode chaosMode;
    private BukkitRunnable runnable;

    public IntervalMode(ChaosMode chaosMode, String name) {
        super(name);
        this.chaosMode = chaosMode;
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                intervalFunction();
            }
        };
    }

    public abstract void intervalFunction();

    public void runInterval() {
        runnable.runTaskTimer(chaosMode, 0, this.getInterval());
    }

    public void stopInterval() {
        if (!runnable.isCancelled()) {
            runnable.cancel();
        }
    }

    public int getInterval() {
        return interval.getValue();
    }

    public void setInterval(int value) {
        this.interval.setValue(value);
    }
}
