package triviaGame;

import java.util.ArrayList;

/**
 * 
 * @author Cooper and Jaims
 */
public class Question 
{
	String text;
	ArrayList<String> choices;
	int answer;
	
	/**
	 * Creates an instance of a question with the text,
	 * choices, and correct index.
	 * @param text
	 * @param choices
	 * @param answer
	 */
	public Question(String text, ArrayList<String> choices, int answer)
	{
		this.text = text;
		this.choices = choices;
		this.answer = answer;
	}

	/**
	 * Returns the questions itself.
	 * @return the text
	 */
	public String getText() 
	{
		return text;
	}

	/**
	 * Returns the choices.
	 * @return the choices
	 */
	public ArrayList<String> getChoices() 
	{
		return choices;
	}	
	
	/**
	 * Checks if the choice is correct.
	 * @return true/false
	 */
	public boolean isCorrectChoice(int choice)
	{
		return this.answer == choice;
	}
}
