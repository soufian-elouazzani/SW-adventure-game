package controller;

import model.Game;
import view.GameFrame;

/**
 * Receives a user action, asks the model to apply it, then refreshes the view.
 */
public class GameController {
    private final Game game;
    private final GameFrame view;

    public GameController(Game game, GameFrame view) {
        this.game = game;
        this.view = view;
    }

    public void start() {
        view.show(game.look());
    }

    public void onGo(String direction) {
        game.go(direction);
        view.show(game.look());
    }

    public void onTake(String itemName) {
        game.take(itemName);
        view.show(game.look());
    }

    public void onUse(String itemName) {
        game.use(itemName);
        view.show(game.look());
    }

    public void onTalk() {
        game.talk();
        view.show(game.look());
    }

    public void onLook() {
        game.lookAround();
        view.show(game.look());
    }

    public void onInventory() {
        game.showInventory();
        view.show(game.look());
    }

    public void onQuit() {
        view.dispose();
    }
}
