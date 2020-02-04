package birds.chaosMode.ChaosMode.modes.chaosModes;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.IntervalMode;
import birds.chaosMode.ChaosMode.modes.options.IntegerOption;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class NeverStop extends IntervalMode {

    private IntegerOption speed = new IntegerOption(5, 1, 100, "speed");

    public NeverStop(ChaosMode chaosMode) {
        super(chaosMode, "Never Stop");
        setInternalName("neverstop");

        speed.setIcon(Material.SUGAR, ChatColor.RESET.toString() + "Speed");
        addOption(speed);

        interval.setValue(1);
        interval.setDefaultValue(1);
        this.setInterval(1);
        removeOption(interval);

        setIcon(Material.SUGAR, ChatColor.RESET.toString() + getName());
    }

    @Override
    public BukkitRunnable getRunnable() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    // player's current speed
                    Vector playerVelocity = onlinePlayer.getVelocity();

                    // player's facing direction
                    Vector facingDirection = onlinePlayer.getEyeLocation().getDirection();

                    // speed level
                    double speedValue = (double) speed.getValue() / 100.0;

                    // player's facing direction, normalized, stripped of its y-value, and multiplied by speed
                    Vector adjustedDirection = facingDirection.normalize().setY(0).multiply(speedValue);

                    // set player's velocity to facing direction * speed + current velocity
                    onlinePlayer.setVelocity(playerVelocity.add(adjustedDirection));
                }
            }
        };
    }
}
