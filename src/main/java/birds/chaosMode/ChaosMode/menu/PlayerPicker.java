package birds.chaosMode.ChaosMode.menu;

import birds.chaosMode.ChaosMode.ChaosMode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class PlayerPicker extends InventoryPage {

    private InventoryPage parentPage;
    private PlayerChooseHandler handler;
    private Predicate<Player> selector;
    private int size;

    PlayerPicker(ChaosMode chaosMode, InventoryPage parentPage, PlayerChooseHandler handler, Predicate<Player> selector) {
        super(chaosMode);
        this.handler = handler;
        this.selector = selector;
        this.parentPage = parentPage;

        setUpSlots();
    }

    @Override
    public void runSlotAction(int slot, ItemStack item, Player player, ClickType click) {
        if(slot >= size - 1) return;

        // ignore double-clicks
        if(click.equals(ClickType.DOUBLE_CLICK)) return;

        // xp sound
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        switch (item.getType()) {
            case PLAYER_HEAD:
                if (!(item.getItemMeta() instanceof SkullMeta)) break;
                SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
                Player owningPlayer = (Player) skullMeta.getOwningPlayer();
                handler.handlePlayerChoose(owningPlayer);
                List<ItemStack> contentList = new ArrayList<>(Arrays.asList(page.getContents()));
                if (contentList.contains(item)) {
                    int index = contentList.indexOf(item);
                    contentList.set(index, new ItemStack(Material.AIR));
                }
                page.setContents(contentList.toArray(new ItemStack[0]));
                player.closeInventory();
                this.showInventory(player);
                break;
            case IRON_INGOT:
                player.closeInventory();
                parentPage.showInventory(player);
                break;
        }
    }

    @Override
    public void setUpSlots() {
        int row_width = 9;
        int players = Bukkit.getOnlinePlayers().size();
        size = players + (row_width - (players % row_width)) + row_width;

        page = Bukkit.createInventory(this, size, "Choose Player");
        ItemStack[] contents = new ItemStack[size];

        int iterator = 0;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!selector.test(player)) continue;
            ItemStack playerSkull = createGuiItem(Material.PLAYER_HEAD, ChatColor.RESET + player.getDisplayName(), ChatColor.GREEN + "Click to select.");
            SkullMeta skullMeta = (SkullMeta) playerSkull.getItemMeta();
            skullMeta.setOwningPlayer(player);
            playerSkull.setItemMeta(skullMeta);

            contents[iterator] = playerSkull;

            iterator++;
        }

        contents[size - 5] = createGuiItem(Material.IRON_INGOT, ChatColor.RESET + "Exit");
        page.setContents(contents);
    }
}
