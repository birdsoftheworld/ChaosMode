package birds.chaosMode.ChaosMode.modes.chaosModes;

import birds.chaosMode.ChaosMode.ChaosMode;
import birds.chaosMode.ChaosMode.modes.ListenerMode;
import birds.chaosMode.ChaosMode.modes.options.IntegerOption;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class InventoryKerfuffle extends ListenerMode {

    private IntegerOption chance = new IntegerOption(10, 1, 100, "shuffle-chance");
    private Random random = new Random();

    public InventoryKerfuffle(ChaosMode chaosMode) {
        super(chaosMode, "Inventory Shuffle");
        setInternalName("inventory-shuffle");

        chance.setIcon(Material.GLASS, ChatColor.RESET.toString() + "Chance");
        addOption(chance);

        setIcon(Material.GUNPOWDER, ChatColor.RESET.toString() + getName(), "Click to change settings!");
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if(!isEnabled())
            return;

        Entity entity = event.getEntity();

        // only include InventoryHolder entities
        // note that monsters with an inventory have inventories implemented differently, so this mostly affects players
        if(!(entity instanceof InventoryHolder))
            return;

        // return if chance check fails
        if(random.nextInt(101) > chance.getValue())
            return;

        // get InventoryHolder
        InventoryHolder holder = (InventoryHolder) entity;

        // get Inventory
        Inventory holderInventory = holder.getInventory();

        ItemStack[] contents = holderInventory.getContents();

        if(contents == null)
            return;

        // convert inventory to list
        List<ItemStack> contentsList = Arrays.asList(contents);

        // shuffle it
        Collections.shuffle(contentsList);

        // set it to new
        holderInventory.setContents((ItemStack[]) contentsList.toArray());
    }
}
