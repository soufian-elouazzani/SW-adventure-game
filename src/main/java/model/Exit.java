package model;

/**
 * A passage from one room to another.
 * A passage can be locked (it opens with one named item)
 * and guarded (a hostile character blocks it until pacified).
 */
public class Exit {
    private final Room destination;
    private final String requiredItem;
    private final boolean guarded;
    private boolean locked;

    public Exit(Room destination) {
        this(destination, null, false);
    }

    public Exit(Room destination, String requiredItem, boolean guarded) {
        this.destination = destination;
        this.requiredItem = requiredItem;
        this.guarded = guarded;
        this.locked = requiredItem != null;
    }

    public Room getDestination() {
        return destination;
    }

    public String getRequiredItem() {
        return requiredItem;
    }

    public boolean isGuarded() {
        return guarded;
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean needsItem(String itemName) {
        return requiredItem != null && requiredItem.equals(itemName);
    }

    /**
     * @return true when this exit was locked and the given item opens it
     */
    public boolean unlockWith(String itemName) {
        if (locked && needsItem(itemName)) {
            locked = false;
            return true;
        }
        return false;
    }
}
