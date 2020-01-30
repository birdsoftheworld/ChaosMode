package birds.chaosMode.ChaosMode.modes.chaosModes;

import birds.chaosMode.ChaosMode.modes.ListenerMode;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public class MoonGravity extends ListenerMode {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (Objects.requireNonNull(event.getTo()).getBlockY() < event.getFrom().getBlockY()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 10, 1));
        }
    }

    @EventHandler
    public void onJump(PlayerStatisticIncrementEvent event) {
        Player player = event.getPlayer();

        if(!(event.getStatistic().equals(Statistic.JUMP))) {
            return;
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 20, 1));
    }
}
