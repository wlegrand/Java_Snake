import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static final int Screen_WIDTH = 600;
    static final int Screen_HEIGHT = 600;
    static final int Unit_Size = 25;
    static final int Game_unites = (Screen_WIDTH * Screen_HEIGHT) / Unit_Size;
    static final int Delay = 75;

    final int x[] = new int[Game_unites];
    final int y[] = new int[Game_unites];
    int bodyParts = 6;
    int appleEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;


    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(Screen_WIDTH, Screen_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();

    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(Delay, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            /*
            for (int i = 0; i < Screen_HEIGHT / Unit_Size; i++) {
                g.drawLine(i * Unit_Size, 0, i * Unit_Size, Screen_HEIGHT);
                g.drawLine(0, i * Unit_Size, Screen_WIDTH, i * Unit_Size);
            }
             */
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, Unit_Size, Unit_Size);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], Unit_Size, Unit_Size);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                    g.fillRect(x[i], y[i], Unit_Size, Unit_Size);
                }
            }
            g.setColor(Color.CYAN);
            g.setFont( new Font("Ink Free",Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: "+appleEaten, (Screen_WIDTH - metrics.stringWidth("Score: "+appleEaten))/2, g.getFont().getSize());
        }
        else {
            gameOver(g);
        }
    }

    public void newApple() {
        appleX = random.nextInt((int) (Screen_WIDTH / Unit_Size)) * Unit_Size;
        appleY = random.nextInt((int) (Screen_HEIGHT / Unit_Size)) * Unit_Size;
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U' -> y[0] = y[0] - Unit_Size;
            case 'D' -> y[0] = y[0] + Unit_Size;
            case 'L' -> x[0] = x[0] - Unit_Size;
            case 'R' -> x[0] = x[0] + Unit_Size;
        }
    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            appleEaten++;
            newApple();
        }
    }

    public void checkCollisions() {
        //checks if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        //check if head touches left border
        if (x[0] < 0) {
            running = false;
        }

        //check if head touches right border
        if (x[0] > Screen_WIDTH) {
            running = false;
        }
        //check if head touches top border
        if (y[0] < 0) {
            running = false;
        }
        //check if head touches bottom border
        if (y[0] > Screen_HEIGHT) {
            running = false;
        }
        if (!running) timer.stop();
    }

    public void gameOver(Graphics g) {
        //Game Over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (Screen_WIDTH - metrics1.stringWidth("Game Over"))/2, Screen_HEIGHT/2);

        g.setColor(Color.CYAN);
        g.setFont( new Font("Ink Free",Font.BOLD, 40));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: "+appleEaten, (Screen_WIDTH - metrics2.stringWidth("Score: "+appleEaten))/2, g.getFont().getSize());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT -> {
                    if (direction != 'R') {
                        direction = 'L';
                    }
                }
                case KeyEvent.VK_RIGHT -> {
                    if (direction != 'L') {
                        direction = 'R';
                    }
                }
                case KeyEvent.VK_UP -> {
                    if (direction != 'D') {
                        direction = 'U';
                    }
                }
                case KeyEvent.VK_DOWN -> {
                    if (direction != 'U') {
                        direction = 'D';
                    }
                }
            }
        }
    }
}
