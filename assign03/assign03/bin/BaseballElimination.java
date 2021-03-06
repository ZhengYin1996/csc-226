/* BaseballElimination.java
   CSC 226 - Summer 2019
   Assignment 4 - Baseball Elimination Program

   This template includes some testing code to help verify the implementation.
   To interactively provide test inputs, run the program with
	java BaseballElimination

   To conveniently test the algorithm with a large input, create a text file
   containing one or more test divisions (in the format described below) and run
   the program with
	java -cp .;algs4.jar BaseballElimination file.txt (Windows)
   or
    java -cp .:algs4.jar BaseballElimination file.txt (Linux or Mac)
   where file.txt is replaced by the name of the text file.

   The input consists of an integer representing the number of teams in the division and then
   for each team, the team name (no whitespace), number of wins, number of losses, and a list
   of integers represnting the number of games remaining against each team (in order from the first
   team to the last). That is, the text file looks like:

	<number of teams in division>
	<team1_name wins losses games_vs_team1 games_vs_team2 ... games_vs_teamn>
	...
	<teamn_name wins losses games_vs_team1 games_vs_team2 ... games_vs_teamn>


   An input file can contain an unlimited number of divisions but all team names are unique, i.e.
   no team can be in more than one division.


   R. Little - 03/22/2019
*/

import edu.princeton.cs.algs4.*;
import java.util.*;
import java.io.File;

//Do not change the name of the BaseballElimination class
public class BaseballElimination{
	
	// We use an ArrayList to keep track of the eliminated teams.
	public ArrayList<String> eliminated = new ArrayList<String>();

	/* BaseballElimination(s)
		Given an input stream connected to a collection of baseball division
		standings we determine for each division which teams have been eliminated 
		from the playoffs. For each team in each division we create a flow network
		and determine the maxflow in that network. If the maxflow exceeds the number
		of inter-divisional games between all other teams in the division, the current
		team is eliminated.
	*/
	ArrayList<String> AL = new ArrayList<String>();
	public BaseballElimination(Scanner s)
	{
		
		int size = s.nextInt();
		int[] wins = new int[size];
		int[] remaining = new int[size];
		int[][] games =  new int[size][size];
		String[] teams = new String[size];
		
		//
		for (int i = 0; i < size; i++) 
		{
			teams[i] = s.next();
			AL.add(teams[i]);
			wins[i] = s.nextInt();
			remaining[i] = s.nextInt();
			
			for (int j = 0; j < size; j++) {
				games[i][j] = s.nextInt();
			}
		}
		
		for (int i = 0; i < AL.size();i++) 
		{
			
			String current = AL.get(i);
			int pos = i;
			boolean currentOut = false;
			int outflow = 0;
			for (int j = 0; j < size; j++) 
			{
				if (j != pos ) 
				{
					if(wins[pos] + remaining[pos] < wins[j])
					{
						eliminated.add(current);
						currentOut = true;
						break;
					}
					
				}
			}
			
			if (currentOut== false)
			{
				int verticeNum = size*(size-1)/2 + 3;
				FlowNetwork FNet = new FlowNetwork(verticeNum);
				int v = size;
				for (int j = 0; j < size; j++) 
				{
					if (j != pos) {
						for (int k = j+1; k < size; k++) 
						{
							if (k != pos) 
							{
								int curGame = games[j][k];
								outflow += curGame;
								FlowEdge edge1 = new FlowEdge(verticeNum-2, v, curGame);
								FlowEdge edge2 = new FlowEdge(v, j, Double.POSITIVE_INFINITY);
								FlowEdge edge3 = new FlowEdge(v, k, Double.POSITIVE_INFINITY);
								FNet.addEdge(edge1);
								FNet.addEdge(edge2);
								FNet.addEdge(edge3);
								v++;
							}
							
						}
					}
					
				}
				
				for (int j = 0; j < size; j++) 
				{
					if (j != pos) {
						int cap = wins[pos] + remaining[pos] - wins[j];
						FlowEdge edge = new FlowEdge(j, verticeNum-1, cap);
						FNet.addEdge(edge);
					}
				}
				System.out.println(current);
				System.out.println(FNet.toString());
				
				FordFulkerson flow = new FordFulkerson(FNet, verticeNum-2, verticeNum-1);
				
				if (flow.value() != outflow) 
					eliminated.add(current);
			}
		}
	}
		
	/* main()
	   Contains code to test the BaseballElimantion function. You may modify the
	   testing code if needed, but nothing in this function will be considered
	   during marking, and the testing process used for marking will not
	   execute any of the code below.
	*/
	public static void main(String[] args){
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
		
		BaseballElimination be = new BaseballElimination(s);		
		
		if (be.eliminated.size() == 0)
			System.out.println("No teams have been eliminated.");
		else
			System.out.println(be.AL);
			System.out.println("Teams eliminated: " + be.eliminated);
	}
}