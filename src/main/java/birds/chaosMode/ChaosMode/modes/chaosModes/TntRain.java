package birds.chaosMode.ChaosMode.modes.chaosModes;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.IntervalMode;
import org.bukkit.Bukkit;

public class TntRain extends IntervalMode {
    public TntRain(ChaosMode chaosMode) {
        super(chaosMode);
        this.setName("TNT Rain");
        this.setInterval(40);
    }



    @Override
    public void intervalFunction() {
        Bukkit.getLogger().info("Does a different thing!");
    }
}
