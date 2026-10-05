package model;

import java.util.List;

/**
 * A snapshot of what the view is allowed to show.
 * The window reads this object and does not touch the rooms or the player.
 */
public class RoomView {
    private final String roomName;
    private final String description;
    private final String imagePath;
    private final List<String> exits;
    private final List<String> items;
    private final String characterName;
    private final String characterDescription;
    private final String message;
    private final List<String> inventory;
    private final int carriedWeight;
    private final int maxWeight;
    private final boolean finished;
    private final boolean won;

    public RoomView(String roomName, String description, String imagePath,
                    List<String> exits, List<String> items,
                    String characterName, String characterDescription,
                    String message, List<String> inventory,
                    int carriedWeight, int maxWeight,
                    boolean finished, boolean won) {
        this.roomName = roomName;
        this.description = description;
        this.imagePath = imagePath;
        this.exits = List.copyOf(exits);
        this.items = List.copyOf(items);
        this.characterName = characterName;
        this.characterDescription = characterDescription;
        this.message = message;
        this.inventory = List.copyOf(inventory);
        this.carriedWeight = carriedWeight;
        this.maxWeight = maxWeight;
        this.finished = finished;
        this.won = won;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public List<String> getExits() {
        return exits;
    }

    public List<String> getItems() {
        return items;
    }

    public String getCharacterName() {
        return characterName;
    }

    public String getCharacterDescription() {
        return characterDescription;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getInventory() {
        return inventory;
    }

    public int getCarriedWeight() {
        return carriedWeight;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isWon() {
        return won;
    }
}
