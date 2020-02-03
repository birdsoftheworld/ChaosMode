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
    private BooleanOption includeWithering = new BooleanOption(false, "includeWithering");
    private IntegerOption potionDuration = new IntegerOption(400, 1, Integer.MAX_VALUE, "effectDuration");

    public PotionEffects(ChaosMode chaosMode, Usables usables) {
        super(chaosMode, "Potion Effects");
        setInternalName("potioneffects");

        this.usables = usables;

        interval.setValue(800);
        interval.setDefaultValue(800);
        interval.setName("interval");
        this.setInterval(800);

        includeWithering.setIcon(Material.WITHER_SKELETON_SKULL, ChatColor.RESET.toString() + "Include Withering");
        addOption(includeWithering);

        potionDuration.setIcon(Material.REDSTONE, ChatColor.RESET.toString() + "Potion Effect Duration");
        addOption(potionDuration);

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
