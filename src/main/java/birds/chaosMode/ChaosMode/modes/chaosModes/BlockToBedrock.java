package birds.chaosMode.ChaosMode.modes.chaosModes;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.ListenerMode;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockToBedrock extends ListenerMode {

    public BlockToBedrock(ChaosMode chaosMode) {
        super(chaosMode, "Block to Bedrock");
        setIcon(Material.BEDROCK, ChatColor.RESET.toString() + getName(), "Click to activation");
    }

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent event) {
        if(!isEnabled()) return;
        event.getBlockPlaced().setType(Material.BEDROCK);
    }
}
