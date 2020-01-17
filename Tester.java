package cs146F19.Bedi.project4;

import java.io.BufferedReader;
import java.io.FileReader;

public class Tester
{
	//this method reads the words from the dictionary file and inserts them into a RBtree
	private static void createRBT(RedBlackTree redBlackTree)
	{
		try
		{
			BufferedReader brDictionary = new BufferedReader(new FileReader("dictionary.txt"));
			String word;

			while ((word = brDictionary.readLine()) != null)
			{
				redBlackTree.insert(word);
			}

			brDictionary.close();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	//this method reads the words from the poem file and saves them into an array of strings
	//and looks up individual words from the dictionary and returns the count of misspelled words.
	private static int spellChecker(RedBlackTree redBlackTree)
	{
		try
		{
			BufferedReader brPoem = new BufferedReader(new FileReader("poem.txt"));

			String line;
			int misspelledWords = 0;

			while ((line = brPoem.readLine()) != null)
			{
				String[] words = line.split(" ");

				for (int i = 0; i < words.length; i++)
				{
					String word = words[i].replaceAll("[^a-zA-Z- ]", "").toLowerCase();
					if (redBlackTree.lookup(word) == null)
					{
						misspelledWords++;
					}
				}
			}

			brPoem.close();
			return misspelledWords;
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			return -1;
		}
	}
	
	//this main method times how long it takes to create the dictionary in the RBTree form
	//and times how long it takes to check and spelling of the poem words
	public static void main (String [] Args)
	{
		try
		{
			RedBlackTree<String> rbtDictionary = new RedBlackTree<String>();
			
			long startInsert = System.currentTimeMillis();
			createRBT(rbtDictionary);
			long endInsert = System.currentTimeMillis();
			long totalInsert = endInsert - startInsert;
			
			long startCheck = System.currentTimeMillis();
			int mispelledWordsTotal = spellChecker(rbtDictionary);
			long endCheck = System.currentTimeMillis();
			long totalCheck = endCheck - startCheck;

			System.out.println("Time to insert dictionary into an RedBlackTree: " + totalInsert + " milliseconds");
			System.out.println("Time to spell check poem against words from dictionary: " + totalCheck + " milliseconds");
			System.out.println("Total mispelled words that were found: " + mispelledWordsTotal + " words");
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
}