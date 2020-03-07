package birds.chaosMode.ChaosMode.modes.chaosModes;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.ListenerMode;
import birds.chaosMode.ChaosMode.modes.options.BlockListOption;
import birds.chaosMode.ChaosMode.modes.options.BooleanOption;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GodMode extends ListenerMode {

    private ChaosMode chaosMode;
    private BooleanOption doRemoveBlocks = new BooleanOption(true, "do-remove-blocks");
    private BlockListOption summonedBlockType = new BlockListOption("Summoned Block");
    private Player godPlayer;
    private int maxChecks = 32;

    public GodMode(ChaosMode chaosMode) {
        super(chaosMode, "God Mode");
        this.chaosMode = chaosMode;

        doRemoveBlocks.setIcon(Material.ENDER_PEARL, "Remove Blocks", "Allow the god player to remove blocks.");
        addOption(doRemoveBlocks);

        Material[] defaultSummonBlock = {Material.COBBLESTONE};
        summonedBlockType.setDefaultItems(Arrays.asList(defaultSummonBlock));
        summonedBlockType.setMaxItems(1);
        summonedBlockType.setIcon(Material.COBBLESTONE, "Summoned Block", "Block that will be summoned by the god player.");
        addOption(summonedBlockType);

        setIcon(Material.NETHER_STAR, ChatColor.RESET.toString() + getName(), "Click to change settings");
    }

    @Override
    public void enable() {
        super.enable();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.getInventory().setItem(0, new ItemStack(Material.NETHER_STAR));
        }
    }

    @Override
    public void disable() {
        super.disable();
        Player god = getGodPlayer();
        god.setInvulnerable(false);
        god.setFlying(false);
        god.setAllowFlight(false);
    }

    private RayTraceResult raytraceBlock(Player eventPlayer) {
        //get player looking direction
        Vector playerFacing = eventPlayer.getEyeLocation().getDirection();
        //location to start raytrace
        Location playerEyeLocation = eventPlayer.getEyeLocation();
        //raytrace from player eye location in facing direction to block, max dist 32, always collide with fluids
        RayTraceResult rayTraceResult = eventPlayer.getWorld().rayTraceBlocks(playerEyeLocation, playerFacing, 64, FluidCollisionMode.NEVER);
        return rayTraceResult;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!isEnabled()) return;
        Action eventAction = event.getAction();
        Player eventPlayer = event.getPlayer();
        if (eventAction == Action.LEFT_CLICK_AIR || eventAction == Action.LEFT_CLICK_BLOCK) {
            switch (eventPlayer.getInventory().getItemInMainHand().getType()) {
                case STICK:
                    if (eventPlayer == getGodPlayer()) {

                        RayTraceResult rayTraceResult = raytraceBlock(eventPlayer);

                        //return if no block found
                        if (rayTraceResult == null) return;
                        //get block from raytrace
                        Block resultingBlock = rayTraceResult.getHitBlock();

                        if (resultingBlock == null) return;

                        //delete block
                        resultingBlock.setType(Material.AIR);
                    }
                    break;
                case BLAZE_ROD:
                    if (eventPlayer == getGodPlayer()) {
                        RayTraceResult rayTraceResult = raytraceBlock(eventPlayer);

                        //return if no block found
                        if (rayTraceResult == null) return;
                        //get block from raytrace
                        Block resultingBlock = rayTraceResult.getHitBlock();

                        if (resultingBlock == null) return;

                        //delete block
                        doDeleteLater(resultingBlock, 0, 1, resultingBlock.getType());
                    }
                    break;
                case BLAZE_POWDER:
                    if (eventPlayer == getGodPlayer()) {
                        Vector playerFacing = eventPlayer.getEyeLocation().getDirection();
                        Location playerEyeLocation = eventPlayer.getEyeLocation();
                        Entity fireball = eventPlayer.getWorld().spawnEntity(playerEyeLocation, EntityType.FIREBALL);
                        fireball.setVelocity(playerFacing.multiply(2));
                    }
                    break;
            }
        } else if (eventAction == Action.RIGHT_CLICK_AIR || eventAction == Action.RIGHT_CLICK_BLOCK) {
            switch (eventPlayer.getInventory().getItemInMainHand().getType()) {
                case NETHER_STAR:
                    //make player the god player
                    setGodPlayer(eventPlayer);
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        onlinePlayer.getInventory().remove(Material.NETHER_STAR);
                    }
                    eventPlayer.getInventory().clear();
                    eventPlayer.getInventory().setHeldItemSlot(0);
                    HashMap<String[], Material> newInventory = new HashMap<>();
                    newInventory.put(new String[]{"Many Blocks", "Left Click to Remove"}, Material.BLAZE_ROD);
                    newInventory.put(new String[]{"Fireball", "Left Click for Fireball", "Right Click for Lightning"}, Material.BLAZE_POWDER);
                    newInventory.put(new String[]{"Single Blocks", "Left Click to Remove", "Right Click to Add"}, Material.STICK);
                    int index = 0;
                    for (String[] s : newInventory.keySet()) {
                        String itemName = s[0];
                        ItemStack item = new ItemStack(newInventory.get(s), 1);
                        ItemMeta meta = item.getItemMeta();
                        if (meta != null) {
                            meta.setDisplayName(itemName);
                            if (s.length > 1) {
                                List<String> loreArray = new ArrayList<>();
                                for (int i = 1; i < s.length; i++) {
                                    loreArray.add(s[i]);
                                }
                                meta.setLore(loreArray);
                            }
                            item.setItemMeta(meta);
                        }
                        eventPlayer.getInventory().setItem(index, item);
                        index += 1;
                    }
                    eventPlayer.setInvulnerable(true);
                    eventPlayer.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 1, false, false));
                    eventPlayer.sendMessage(ChatColor.BOLD.toString() + ChatColor.GOLD.toString() + "You are now the god.");
                    eventPlayer.setAllowFlight(true);
                    eventPlayer.setFlying(true);
                    eventPlayer.setHealth(20.0);
                    break;
                case STICK:
                    if (eventPlayer == godPlayer) {

                        RayTraceResult rayTraceResult = raytraceBlock(eventPlayer);

                        //return if no block found
                        if (rayTraceResult == null) return;
                        //get block from raytrace
                        Block resultBlock = rayTraceResult.getHitBlock();
                        //get block face from raytrace
                        BlockFace resultHitBlockFace = rayTraceResult.getHitBlockFace();

                        if (resultBlock == null || resultHitBlockFace == null) return;

                        //set to cobble
                        resultBlock.getRelative(resultHitBlockFace).setType(summonedBlockType.getItems().get(0));
                    }
                    break;
                case BLAZE_POWDER:
                    if (eventPlayer == godPlayer) {
                        RayTraceResult rayTraceResult = raytraceBlock(eventPlayer);

                        if (rayTraceResult == null) return;

                        Block resultBlock = rayTraceResult.getHitBlock();
                        //get targeted block's location
                        Location targetLocation = resultBlock.getLocation();
                        //spawn lightning at target location
                        eventPlayer.getWorld().spawnEntity(targetLocation, EntityType.LIGHTNING);
                    }
                    break;
            }
        }
    }

    private void doDeleteLater(final Block block, final int blockNumber, final int delay, final Material initialMaterial) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(blockNumber + 1 > maxChecks)
                    return;
                doRecursiveDeletingForBlock(block, blockNumber + 1, initialMaterial);
            }
        }.runTaskLater(chaosMode, delay);
    }

    private void doRecursiveDeletingForBlock(Block block, int checks, Material initialMaterial) {
        BlockFace[] neighbors = {BlockFace.SELF, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN};

        float hardness = block.getBlockData().getMaterial().getHardness();

        if(block.isLiquid() || hardness >= 5 || hardness < 0) return;

        for(BlockFace face : neighbors) {
            final Block selectedBlock = block.getRelative(face);

            // ignore passable blocks
            if(selectedBlock.isPassable()) continue;

            // ignore liquids
            if(selectedBlock.isLiquid()) continue;

            // ignore hard blocks
            if(selectedBlock.getBlockData().getMaterial().getHardness() >= 5 || selectedBlock.getBlockData().getMaterial().getHardness() < 0) continue;

            // only fall if block below is empty
            if(!(selectedBlock.getType() == initialMaterial)) continue;

            selectedBlock.setType(Material.AIR);

            // don't fall itself, as that would cause an infinite loop
            if(!face.equals(BlockFace.SELF))
                doDeleteLater(selectedBlock, checks, 1, initialMaterial);
        }
    }

    public Player getGodPlayer() {
        return godPlayer;
    }

    public void setGodPlayer(Player godPlayer) {
        this.godPlayer = godPlayer;
    }

}
