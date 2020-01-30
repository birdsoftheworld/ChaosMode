package birds.chaosMode.ChaosMode.modes;

import birds.chaosMode.ChaosMode.ChaosMode;
import org.bukkit.event.Listener;

public class ListenerMode extends Mode implements Listener {

    public ListenerMode(ChaosMode chaosMode) {
        chaosMode.getServer().getPluginManager().registerEvents(this, chaosMode);
    }
}
