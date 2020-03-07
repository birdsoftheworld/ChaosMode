package birds.chaosMode.ChaosMode.modes;

import birds.chaosMode.ChaosMode.modes.options.ConfigurableOption;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Mode {

    private ArrayList<ConfigurableOption> options = new ArrayList<>();
    private ItemStack icon;
    private boolean enabled;
    private String name;
    private String internalName;

    Mode(String name) {
        this.enabled = false;
        this.name = name;
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

    public void update() {}

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

    public void setInternalName(String name) {
        this.internalName = name;
    }

    public String getInternalName() {
        return internalName;
    }

    protected void setIcon(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);

        ArrayList<String> metaLore = new ArrayList<>(Arrays.asList(lore));

        meta.setLore(metaLore);
        item.setItemMeta(meta);

        this.icon = item;
    }

    public ItemStack getIcon() {
        return icon;
    }

    protected void addOption(ConfigurableOption option) {
        options.add(option);
    }

    protected void removeOption(ConfigurableOption option) {
        options.remove(option);
    }

    public ArrayList<ConfigurableOption> getOptions() {
        return options;
    }
}
