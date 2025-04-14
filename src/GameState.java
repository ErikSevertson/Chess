import javax.swing.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class GameState extends JPanel {

    private static GameState menu;

    public static void main(String[] args) {
        init_menu();
        while (true) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            menu.repaint();
        }
    }

    public static void init_menu() {
        menu = new GameState();
        JFrame frame = new JFrame();
        frame.add(menu);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.setSize(GameDriver.X_LENGTH, GameDriver.Y_LENGTH + 37); // 37 to account for bottom border width
        frame.setVisible(true);
        frame.setResizable(false);

        menu.setLayout(null); // so that we manually specify button placement

        JButton twoPlayerButton = new JButton("Player vs. Player");
        twoPlayerButton.setPreferredSize(new java.awt.Dimension(400, 120));
        twoPlayerButton.setBounds(100, 50, 400, 120);
        twoPlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { // handles button clicks on two player button
                frame.dispose();
                GameDriver.init_board("TwoPlayer");
            }
        });

        JButton computerButton = new JButton("Player vs. Computer");
        computerButton.setPreferredSize(new java.awt.Dimension(400, 120));
        computerButton.setBounds(100, 220, 400, 120);
        computerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { // handles button clicks on two player button
                frame.dispose();
                GameDriver.init_board("Computer");
            }
        });

        menu.add(twoPlayerButton);
        menu.add(computerButton);

    }

    public GameState() {

    }

}
