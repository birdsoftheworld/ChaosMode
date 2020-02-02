package birds.chaosMode.ChaosMode.modes;

import birds.chaosMode.ChaosMode.ChaosMode;
import org.bukkit.event.Listener;

public abstract class ListenerMode extends Mode implements Listener {
    protected ChaosMode chaosMode;

    public ListenerMode(ChaosMode chaosMode, String name) {
        super(name);
        this.chaosMode = chaosMode;
        chaosMode.getServer().getPluginManager().registerEvents(this, chaosMode);
    }
}
