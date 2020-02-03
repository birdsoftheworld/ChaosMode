package birds.chaosMode.ChaosMode.modes.options;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class ItemListOption extends ConfigurableOption {
    ItemListOption(String name) {
        this.name = name;
    }

    List<Material> items = new ArrayList<>();

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

    public boolean itemIsAcceptable(Material item) {
        // return true unless duplicate
        return !items.contains(item);
    }
}
