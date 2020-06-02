package birds.chaosMode.ChaosMode.modes.chaosModes;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.IntervalMode;
import birds.chaosMode.ChaosMode.modes.options.EntityListOption;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class FishMode extends IntervalMode {

    private ChaosMode chaosMode;
    private EntityListOption entity = new EntityListOption("entity");
    private HashMap<Player, Entity> playerFish = new HashMap<Player, Entity>();


    public FishMode(ChaosMode chaosMode) {
        super(chaosMode, "Fish Mode");
        this.chaosMode = chaosMode;

        interval.setDefaultValue(1);
        interval.setDefaultValue(1);
        interval.setName("interval");
        this.setInterval(1);

        entity.setMaxItems(1);
        entity.setIcon(Material.CREEPER_SPAWN_EGG, ChatColor.RESET.toString() + "Entity to Spawn");
        entity.addItem(Material.TROPICAL_FISH_SPAWN_EGG);
        addOption(entity);

        removeOption(interval);

        setIcon(Material.TROPICAL_FISH, ChatColor.RESET.toString() + getName(), "Click to change settings");
    }

    @Override
    public void enable() {
        super.enable();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (ChaosMode.playerIsExcluded(onlinePlayer)) return;
            Entity fish = onlinePlayer.getWorld().spawnEntity(onlinePlayer.getLocation(), entity.getEntities().get(0));
            fish.setCustomName(onlinePlayer.getName());
            fish.setInvulnerable(true);
            ((LivingEntity) fish).setCollidable(false);
            playerFish.put(onlinePlayer, fish);
        }
    }

    @Override
    public void disable() {
        super.disable();
        for (Player key : playerFish.keySet()) {
            playerFish.get(key).remove();
            playerFish.remove(key);
        }
    }

    public BukkitRunnable getRunnable() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                if (!isEnabled()) return;
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (ChaosMode.playerIsExcluded(onlinePlayer)) return;
                    Block playerBlock = onlinePlayer.getWorld().getBlockAt(onlinePlayer.getEyeLocation());
                    if (playerBlock.getType() == Material.WATER || playerBlock.getBlockData() instanceof Waterlogged) {
                        onlinePlayer.addPotionEffect(new PotionEffect(PotionEffectType.CONDUIT_POWER, 600, 255));
                    } else {
                        if (!onlinePlayer.isDead()) {
                            onlinePlayer.setHealth(Math.max(onlinePlayer.getHealth() - 0.1, 0.0));
                            if (onlinePlayer.getHealth() <= 0) {
                                onlinePlayer.setHealth(0.0);
                            }
                        } else {
                            playerFish.get(onlinePlayer).remove();
                            playerFish.remove(onlinePlayer);
                        }
                        onlinePlayer.removePotionEffect(PotionEffectType.CONDUIT_POWER);
                    }
                    if (!onlinePlayer.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                        onlinePlayer.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 600, 1));
                    }
                    if (playerFish.containsKey(onlinePlayer)) {
                        playerFish.get(onlinePlayer).teleport(onlinePlayer.getEyeLocation());
                    } else {
                        if (!onlinePlayer.isDead()) {
                            Entity fish = onlinePlayer.getWorld().spawnEntity(onlinePlayer.getLocation(), entity.getEntities().get(0));
                            playerFish.put(onlinePlayer, fish);
                        }
                    }
                }
            }
        };
    }
}
