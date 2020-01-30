package birds.chaosMode.ChaosMode.menu;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.Mode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class OptionsHub extends InventoryPage {
    private Mode[] modes;

    public OptionsHub(ChaosMode chaosMode) {
        super(chaosMode);
        modes = chaosMode.getModes();
        page = Bukkit.createInventory(this, 18, "Options");
        ItemStack[] items = new ItemStack[18];

        int iterator = 0;
        // iterate through currently set modes and set their icon
        for(Mode item : modes) {
            items[iterator] = item.getIcon();
            iterator++;
        }

        page.setContents(items);
    }

    @Override
    public void runSlotAction(int slot, ItemStack item, Player player) {
        Mode currentMode = modes[slot];
        if(currentMode.isEnabled()) {
            currentMode.disable();
        } else {
            currentMode.enable();
        }
    }
}
