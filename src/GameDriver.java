import javax.swing.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.*;
import java.awt.MouseInfo;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.File;
import java.io.IOException;


@SuppressWarnings("serial")
public class GameDriver extends JPanel implements MouseListener {

	public static final int TILE_WIDTH = 80;
	public static final int X_LENGTH = 8 * TILE_WIDTH + 15;
	public static final int Y_LENGTH = 8 * TILE_WIDTH;

	private static Image[] pieces = new Image[13];

	public static GameDriver board;

	private static String gameType = "";

	private static boolean isPlayerTurn = true;

	private static BotLogic bot;

	public static void main(String[] args) {
		init_board("TwoPlayer");
	}

	public static void init_board(String playerOrComputer) {
		gameType = playerOrComputer;
		if (gameType == "computer") {
			bot = new BotLogic("computer");
		}
		board = new GameDriver();
		JFrame frame = new JFrame();
		frame.add(board);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationByPlatform(true);
		frame.setSize(X_LENGTH, Y_LENGTH + 37); // 37 to account for bottom border width
		frame.setVisible(true);
		frame.setResizable(false);

		if (gameType == "TwoPlayer") {

			new Thread(() -> {
				while (true) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					board.repaint(); // triggers paintComponent
				}
			}).start();
		}

		if (gameType == "Computer") {
			new Thread(() -> {
				while (true) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					board.repaint(); // triggers paintComponent
				}
			}).start();
		}
	}


	public GameDriver() {
		addMouseListener(this);
		File dir = new File("C:\\Users\\erik2\\eclipse-workspace\\ChessGame\\images");
		File[] directoryListing = dir.listFiles();
		for (int i = 1; i < 13; ++i) {
			try {
				pieces[i] = ImageIO.read(directoryListing[i - 1]);
			} catch (IOException e) {
				e.printStackTrace();
			}
			pieces[i] = pieces[i].getScaledInstance(TILE_WIDTH, TILE_WIDTH, Image.SCALE_SMOOTH);
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// draws the game board empty
		for (int i = 0; i < 8; ++i) {
			for (int j = 0; j < 8; ++j) {
				if ((i + j) % 2 == 0) {
					g.setColor(new Color(240, 236, 212));
					g.fillRect(j * TILE_WIDTH, i * TILE_WIDTH, TILE_WIDTH, TILE_WIDTH);
				} else {
					g.setColor(new Color(120, 148, 84));
					g.fillRect(j * TILE_WIDTH, i * TILE_WIDTH, TILE_WIDTH, TILE_WIDTH);
				}
			}
		}
		// draws each piece in its square based on gameState array.
		for (int i = 0; i < 8; ++i) {
			for (int j = 0; j < 8; ++j) {
				if (Board.gameState[i][j] > 0) {
					if (!(Board.isPieceInHand && i == Board.previousLocation[0] && j == Board.previousLocation[1])) {
						g.drawImage(pieces[Board.gameState[i][j]], j * TILE_WIDTH, i * TILE_WIDTH, this);
					}
				}
			}
		}

		// draws piece in hand
		if (Board.isPieceInHand == true) {
			g.drawImage(pieces[Board.piecePickedUp], mouseLocation()[1] - (TILE_WIDTH / 2),
					mouseLocation()[0] - (TILE_WIDTH / 2), this);

		}
	}

	// gets mouse location on call
	public static int[] mouseLocation() {
		int[] coords = new int[2];
		int x_pos = (int) (MouseInfo.getPointerInfo().getLocation().getX() - board.getLocationOnScreen().getX());
		int y_pos = (int) (MouseInfo.getPointerInfo().getLocation().getY() - board.getLocationOnScreen().getY());
		coords[0] = y_pos;
		coords[1] = x_pos;
		return coords;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// intentionally empty method required for the interface
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (gameType == "TwoPlayer") {
			if (Board.isPieceInHand) {
				Board.placePiece();
			} else {
				Board.pickPieceUp();
			}
		}

		else if (gameType == "Computer") {
			if (isPlayerTurn) {
				if (Board.isPieceInHand) {
					Board.placePiece();
					isPlayerTurn = false;
					bot.makeMove();
				} else {
					Board.pickPieceUp();
				}
			}
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// intentionally empty method required for the interface
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// intentionally empty method required for the interface
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// intentionally empty method required for the interface
	}
}
