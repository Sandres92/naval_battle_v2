package battle;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.logging.Logger;

public class JavaNavalBattle extends JFrame {
    private JPanel panel;

    private Game game;

    private static final Logger logger = Logger.getLogger("battle.JavaNavalBattle");

    private static final int ROWS = 10;
    private static final int COLS = 10;
    private final int IMAGE_SIZE = 30;

    Thread thread;
    boolean running = true;

    public static void main(String[] args) {
        new JavaNavalBattle();
    }

    private JavaNavalBattle() {
        Ranges.setImageSize(IMAGE_SIZE);

        int countPlayers = 2;
        game = new Game(ROWS, COLS, countPlayers);

        game.start();

        setImages();
        initPanel();
        initFrame();

        initGameUpdate();
    }

    private void initGameUpdate() {
        thread = new Thread(() -> {
            int fps = 30; //number of update per second.
            double tickPerSecond = 1000000000.f / fps;
            double delta = 0;
            long now;
            long lastTime = System.nanoTime();

            while (game.getGameState() == GameState.PLAYED) {
                now = System.nanoTime();
                delta += (now - lastTime) / tickPerSecond;
                lastTime = now;

                if (delta >= 1) {

                    tick();
                    render();
                    delta--;
                }
            }
        });

        thread.start();
    }

    private void tick() {
        game.update();
    }

    private void render() {
        panel.repaint();
    }

    private void initFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Морской бой");
        setResizable(false);
        setVisible(true);

        pack();

        setLocationRelativeTo(null);
        setIconImage(null);
    }

    private void initPanel() {
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                for (int i = 0; i < COLS; i++) {
                    for (int j = 0; j < ROWS; j++) {
                        g.drawImage(getImageByName(game.getCellType(0, new Coord(i, j)).toString()), i * IMAGE_SIZE, (j + 1) * IMAGE_SIZE, this);
                    }
                }

                int offsetX = (COLS * IMAGE_SIZE) + IMAGE_SIZE;
                for (int i = 0; i < COLS; i++) {
                    for (int j = 0; j < ROWS; j++) {
                        g.drawImage(getImageByName(game.getCellType(1, new Coord(i, j)).toString()), offsetX + (i * IMAGE_SIZE), (j + 1) * IMAGE_SIZE, this);
                    }
                }

                int textStartX = 0;
                int textStartY = 20;

                String playerTurnString = "Ваш ход";
                Color textColor = Color.GREEN;
                Color backColor = Color.BLUE;

                if (!game.isPlayerTurn()) {
                    playerTurnString = "Ход соперника";
                    textColor = Color.RED;
                    backColor = Color.BLACK;
                }

                FontMetrics fm = g.getFontMetrics();
                Rectangle2D rect = fm.getStringBounds(playerTurnString, g);

                g.setColor(backColor);
                g.fillRect(textStartX,
                        textStartY - fm.getAscent(),
                        (int) rect.getWidth(),
                        (int) rect.getHeight());

                g.setColor(textColor);
                g.drawString(playerTurnString, textStartX, textStartY);


                int offsettartPosX = 100;
                for (int i = 0; i < game.getRemainingTimeOnTurn(); i++) {
                    g.drawImage(getImageByName("time"), offsettartPosX + (i * (15 + 2)), 5, this);
                }
            }
        };

        panel.setPreferredSize(new Dimension((COLS * IMAGE_SIZE * 2) + IMAGE_SIZE, (ROWS * IMAGE_SIZE) + IMAGE_SIZE));

        add(panel);

        game.initPanel(panel);
    }

    private void setImages() {
        for (CellType cellType : CellType.values()) {
            cellType.image = getImageByName(cellType.name().toLowerCase());
        }
    }

    Image getImageByName(String nameImage) {
        String pathToImage = "/img/" + nameImage.toLowerCase() + ".png";

        ImageIcon imageIcon = new ImageIcon(getClass().getResource(pathToImage));

        return imageIcon.getImage();
    }
}
