package birds.chaosMode.ChaosMode.modes.chaosModes;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.IntervalMode;
import birds.chaosMode.ChaosMode.modes.options.BooleanOption;
import birds.chaosMode.ChaosMode.modes.options.IntegerOption;
import birds.chaosMode.ChaosMode.utility.Usables;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class PotionEffects extends IntervalMode {
    private Usables usables;
    private BooleanOption includeWithering;
    private IntegerOption potionDuration;

    public PotionEffects(ChaosMode chaosMode, Usables usables) {
        super(chaosMode, "Potion Effects");
        this.usables = usables;

        interval.setValue(40);
        interval.setDefaultValue(40);
        this.setInterval(40);

        includeWithering = new BooleanOption(false);
        includeWithering.setIcon(Material.WITHER_SKELETON_SKULL, "Include Withering", "Include Withering");

        potionDuration = new IntegerOption(400, 1, Integer.MAX_VALUE);
        potionDuration.setIcon(Material.REDSTONE, "Potion Effect Duration", "Potion Effect Duration");

        setIcon(Material.POTION, ChatColor.RESET.toString() + getName(), "Click to change settings");
    }

    @Override
    public BukkitRunnable getRunnable() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    PotionEffectType effect = usables.getUsablePotionEffect();

                    // don't allow withering if includeWithering is false
                    while(!includeWithering.getValue() && effect.equals(PotionEffectType.WITHER)) {
                        effect = usables.getUsablePotionEffect();
                    }

                    // apply effect
                    onlinePlayer.addPotionEffect(new PotionEffect(effect, potionDuration.getValue(), 0));
                }
            }
        };
    }
}
