/* BaseballElimination.java
   CSC 226 - Spring 2019
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


    public BaseballElimination(Scanner s){

        String n = s.next();
        int nn = Integer.parseInt(n);           //nn is the number of whole teams

        //creat string map first
        String[][] map = new String[nn][nn+3];
        int numLine = 0;
        int numWord = 0;
        while(s.hasNext()){
            map[numLine][numWord] = s.next();
            numWord++;
            if(numWord == nn + 3){
                numLine++;
                numWord = 0;
            }
        }

        //creat int map1
        int[][] map1 = new int[nn][nn+2];
        for(int i = 0; i < nn; i++){
            for(int j = 0; j< nn+2; j++){
                map1[i][j] = Integer.parseInt(map[i][j+1]);
            }
        }


        int limit = 2;
        int condi = 1;
        while(limit < nn-1){
            condi = condi + limit;
            limit++;
        }

        //calculate the number of vertices and edges
        int vertices = condi + nn + 1;
        int edges = condi*3 + nn - 1;

        //creat int map2
        int[][] map2 = new int[nn][nn];
        for(int i = 0; i < nn; i++){
            for(int j = 0; j < nn; j++){
                map2[i][j] = map1[i][j+2];
            }
        }

        //creat each team's network
        FlowNetwork net = new FlowNetwork(vertices);
        for(int i = 0; i < nn; i++){
            net = new FlowNetwork(vertices);//there nn nets
            int vL = 1; 									   //vL is left vertices' number
            int vR = condi + 1;							   //vR is right vertices's number
            int sumWins = map1[i][0] + map1[i][1];			   //team(i) max wins
            int another = 0;
            String teamName = map[i][0];
            int sumOfCap = 0;
            FlowEdge e;

            for(int j = 0; j < nn; j++){ 			//vertex(j,another)
                if(j != i){							//j should not equal i
                    //creat edges from VR to t
                    int Wins = map1[j][0];
                    int mini = sumWins - Wins;
                    if(mini >= 0){
                        //creat edges from s to VL
                        //creat edges from VL to VR
                        another = j + 1;//another team number bigger than team j
                        while(another < nn){//another != i
                            if(another != i){
                                e = new FlowEdge(0,vL,map2[j][another]);
                                sumOfCap += map2[j][another];
                                net.addEdge(e);
                                if((j < i) && (another < i)){
                                    e = new FlowEdge(vL,j+condi+1,Double.POSITIVE_INFINITY);
                                    net.addEdge(e);
                                    e = new FlowEdge(vL,another+condi+1,Double.POSITIVE_INFINITY);
                                    net.addEdge(e);
                                }
                                if((j < i) && (another > i)){
                                    e = new FlowEdge(vL,j+condi+1,Double.POSITIVE_INFINITY);
                                    net.addEdge(e);
                                    e = new FlowEdge(vL,another+condi,Double.POSITIVE_INFINITY);
                                    net.addEdge(e);
                                }
                                if((j > i) && (another > i)){
                                    e = new FlowEdge(vL,j+condi,Double.POSITIVE_INFINITY);
                                    net.addEdge(e);
                                    e = new FlowEdge(vL,another+condi,Double.POSITIVE_INFINITY);
                                    net.addEdge(e);
                                }
                                vL++;
                            }
                            another++;
                        }
                        e = new FlowEdge(vR, vertices-1, sumWins-Wins);
                        net.addEdge(e);
                        vR++;
                    }
                    else{
                        eliminated.add(teamName);
                    }
                }
            }
            FordFulkerson net1 = new FordFulkerson(net,0,nn-1);
            if(net1.value() != sumOfCap){
                eliminated.add(teamName);
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

        ////////////////////////////////////////////////////////////////////////////////////////



        //test
		/*
		String n = s.next();
		int nn = Integer.parseInt(n);
		System.out.println("nn = " + nn);
		String[][] map = new String[nn][nn+3];
		int numLine = 0;
		int numWord = 0;
		while(s.hasNext()){
			map[numLine][numWord] = s.next();
			numWord++;
			if(numWord == nn + 3){
				numLine++;
				numWord = 0;
			}
		}

		System.out.print("[");
		for(int i = 0; i < nn; i++){
			if(i != nn-1){
				System.out.print(map[i][0] + ",");
			}
			else{
				System.out.print(map[i][0]);
			}
		}
		System.out.print("]");
		*/



        BaseballElimination be = new BaseballElimination(s);

        if (be.eliminated.size() == 0)
            System.out.println("No teams have been eliminated.");
        else
            System.out.println("Teams eliminated: " + be.eliminated);



    }
}
