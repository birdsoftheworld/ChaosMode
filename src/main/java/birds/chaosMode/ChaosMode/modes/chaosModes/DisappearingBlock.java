package birds.chaosMode.ChaosMode.modes.chaosModes;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.IntervalMode;
import birds.chaosMode.ChaosMode.modes.options.BlockListOption;
import birds.chaosMode.ChaosMode.modes.options.BooleanOption;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.HashMap;

public class DisappearingBlock extends IntervalMode {

    private BlockListOption blacklist = new BlockListOption("blacklist");
    private BlockListOption blockToSet = new BlockListOption("blocks-list");
    private BooleanOption onLookAway = new BooleanOption(false, "on-look-away");

    public DisappearingBlock(ChaosMode chaosMode) {
        super(chaosMode, "Disappearing Blocks");
        setInternalName("disappearing-blocks");
        setIcon(Material.BUCKET, ChatColor.RESET.toString() + getName());

        blacklist.setIcon(Material.BARRIER, ChatColor.RESET + "Block Blacklist");
        Material[] defaultBlacklist = {Material.OBSIDIAN, Material.NETHER_PORTAL, Material.END_PORTAL,
                                       Material.END_PORTAL_FRAME, Material.BEDROCK};
        blacklist.setDefaultItems(Arrays.asList(defaultBlacklist));
        addOption(blacklist);

        blockToSet.setIcon(Material.GRASS, ChatColor.RESET + "Block to turn into", "If none set, will default to air.");
        blockToSet.setMaxItems(1);
        addOption(blockToSet);

        onLookAway.setIcon(Material.ENDER_EYE, ChatColor.RESET.toString() + "Change on Look Away");
        addOption(onLookAway);

        interval.setValue(20);
        interval.setDefaultValue(20);
    }

    @Override
    public BukkitRunnable getRunnable() {
        return new BukkitRunnable() {
            HashMap<Player, Location> cachedBlock = new HashMap<>();

            @Override
            public void run() {

                // set type of block to set
                Material type = Material.AIR;
                if(blockToSet.getBlocks().size() > 0)
                    type = blockToSet.getBlocks().get(0);

                for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    // player's look direction
                    Vector playerFacing = onlinePlayer.getEyeLocation().getDirection();

                    // player's head block
                    Location headLocation = onlinePlayer.getEyeLocation();

                    // raytrace from player to facing direction with max of 32 blocks
                    RayTraceResult result = onlinePlayer.getWorld().rayTraceBlocks(headLocation, playerFacing, 32, FluidCollisionMode.ALWAYS);

                    if(result == null)
                        continue;

                    Block resultingBlock = result.getHitBlock();

                    // don't do anything if the block is air
                    if(resultingBlock.isEmpty())
                        continue;

                    // don't do anything for blacklisted items
                    if(blacklist.getBlocks().contains(resultingBlock.getType()))
                        continue;

                    // if onLookAway is enabled, only set blocks when the player looks away from a block
                    if(onLookAway.getValue()) {
                        if(cachedBlock.containsKey(onlinePlayer)) {
                            Location cachedBlockLocation = cachedBlock.get(onlinePlayer);
                            // if the resulting block's location equals the cached block location, set the cached to type
                            if(!(resultingBlock.getLocation().equals(cachedBlockLocation))) {
                                cachedBlockLocation.getBlock().setType(type);
                            }
                        }
                    } else {
                        resultingBlock.setType(type);
                    }

                    cachedBlock.put(onlinePlayer, resultingBlock.getLocation());
                }
            }
        };
    }
}
