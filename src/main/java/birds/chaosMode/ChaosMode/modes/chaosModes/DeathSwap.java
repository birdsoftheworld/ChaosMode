package birds.chaosMode.ChaosMode.modes.chaosModes;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.IntervalMode;
import birds.chaosMode.ChaosMode.modes.options.IntegerOption;
import birds.chaosMode.ChaosMode.utility.ProgressBar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class DeathSwap extends IntervalMode {

    private ProgressBar progressBar;

    public DeathSwap(ChaosMode chaosMode) {
        super(chaosMode, "Death Swap");
        setInternalName("deathswap");

        interval.setValue(6000);
        interval.setDefaultValue(6000);
        interval.setName("interval");

        this.setInterval(interval.getValue());

        setIcon(Material.ENDER_PEARL, ChatColor.RESET.toString() + getName(), "Click to change settings");

        progressBar = new ProgressBar(chaosMode);
    }

    @Override
    public void enable() {
        super.enable();
        progressBar.start(this);
    }

    @Override
    public void disable() {
        super.disable();
        progressBar.stop();
    }

    @Override
    public BukkitRunnable getRunnable() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                ArrayList<Player> players = new ArrayList<>();
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    players.add(onlinePlayer);
                }
                if (players.size() < 2) return;
                Location player1Location = players.get(0).getLocation();
                Location player2Location = players.get(1).getLocation();
                players.get(0).teleport(player2Location);
                players.get(1).teleport(player1Location);
            }
        };
    }
}
