package birds.chaosMode.ChaosMode.modes.chaosModes;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.IntervalMode;
import birds.chaosMode.ChaosMode.modes.ProgressBar;
import birds.chaosMode.ChaosMode.modes.options.ConfigurableOption;
import birds.chaosMode.ChaosMode.modes.options.IntChangeEvent;
import birds.chaosMode.ChaosMode.modes.options.IntegerOption;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class TntDrop extends IntervalMode {

    private ChaosMode chaosMode;
    private BossBar bossBar;

    public TntDrop(ChaosMode chaosMode) {
        super(chaosMode, "TNT Drop");
        this.chaosMode = chaosMode;
        interval.setValue(600);
        interval.setDefaultValue(600);
        setIcon(Material.TNT, ChatColor.RESET.toString() + getName(), "Click to change settings");
        this.setInterval(600);
        BossBar bossBar = cooldownBar.getBar("TNT Drop", BarColor.RED, BarStyle.SOLID);
        this.bossBar = bossBar;
    }

    private ProgressBar cooldownBar = new ProgressBar(chaosMode) {
        @Override
        public BukkitRunnable getRunnable() {
            return new BukkitRunnable() {
                @Override
                public void run() {
                    if (bossBar.getProgress() < 1) {
                        bossBar.setProgress(bossBar.getProgress() + (1 / interval.getValue()));
                    } else {
                        bossBar.setProgress(0);
                    }
                }
            };
        }
    };

    @Override
    public void enable() {
        super.enable();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            bossBar.addPlayer(onlinePlayer);
        }
        bossBar.setVisible(true);
        cooldownBar.startInterval(chaosMode, interval);
    }

    @Override
    public void disable() {
        super.disable();
        bossBar.setVisible(false);
        cooldownBar.stopInterval();
    }

    @Override
    public BukkitRunnable getRunnable() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    // spawn tnt on the player
                    onlinePlayer.getWorld().spawnEntity(onlinePlayer.getLocation(), EntityType.PRIMED_TNT);
                }
            }
        };
    }
}
