package birds.chaosMode.ChaosMode.modes.chaosModes;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.ListenerMode;
import birds.chaosMode.ChaosMode.modes.options.BlockListOption;
import birds.chaosMode.ChaosMode.modes.options.BooleanOption;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class BlockCollapse extends ListenerMode {
    private BooleanOption followWhiteList = new BooleanOption(false);
    private BlockListOption blacklist = new BlockListOption();
    private BlockListOption whitelist = new BlockListOption();

    public BlockCollapse(ChaosMode chaosMode) {
        super(chaosMode, "Block Collapse");

        followWhiteList.setIcon(Material.WHITE_WOOL, ChatColor.RESET.toString() + "Follow Item Whitelist");
        addOption(followWhiteList);

        blacklist.setIcon(Material.BARRIER, ChatColor.RESET + "Item Blacklist");
        addOption(blacklist);

        whitelist.setIcon(Material.LIME_DYE, ChatColor.RESET + "Item Whitelist");
        addOption(whitelist);

        setIcon(Material.GLASS, ChatColor.RESET.toString() + getName(), ChatColor.RESET.toString() + ChatColor.RED.toString() + ChatColor.BOLD.toString() + "WARNING: This mode can get very laggy. Use with caution.");
    }

    @EventHandler
    public void onDestroyBlock(BlockBreakEvent event) {
        if(!isEnabled()) return;

        Block block = event.getBlock();
        doFallLater(block);
    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent event) {
        if(!isEnabled()) return;

        Block block = event.getBlock();
        doFallLater(block);
    }

    private void doRecursiveFallingForBlock(Block block) {
        BlockFace[] neighbors = {BlockFace.SELF, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN};
        float hardness = block.getBlockData().getMaterial().getHardness();
        if(block.isLiquid() || hardness >= 5 || hardness < 0) return;
        if(blacklist.getItems().contains(block.getType())) return;
        if(followWhiteList.getValue() && !whitelist.getItems().contains(block.getType())) return;

        for(BlockFace face : neighbors) {
            final Block selectedBlock = block.getRelative(face);
            // ignore passable blocks
            if(selectedBlock.isPassable()) continue;

            // ignore liquids
            if(selectedBlock.isLiquid()) continue;

            // ignore hard blocks
            if(selectedBlock.getBlockData().getMaterial().getHardness() >= 5) continue;

            // only fall if block below is empty
            if(!selectedBlock.getRelative(BlockFace.DOWN).isEmpty()) continue;

            Location blockLocation = selectedBlock.getLocation().add(0.5, 0.0, 0.5);
            if(blockLocation.getWorld() == null) continue;

            blockLocation.getWorld().spawnFallingBlock(blockLocation, selectedBlock.getBlockData());
            selectedBlock.setType(Material.AIR);

            // don't fall itself, as that would cause an infinite loop
            if(!face.equals(BlockFace.SELF))
                doFallLater(selectedBlock);
        }
    }

    private void doFallLater(final Block block) {
        new BukkitRunnable() {
            @Override
            public void run() {
                doRecursiveFallingForBlock(block);
            }
        }.runTaskLater(chaosMode, 1);
    }
}
