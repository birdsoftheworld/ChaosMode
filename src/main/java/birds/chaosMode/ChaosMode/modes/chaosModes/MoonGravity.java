package birds.chaosMode.ChaosMode.modes.chaosModes;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.ListenerMode;
import birds.chaosMode.ChaosMode.modes.options.ConfigurableOption;
import birds.chaosMode.ChaosMode.modes.options.IntegerOption;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public class MoonGravity extends ListenerMode {

    private IntegerOption effectDuration;
    private ConfigurableOption[] options;

    public MoonGravity(ChaosMode chaosMode) {
        super(chaosMode);
        setIcon(Material.SLIME_BLOCK, ChatColor.RESET.toString() + "Gravity Mode", "Click to activation");
        effectDuration = new IntegerOption();
        effectDuration.setValue(20);
        effectDuration.setMinimumValue(1);
        effectDuration.setMaximumValue(Integer.MAX_VALUE);
        effectDuration.setIcon(Material.POTION);
        this.setName("MoonGravity");
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
        if(!isEnabled()) return;
        Player player = event.getPlayer();

        // only continue if action is a jump
        if(!(event.getStatistic().equals(Statistic.JUMP))) {
            return;
        }

        int duration = effectDuration.getValue();
        player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, duration, 1));
    }
}
