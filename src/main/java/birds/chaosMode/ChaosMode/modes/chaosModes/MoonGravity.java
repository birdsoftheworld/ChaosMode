package birds.chaosMode.ChaosMode.modes.chaosModes;

import birds.chaosMode.ChaosMode.modes.ListenerMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MoonGravity extends ListenerMode {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getFrom().getBlockY() != event.getTo().getBlockY()) {
            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 10, 1));
        }
    }
}
