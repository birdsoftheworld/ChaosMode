package birds.chaosMode.ChaosMode.modes.chaosModes;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.IntervalMode;
import birds.chaosMode.ChaosMode.modes.ListenerMode;
import birds.chaosMode.ChaosMode.modes.options.ConfigurableOption;
import birds.chaosMode.ChaosMode.modes.options.IntegerOption;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Objects;

public class MoonGravity extends ListenerMode {

    private IntegerOption effectDuration;

    public MoonGravity(ChaosMode chaosMode) {
        super(chaosMode, "Moon Gravity");
        setInternalName("moongravity");

        effectDuration = new IntegerOption(20, 1, Integer.MAX_VALUE, "effect-duration");
        effectDuration.setIcon(Material.POTION, ChatColor.RESET.toString() + "Potion Duration");
        addOption(effectDuration);

        setIcon(Material.SLIME_BLOCK, ChatColor.RESET.toString() + getName(), "Click to change settings");
    }



    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if(!isEnabled()) return;
        Player player = event.getPlayer();

        // if new position is lower than current position, then apply slow falling
        if (Objects.requireNonNull(event.getTo()).getBlockY() < event.getFrom().getBlockY()) {
            int duration = effectDuration.getValue();
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, duration, 0));
        }
    }

    @EventHandler
    public void onJump(PlayerStatisticIncrementEvent event) {
        if (!isEnabled()) return;
        Player player = event.getPlayer();

        // only continue if action is a jump
        if (!(event.getStatistic().equals(Statistic.JUMP))) {
            return;
        }

        int duration = effectDuration.getValue();
        player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, duration, 1));
        player.spawnParticle(Particle.CLOUD, player.getLocation().subtract(0.0, 0.5, 0.0),25);
    }
}
