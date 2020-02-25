package birds.chaosMode.ChaosMode.modes.chaosModes;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.IntervalMode;
import birds.chaosMode.ChaosMode.modes.options.EntityListOption;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TntDrop extends IntervalMode {

    private EntityListOption entity = new EntityListOption("entity");
    public TntDrop(ChaosMode chaosMode) {
        super(chaosMode, "TNT Drop");
        setInternalName("tntdrop");

        entity.setMaxItems(1);
        entity.setIcon(Material.CREEPER_SPAWN_EGG, ChatColor.RESET.toString() + "Entity to Spawn");
        entity.addItem(Material.TNT);
        addOption(entity);

        interval.setValue(600);
        interval.setDefaultValue(600);
        interval.setName("interval");

        setIcon(Material.TNT, ChatColor.RESET.toString() + getName(), "Click to change settings");
        this.setInterval(600);
    }

    @Override
    public BukkitRunnable getRunnable() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    // spawn tnt on the player
                    if(entity.getEntities().size() >= 1) {
                        onlinePlayer.getWorld().spawnEntity(onlinePlayer.getLocation(), entity.getEntities().get(0));
                    }
                }
            }
        };
    }
}
