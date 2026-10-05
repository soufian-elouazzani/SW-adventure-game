package model;

/**
 * An object the player can find in a room or carry in the bag.
 */
public class Item {
    private final String name;
    private final String description;
    private final boolean portable;
    private final int weight;

    public Item(String name, String description, boolean portable, int weight) {
        this.name = name;
        this.description = description;
        this.portable = portable;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPortable() {
        return portable;
    }

    public int getWeight() {
        return weight;
    }
}
