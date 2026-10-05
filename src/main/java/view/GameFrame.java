package view;

import model.RoomView;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * The window: room image, description of the place and of the possible
 * actions, then the buttons. It contains no game rules.
 */
public class GameFrame extends JFrame {
    private final JLabel imageLabel = new JLabel("", JLabel.CENTER);
    private final JTextArea descriptionArea = new JTextArea();
    private final JLabel statusLabel = new JLabel(" ");
    private final JLabel inventoryLabel = new JLabel(" ");
    private final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private controller.GameController controller;

    public GameFrame() {
        super("Jeu d'aventures");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(700, 680));

        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setFont(new Font(Font.SERIF, Font.PLAIN, 16));
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        statusLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(4, 8, 0, 8));
        inventoryLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        inventoryLabel.setBorder(BorderFactory.createEmptyBorder(0, 8, 4, 8));

        imageLabel.setPreferredSize(new Dimension(640, 360));
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        inventoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel south = new JPanel();
        south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));
        south.add(statusLabel);
        south.add(Box.createVerticalStrut(4));
        south.add(inventoryLabel);
        south.add(buttonPanel);

        add(imageLabel, BorderLayout.NORTH);
        add(new JScrollPane(descriptionArea), BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);
    }

    public void setController(controller.GameController controller) {
        this.controller = controller;
    }

    public void show(RoomView room) {
        ImageIcon icon = loadImage(room.getImagePath());
        if (icon == null) {
            imageLabel.setIcon(null);
            imageLabel.setText(room.getRoomName());
        } else {
            imageLabel.setText(null);
            imageLabel.setIcon(icon);
        }
        descriptionArea.setText(formatRoom(room));
        descriptionArea.setCaretPosition(0);
        statusLabel.setText(room.getMessage());
        inventoryLabel.setText(formatInventory(room));
        rebuildButtons(room);
        if (!isVisible()) {
            pack();
            setLocationRelativeTo(null);
            setVisible(true);
        } else {
            revalidate();
            repaint();
        }
    }

    private String formatRoom(RoomView room) {
        StringBuilder text = new StringBuilder();
        text.append(room.getRoomName()).append("\n\n");
        text.append(room.getDescription()).append("\n");
        if (room.getCharacterName() != null) {
            text.append("\nPrésent : ").append(room.getCharacterName());
            text.append(" — ").append(room.getCharacterDescription());
        }
        if (!room.getItems().isEmpty()) {
            text.append("\nAu sol : ").append(String.join(", ", room.getItems()));
        }
        if (!room.isFinished()) {
            text.append("\n\nVous pouvez :");
            for (String exit : room.getExits()) {
                text.append("\n- aller vers ").append(exit);
            }
            for (String item : room.getItems()) {
                text.append("\n- prendre ").append(item);
            }
            for (String item : room.getInventory()) {
                text.append("\n- utiliser ").append(item);
            }
            if (room.getCharacterName() != null) {
                text.append("\n- parler");
            }
            text.append("\n- observer");
            text.append("\n- consulter l'inventaire");
            text.append("\n- quitter");
        }
        return text.toString();
    }

    private String formatInventory(RoomView room) {
        List<String> inventory = room.getInventory();
        String contents = inventory.isEmpty() ? "vide" : String.join(", ", inventory);
        return "Sac (" + room.getCarriedWeight() + "/" + room.getMaxWeight() + ") : " + contents;
    }

    private void rebuildButtons(RoomView room) {
        buttonPanel.removeAll();
        if (!room.isFinished()) {
            for (String exit : room.getExits()) {
                buttonPanel.add(action("Aller " + exit, () -> controller.onGo(exit)));
            }
            for (String item : room.getItems()) {
                buttonPanel.add(action("Prendre " + item, () -> controller.onTake(item)));
            }
            for (String item : room.getInventory()) {
                buttonPanel.add(action("Utiliser " + item, () -> controller.onUse(item)));
            }
            if (room.getCharacterName() != null) {
                buttonPanel.add(action("Parler", controller::onTalk));
            }
            buttonPanel.add(action("Observer", controller::onLook));
            buttonPanel.add(action("Inventaire", controller::onInventory));
        }
        buttonPanel.add(action("Quitter", controller::onQuit));
        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    private JButton action(String label, Runnable command) {
        JButton button = new JButton(label);
        button.addActionListener(event -> command.run());
        return button;
    }

    private ImageIcon loadImage(String path) {
        try (InputStream in = GameFrame.class.getClassLoader().getResourceAsStream(path)) {
            if (in == null) {
                return null;
            }
            BufferedImage image = ImageIO.read(in);
            if (image == null) {
                return null;
            }
            Image scaled = image.getScaledInstance(640, 360, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (IOException ex) {
            return null;
        }
    }
}
