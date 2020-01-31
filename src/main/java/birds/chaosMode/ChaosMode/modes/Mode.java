package birds.chaosMode.ChaosMode.modes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Mode {

    private ItemStack icon;
    private boolean enabled;
    private String name;

    public Mode() {
        this.enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        if (this.name != null) {
            return this.name;
        } else {
            return "This mode doesn't have a name!";
        }
    }

    public void setIcon(Material material, String name, String...lore) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);

        ArrayList<String> metaLore = new ArrayList<String>(Arrays.asList(lore));

        meta.setLore(metaLore);
        item.setItemMeta(meta);

        this.icon = item;
    }

    public ItemStack getIcon() {
        return icon;
    }
}
