package birds.chaosMode.ChaosMode.menu;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.Mode;
import birds.chaosMode.ChaosMode.modes.options.BooleanOption;
import birds.chaosMode.ChaosMode.modes.options.ConfigurableOption;
import birds.chaosMode.ChaosMode.modes.options.IntegerOption;
import birds.chaosMode.ChaosMode.modes.options.ItemListOption;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class SettingsPage extends InventoryPage {
    private ArrayList<ConfigurableOption> options;
    private Mode mode;
    private OptionsHub hub;
    private int size;

    SettingsPage(ChaosMode chaosMode, Mode mode, OptionsHub hub) {
        super(chaosMode);
        this.options = mode.getOptions();
        this.mode = mode;
        this.hub = hub; // for exiting the menu
        setUpSlots();
    }

    @Override
    public void runSlotAction(int slot, ItemStack item, Player player, ClickType click) {
        if(slot >= size - 1) return;

        // ignore double-clicks
        if(click.equals(ClickType.DOUBLE_CLICK)) return;

        // xp sound
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);

        ConfigurableOption selected;
        boolean isShift = click.equals(ClickType.SHIFT_LEFT) || click.equals(ClickType.SHIFT_RIGHT);
        switch(item.getType()) {
            case RED_STAINED_GLASS_PANE:
                selected = options.get((slot - 1) / 9);
                if(selected instanceof IntegerOption) {
                    IntegerOption integerSelected = (IntegerOption) selected;
                    if(isShift)
                        integerSelected.setValue(integerSelected.getValue() - 100);
                    else
                        integerSelected.setValue(integerSelected.getValue() - 20);
                    if(integerSelected.getValue() < integerSelected.getMinimumValue())
                        integerSelected.setValue(integerSelected.getMinimumValue());
                }
                if(selected instanceof BooleanOption)
                    ((BooleanOption) selected).setValue(false);
                // redisplay dialog
                setUpSlots();
                player.openInventory(page);
                break;

            case PINK_STAINED_GLASS_PANE:
                selected = options.get((slot - 2) / 9);
                if(selected instanceof IntegerOption)
                    ((IntegerOption) selected).setValue(((IntegerOption) selected).getValue() - 1);
                // redisplay dialog
                setUpSlots();
                player.openInventory(page);
                break;

            case LIME_STAINED_GLASS_PANE:
                selected = options.get((slot - 4) / 9);
                if (selected instanceof IntegerOption)
                    ((IntegerOption) selected).setValue(((IntegerOption) selected).getValue() + 1);
                if(selected instanceof BooleanOption)
                    ((BooleanOption) selected).setValue(true);
                // redisplay dialog
                setUpSlots();
                player.openInventory(page);
                break;

            case GREEN_STAINED_GLASS_PANE:
                selected = options.get((slot - 5) / 9);
                if(selected instanceof IntegerOption) {
                    IntegerOption integerSelected = (IntegerOption) selected;
                    if(isShift)
                        integerSelected.setValue(integerSelected.getValue() + 100);
                    else
                        integerSelected.setValue(integerSelected.getValue() + 20);
                    if(integerSelected.getValue() > integerSelected.getMaximumValue())
                        integerSelected.setValue(integerSelected.getMaximumValue());
                }
                // redisplay dialog
                setUpSlots();
                player.openInventory(page);
                break;

            case WHITE_STAINED_GLASS_PANE:
                selected = options.get((slot + 1) / 9);
                if (selected instanceof IntegerOption)
                    ((IntegerOption) selected).setValue(((IntegerOption) selected).getMinimumValue());
                // redisplay dialog
                setUpSlots();
                player.openInventory(page);
                break;

            case BLACK_STAINED_GLASS_PANE:
                selected = options.get((slot - 7) / 9);
                if (selected instanceof IntegerOption)
                    ((IntegerOption) selected).setValue(((IntegerOption) selected).getMaximumValue());
                // redisplay dialog
                setUpSlots();
                player.openInventory(page);
                break;

            case IRON_INGOT:
                player.closeInventory();
                hub.showInventory(player);

                mode.update();
                break;

            default:
                selected = options.get((slot - 3) / 9);
                if (selected instanceof IntegerOption)
                    ((IntegerOption) selected).setValue(((IntegerOption) selected).getDefaultValue());
                if (selected instanceof BooleanOption)
                    ((BooleanOption) selected).setValue(((BooleanOption) selected).getDefaultValue());
                if (selected instanceof ItemListOption) {
                    ItemListPage listPage = new ItemListPage(chaosMode, (ItemListOption) selected, mode, this);

                    listPage.showInventory(player);
                    break;
                }

                // redisplay dialog
                setUpSlots();
                player.openInventory(page);
                break;
        }
    }

    @Override
    public void setUpSlots() {
        // create a new inventory with 1 line per setting
        size = 9 * options.size() + 9;
        page = Bukkit.createInventory(this, size, mode.getName() + " Settings");
        ItemStack[] contents = new ItemStack[9 * options.size() + 9];

        int iterator = 0;
        for(ConfigurableOption option : options) {
            // on click: reset to default
            ItemStack optionIcon = option.getIcon();
            ItemMeta optionMeta = optionIcon.getItemMeta();
            if(option instanceof IntegerOption) {
                // on click: set to minimum value
                contents[iterator] = createGuiItem(Material.WHITE_STAINED_GLASS_PANE, ChatColor.RESET.toString() + "Set to Minimum Value");

                // on click: subtract 20
                if(((IntegerOption) option).getValue() - 20 >= ((IntegerOption) option).getMinimumValue())
                    contents[iterator + 2] = createGuiItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RESET.toString() + ChatColor.RED.toString() + "-20", "Hold shift to subtract 100");

                // on click: subtract 1
                if(((IntegerOption) option).getValue() > ((IntegerOption) option).getMinimumValue())
                    contents[iterator + 3] = createGuiItem(Material.PINK_STAINED_GLASS_PANE, ChatColor.RESET.toString() + ChatColor.RED.toString() + "-1");

                assert optionMeta != null;
                ArrayList<String> lores = new ArrayList<>();
                lores.add(Integer.toString(((IntegerOption) option).getValue()));
                optionMeta.setLore(lores);

                // on click: add 1
                if(((IntegerOption) option).getValue() < ((IntegerOption) option).getMaximumValue())
                    contents[iterator + 5] = createGuiItem(Material.LIME_STAINED_GLASS_PANE, ChatColor.RESET.toString() + ChatColor.GREEN.toString() + "+1");

                // on click: add 20
                if(((IntegerOption) option).getValue() + 20 <= ((IntegerOption) option).getMaximumValue())
                    contents[iterator + 6] = createGuiItem(Material.GREEN_STAINED_GLASS_PANE, ChatColor.RESET.toString() + ChatColor.GREEN.toString() + "+20", "Hold shift to subtract 100");

                // on click: set to maximum value
                if(((IntegerOption) option).getMaximumValue() != Integer.MAX_VALUE)
                    contents[iterator + 8] = createGuiItem(Material.BLACK_STAINED_GLASS_PANE, ChatColor.RESET.toString() + "Set to Maximum Value");
            }
            if(option instanceof BooleanOption) {
                // on click: disable
                if(((BooleanOption) option).getValue())
                    contents[iterator + 3] = createGuiItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RESET.toString() + ChatColor.RED.toString() + "Disable");

                assert optionMeta != null;
                ArrayList<String> lores = new ArrayList<>();
                lores.add(Boolean.toString(((BooleanOption) option).getValue()));
                optionMeta.setLore(lores);

                // on click: enable
                if(!((BooleanOption) option).getValue())
                    contents[iterator + 5] = createGuiItem(Material.LIME_STAINED_GLASS_PANE, ChatColor.RESET.toString() + ChatColor.GREEN.toString() + "Enable");

            }
            if(option instanceof ItemListOption) {
                assert optionMeta != null;
                ArrayList<String> lores = new ArrayList<>();
                lores.add("Click to configure item list!");
                optionMeta.setLore(lores);
            }
            optionIcon.setItemMeta(optionMeta);
            contents[iterator + 4] = optionIcon;

            iterator += 9;
            if(iterator == 9 * options.size())
                contents[iterator + 4] = createGuiItem(Material.IRON_INGOT, ChatColor.RESET.toString() + "Exit");
        }

        page.setContents(contents);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if(!event.getInventory().equals(page)) return;

         mode.update();
    }
}
