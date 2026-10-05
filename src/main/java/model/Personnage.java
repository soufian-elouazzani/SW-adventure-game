package model;

/**
 * A character met in a room.
 * A friendly character can give one item. A hostile one blocks a guarded exit
 * until the player uses the right item.
 */
public class Personnage {
    private final String name;
    private final String description;
    private final boolean hostile;
    private final Item gift;
    private boolean giftGiven;
    private boolean pacified;

    public Personnage(String name, String description, boolean hostile, Item gift) {
        this.name = name;
        this.description = description;
        this.hostile = hostile;
        this.gift = gift;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        if (hostile && pacified) {
            return "Le garde s'est écarté et vous laisse passer.";
        }
        if (!hostile && giftGiven) {
            return "L'ermite n'a plus rien à vous offrir.";
        }
        return description;
    }

    public boolean isHostile() {
        return hostile;
    }

    public boolean isBlocking() {
        return hostile && !pacified;
    }

    public void pacify() {
        pacified = true;
    }

    /**
     * @return the gift the first time it is offered, otherwise null
     */
    public Item offerGift() {
        if (gift == null || giftGiven) {
            return null;
        }
        return gift;
    }

    public void markGiftGiven() {
        giftGiven = true;
    }
}
