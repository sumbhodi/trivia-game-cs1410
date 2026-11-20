package triviaGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * One screen for three jobs: avatar selection, the questions, then the high
 * scores. The layout stays the same (the pink panel, the oval, four buttons);
 * only what is in them changes.
 *
 * Notes from Cooper:
 *   Cooper, check my work and edit as you see fit. Leave me a note here,
 *   one dated line each (like "20 Nov: ..."), and I will compare it to my
 *   source, then fold it in.
 *   - (none yet)
 *
 * @author Jaims
 */
public class GamePanel extends JPanel {

	/**
	 * Marks the version of this class. Private static final: one value for the
	 * whole class that never changes.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The colours from the prototype. Private static final: shared by the
	 * whole class, never changed. Green means right only.
	 */
	private static final Color STAGE = new Color(0x121935);
	private static final Color PANEL = new Color(0xF2A59C);
	private static final Color START_BLUE = new Color(0xCFE2F5);
	private static final Color WRONG_GREY = new Color(0x5B6170);
	private static final Color RIGHT_GREEN = new Color(0x2F9E6A);
	private static final Color UNUSED = new Color(0x3A4158);

	/**
	 * The avatars, in button order. Private static final: one list for the
	 * whole class, never changed.
	 */
	private static final String[] AVATARS = { "owl", "fox", "robot", "cat" };

	/**
	 * The parts of the screen. Private: only this panel changes them.
	 */
	private final JLabel prompt = new JLabel();
	private final JTextField nameField = new JTextField();
	private final JLabel picture = new JLabel();
	private final JLabel player = new JLabel();
	private final JButton[] buttons = new JButton[4];
	private final JButton start = new DepthButton();

	/**
	 * The high-score list and the scroller around it, shown where the name
	 * box was. Private: only this panel fills them.
	 */
	private final JList<String> board = new JList<>();
	private final JScrollPane scroller = new JScrollPane(board);

	/**
	 * The window, so a press can be passed on. Private: only this panel uses
	 * it.
	 */
	private final TriviaGUI window;

	/**
	 * True while picking an avatar, false while answering. Private: only this
	 * panel switches it.
	 */
	private boolean picking = true;

	/**
	 * True while the high scores are showing, so the buttons mean Play again
	 * and Quit instead of answers. Private: only this panel switches it.
	 */
	private boolean scoring = false;

	/**
	 * True while "No questions found" is showing, so only the Next button
	 * works. Private: only this panel switches it.
	 */
	private boolean empty = false;

	/**
	 * The avatar picked so far. Private: the start button hands it to the
	 * window.
	 */
	private String avatar = AVATARS[0];

	/**
	 * Builds the screen: the pink panel on top, the four buttons below.
	 * Public: TriviaGUI calls it.
	 *
	 * @param window the window this screen sits in
	 */
	public GamePanel(TriviaGUI window) {
		this.window = window;
		setBackground(STAGE);
		setLayout(new BorderLayout(20, 20));
		setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
		// (20 Nov) her trick from the WindowBuilder labs: each piece is built in its own method, so this reads like the screen
		add(newTopPanel(), BorderLayout.CENTER);
		add(newBottomPanel(), BorderLayout.SOUTH);
	}

	/**
	 * Builds the pink panel on top: the words on the left, the picture on the
	 * right. Private: only the constructor builds it. Returns the panel.
	 *
	 * @return the finished top panel
	 */
	private JPanel newTopPanel() {
		JPanel top = new DepthPanel(new BorderLayout(20, 0));
		top.setBackground(PANEL);
		top.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
		top.add(newWords(), BorderLayout.CENTER);
		top.add(picture, BorderLayout.EAST);
		return top;
	}

