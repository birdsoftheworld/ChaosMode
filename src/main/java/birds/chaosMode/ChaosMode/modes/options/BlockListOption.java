package birds.chaosMode.ChaosMode.modes.options;

import org.bukkit.Material;

public class BlockListOption extends ItemListOption {
    @Override
    public boolean itemIsAcceptable(Material item) {
        return item.isBlock() && !items.contains(item);
    }
}
