package model;

import java.util.ArrayList;
import java.util.List;

/**
 * The character controlled by the user: current room and bag.
 */
public class Player {
    public static final int MAX_WEIGHT = 5;

    private Room currentRoom;
    private final List<Item> inventory = new ArrayList<>();

    public Player(Room startingRoom) {
        this.currentRoom = startingRoom;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    public boolean hasItem(String name) {
        return findItem(name) != null;
    }

    public boolean canCarry(Item item) {
        return getCarriedWeight() + item.getWeight() <= MAX_WEIGHT;
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public int getCarriedWeight() {
        int weight = 0;
        for (Item item : inventory) {
            weight += item.getWeight();
        }
        return weight;
    }

    public List<String> getItemNames() {
        List<String> names = new ArrayList<>();
        for (Item item : inventory) {
            names.add(item.getName());
        }
        return names;
    }

    private Item findItem(String name) {
        for (Item item : inventory) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }
}
