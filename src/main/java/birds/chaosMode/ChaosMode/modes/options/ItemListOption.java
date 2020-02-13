package birds.chaosMode.ChaosMode.modes.options;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class ItemListOption extends ConfigurableOption {
    ItemListOption(String name) {
        this.name = name;
    }

    List<Material> items = new ArrayList<>();
    private List<Material> defaultItems = new ArrayList<>();
    int maxItems = 54;

    public List<Material> getItems() {
        return items;
    }

    public void setItems(List<Material> items) {
        this.items = items;
    }

    public void addItem(Material item) {
        this.items.add(item);
    }

    public void removeItem(Material item) {
        this.items.remove(item);
    }

    public void setMaxItems(int maxItems) {
        this.maxItems = maxItems;
    }

    public void setDefaultItems(List<Material> defaultItems) {
        this.defaultItems = defaultItems;
    }

    public List<Material> getDefaultItems() {
        return defaultItems;
    }

    public boolean itemIsAcceptable(Material item) {
        // return true unless duplicate
        return !items.contains(item) && items.size() + 1 <= maxItems;
    }
}
