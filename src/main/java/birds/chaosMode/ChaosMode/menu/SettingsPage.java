package birds.chaosMode.ChaosMode.menu;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.Mode;
import birds.chaosMode.ChaosMode.modes.options.ConfigurableOption;
import birds.chaosMode.ChaosMode.modes.options.IntegerOption;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class SettingsPage extends InventoryPage {
    private ArrayList<ConfigurableOption> options;
    private Inventory page;
    private Mode mode;

    SettingsPage(ChaosMode chaosMode, Mode mode) {
        super(chaosMode);
        options = mode.getOptions();
        this.mode = mode;
        setUpSlots();
    }

    @Override
    public void runSlotAction(int slot, ItemStack item, Player player) {

    }

    @Override
    public void setUpSlots() {
        // create a new inventory with 1 line per setting
        page = Bukkit.createInventory(this, 9 * options.size(), mode.getName() + " Settings");
        ItemStack[] contents = new ItemStack[9 * options.size()];

        int iterator = 0;
        for(ConfigurableOption option : options) {
            if(option instanceof IntegerOption) {
                // on click: add 1
                contents[iterator + 3] = createGuiItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RESET.toString() + ChatColor.RED.toString() + "-1");
                // on click: reset to default
                contents[iterator + 4] = option.getIcon();
                // on click: subtract 1
                contents[iterator + 5] = createGuiItem(Material.GREEN_STAINED_GLASS_PANE, ChatColor.RESET.toString() + ChatColor.GREEN.toString() + "+1");
            }
            iterator += 9;
        }

        page.setContents(contents);
    }


    // not overriding this method somehow causes a NullPointerException when calling player.openInventory. do not remove
    @Override
    public void showInventory(Player player) {
        // open the inventory
        player.openInventory(page);
    }
}
