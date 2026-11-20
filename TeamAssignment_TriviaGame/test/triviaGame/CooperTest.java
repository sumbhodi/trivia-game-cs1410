package triviaGame;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * JUnit 5 tests for Cooper's classes, written to his code as it is on 20 Nov:
 * Question, FileHandler.readQuestions and QuizManager. Every test that is on
 * passes now. The @Disabled ones wait for answer() and the high scores to come
 * back: take the @Disabled line off when that part is ready.
 *
 * Notes from Cooper:
 *   Cooper, edit these as you see fit; they are yours to keep or change.
 *   - (none yet)
 *
 * @author Jaims
 */
class CooperTest {

	/** A questions file in Cooper's format (answers counted from 1), in a temp file so the real one is never touched. */
	private File questionsFile() throws IOException {
		File f = File.createTempFile("questions", ".txt");
		f.deleteOnExit();
		try (PrintWriter w = new PrintWriter(f)) {
			w.println("What is the capital city of Italy?|Milan|Rome|Venice|Naples|2");
			w.println("How many continents are there on Earth?|5|6|7|8|3");
			w.println("In Java, which keyword is used to inherit a class?|implements|extends|inherits|super|2");
		}
		return f;
	}

	@Test
	void questionKeepsItsWordsAndChoices() {
		ArrayList<String> choices = new ArrayList<>();
		choices.add("Milan"); choices.add("Rome"); choices.add("Venice"); choices.add("Naples");
		Question q = new Question("What is the capital city of Italy?", choices, 2);
		assertEquals("What is the capital city of Italy?", q.getText());
		assertEquals(4, q.getChoices().size());
		assertTrue(q.isCorrectChoice(2), "2 is the answer as stored");
		assertFalse(q.isCorrectChoice(1));
	}

	@Test
	void readQuestionsReadsEveryLine() throws IOException {
		ArrayList<Question> qs = FileHandler.readQuestions(questionsFile().getPath());
		assertEquals(3, qs.size());
		assertEquals("What is the capital city of Italy?", qs.get(0).getText());
		assertEquals("Rome", qs.get(0).getChoices().get(1));
		assertEquals(4, qs.get(1).getChoices().size());
	}

	@Test
	void readQuestionsMissingFileIsEmptyNotACrash() {
		assertTrue(FileHandler.readQuestions("no-such-file-anywhere.txt").isEmpty());
	}

	@Test
	void startLoadsTheGame() throws IOException {
		QuizManager quiz = new QuizManager(questionsFile().getPath(), "no-scores-yet.txt");
		quiz.start("Cooper", "owl");
		assertTrue(quiz.hasNext());
		assertNotNull(quiz.getQuestion());
		assertEquals(0, quiz.getScore());
		assertEquals("Cooper", quiz.getPlayerName());
	}

	@Disabled("turn on when answer() comes back")
	@Test
	void firstTryRightScoresFour() throws IOException {
		QuizManager quiz = new QuizManager(questionsFile().getPath(), "no-scores-yet.txt");
		quiz.start("Cooper", "owl");
		int right = 0;
		for (int i = 0; i <= 4; i++) if (quiz.getQuestion().isCorrectChoice(i)) right = i;
		assertTrue(quiz.answer(right));
		assertEquals(4, quiz.getScore());
	}

	@Disabled("turn on when answer() comes back")
	@Test
	void wrongThenRightScoresTwo() throws IOException {
		QuizManager quiz = new QuizManager(questionsFile().getPath(), "no-scores-yet.txt");
		quiz.start("Cooper", "owl");
		int right = 0;
		for (int i = 0; i <= 4; i++) if (quiz.getQuestion().isCorrectChoice(i)) right = i;
		int wrong = right == 1 ? 2 : 1;
		assertFalse(quiz.answer(wrong), "a wrong pick is not right");
		assertTrue(quiz.answer(right), "the same question, still there to try again");
		assertEquals(2, quiz.getScore());
	}

	@Disabled("turn on when answer() comes back")
	@Test
	void theLastQuestionDoesNotCrash() throws IOException {
		QuizManager quiz = new QuizManager(questionsFile().getPath(), "no-scores-yet.txt");
		quiz.start("Cooper", "owl");
		for (int step = 0; step < 10 && quiz.hasNext(); step++) {   // at most 10 tries, so a stuck answer() fails instead of hanging
			int right = 0;
			for (int i = 0; i <= 4; i++) if (quiz.getQuestion().isCorrectChoice(i)) right = i;
			assertDoesNotThrow(() -> quiz.answer(0));
			quiz.answer(right);
		}
		assertFalse(quiz.hasNext());
	}
}
