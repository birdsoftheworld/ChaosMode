package birds.chaosMode.ChaosMode.modes;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.options.IntegerOption;
import javafx.concurrent.Task;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

public class IntervalMode extends Mode {

    private IntegerOption interval = new IntegerOption();
    TaskRunner taskRunner = new TaskRunner();
    ChaosMode chaosMode;

    public IntervalMode(ChaosMode chaosMode) {
        this.chaosMode = chaosMode;
    }

    public void intervalFunction() {
        Bukkit.getLogger().info("Does the thing.");
    }

    public void runInterval() {
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                intervalFunction();
            }
        };
        runnable.runTaskTimer(chaosMode, 0, this.getInterval());
    }

    public int getInterval() {
        return interval.getValue();
    }

    public void setInterval(int value) {
        this.interval.setValue(value);
    }
}
