package triviaGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Manges the connection between our manager
 * and the txt files: questions.txt and highscores.txt
 * @author Cooper and Jaims
 */
public class FileHandler 
{
	
	public static ArrayList<Question> readQuestions(String fileName)
	{
		ArrayList<Question> temp = new ArrayList<>();
		
		try(Scanner reader = new Scanner(new File(fileName)))
		{
			while(reader.hasNextLine())
			{
				String nextLine = reader.nextLine();
				
				System.out.println(nextLine);
				String[] parts = nextLine.split("\\|");
				ArrayList<String> choices = new ArrayList<>();
				
				for (int i = 1; i < parts.length - 1; i++)
				{
					choices.add(parts[i]);
				}
				
				temp.add(new Question(parts[0], choices, Integer.parseInt(parts[parts.length - 1])));
			}
		}
		catch (FileNotFoundException e)
		{
			System.err.print("File not found: " + fileName);
		}
		
		return temp;
	}
	
//	public static ArrayList<ScoreEntry> readHighScores(String fileName)
//	{
//		ArrayList<ScoreEntry> temp = new ArrayList<>();
//		
//		try(Scanner reader = new Scanner(new File(fileName)))
//		{
//			while(reader.hasNextLine())
//			{
//				String nextLine = reader.nextLine();
//				
//				System.out.println(nextLine);
//				String[] parts = nextLine.split("\\|");
//				
//				temp.add(new ScoreEntry(parts[0], Integer.parseInt(parts[parts.length - 1])));
//			}
//		}
//		catch (FileNotFoundException e)
//		{
//			System.err.print("File not found: " + fileName);
//		}
//		
//		return temp;
//	}
	
	public void writeQuestion(String fileName, Question q)
	{
		try(PrintWriter writer = new PrintWriter(new FileWriter(fileName, true)))
		{
			writer.append("");
			//Needs to be implemented
		}
		catch (IOException e)
		{
			System.err.print("File not found: " + fileName);
		}
	}
	
	
}
