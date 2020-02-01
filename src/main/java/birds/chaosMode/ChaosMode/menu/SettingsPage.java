package birds.chaosMode.ChaosMode.menu;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.Mode;
import birds.chaosMode.ChaosMode.modes.options.ConfigurableOption;
import birds.chaosMode.ChaosMode.modes.options.IntegerOption;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class SettingsPage extends InventoryPage {
    private ArrayList<ConfigurableOption> options;
    private Inventory page;
    private Mode mode;
    private OptionsHub hub;

    SettingsPage(ChaosMode chaosMode, Mode mode, OptionsHub hub) {
        super(chaosMode);
        this.options = mode.getOptions();
        this.mode = mode;
        this.hub = hub; // for exiting the menu
        setUpSlots();
    }

    @Override
    public void runSlotAction(int slot, ItemStack item, Player player, ClickType click) {
        // ignore double-clicks
        if(click.equals(ClickType.DOUBLE_CLICK)) return;
        if(item.getType().equals(Material.GREEN_STAINED_GLASS_PANE)) {
            // xp sound
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            ConfigurableOption selected = options.get((slot - 3) / 9);
            if(selected instanceof IntegerOption)
                ((IntegerOption) selected).setValue(((IntegerOption) selected).getValue() + 1);
            // redisplay dialog
            setUpSlots();
            player.openInventory(page);
        } else if(item.getType().equals(Material.RED_STAINED_GLASS_PANE)) {
            // xp sound
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            ConfigurableOption selected = options.get((slot - 5) / 9);
            if(selected instanceof IntegerOption)
                ((IntegerOption) selected).setValue(((IntegerOption) selected).getValue() - 1);
            // redisplay dialog
            setUpSlots();
            player.openInventory(page);
        } else if(item.getType().equals(Material.IRON_INGOT)) {
            player.closeInventory();
            hub.showInventory(player);
        }
    }

    @Override
    public void setUpSlots() {
        // create a new inventory with 1 line per setting
        page = Bukkit.createInventory(this, 9 * options.size() + 9, mode.getName() + " Settings");
        ItemStack[] contents = new ItemStack[9 * options.size() + 9];

        int iterator = 0;
        for(ConfigurableOption option : options) {
            if(option instanceof IntegerOption) {
                // on click: add 1
                if(((IntegerOption) option).getValue() > ((IntegerOption) option).getMinimumValue())
                    contents[iterator + 3] = createGuiItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RESET.toString() + ChatColor.RED.toString() + "-1");
                // on click: reset to default
                ItemStack optionIcon = option.getIcon();
                ItemMeta optionMeta = optionIcon.getItemMeta();
                optionMeta.setDisplayName(ChatColor.RESET.toString() + ((IntegerOption) option).getValue());
                optionIcon.setItemMeta(optionMeta);
                contents[iterator + 4] = optionIcon;
                // on click: subtract 1
                if(((IntegerOption) option).getValue() < ((IntegerOption) option).getMaximumValue())
                    contents[iterator + 5] = createGuiItem(Material.GREEN_STAINED_GLASS_PANE, ChatColor.RESET.toString() + ChatColor.GREEN.toString() + "+1");
            }
            iterator += 9;
            if(iterator == 9 * options.size())
                contents[iterator + 4] = createGuiItem(Material.IRON_INGOT, ChatColor.RESET.toString() + "Exit");
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
