package adventure;

import controller.GameController;
import model.Game;
import view.GameFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Game game = new Game();
            GameFrame frame = new GameFrame();
            GameController controller = new GameController(game, frame);
            frame.setController(controller);
            controller.start();
        });
    }
}
