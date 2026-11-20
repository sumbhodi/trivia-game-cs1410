package triviaGame;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages the logic of our Quiz, 
 * updating the questions, score, name, and avater.
 * @author Cooper & Jaims
 */
public class QuizManager 
{
	List<Question> questions = new ArrayList<>();
	List<ScoreEntry> highScores = new ArrayList<>();
	private int currentIndex;
	int tries;
	private int score;
	String playerName;
	String playerAvatar;
	
	/**
	 * Creates a QuizManager while assigning questionFile
	 * and scoreFile.
	 * @param questionFile
	 * @param scoreFile
	 */
	public QuizManager(String questionFile, String scoreFile)
	{
		questions = FileHandler.readQuestions(questionFile);
		//highScores = FileHandler.readHighScores(scoreFile);
	}
	
	/**
	 * Starts the game and assigns name, and avatar.
	 * While also shuffling the questions.
	 * @param name
	 * @param avatar
	 */
	public void start(String name, String avatar)
	{
		this.playerName = name;
		this.playerAvatar = avatar;
		
		score = 0;
		currentIndex = 0;
		Collections.shuffle(questions);
	}
	
	/**
	 * If there is another question it
	 * returns the next question.
	 * @return
	 */
	public Question getQuestion()
	{
		if(hasNext()) 
		{
			return questions.get(currentIndex);
		}
		return null;
	} 
	
	/**
	 * Returns if the choice was correct
	 * while updating the score correctly:
	 * First guess: +4
	 * Second guess: +2
	 * Third guess: +1
	 * @param choice
	 * @return
	 */
	public boolean answer(int choice)
	{
		// In progress, commented out for the Significant Progress video (20 Nov), as her brief says to do with code that
		// doesn't work yet. Three things to fix when it comes back: check the pick BEFORE adding points, count tries up on a
		// wrong pick, and move to the next question only AFTER checking (as written it checks the next question, and the
		// last one goes past the end of the list).
//		if(!hasNext())
//		{
//			return false;
//		}
//		else
//		{
//			if(tries == 0)
//			{
//				score += 4;
//			}
//			else if (tries == 1)
//			{
//				score += 2;
//			}
//			else if (tries == 2)
//			{
//				score += 1;
//			}

//			currentIndex++;
//			return questions.get(currentIndex).isCorrectChoice(choice);
//		}

		return false; // placeholder until the scoring above comes back
	
	}
	
	/**
	 * Checks if there is another question.
	 * @return
	 */
	public boolean hasNext()
	{
		return currentIndex < questions.size();
	}
	
	/**
	 * Returns the score.
	 * @return
	 */
	public int getScore()
	{
		return score;
	}
	
	/**
	 * Returns highScores
	 * @return highScores
	 */
	public List<ScoreEntry> getHighScore()
	{
		return highScores;
	}
	
	/**
	 * Saves the highscore in txt.
	 */
	public void saveScore()
	{
		//FileHandler.writeHighScores();
	}

	// ── Added by Jaims, 20 Nov, so the screens compile against this class. Cooper: rename, move or replace any of these. ──

	/**
	 * How many rows the score screen shows. Public static final: the screens
	 * read it.
	 */
	public static final int MAX_SCORES = 10;

	/**
	 * Says whether the questions file gave any questions at all. Public:
	 * TriviaGUI shows "No questions found" instead of an empty game.
	 *
	 * @return true when at least one question loaded
	 */
	public boolean hasQuestions()
	{
		return !questions.isEmpty();
	}

	/**
	 * The player's name. Public: the score screen shows it.
	 *
	 * @return the name given to start()
	 */
	public String getPlayerName()
	{
		return playerName;
	}

	/**
	 * The player's avatar. Public: the score screen shows it.
	 *
	 * @return the avatar given to start()
	 */
	public String getPlayerAvatar()
	{
		return playerAvatar;
	}

	/**
	 * The player's place in the high scores after saveScore. Public: the
	 * score screen highlights that row.
	 *
	 * @return the place, from 0
	 */
	public int getLastPlace()
	{
		// TODO Cooper: once saveScore sorts the list, remember where the new score landed and return it here
		return 0;
	}
}
