package triviaGame;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * The Trivia Game's one window. It holds one screen, the game panel, which
 * does avatar selection, the questions and the high scores in turn.
 *
 * Notes from Cooper:
 *   Cooper, check my work and edit as you see fit. Leave me a note here,
 *   one dated line each (like "20 Nov: ..."), and I will compare it to my
 *   source, then fold it in.
 *   - (none yet)
 *
 * @author Jaims
 */
public class TriviaGUI extends JFrame {

	/**
	 * Marks the version of this class. Private: only this class uses it.
	 * Static final: one value for the whole class that never changes.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Where the game's two data files live, from the project's root (beside Cooper's code, his layout). Private
	 * static final: one path each, written once, never changed.
	 */
	private static final String QUESTIONS_FILE = "src/triviaGame/questions.txt";
	private static final String HIGHSCORES_FILE = "src/triviaGame/highscores.txt";

	/**
	 * The one screen. Private: the window owns it and tells it what to show.
	 */
	private final GamePanel gamePanel = new GamePanel(this);

	/**
	 * Cooper's game: the questions, the tries, the score. Private: only the
	 * window talks to it. The two files sit in the data folder.
	 */
	private final QuizManager quiz = new QuizManager(QUESTIONS_FILE, HIGHSCORES_FILE);

	/**
	 * True during the short pause after a right answer, so a fast second press
	 * is ignored. Private: only the window switches it.
	 */
	private boolean waiting = false;

	/**
	 * Starts the program. Public: Java calls it. Static: it runs before any
	 * window exists. Void: it returns nothing.
	 *
	 * @param args not used
	 */
	public static void main(String[] args) {
		// TRACE: where Java looks for the data files (remove later)
		System.out.println("TRACE working dir: " + System.getProperty("user.dir")); // TRACE
		for (String path : new String[] { QUESTIONS_FILE, HIGHSCORES_FILE }) { // TRACE
			java.io.File f = new java.io.File(path); // TRACE
			System.out.println("TRACE " + path + " -> " + f.getAbsolutePath() + " (exists: " + f.exists() + ")"); // TRACE
		} // TRACE
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				TriviaGUI frame = new TriviaGUI();
				frame.setVisible(true);
			}
		});
	}

	/**
	 * Builds the window, puts the game screen in it and starts on avatar
	 * selection. Public: main calls it.
	 */
	public TriviaGUI() {
		setTitle("Trivia Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1280, 800);
		setContentPane(gamePanel);
		gamePanel.showAvatarPick();
	}

	/**
	 * Starts a game with the player's name and avatar. Public: the game
	 * screen's Start button calls it. Void.
	 *
	 * @param name   the player's name
	 * @param avatar the avatar picked, e.g. owl
	 */
	public void startQuiz(String name, String avatar) {
		quiz.start(name, avatar);
		if (!quiz.hasQuestions()) {
			gamePanel.showNoQuestions(QUESTIONS_FILE); // say why, with a Next to the scores
			return;
		}
		showNextQuestion();
	}

	/**
	 * Goes from the "No questions found" screen to the high scores, saving
	 * nothing. Public: that screen's Next button calls it. Void.
	 */
	public void skipToScores() {
		endGame();
	}

	/**
	 * Passes an answer press to the quiz and shows the result: a wrong pick
	 * is marked and locked; a right one turns green, waits a moment, then
	 * moves on. Public: the game screen calls it. Void.
	 *
	 * @param choice the button pressed, 0 to 3
	 */
	public void answerPressed(int choice) {
		if (waiting) {
			return;
		}
		if (quiz.answer(choice)) {
			gamePanel.markRight(choice);
			waiting = true;
			Timer wait = new Timer(1200, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					waiting = false;
					showNextQuestion();
				}
			});
			wait.setRepeats(false);
			wait.start();
		} else {
			gamePanel.markWrong(choice);
		}
	}

	/**
	 * Goes back to avatar selection for a new game. The name and avatar stay
	 * filled in. Public: the score screen's Play again button calls it. Void.
	 */
	public void playAgain() {
		gamePanel.showAvatarPick();
	}

	/**
	 * Closes the window and ends the program. Public: the score screen's Quit
	 * button calls it. Void.
	 */
	public void quit() {
		dispose();
		System.exit(0);
	}

	/**
	 * Shows the next question, or the high scores when there are none left.
	 * Private: only the window moves the game along. Void.
	 */
	private void showNextQuestion() {
		if (quiz.hasNext()) {
			Question q = quiz.getQuestion();
			gamePanel.showQuestion(q.getText(), q.getChoices().toArray(new String[0]));
		} else {
			endGame();
		}
	}

	/**
	 * Saves the score and shows the top MAX_SCORES rows on the game screen. If
	 * the player is below them, a ⋯ row comes next, then the player's own row
	 * with their real place. Their row is highlighted. Private:
	 * showNextQuestion calls it after the last question. Void.
	 */
	private void endGame() {
		quiz.saveScore();
		int place = quiz.getLastPlace();
		java.util.List<ScoreEntry> list = quiz.getHighScore(); // Cooper's name for it
		int shown = Math.min(QuizManager.MAX_SCORES, list.size());
		ArrayList<String> rows = new ArrayList<>();
		for (int i = 0; i < shown; i++) {
			rows.add((i + 1) + ".  " + list.get(i));
		}
		int rank = place;
		if (place >= shown) {
			// below the board: a gap, then the player's own row at their real place
			rows.add("\u22EF");
			rows.add((place + 1) + ".  " + list.get(place));
			rank = rows.size() - 1;
		}
		gamePanel.showScores(quiz.getPlayerName(), quiz.getPlayerAvatar(), rows.toArray(new String[0]), rank);
	}
}
