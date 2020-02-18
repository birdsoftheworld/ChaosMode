package birds.chaosMode.ChaosMode.modes.chaosModes;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.IntervalMode;
import birds.chaosMode.ChaosMode.modes.options.BlockListOption;
import birds.chaosMode.ChaosMode.modes.options.BooleanOption;
import birds.chaosMode.ChaosMode.modes.options.IntegerOption;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class BlockCollapse extends IntervalMode implements Listener {
    private BooleanOption collapseUnderPlayers = new BooleanOption(true, "collapse-under-players");
    private BooleanOption collapseWhenInteracted = new BooleanOption(true, "collapse-when-interacted");
    private BooleanOption followCheckLimit = new BooleanOption(false, "follow-max-blocks");
    private IntegerOption maxChecks = new IntegerOption(32, 1, Integer.MAX_VALUE, "max-blocks");
    private BlockListOption blacklist = new BlockListOption("blacklist");

    private ChaosMode chaosMode;

    public BlockCollapse(ChaosMode chaosMode) {
        super(chaosMode, "Block Collapse");

        chaosMode.getServer().getPluginManager().registerEvents(this, chaosMode);

        this.chaosMode = chaosMode;
        setInternalName("blockcollapse");

        collapseUnderPlayers.setIcon(Material.STONE_PRESSURE_PLATE, ChatColor.RESET.toString() + "Collapse Under Players");
        addOption(collapseUnderPlayers);

        collapseWhenInteracted.setIcon(Material.IRON_PICKAXE, ChatColor.RESET.toString() + "Collapse on Interactions");
        addOption(collapseWhenInteracted);

        followCheckLimit.setIcon(Material.BRICK_WALL, ChatColor.RESET.toString() + "Follow Check Limit");
        addOption(followCheckLimit);

        maxChecks.setIcon(Material.BRICK, ChatColor.RESET.toString() + "Fall Checks");
        addOption(maxChecks);

        blacklist.setIcon(Material.BARRIER, ChatColor.RESET + "Item Blacklist");
        addOption(blacklist);

        interval.setValue(1);
        interval.setDefaultValue(1);
        removeOption(interval);

        setIcon(Material.GLASS, ChatColor.RESET.toString() + getName(), ChatColor.RESET.toString() + ChatColor.RED.toString() + ChatColor.BOLD.toString() + "WARNING: This can get laggy.");
    }

    @EventHandler
    public void onDestroyBlock(BlockBreakEvent event) {
        if(!isEnabled()) return;
        if(!(collapseWhenInteracted.getValue())) return;

        Block block = event.getBlock();
        doFallLater(block, 0, 1);
    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent event) {
        if(!isEnabled()) return;
        if(!(collapseWhenInteracted.getValue())) return;

        Block block = event.getBlock();
        doFallLater(block, 0, 1);
    }

    private void doRecursiveFallingForBlock(Block block, int checks) {
        BlockFace[] neighbors = {BlockFace.SELF, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN};

        float hardness = block.getBlockData().getMaterial().getHardness();

        if(block.isLiquid() || hardness >= 5 || hardness < 0) return;

        for(BlockFace face : neighbors) {
            final Block selectedBlock = block.getRelative(face);

            // ignore blacklisted or non-whitelisted blocks
            if(blacklist.getBlocks().contains(selectedBlock.getType())) continue;

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
                doFallLater(selectedBlock, checks, 1);
        }
    }

    private void doFallLater(final Block block, final int blockNumber, final int delay) {
        // ignore blacklisted or non-whitelisted blocks
        if(blacklist.getBlocks().contains(block.getType())) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                if(blockNumber + 1 > maxChecks.getValue() && followCheckLimit.getValue())
                    return;
                doRecursiveFallingForBlock(block, blockNumber + 1);
            }
        }.runTaskLater(chaosMode, delay);
    }

    @Override
    public BukkitRunnable getRunnable() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                if(!(collapseUnderPlayers.getValue())) return;
                for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    Location location = onlinePlayer.getLocation();
                    location.setY(location.getY() - 1);
                    Block block = location.getBlock();
                    doFallLater(block, 0, 5); // 5 is enough for the player to land on the block
                }
            }
        };
    }
}
