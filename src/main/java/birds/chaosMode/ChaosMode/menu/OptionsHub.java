package birds.chaosMode.ChaosMode.menu;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.Mode;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class OptionsHub extends InventoryPage {
    private List<Mode> modes;

    public OptionsHub(ChaosMode chaosMode) {
        super(chaosMode);
        modes = chaosMode.getModes();
        setUpSlots();
    }

    @Override
    public void runSlotAction(int slot, ItemStack item, Player player) {

        if (item.getType().equals(Material.GREEN_STAINED_GLASS_PANE)) {
            // disable corresponding mode if mode is enabled
            modes.get(slot - 9).disable();
            setUpSlots();
            player.openInventory(page);
        } else if (item.getType().equals(Material.RED_STAINED_GLASS_PANE)) {
            // enable corresponding mode if mode is enabled
            modes.get(slot - 9).enable();
            setUpSlots();
            player.openInventory(page);
        } else {
            // otherwise, go into settings
            Mode currentMode = modes.get(slot);

            SettingsPage settings = new SettingsPage(chaosMode, currentMode);
            player.closeInventory();
            settings.showInventory(player);
        }
    }

    @Override
    public void setUpSlots() {
        // set size to 2 lines per 9 modes
        int size = 18 * ((modes.size() - 1) / 9 + 1);
        page = Bukkit.createInventory(this, size, "Options");
        ItemStack[] items = new ItemStack[18];

        int iterator = 0;
        // iterate through currently set modes and set their icon
        for(Mode mode : modes) {
            items[iterator] = mode.getIcon();
            iterator++;
            if(iterator % 9 == 0)
                iterator += 9;
        }

        // go through every other line and add enable / disable toggles
        iterator = 0;
        for(int toggleInterval = 0; toggleInterval < size; toggleInterval++) {
            // skip lines
            if(toggleInterval % 9 == 0)
                toggleInterval += 9;

            // stop if no more modes
            if(iterator >= modes.size())
                break;

            ItemStack toggleIcon;
            // find which icon to use
            if(modes.get(iterator).isEnabled()) {
                toggleIcon = createGuiItem(Material.GREEN_STAINED_GLASS_PANE, "Click to Disable");
            } else {
                toggleIcon = createGuiItem(Material.RED_STAINED_GLASS_PANE, "Click to Enable");
            }

            items[toggleInterval] = toggleIcon;

            iterator++;
        }

        page.setContents(items);
    }
}