	/**
	 * Builds the words column: the prompt, the name box, Start, and the
	 * high-score list (hidden until the end). Private: only the top panel
	 * uses it. Returns the column.
	 *
	 * @return the finished column
	 */
	private JPanel newWords() {
		JPanel words = new JPanel();
		words.setOpaque(false);
		words.setLayout(new BoxLayout(words, BoxLayout.Y_AXIS));
		prompt.setFont(new Font("Georgia", Font.BOLD, 34));
		nameField.setFont(new Font("Georgia", Font.PLAIN, 30));
		nameField.setMaximumSize(new java.awt.Dimension(520, 60));
		nameField.setAlignmentX(LEFT_ALIGNMENT);
		// a solid darker edge on the top and left, so the box looks set into the panel
		// (the reverse of a button's), plus a little room inside for the words
		nameField.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(2, 2, 0, 0, Color.WHITE.darker()),
				BorderFactory.createEmptyBorder(4, 10, 4, 10)));
		prompt.setAlignmentX(LEFT_ALIGNMENT);
		words.add(prompt);
		words.add(nameField);
		start.setText("Start");
		start.setFont(new Font("Georgia", Font.BOLD, 26));
		start.setBackground(new Color(0xF2C14E));
		start.setOpaque(true);
		start.setBorderPainted(false);
		start.setAlignmentX(LEFT_ALIGNMENT);
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = nameField.getText().trim();
				if (name.isEmpty()) {
					name = "Player";
				}
				player.setText(name);
				window.startQuiz(name, avatar);
			}
		});
		words.add(start);
		scroller.setAlignmentX(LEFT_ALIGNMENT);
		// the same set-in edge as the name box
		scroller.setBorder(BorderFactory.createMatteBorder(2, 2, 0, 0, Color.WHITE.darker()));
		scroller.setVisible(false);
		words.add(scroller);
		return words;
	}

	/**
	 * Builds the strip below: the player's badge on the left, the four
	 * buttons beside it. Private: only the constructor builds it. Returns
	 * the strip.
	 *
	 * @return the finished bottom strip
	 */
	private JPanel newBottomPanel() {
		JPanel bottom = new JPanel(new BorderLayout(20, 0));
		bottom.setOpaque(false);
		player.setForeground(Color.WHITE);
		player.setFont(new Font("Georgia", Font.BOLD, 22));
		player.setHorizontalTextPosition(SwingConstants.CENTER);
		player.setVerticalTextPosition(SwingConstants.BOTTOM);
		bottom.add(player, BorderLayout.WEST);
		bottom.add(newButtonGrid(), BorderLayout.CENTER);
		return bottom;
	}

	/**
	 * Builds the four answer buttons, two by two, each wired once to
	 * pressed(). Private: only the bottom strip uses it. Returns the grid.
	 *
	 * @return the finished grid of four buttons
	 */
	private JPanel newButtonGrid() {
		JPanel grid = new JPanel(new GridLayout(2, 2, 20, 20));
		grid.setOpaque(false);
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new DepthButton();
			buttons[i].setFont(new Font("Georgia", Font.BOLD, 28));
			buttons[i].setBackground(START_BLUE);
			buttons[i].setOpaque(true);
			buttons[i].setBorderPainted(false);
			buttons[i].setPreferredSize(new java.awt.Dimension(500, 86));
			// one listener per button, added ONCE: a button keeps every listener it is given, so pressed() decides what a
			// press means. pick is a copy of i that never changes, because the listener runs long after the loop is over
			final int pick = i;
			buttons[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					pressed(pick);
				}
			});
			grid.add(buttons[i]);
		}
		return grid;
	}

	/**
	 * Sets the screen up for avatar selection: the name box where the
	 * question goes, the four avatars on the buttons. Public: TriviaGUI calls
	 * it. Void: returns nothing.
	 */
	public void showAvatarPick() {
		picking = true;
		scoring = false;
		empty = false;
		prompt.setText("<html>Trivia Game<br>your name</html>");
		player.setIcon(icon(avatar, 80));
		player.setText("player");
		nameField.setVisible(true);
		start.setVisible(true);
		scroller.setVisible(false);
		picture.setIcon(icon(avatar, 300));
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setText(AVATARS[i]);
			buttons[i].setIcon(icon(AVATARS[i], 56));
			buttons[i].setBackground(START_BLUE);
			buttons[i].setEnabled(true);
		}
		// yours: when a button is pressed, show that avatar large in the
		// picture, and small beside the name in the player label
		// (done 14 Nov: in pressed(), below)
	}

	/**
	 * Does what a press of button i means right now: picks an avatar, or
	 * answers. Private: only the listeners call it. Void.
	 *
	 * @param i which button, 0 to 3
	 */
	private void pressed(int i) {
		if (empty) {
			if (i == 3) {
				window.skipToScores(); // Next: the only live button
			}
		} else if (scoring) {
			// the score screen: 0 and 1 only show rows, 2 is Play again, 3 is Quit.
			// without this a press went to answerPressed and the quiz read past its
			// last question (IndexOutOfBoundsException)
			if (i == 2) {
				window.playAgain();
			} else if (i == 3) {
				window.quit();
			}
		} else if (picking) {
			avatar = AVATARS[i];
			picture.setIcon(icon(avatar, 300));
			player.setIcon(icon(avatar, 80));
		} else {
			window.answerPressed(i);
		}
	}

	/**
	 * Sets the screen up for one question: its words where the name box was,
	 * Trivia Guy in the oval, the four answers on the buttons. Public:
	 * TriviaGUI calls it. Void: returns nothing.
	 *
	 * @param question the question's words
	 * @param answers  its four answers, A to D
	 */
	public void showQuestion(String question, String[] answers) {
		picking = false;
		scoring = false;
		empty = false;
		prompt.setText("<html><div style='width:560px'>" + question + "</div></html>");
		prompt.getParent().invalidate(); // a long question after a short one: the column's layout re-measures it, or it keeps the old height
		prompt.getParent().revalidate();
		nameField.setVisible(false);
		start.setVisible(false);
		scroller.setVisible(false);
		picture.setIcon(icon("trivia-guy", 420));
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setText((char) ('A' + i) + "   " + answers[i]);
			buttons[i].setIcon(null);
			buttons[i].setDisabledIcon(null);
			buttons[i].setBackground(START_BLUE);
			buttons[i].setEnabled(true);
		}
		// yours: what a press does (grey with a red X for wrong, green for
		// right, and the points for the try)
		// (done 14 Nov: a press goes to the window through pressed(); the window asks QuizManager, which keeps the points,
		// and calls markWrong or markRight below)
	}

	/**
	 * Says the questions file gave no questions, and where it looked. Only
	 * button D works: Next, to the high scores. Public: TriviaGUI calls it.
	 * Void.
	 *
	 * @param file the questions file it tried
	 */
	public void showNoQuestions(String file) {
		picking = false;
		scoring = false;
		empty = true;
		prompt.setText("<html><div style='width:560px'>No questions found<br><small>check " + file
				+ "</small></div></html>");
		nameField.setVisible(false);
		start.setVisible(false);
		scroller.setVisible(false);
		picture.setIcon(icon("trivia-guy", 420));
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setText("");
			buttons[i].setIcon(null);
			buttons[i].setDisabledIcon(null);
			buttons[i].setBackground(START_BLUE);
			buttons[i].setEnabled(false);
		}
		buttons[3].setText("Next");
		buttons[3].setEnabled(true);
		dimBlank();
	}

	/**
	 * Darkens and locks every button with no words on it, so a button that
	 * does nothing looks it. Private: the no-questions and score screens
	 * share it. Void.
	 */
	private void dimBlank() {
		for (JButton button : buttons) {
			if (button.getText().isEmpty()) {
				button.setBackground(UNUSED);
				button.setEnabled(false);
			}
		}
	}

	/**
	 * Marks a wrong pick: grey, a red X, and it can't be pressed again.
	 * Public: the window calls it. Void.
	 *
	 * @param i which button
	 */
	public void markWrong(int i) {
		mark(i, WRONG_GREY, "wrong");
		buttons[i].setEnabled(false);
	}

	/**
	 * Marks the right answer: green with a check. Public: the window calls
	 * it. Void.
	 *
	 * @param i which button
	 */
	public void markRight(int i) {
		mark(i, RIGHT_GREEN, "right");
	}

	/**
	 * Colours a button, puts the mark on it and plays the mark's sound.
	 * Private: markWrong and markRight share it. Void.
	 *
	 * @param i      which button
	 * @param colour its new colour
	 * @param name   right or wrong: the picture and the sound
	 */
	private void mark(int i, Color colour, String name) {
		buttons[i].setBackground(colour);
		buttons[i].setIcon(icon(name, 40));
		buttons[i].setDisabledIcon(icon(name, 40)); // a locked button greys its icon unless it has its own
		play(name);
	}

	/**
	 * Plays one short sound from the sounds folder. Private: only this panel
	 * needs it. Void.
	 *
	 * @param name the file name without .wav
	 */
	private void play(String name) {
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(getClass().getResource("sounds/" + name + ".wav")));
			clip.start();
		} catch (Exception e) {
			// no sound is not worth stopping the game for
		}
	}

	/**
	 * Reads one picture from the images folder, sized to fit. No access word:
	 * any class in package trivia may use it (ScorePanel does). Static: no
	 * panel is needed to read a picture. Returns the picture as an icon.
	 *
	 * @param name   the file name without .png
	 * @param height how tall to draw it, in pixels
	 * @return the picture, ready for a label or a button
	 */
	static ImageIcon icon(String name, int height) {
		ImageIcon full = new ImageIcon(GamePanel.class.getResource("images/" + name + ".png"));
		int width = full.getIconWidth() * height / full.getIconHeight();
		return new ImageIcon(full.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
	}

	/**
	 * Sets the screen up for the high scores: the list where the question
	 * was, the player's avatar in the oval, the top rows on the buttons.
	 * Rows 0-2 are badged gold, silver and laurel. Public: TriviaGUI calls
	 * it. Void: returns nothing.
	 *
	 * @param name   the player's name
	 * @param avatar the player's avatar
	 * @param rows   the high-score rows, best first
	 * @param rank   the player's row in the list, highlighted
	 */
	public void showScores(String name, String avatar, String[] rows, int rank) {
		picking = false;
		scoring = true;
		empty = false;
		prompt.setText("High scores");
		nameField.setVisible(false);
		start.setVisible(false);
		board.setFont(new Font("Georgia", Font.PLAIN, 30));
		board.setCellRenderer(new javax.swing.DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index,
					boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (rows.length > 0) {
					setIcon(badgeIcon(index, 34)); // the crown, silver crown or laurel picture; null past third
				} else {
					setForeground(Color.GRAY); // a message, not a player's row
					setFont(getFont().deriveFont(Font.ITALIC));
				}
				return this;
			}
		});
		board.setListData(rows.length > 0 ? rows : new String[] { "No scores yet" });
		scroller.setVisible(true);
		board.setSelectedIndex(rank);
		board.ensureIndexIsVisible(rank);
		picture.setIcon(icon(avatar, 300));
		player.setIcon(icon(avatar, 80));
		player.setText(name);
		// scores mode: top score, your row, then Play again and Quit
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setIcon(null);
			buttons[i].setDisabledIcon(null);
			buttons[i].setBackground(START_BLUE);
			buttons[i].setEnabled(true);
		}
		buttons[0].setText(rows.length > 0 ? rows[0] : "");
		buttons[1].setText(rank >= 0 && rank < rows.length ? rows[rank] : ""); // rank -1: no game was saved
		buttons[0].setIcon(rows.length > 0 ? badgeIcon(0, 40) : null);
		buttons[1].setIcon(rank >= 0 && rank < rows.length ? badgeIcon(rank, 40) : null);
		buttons[2].setText("Play again");
		buttons[3].setText("Quit");
		dimBlank();
	}

	/**
	 * The badge picture for a top place: the gold crown, the silver crown or
	 * the laurel (images/crown.png, silver.png, laurel.png). Private: only
	 * this panel badges rows. Static: no panel is needed. Returns the picture,
	 * or null past third place, which means no picture.
	 *
	 * @param place  the row, 0 for first
	 * @param height how tall to draw it, in pixels
	 * @return the badge picture, or null
	 */
	private static ImageIcon badgeIcon(int place, int height) {
		if (place == 0) {
			return icon("crown", height);
		}
		if (place == 1) {
			return icon("silver", height);
		}
		if (place == 2) {
			return icon("laurel", height);
		}
		return null;
	}
}
