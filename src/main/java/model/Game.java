package model;

/**
 * Rules of the adventure. This class creates the map and applies every
 * player action. It does not print and it does not know about the window.
 */
public class Game {
    private final Player player;
    private String lastMessage;
    private boolean finished;
    private boolean won;

    public Game() {
        player = new Player(createWorld());
        lastMessage = "Bienvenue. Trouvez le trésor du château.";
    }

    public RoomView look() {
        Room room = player.getCurrentRoom();
        Personnage personnage = room.getPersonnage();
        String characterName = null;
        String characterDescription = null;
        if (personnage != null) {
            characterName = personnage.getName();
            characterDescription = personnage.getDescription();
        }
        return new RoomView(
                room.getName(),
                room.getDescription(),
                room.getImagePath(),
                room.getExitNames(),
                room.getItemNames(),
                characterName,
                characterDescription,
                lastMessage,
                player.getItemNames(),
                player.getCarriedWeight(),
                Player.MAX_WEIGHT,
                finished,
                won
        );
    }

    public void go(String direction) {
        if (finished) {
            return;
        }
        Exit exit = player.getCurrentRoom().getExit(direction);
        if (exit == null) {
            lastMessage = "Il n'y a pas de passage vers " + direction + ".";
            return;
        }
        Personnage personnage = player.getCurrentRoom().getPersonnage();
        if (exit.isGuarded() && personnage != null && personnage.isBlocking()) {
            if (!player.hasItem("épée")) {
                finished = true;
                won = false;
                lastMessage = "Le garde vous frappe. Sans épée, vous avez perdu.";
                return;
            }
            lastMessage = "Le garde vous barre la route. Utilisez l'épée.";
            return;
        }
        if (exit.isLocked()) {
            lastMessage = "La porte est verrouillée. Il faut une " + exit.getRequiredItem() + ".";
            return;
        }
        player.setCurrentRoom(exit.getDestination());
        if (player.getCurrentRoom().isGoal()) {
            finished = true;
            won = true;
            lastMessage = "Vous avez trouvé le trésor. Vous avez gagné.";
            return;
        }
        lastMessage = "Vous allez vers " + direction + ".";
    }

    public void take(String itemName) {
        if (finished) {
            return;
        }
        Room room = player.getCurrentRoom();
        Item item = room.removeItem(itemName);
        if (item == null) {
            lastMessage = "Il n'y a pas de " + itemName + " ici.";
            return;
        }
        if (!item.isPortable()) {
            room.addItem(item);
            lastMessage = "Vous ne pouvez pas emporter " + item.getName() + ".";
            return;
        }
        if (!player.canCarry(item)) {
            room.addItem(item);
            lastMessage = "Votre sac est trop lourd pour " + item.getName() + ".";
            return;
        }
        player.addItem(item);
        lastMessage = "Vous ramassez " + item.getName() + ".";
    }

    public void use(String itemName) {
        if (finished) {
            return;
        }
        if (!player.hasItem(itemName)) {
            lastMessage = "Vous n'avez pas " + itemName + ".";
            return;
        }
        Room room = player.getCurrentRoom();
        Personnage personnage = room.getPersonnage();
        if ("épée".equals(itemName) && personnage != null && personnage.isHostile()) {
            if (personnage.isBlocking()) {
                personnage.pacify();
                lastMessage = "Vous brandissez l'épée. Le garde recule.";
            } else {
                lastMessage = "Le garde s'est déjà écarté.";
            }
            return;
        }
        boolean matchedDoor = false;
        for (Exit exit : room.getExits()) {
            if (!exit.needsItem(itemName)) {
                continue;
            }
            matchedDoor = true;
            if (exit.unlockWith(itemName)) {
                lastMessage = "Vous utilisez " + itemName + ". La porte s'ouvre.";
                return;
            }
        }
        if (matchedDoor) {
            lastMessage = "Cette porte est déjà ouverte.";
            return;
        }
        lastMessage = "Vous ne pouvez pas utiliser " + itemName + " ici.";
    }

    public void talk() {
        if (finished) {
            return;
        }
        Personnage personnage = player.getCurrentRoom().getPersonnage();
        if (personnage == null) {
            lastMessage = "Il n'y a personne à qui parler.";
            return;
        }
        if (personnage.isHostile()) {
            lastMessage = personnage.getName() + " refuse de parler.";
            return;
        }
        Item gift = personnage.offerGift();
        if (gift == null) {
            lastMessage = personnage.getName() + " n'a plus rien à vous donner.";
            return;
        }
        if (!player.canCarry(gift)) {
            lastMessage = "Votre sac est trop lourd pour accepter " + gift.getName() + ".";
            return;
        }
        personnage.markGiftGiven();
        player.addItem(gift);
        lastMessage = personnage.getName() + " vous donne " + gift.getName() + ".";
    }

    public void lookAround() {
        if (finished) {
            return;
        }
        Room room = player.getCurrentRoom();
        StringBuilder text = new StringBuilder("Vous observez les lieux. ");
        text.append(room.getDescription());
        if (room.getItemNames().isEmpty()) {
            text.append(" Il n'y a rien à ramasser.");
        } else {
            text.append(" Au sol : ").append(String.join(", ", room.getItemNames())).append(".");
        }
        lastMessage = text.toString();
    }

    public void showInventory() {
        if (finished) {
            return;
        }
        if (player.getItemNames().isEmpty()) {
            lastMessage = "Votre sac est vide.";
            return;
        }
        lastMessage = "Votre sac contient : " + String.join(", ", player.getItemNames()) + ".";
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isWon() {
        return won;
    }

    private Room createWorld() {
        Room entrance = new Room(
                "Entrée",
                "Vous êtes devant l'entrée du château en ruine. Une porte ouverte mène au nord.",
                "images/entree.png",
                false);
        Room hall = new Room(
                "Grand hall",
                "Vous êtes dans un grand hall poussiéreux. Des passages partent au sud, à l'ouest et à l'est.",
                "images/hall.png",
                false);
        Room hermitage = new Room(
                "Ermitage",
                "Une petite pièce éclairée par une bougie. Un vieil ermite est assis dans un coin.",
                "images/ermitage.png",
                false);
        Room corridor = new Room(
                "Couloir",
                "Un couloir sombre. La porte de l'est mène vers le trésor, mais elle est gardée.",
                "images/couloir.png",
                false);
        Room treasury = new Room(
                "Salle du trésor",
                "La salle du trésor. Un coffre déborde d'or.",
                "images/tresor.png",
                true);

        entrance.addExit("nord", new Exit(hall));
        hall.addExit("sud", new Exit(entrance));
        hall.addExit("ouest", new Exit(hermitage));
        hermitage.addExit("est", new Exit(hall));
        hall.addExit("est", new Exit(corridor));
        corridor.addExit("ouest", new Exit(hall));
        corridor.addExit("est", new Exit(treasury, "clé rouillée", true));
        treasury.addExit("ouest", new Exit(corridor));

        hall.addItem(new Item(
                "clé rouillée",
                "Une clé rouillée, assez légère pour le sac.",
                true,
                1));
        treasury.addItem(new Item(
                "trésor",
                "Un coffre rempli d'or, trop lourd pour être emporté.",
                false,
                10));

        hermitage.setPersonnage(new Personnage(
                "Ermite",
                "Un vieil homme qui semble vous attendre.",
                false,
                new Item("épée", "Une épée courte.", true, 2)));
        corridor.setPersonnage(new Personnage(
                "Garde",
                "Un garde hostile bloque la porte de l'est.",
                true,
                null));

        return entrance;
    }
}
