package birds.chaosMode.ChaosMode.modes.chaosModes;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.utility.Usables;
import birds.chaosMode.ChaosMode.modes.IntervalMode;
import birds.chaosMode.ChaosMode.modes.options.IntegerOption;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class Corruption extends IntervalMode {
    private IntegerOption radius = new IntegerOption(16, 1, Integer.MAX_VALUE, "radius");
    private IntegerOption biasTowardsSpecial = new IntegerOption(4, 0, 100, "biasTowardsSpecialBlocks");
    private IntegerOption entityChance = new IntegerOption(25, 0, 100, "EntityChance");
    private Random random = new Random();
    private Usables usables;

    public Corruption(ChaosMode chaosMode, Usables usables) {
        super(chaosMode, "Corruption");
        setInternalName("corruption");

        this.usables = usables;

        radius.setIcon(Material.STONE, ChatColor.RESET.toString() + "Radius");
        addOption(radius);

        entityChance.setIcon(Material.CREEPER_SPAWN_EGG, ChatColor.RESET.toString() + "Spawn Entities");
        addOption(entityChance);

        biasTowardsSpecial.setIcon(Material.CHEST, ChatColor.RESET.toString() + "Bias towards Chests/Spawners");
        addOption(biasTowardsSpecial);

        interval.setValue(40);
        interval.setDefaultValue(40);
        interval.setName("interval");

        setIcon(Material.SPONGE, ChatColor.RESET.toString() + getName(), "Click to change settings");
        this.setInterval(40);
    }

    private int randInt(int lowerBound, int higherBound) {
        return random.nextInt((higherBound - lowerBound) + 1) + lowerBound;
    }

    @Override
    public BukkitRunnable getRunnable() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    // ignore players if they're somehow null
                    if(onlinePlayer == null) continue;
                    // get their location
                    Location location = onlinePlayer.getLocation();
                    // get random block in range
                    int range = radius.getValue();
                    double x = randInt(-range, range);
                    double y = randInt(-range, range);
                    double z = randInt(-range, range);
                    // don't go outside of the build limit
                    if(y > 255) y = 255;
                    if(y < 0) y = 0;
                    Location outputLocation = location.add(x, y, z);

                    int result = randInt(1, 100);
                    if(result <= entityChance.getValue()) {
                        onlinePlayer.getWorld().spawnEntity(outputLocation, usables.getUsableEntity());
                    }
                    else {
                        Block block = outputLocation.getBlock();
                        // set the block to a random block material
                        Material newBlock = usables.getUsableBlock();

                        if(result > (100 - biasTowardsSpecial.getValue())) {
                            switch (randInt(0, 1)) {
                                case 0:
                                    newBlock = Material.CHEST;
                                    break;
                                case 1:
                                    newBlock = Material.SPAWNER;
                                    break;
                            }
                        }

                        block.setType(newBlock);
                        block.getState().update(true);
                        // set spawners to random type
                        if (newBlock.equals(Material.SPAWNER)) {
                            CreatureSpawner cs = (CreatureSpawner) block.getState();
                            cs.setSpawnedType(usables.getUsableEntity());
                            cs.update(true);
                        }
                        // set chests to random loot tables
                        if (newBlock.equals(Material.CHEST) || newBlock.equals(Material.TRAPPED_CHEST)) {
                            Chest chest = (Chest) block.getState();
                            chest.setLootTable(usables.getUsableLootTable());
                            chest.update(true);
                        }
                    }
                }
            }
        };
    }
}
