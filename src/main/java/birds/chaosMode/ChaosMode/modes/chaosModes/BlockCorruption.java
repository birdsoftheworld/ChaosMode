package birds.chaosMode.ChaosMode.modes.chaosModes;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.IntervalMode;
import birds.chaosMode.ChaosMode.modes.options.IntegerOption;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;

public class BlockCorruption extends IntervalMode {
    private IntegerOption radius;
    private Random random;
    private ArrayList<Material> usableBlocks;

    private void discoverBlocks() {
        Bukkit.getLogger().info("Starting block discovery...");
        usableBlocks = new ArrayList<>();
        // get all blocks
        for(Material material : Material.values()) {
            // only include if a block and isn't deprecated
            if(material.isBlock() && !(material.toString().contains("LEGACY")))
                usableBlocks.add(material);
        }
        Bukkit.getLogger().info("Finished block discovery!");
    }

    public BlockCorruption(ChaosMode chaosMode) {
        super(chaosMode, "BlockCorruption");
        random = new Random();
        radius = new IntegerOption(7, 1, Integer.MAX_VALUE);
        radius.setIcon(Material.STONE, "Radius", "Radius");
        addOption(radius);
        interval.setValue(10);
        interval.setDefaultValue(10);
        setIcon(Material.SPONGE, ChatColor.RESET.toString() + getName(), "Click to change settings");
        this.setInterval(10);

        discoverBlocks();
    }

    private int randInt(int lowerBound, int higherBound) {
        return random.nextInt((higherBound - lowerBound) + 1) + lowerBound;
    }

    public Material getUsableBlock() {
        return usableBlocks.get(randInt(0, usableBlocks.size() - 1));
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
                    Block block = location.add(x, y, z).getBlock();
                    // set the block to a random block material
                    block.setType(getUsableBlock());
                    block.getState().update(true);
                }
            }
        };
    }
}
