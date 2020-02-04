package birds.chaosMode.ChaosMode.menu;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.Mode;
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

public class ItemListPage extends InventoryPage {
    private ItemListOption itemList;
    private Mode mode;
    private int size;
    private SettingsPage settingsPage;

    ItemListPage(ChaosMode chaosMode, ItemListOption itemList, Mode mode, SettingsPage settingsPage) {
        super(chaosMode);
        this.itemList = itemList;
        this.mode = mode;
        this.settingsPage = settingsPage;
        setUpSlots();
    }

    @Override
    public void runSlotAction(int slot, ItemStack item, Player player, ClickType click) {
        boolean isPlayerInventory = false;
        if(slot >= size - 1) isPlayerInventory = true;

        // exit button
        if(slot == size - 5) {
            // xp sound
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            mode.update();
            settingsPage.showInventory(player);
            return;
        }

        if(!isPlayerInventory) {
            itemList.removeItem(item.getType());

            // xp sound
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);

            setUpSlots();
            player.openInventory(page);
        } else {
            // make sure item is an item
            if(item.getType().equals(Material.AIR)) return;

            // make sure item is acceptable for the ItemList
            if(!itemList.itemIsAcceptable(item.getType())) {
                // "no" sound
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                return;
            }

            // xp sound
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);

            itemList.addItem(item.getType());

            setUpSlots();
            player.openInventory(page);
        }
    }

    @Override
    public void setUpSlots() {
        // 1 row per 9 items, plus the back button
        this.size = 9 * ((itemList.getItems().size() - 1) / 9 + 1) + 9;
        page = Bukkit.createInventory(this, size, "Options");

        ItemStack[] items = new ItemStack[size];
        int iterator = 0;
        for(Material item : itemList.getItems()) {
            items[iterator] = new ItemStack(item);
            iterator++;
        }
        items[size - 5] = createGuiItem(Material.IRON_INGOT, ChatColor.RESET.toString() + "Back");

        page.setContents(items);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if(!event.getInventory().equals(page)) return;

        mode.update();
    }
}
