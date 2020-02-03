package birds.chaosMode.ChaosMode.modes.chaosModes;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.ListenerMode;
import birds.chaosMode.ChaosMode.modes.options.BooleanOption;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.TimeSkipEvent;

import java.util.HashMap;

public class EternalNight extends ListenerMode {
    private HashMap<World, Long> cachedTimes = new HashMap<>();
    private HashMap<World, Difficulty> cachedDifficulties = new HashMap<>();

    private BooleanOption moreDifficult = new BooleanOption(true, "moreDifficult");

    public EternalNight(ChaosMode chaosMode) {
        super(chaosMode, "Eternal Night");
        setInternalName("eternalnight");

        moreDifficult.setIcon(Material.CREEPER_HEAD, ChatColor.RESET.toString() + "Harder Difficulty");
        addOption(moreDifficult);

        setIcon(Material.DAYLIGHT_DETECTOR, ChatColor.RESET.toString() + getName(), "Click to change settings");
    }

    @EventHandler
    public void onSkipTime(TimeSkipEvent event) {
        // don't let players skip to day
        if(isEnabled()) {
            // allow plugins to set time
            if(!event.getSkipReason().equals(TimeSkipEvent.SkipReason.CUSTOM))
                event.setCancelled(true);
        }
    }

    @Override
    public void update() {
        for(World world : Bukkit.getWorlds()) {
            if(moreDifficult.getValue())
                world.setDifficulty(Difficulty.HARD);
            else
                world.setDifficulty(cachedDifficulties.get(world));
        }
    }

    @Override
    public void disable() {
        super.disable();
        for(World world : Bukkit.getWorlds()) {
            // restore to saved time
            world.setTime(cachedTimes.get(world));
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
        }
    }

    @Override
    public void enable() {
        super.enable();
        for(World world : Bukkit.getWorlds()) {
            // put current time in a HashMap for later restoration
            cachedTimes.put(world, world.getTime());

            // put current difficulty in a HashMap for later restoration
            cachedDifficulties.put(world, world.getDifficulty());

            world.setTime(18000);
            if(moreDifficult.getValue())
                world.setDifficulty(Difficulty.HARD);
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        }
    }
}
