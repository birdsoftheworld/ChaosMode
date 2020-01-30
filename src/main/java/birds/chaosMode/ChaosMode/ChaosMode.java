package birds.chaosMode.ChaosMode;

import birds.chaosMode.ChaosMode.modes.chaosModes.MoonGravity;
import org.bukkit.plugin.java.JavaPlugin;

public class ChaosMode extends JavaPlugin {

    MoonGravity moonGravity;

    @Override
    public void onEnable() {
        moonGravity = new MoonGravity();

        getServer().getPluginManager().registerEvents(moonGravity, this);
    }
}
