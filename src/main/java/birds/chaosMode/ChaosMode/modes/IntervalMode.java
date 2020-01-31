package birds.chaosMode.ChaosMode.modes;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.options.IntegerOption;
import javafx.concurrent.Task;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

public class IntervalMode extends Mode {

    private IntegerOption interval;
    TaskRunner taskRunner = new TaskRunner();
    ChaosMode chaosMode;

    public IntervalMode(ChaosMode chaosMode) {
        this.chaosMode = chaosMode;
    }

    public void runInterval() {
        taskRunner.runTaskTimer(chaosMode, this.getInterval(), 0);
    }

    public int getInterval() {
        return interval.getValue();
    }

    public void setInterval(int value) {
        this.interval.setValue(value);
    }
}
