package birds.chaosMode.ChaosMode.modes;

import birds.chaosMode.ChaosMode.modes.options.IntegerOption;
import org.bukkit.Bukkit;

public class IntervalMode extends Mode {
    public int getInterval() {
        return interval.getValue();
    }

    public void setInterval(int value) {
        this.interval.setValue(value);
    }

    private IntegerOption interval;
}
