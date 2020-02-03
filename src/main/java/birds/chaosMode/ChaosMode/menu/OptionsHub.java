package birds.chaosMode.ChaosMode.menu;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.Mode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class OptionsHub extends InventoryPage {
    private ArrayList<Mode> modes;
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
                modes.get(slot - 9).disable();
                setUpSlots();
                // xp sound
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                player.openInventory(page);
                break;

            case RED_STAINED_GLASS_PANE:
                // enable corresponding mode if mode is enabled
                modes.get(slot - 9).enable();
                setUpSlots();
                // xp sound
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                player.openInventory(page);
                break;

            case CHEST:
                // save settings
                chaosMode.saveConfig();

                // reload UI
                setUpSlots();
                player.openInventory(page);
                break;

            case ENDER_CHEST:
                // load settings
                chaosMode.loadConfig();

                // reload UI
                setUpSlots();
                player.openInventory(page);
                break;

            case ANVIL:
                // reset settings
                chaosMode.resetToDefaults();

                // reload UI
                setUpSlots();
                player.openInventory(page);
                break;

            default:
                // otherwise, go into settings
                Mode currentMode = modes.get(slot);

                SettingsPage settings = new SettingsPage(chaosMode, currentMode, this);
                settings.showInventory(player);
                break;
        }
    }

    @Override
    public void setUpSlots() {
        // set size to 2 lines per 9 modes
        int size = 18 * ((modes.size() - 1) / 9 + 1) + 9;
        this.size = size;
        page = Bukkit.createInventory(this, size, "Options");
        ItemStack[] items = new ItemStack[size];

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
                toggleIcon = createGuiItem(Material.GREEN_STAINED_GLASS_PANE, ChatColor.RESET.toString() + ChatColor.GREEN.toString() + "Enabled");
            } else {
                toggleIcon = createGuiItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RESET.toString() + ChatColor.RED.toString() + "Disabled");
            }

            items[toggleInterval] = toggleIcon;

            iterator++;
        }

        items[size - 4] = createGuiItem(Material.CHEST, ChatColor.RESET.toString() + "Save");
        items[size - 5] = createGuiItem(Material.ANVIL, ChatColor.RESET.toString() + "Reset All");
        items[size - 6] = createGuiItem(Material.ENDER_CHEST, ChatColor.RESET.toString() + "Load");

        page.setContents(items);
    }
}
