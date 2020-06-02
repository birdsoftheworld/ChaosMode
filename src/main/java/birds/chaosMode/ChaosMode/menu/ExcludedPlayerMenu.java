package birds.chaosMode.ChaosMode.menu;

import birds.chaosMode.ChaosMode.ChaosMode;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class ExcludedPlayerMenu extends InventoryPage {

    private int size;

    ExcludedPlayerMenu(ChaosMode chaosMode) {
        super(chaosMode);
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
            case COMPASS:
                PlayerPicker playerPicker = new PlayerPicker(chaosMode, this, new PlayerChooseHandler() {
                    @Override
                    public void handlePlayerChoose(Player player) {
                        ChaosMode.excludePlayer(player);
                    }
                }, new Predicate<Player>() {
                    @Override
                    public boolean test(Player player) {
                        return !ChaosMode.playerIsExcluded(player);
                    }
                });

                playerPicker.showInventory(player);
                break;
            case PLAYER_HEAD:
                if (!(item.getItemMeta() instanceof SkullMeta)) break;
                SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
                Player owningPlayer = (Player) skullMeta.getOwningPlayer();
                ChaosMode.includePlayer(owningPlayer);
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
                OptionsHub optionsHub = new OptionsHub(chaosMode);
                optionsHub.showInventory(player);
                break;
        }
    }

    @Override
    public void setUpSlots() {

        int row_width = 9;

        List<String> excludedPlayers = ChaosMode.getExcludedPlayers();

        size = excludedPlayers.size() + (row_width - (excludedPlayers.size() % row_width)) + row_width;
        page = Bukkit.createInventory(this, size, "Excluded Players");

        ItemStack[] contents = new ItemStack[size];

        for (int i = 0; i < excludedPlayers.size(); i++) {
            String playerId = excludedPlayers.get(i);
            Player player = Bukkit.getPlayer(UUID.fromString(playerId));
            if (player == null) continue;

            ItemStack playerSkull = createGuiItem(Material.PLAYER_HEAD, ChatColor.RESET + player.getDisplayName(), ChatColor.RED + "Click to include again.");
            SkullMeta skullMeta = (SkullMeta) playerSkull.getItemMeta();
            skullMeta.setOwningPlayer(player);
            playerSkull.setItemMeta(skullMeta);

            contents[i] = playerSkull;
        }

        contents[size - 5] = createGuiItem(Material.IRON_INGOT, ChatColor.RESET + "Exit");
        contents[size - 4] = createGuiItem(Material.COMPASS, ChatColor.RESET + "Add Player", ChatColor.GREEN + "Click to exclude a player.");
        page.setContents(contents);
    }
}
