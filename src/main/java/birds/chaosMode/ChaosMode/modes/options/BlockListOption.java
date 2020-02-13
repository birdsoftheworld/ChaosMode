package birds.chaosMode.ChaosMode.modes.options;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.List;

public class BlockListOption extends ItemListOption {
    private HashMap<Material, Material> redirectItems = new HashMap<>();

    public BlockListOption(String name) {
        super(name);

        // items which can count as blocks
        redirectItems.put(Material.LAVA_BUCKET, Material.LAVA);
        redirectItems.put(Material.WATER_BUCKET, Material.WATER);
    }

    public List<Material> getBlocks() {
        // get blocks plus items which can be re-directed to blocks

        List<Material> blocks = items;
        for(Material item : blocks) {
            if(redirectItems.containsKey(item)) {
                blocks.remove(item);
                blocks.add(redirectItems.get(item));
            }
        }

        return blocks;
    }

    @Override
    public boolean itemIsAcceptable(Material item) {
        // only accept non-duplicated blocks (or re-directed items)
        boolean isBlock = item.isBlock() || redirectItems.containsKey(item);
        boolean isUnique = !items.contains(item);
        boolean fitsInMax = items.size() + 1 <= maxItems;
        return isBlock && isUnique && fitsInMax;
    }
}
