package birds.chaosMode.ChaosMode.menu;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.Mode;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class OptionsHub extends InventoryPage {
    private Mode[] modes;
    private int size;

    public OptionsHub(ChaosMode chaosMode) {
        super(chaosMode);
        modes = chaosMode.getModes();
        setUpSlots();
    }

    @Override
    public void runSlotAction(int slot, ItemStack item, Player player, ClickType click) {
        if(slot >= size) return;

        switch(item.getType()) {
            case GREEN_STAINED_GLASS_PANE:
                // disable corresponding mode if mode is enabled
                modes[slot - 9].disable();
                setUpSlots();
                // xp sound
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                player.openInventory(page);
                break;

            case RED_STAINED_GLASS_PANE:
                // enable corresponding mode if mode is enabled
                modes[slot - 9].enable();
                setUpSlots();
                // xp sound
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                player.openInventory(page);
                break;

            default:
                // otherwise, go into settings
                Mode currentMode = modes[slot];

                SettingsPage settings = new SettingsPage(chaosMode, currentMode, this);
                player.closeInventory();
                settings.showInventory(player);
                break;
        }
    }

    @Override
    public void setUpSlots() {
        // set size to 2 lines per 9 modes
        int size = 18 * ((modes.length - 1) / 9 + 1);
        this.size = size;
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
            if(iterator >= modes.length)
                break;

            ItemStack toggleIcon;
            // find which icon to use
            if(modes[iterator].isEnabled()) {
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
