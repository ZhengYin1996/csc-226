/* WordSearch.java
   CSC 226 - Summer 2019
   Assignment 4 - Word Search Problem Template
   
   You should be able to compile your program with the command
   
	javac WordSearch.java
	
   To conveniently test the algorithm, create a text file
   containing a word and puzzle (in the format described below) 
   and run the program with
   
	java WordSearch file.txt
	
   where file.txt is replaced by the name of the text file.
   
   Note, I didn't need any of the algs4.jar code. You may include it
   if you need to.
   
   The input consists of a graph (as an adjacency matrix) in the following format:
   
    <dimnsion of puzzle>
	<word string>
	<character grid row 1>
	...
	<character grid row n>

   R. Little - 07/2/2018
*/
// Zheng Yin
// V00915261
// CSC226

import java.util.Scanner;
import java.io.File;

//Do not change the name of the WordSearch class.
public class WordSearch {
	// List of global variables. You may to this list.
	public int startRow; // Starting row of word in puzzle
	public int startCol; // Starting column of word in puzzle
	public int endRow; 	 // Ending row of word in puzzle
	public int endCol;   // Ending column of word in puzzle


	/* WordSearch(word)
		Use the WordSearch construcutor to do any preprocessing
		of the search word that may be needed. Examples, the dfa[][]
		for KMP, the right[] for Boyer-Moore, the patHash for Rabin-
		Karp.
	*/
	int[][] dfa ;
	
    public WordSearch(char[] word) {
		
		/* ... Your code here ... */
    	int R = 256;
    	int m = word.length;
    	dfa = new int[R][m];
    	dfa[word[0]][0]=1;
    	for(int x =0,j =1;j<m;j++)
    	{
    		for(int c = 0; c<R;c++)
    			dfa[c][j] = dfa[c][x];
    		dfa[word[j]][j] = j+1;
    		x =dfa[word[j]][x];
    	}
    	
    	
    } 

	
 
	/* search(puzzle)
		Once you have preprocessed the word you need to search the
		puzzle for the word. That happens here. You will assign the
		given global variables here once you find the word and return
		the boolean value "true". If you can't find the word, return 
		"false"
	*/
    public boolean search(char[][] puzzle) {
		
		/* ... Your code here ... */
    	int row = puzzle.length;
    	int m = dfa[0].length;
    	int n = puzzle[0].length;
    	int i, j;
    	for(int k = 0;k<row;k++)
    	{
    		for(i=0,j=0;i<n&&j<m;i++)
    		{
    			j = dfa[puzzle[k][i]][j];
    		}
    		if(j == m)
    		{
    			startRow = k;
    			startCol = i-m;
    			endRow = k;
    			endCol = i-1;
    			return true;
    		}
    			
    	}
    	return false;
    			
		
    }


	/* main()
	   Contains code to test the WordSearch program. You may modify the
	   testing code if needed, but nothing in this function will be considered
	   during marking, and the testing process used for marking will not
	   execute any of the code below. 
	*/
    public static void main(String[] args) {
		Scanner s;
		if (args.length > 0){
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);
		}else{
			s = new Scanner(System.in);
			System.out.printf("Reading input values from stdin.\n");
		}
		
		int n = s.nextInt(); 			// dimension of the puzzle
		String wordAsString = s.next();
		char[] word = wordAsString.toCharArray(); // search word
		char[][] puzzle = new char[n][n];		  // the puzzle
		
		for (int i = 0; i < n; i++){
			String line = s.next();
			for (int j = 0; j < n; j++)
				puzzle[i][j] = line.charAt(j);
		}
		
		WordSearch searcher = new WordSearch(word); // Preprocess word
        boolean result = searcher.search(puzzle);	// Search for word in puzzle
		
		
		// Output the word, the puzzle and the solution
		System.out.println("Word: " + wordAsString);
		System.out.printf("\nPuzzle:    \n");
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++)
				 System.out.print(puzzle[i][j]);
			System.out.println();
		}
		
		if (!result)
			System.out.printf("\nSolution: Search word not found");
		else {
			int x1 = searcher.startRow;
			int y1 = searcher.startCol;
			int x2 = searcher.endRow;
			int y2 = searcher.endCol;
		
			System.out.printf("\nSolution: Search word starts at coordinate ");
			System.out.print("("+x1+","+y1+")");
			System.out.print(" and ends at coordinate ");
			System.out.print("("+x2+","+y2+")");
		}
    }
	
}


