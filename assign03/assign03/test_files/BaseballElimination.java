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
	static int size;
	static String[] teams;
	public static double pos = Double.POSITIVE_INFINITY;
	// We use an ArrayList to keep track of the eliminated teams.
	public static ArrayList<String> eliminated = new ArrayList<String>();

	/* BaseballElimination(s)
		Given an input stream connected to a collection of baseball division
		standings we determine for each division which teams have been eliminated 
		from the playoffs. For each team in each division we create a flow network
		and determine the maxflow in that network. If the maxflow exceeds the number
		of inter-divisional games between all other teams in the division, the current
		team is eliminated.
	*/
	public static ArrayList<String> BaseballElimination(Scanner s){
		
		/* ... Your code here ... */
		size = s.nextInt();
		int[][] table = new int[size][size+3];
		teams  = new String[size];
		for(int i = 0; i < size;i++)
		{
			for(int j = 0; j<size+3;j++)
			{
				if( j ==0)
				{
					teams[i] = s.next();
					table[i][j] = i;
				}
				else
				{
					if(s.hasNextInt())
					{
						table[i][j] = s.nextInt();					
					}
				}
			}
		}
		if(size!=0)
		{
			int testedTeam = 0;
			while(testedTeam<size)
			{
				checkEliminated(testedTeam,table);
				testedTeam++;
			}
		}
		else
			System.out.println("The number of teams is set to 0");
			
		return  eliminated;
	}
	public static void checkEliminated(int testedTeam, int[][] table)
	{
		int win = table[testedTeam][1]+ table[testedTeam][2];
		int games = (size-1)*size/2;
		int gamesLeft = 0;
		ArrayList<couple<Integer>> game = new ArrayList<couple<Integer>>();
		
		for(int i = 0; i<size; i++)
		{
			for( int j = 0 ; j<size; j++)
			{
				if(i != (size-1)&& j != (size-1))
				{
					couple<Integer> pair = new couple<Integer>(i,j);
					game.add(pair);
				}
			}
		}
		int netSize = (size-1) + games +2;
		int elim = netSize -1;
		FlowNetwork FNw1 = new FlowNetwork(netSize);
		FlowNetwork FNw2 = new FlowNetwork(netSize);
		boolean[] edge = new boolean[size];
		for(int i = 0; i < game.size(); i++)
		{
			couple<Integer> pair = game.get(i);
			int gameLeftForPair = 0;
			gameLeftForPair = table[pair.u][pair.v+3];
			gamesLeft +=gameLeftForPair;
			
			if(gameLeftForPair == 0)
			{
				if(table[pair.u][1]>win|| table[pair.v][1]>win)
				{
					if(!eliminated.contains(teams[testedTeam]))
						eliminated.add(teams[testedTeam]);
				}
				continue;
			}
			if(table[pair.u][1]>win||table[pair.v][1]>win)
			{
				if(!eliminated.contains(teams[testedTeam]))
					eliminated.add(teams[testedTeam]);
				return;
			}
			FlowEdge e1 = new FlowEdge(testedTeam, size+1,(double)gameLeftForPair,0.0);
			FlowEdge e2 = new FlowEdge(size+i, pair.u,pos,0.0);
			FlowEdge e3 = new FlowEdge(size+i, pair.v,pos,0.0);
			FNw1.addEdge(e1);
			FNw1.addEdge(e2);
			FNw1.addEdge(e3);
			
			
			if(!edge[pair.u])
			{
				FlowEdge e4= new FlowEdge(pair.u, elim,(double)(win-table[pair.u][1]),0.0);
				FNw1.addEdge(e4);
				edge[pair.u] = true;
			}
			if(!edge[pair.v])
			{
				FlowEdge e5= new FlowEdge(pair.v, elim,(double)(win-table[pair.v][1]),0.0);
				FNw1.addEdge(e5);
				edge[pair.v] = true;
			}
		}
		FordFulkerson FFs = new FordFulkerson(FNw1,testedTeam,elim);
		double value = FFs.value();
		if(value<(double)gamesLeft)
		{
			if(!eliminated.contains(teams[testedTeam]))
				eliminated.add(teams[testedTeam]);
		}
	}
	public static class couple<T>{
		T u, v;
		couple(T u, T v)
		{
			this.u = u;
			this.v = v;
					
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
		}
		else{
			s = new Scanner(System.in);
			System.out.printf("Reading input values from stdin.\n");
		}
		
		BaseballElimination be = new BaseballElimination();	
		be.BaseballElimination(s);
		
		if (be.eliminated.size() == 0)
			System.out.println("No teams have been eliminated.");
		else
			System.out.println("Teams eliminated: " + be.eliminated);
	}
}
