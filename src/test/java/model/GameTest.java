package model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameTest {

    @Test
    void gameStartsAtTheEntrance() {
        RoomView view = new Game().look();

        assertEquals("Entrée", view.getRoomName());
        assertEquals(List.of("nord"), view.getExits());
        assertEquals("images/entree.png", view.getImagePath());
        assertFalse(view.isFinished());
    }

    @Test
    void directionChangesTheRoom() {
        Game game = new Game();

        game.go("nord");

        RoomView view = game.look();
        assertEquals("Grand hall", view.getRoomName());
        assertEquals("images/hall.png", view.getImagePath());
        assertTrue(view.getExits().contains("est"));
        assertTrue(view.getItems().contains("clé rouillée"));
    }

    @Test
    void unknownDirectionDoesNotMove() {
        Game game = new Game();

        game.go("ouest");

        assertEquals("Entrée", game.look().getRoomName());
        assertTrue(game.look().getMessage().contains("pas de passage"));
    }

    @Test
    void playerCanTakeTheKeyAndSeeItInTheBag() {
        Game game = reachHall();

        game.take("clé rouillée");

        RoomView view = game.look();
        assertEquals(List.of("clé rouillée"), view.getInventory());
        assertTrue(view.getItems().isEmpty());
        assertEquals(1, view.getCarriedWeight());
    }

    @Test
    void hermitGivesTheSwordOnce() {
        Game game = reachHall();
        game.go("ouest");

        game.talk();
        game.talk();

        assertEquals(List.of("épée"), game.look().getInventory());
        assertTrue(game.look().getMessage().contains("plus rien"));
    }

    @Test
    void lockedDoorStaysShutUntilTheKeyIsUsed() {
        Game game = reachCorridorWithSword();

        game.go("est");
        assertEquals("Couloir", game.look().getRoomName());
        assertTrue(game.look().getMessage().contains("épée"));

        game.use("épée");
        game.go("est");
        assertEquals("Couloir", game.look().getRoomName());
        assertTrue(game.look().getMessage().contains("verrouillée"));

        game.use("clé rouillée");
        game.go("est");
        assertTrue(game.isWon());
        assertEquals("Salle du trésor", game.look().getRoomName());
    }

    @Test
    void passingTheGuardWithoutASwordLoses() {
        Game game = reachHall();
        game.go("est");

        game.go("est");

        assertTrue(game.isFinished());
        assertFalse(game.isWon());
        assertEquals("Couloir", game.look().getRoomName());
    }

    @Test
    void fullPathWins() {
        Game game = reachHall();
        game.take("clé rouillée");
        game.go("ouest");
        game.talk();
        game.go("est");
        game.go("est");
        game.use("épée");
        game.use("clé rouillée");

        game.go("est");

        RoomView view = game.look();
        assertTrue(view.isFinished());
        assertTrue(view.isWon());
        assertTrue(view.getMessage().contains("gagné"));
        assertTrue(view.getItems().contains("trésor"));
    }

    @Test
    void talkingToNobodyAndUsingTheWrongItemFail() {
        Game game = new Game();

        game.talk();
        assertTrue(game.look().getMessage().contains("personne"));

        game.go("nord");
        game.take("clé rouillée");
        game.use("clé rouillée");
        assertTrue(game.look().getMessage().contains("pas utiliser"));
    }

    @Test
    void inventoryMessageListsTheBag() {
        Game game = reachHall();
        game.take("clé rouillée");

        game.showInventory();

        assertTrue(game.look().getMessage().contains("clé rouillée"));
    }

    private Game reachHall() {
        Game game = new Game();
        game.go("nord");
        return game;
    }

    private Game reachCorridorWithSword() {
        Game game = reachHall();
        game.take("clé rouillée");
        game.go("ouest");
        game.talk();
        game.go("est");
        game.go("est");
        return game;
    }
}
