package birds.chaosMode.ChaosMode.modes.chaosModes;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.ListenerMode;
import birds.chaosMode.ChaosMode.modes.options.BooleanOption;
import birds.chaosMode.ChaosMode.modes.options.IntegerOption;
import org.bukkit.*;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.world.TimeSkipEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class HardMode extends ListenerMode {
    private HashMap<World, Long> cachedTimes = new HashMap<>();
    private HashMap<World, Difficulty> cachedDifficulties = new HashMap<>();

    private BooleanOption moreDifficult = new BooleanOption(true, "more-difficult");
    private BooleanOption mobEffects = new BooleanOption(false, "mob-effects");
    private IntegerOption effectLevel = new IntegerOption(0, 0, 10, "effect-level");
    private BooleanOption alwaysNight = new BooleanOption(true, "always-night");
    private Random random = new Random();

    public HardMode(ChaosMode chaosMode) {
        super(chaosMode, "Hard Mode");
        setInternalName("hardmode");

        moreDifficult.setIcon(Material.CREEPER_HEAD, ChatColor.RESET.toString() + "Harder Difficulty");
        addOption(moreDifficult);

        alwaysNight.setIcon(Material.DAYLIGHT_DETECTOR, ChatColor.RESET.toString() + "Always Night");
        addOption(alwaysNight);

        mobEffects.setIcon(Material.POTION, ChatColor.RESET.toString() + "Mob Effects");
        addOption(mobEffects);

        effectLevel.setIcon(Material.EXPERIENCE_BOTTLE, ChatColor.RESET.toString() + "Max Effect Level");
        addOption(effectLevel);

        setIcon(Material.IRON_SWORD, ChatColor.RESET.toString() + getName(), ChatColor.RESET.toString() + ChatColor.GRAY.toString() + "Check options for functionality.");
    }

    private void applyEffect(Entity entity) {
        // give an entity a random positive effect

        // return if entity can't have effects
        if(!(entity instanceof LivingEntity)) return;

        // creepers drop potion effects on explosion as an AreaEffectCloud, so don't give them any effects
        if(entity instanceof Creeper) return;

        ArrayList<PotionEffectType> effects = new ArrayList<>();
        effects.add(PotionEffectType.DAMAGE_RESISTANCE);
        effects.add(PotionEffectType.SPEED);
        effects.add(PotionEffectType.INCREASE_DAMAGE);
        effects.add(PotionEffectType.HEALTH_BOOST);
        effects.add(PotionEffectType.FIRE_RESISTANCE);
        effects.add(PotionEffectType.JUMP);
        Collections.shuffle(effects);

        int level = random.nextInt(effectLevel.getValue() + 1);
        ((LivingEntity) entity).addPotionEffect(new PotionEffect(effects.get(0), Integer.MAX_VALUE, level));
    }

    @EventHandler
    public void onSkipTime(TimeSkipEvent event) {
        // don't let players skip to day
        if(isEnabled() && alwaysNight.getValue()) {
            // allow plugins to set time
            if(!event.getSkipReason().equals(TimeSkipEvent.SkipReason.CUSTOM))
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMobSpawn(EntitySpawnEvent event) {
        // stop if mode is disabled
        if(!isEnabled()) return;

        // stop if option is disabled
        if(!(mobEffects.getValue())) return;

        Entity entity = event.getEntity();

        // stop if entity isn't a monster
        if(!(entity instanceof Monster)) return;

        // finally, apply an effect
        applyEffect(entity);
    }

    @Override
    public void update() {
        if(!isEnabled()) return;

        for(World world : Bukkit.getWorlds()) {
            if(!(alwaysNight.getValue())) {
                // restore to saved time
                if(cachedTimes.containsKey(world))
                    world.setTime(cachedTimes.get(world));
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);

                cachedTimes = new HashMap<>();
                cachedDifficulties = new HashMap<>();
            }

            if(alwaysNight.getValue()) {
                // put current time in a HashMap for later restoration
                if(!(cachedTimes.containsKey(world)))
                    cachedTimes.put(world, world.getTime());

                // put current difficulty in a HashMap for later restoration
                if(!(cachedDifficulties.containsKey(world)))
                    cachedDifficulties.put(world, world.getDifficulty());

                world.setTime(18000);
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            }

            if(moreDifficult.getValue())
                world.setDifficulty(Difficulty.HARD);
            else
                world.setDifficulty(cachedDifficulties.get(world));

            // apply effects to all monsters
            if(mobEffects.getValue()) {
                for(Entity entity : world.getEntities()) {
                    if(entity instanceof Monster) {
                        applyEffect(entity);
                    }
                }
            }
        }
    }

    @Override
    public void disable() {
        super.disable();
        for(World world : Bukkit.getWorlds()) {
            // restore to saved time
            if(cachedTimes.containsKey(world))
                world.setTime(cachedTimes.get(world));
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
        }
    }

    @Override
    public void enable() {
        super.enable();
        for(World world : Bukkit.getWorlds()) {
            if(alwaysNight.getValue()) {
                // put current time in a HashMap for later restoration
                if(!(cachedTimes.containsKey(world)))
                    cachedTimes.put(world, world.getTime());

                // put current difficulty in a HashMap for later restoration
                if(!(cachedDifficulties.containsKey(world)))
                    cachedDifficulties.put(world, world.getDifficulty());

                world.setTime(18000);
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            }

            if (moreDifficult.getValue())
                world.setDifficulty(Difficulty.HARD);

            // apply effects to all monsters
            if(mobEffects.getValue()) {
                for(Entity entity : world.getEntities()) {
                    if(entity instanceof Monster) {
                        applyEffect(entity);
                    }
                }
            }
        }
    }
}
