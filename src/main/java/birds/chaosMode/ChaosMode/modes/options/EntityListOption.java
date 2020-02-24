package birds.chaosMode.ChaosMode.modes.options;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EntityListOption extends ItemListOption {

    private HashMap<Material, EntityType> spawnItems = new HashMap<>();

    public EntityListOption(String name) {
        super(name);

        // find all spawn eggs
        for(EntityType entityType : EntityType.values()) {
            Material material = Material.getMaterial(entityType.toString() + "_SPAWN_EGG");
            if (material != null) {
                spawnItems.put(material, entityType);
            }
        }

        // other
        spawnItems.put(Material.TNT, EntityType.PRIMED_TNT);
        spawnItems.put(Material.END_CRYSTAL, EntityType.ENDER_CRYSTAL);
    }

    public List<EntityType> getEntities() {
        // get blocks plus items which can be re-directed to blocks

        List<EntityType> entities = new ArrayList<>();
        for(Material item : getItems()) {
            entities.add(spawnItems.get(item));
        }

        return entities;
    }

    @Override
    public boolean itemIsAcceptable(Material item) {
        // only accept non-duplicated entity-spawning items
        boolean isEntity = spawnItems.containsKey(item);
        boolean isUnique = !items.contains(item);
        boolean fitsInMax = items.size() + 1 <= maxItems;
        return isEntity && isUnique && fitsInMax;
    }
}
