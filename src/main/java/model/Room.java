package model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * One place in the game world: a description, an image, exits, items and
 * at most one character.
 */
public class Room {
    private final String name;
    private final String description;
    private final String imagePath;
    private final boolean goal;
    private final Map<String, Exit> exits = new LinkedHashMap<>();
    private final List<Item> items = new ArrayList<>();
    private Personnage personnage;

    public Room(String name, String description, String imagePath, boolean goal) {
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
        this.goal = goal;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public boolean isGoal() {
        return goal;
    }

    public void addExit(String direction, Exit exit) {
        exits.put(direction, exit);
    }

    public Exit getExit(String direction) {
        return exits.get(direction);
    }

    public List<String> getExitNames() {
        return List.copyOf(exits.keySet());
    }

    public List<Exit> getExits() {
        return List.copyOf(exits.values());
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public Item removeItem(String name) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equals(name)) {
                return items.remove(i);
            }
        }
        return null;
    }

    public List<String> getItemNames() {
        List<String> names = new ArrayList<>();
        for (Item item : items) {
            names.add(item.getName());
        }
        return names;
    }

    public void setPersonnage(Personnage personnage) {
        this.personnage = personnage;
    }

    public Personnage getPersonnage() {
        return personnage;
    }
}
