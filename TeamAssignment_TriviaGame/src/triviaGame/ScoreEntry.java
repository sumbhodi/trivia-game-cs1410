package triviaGame;

/**
 * One line of the high scores: a player's name and their points. Two entries
 * are put in order by their scores, highest first, so a sorted list is the
 * leaderboard.
 *
 * Notes from Cooper:
 *   Cooper, check my work and edit as you see fit. Leave me a note here,
 *   one dated line each (like "20 Nov: ..."), and I will compare it to my
 *   source, then fold it in.
 *   - (none yet)
 *
 * @author Jaims
 */
public class ScoreEntry implements Comparable<ScoreEntry> {

	/**
	 * Who played. Private: only this entry holds it; read it with getName.
	 */
	private String name;

	/**
	 * Their points. Private: only this entry holds it; read it with getScore.
	 */
	private int score;

	/**
	 * The avatar they played as, e.g. owl. Private: only this entry holds it;
	 * read it with getAvatar.
	 */
	private String avatar;

	/**
	 * Builds an entry with an avatar. Public: QuizManager and FileHandler make
	 * these.
	 *
	 * @param name   who played
	 * @param score  their points
	 * @param avatar the avatar they played as, e.g. owl
	 */
	public ScoreEntry(String name, int score, String avatar) {
		this.name = name;
		this.score = score;
		this.avatar = avatar;
	}

	/**
	 * Builds an entry with no avatar given, so it gets the owl. Public: old
	 * highscores.txt lines have no avatar and still load through this one.
	 *
	 * @param name  who played
	 * @param score their points
	 */
	public ScoreEntry(String name, int score) {
		this(name, score, "owl");
	}

	/**
	 * Reads the name. Public: anyone may ask. Returns a String.
	 *
	 * @return who played
	 */
	public String getName() {
		return name;
	}

	/**
	 * Reads the score. Public: anyone may ask. Returns an int.
	 *
	 * @return their points
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Reads the avatar. Public: anyone may ask. Returns a String.
	 *
	 * @return the avatar they played as, e.g. owl
	 */
	public String getAvatar() {
		return avatar;
	}

	/**
	 * Puts two entries in order, highest score first, so Collections.sort
	 * makes the leaderboard. Public: Comparable asks for it. Returns an int.
	 *
	 * @param other the entry to compare with
	 * @return a negative number if this one goes first, zero if they tie, a
	 *         positive number if the other goes first
	 */
	@Override
	public int compareTo(ScoreEntry other) {
		return Integer.compare(other.score, score);
	}

	/**
	 * Returns the entry as it shows on the score screen. Public: anyone may
	 * ask. Returns a String.
	 *
	 * @return e.g. kayden2010 12
	 */
	@Override
	public String toString() {
		return name + " " + score;
	}
}
