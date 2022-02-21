/* PrimVsKruskal.java
   CSC 226 - Spring 2019
   Assignment 2 - Prim MST versus Kruskal MST Template

   The file includes the "import edu.princeton.cs.algs4.*;" so that yo can use
   any of the code in the algs4.jar file. You should be able to compile your program
   with the command

	javac -cp .;algs4.jar PrimVsKruskal.java

   To conveniently test the algorithm with a large input, create a text file
   containing a test graphs (in the format described below) and run
   the program with

	java -cp .;algs4.jar PrimVsKruskal file.txt

   where file.txt is replaced by the name of the text file.

   The input consists of a graph (as an adjacency matrix) in the following format:

    <number of vertices>
	<adjacency matrix row 1>
	...
	<adjacency matrix row n>

   Entry G[i][j] >= 0.0 of the adjacency matrix gives the weight (as type double) of the edge from
   vertex i to vertex j (if G[i][j] is 0.0, then the edge does not exist).
   Note that since the graph is undirected, it is assumed that G[i][j]
   is always equal to G[j][i].


   R. Little - 03/07/2019
*/

 import edu.princeton.cs.algs4.*;
 import java.util.Scanner;
 import java.io.File;
 import java.util.Iterator;
 import java.util.NoSuchElementException;
 import java.util.Arrays;


//Do not change the name of the PrimVsKruskal class
public class PrimVsKruskal{

	/* PrimVsKruskal(G)
		Given an adjacency matrix for connected graph G, with no self-loops or parallel edges,
		determine if the minimum spanning tree of G found by Prim's algorithm is equal to
		the minimum spanning tree of G found by Kruskal's algorithm.

		If G[i][j] == 0.0, there is no edge between vertex i and vertex j
		If G[i][j] > 0.0, there is an edge between vertices i and j, and the
		value of G[i][j] gives the weight of the edge.
		No entries of G will be negative.
	*/
	static boolean PrimVsKruskal(double[][] G){
		int n = G.length;
    //convert G to edge EdgeWeightedGraph
    EdgeWeightedGraph Graph = new EdgeWeightedGraph(n*n);
    Edge e;
    /*
    for(int i=0; i<n; i++){
      for(int j=0; j<n; j++){
    */
    for(int i=n-1; i>=0; i--){
      for(int j=n-1; j>=0; j--){
        //convert entry to edge
        e = new Edge(i, j, G[i][j]);
        //add edge
        if(G[i][j]>0)
          Graph.addEdge(e);
      }
    }

    KruskalMST k = new KruskalMST(Graph);
    PrimMST p = new PrimMST(Graph);

    //Iterable<Edge> pEdges = p.edges();
    //Iterable<Edge> kEdges = k.edges();

    double[][] kArray = new double[n][n];
    //System.out.println("Printing Kruskal");
    for (Edge Kedg : k.edges()) {
      kArray[Kedg.either()][Kedg.other(Kedg.either())] = Kedg.weight();
      //StdOut.println(Kedg);
    }

    double[][] pArray = new double[n][n];
    //System.out.println("Printing Prim");
    for (Edge Pedg : p.edges()) {
      pArray[Pedg.either()][Pedg.other(Pedg.either())] = Pedg.weight();
      //StdOut.println(Pedg);
    }

    boolean pvk = true;

    //System.out.println(Arrays.deepToString(kArray));
    //System.out.printf("\n");
    //System.out.println(Arrays.deepToString(pArray));

    for(int i=0; i<n; i++){
      for(int j=0;j<n;j++){
        if(pArray[i][j]==0){
          if(pArray[j][i]!=0)
            continue;
          else if(kArray[i][j]>0 || kArray[j][i]>0){
            pvk = false;
            System.out.println("It's breaking in the 0 condition at "+i+" "+j);
            System.out.println("kArray[i][j]="+kArray[i][j]+", kArray[j][i]="+kArray[j][i]);
            break;
          }
        else{
          if(kArray[i][j]!=pArray[i][j] && kArray[j][i]!=pArray[i][j] ){
            pvk = false;
            System.out.println("It's breaking in the not-equal condition at "+i+" "+j);
            break;
          }
        }
        if(pvk == false)
          break;
        }
      }
    }


		return pvk;
	}

	/* main()
	   Contains code to test the PrimVsKruskal function. You may modify the
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

		int n = s.nextInt();
		double[][] G = new double[n][n];
		int valuesRead = 0;
		for (int i = 0; i < n && s.hasNextDouble(); i++){
			for (int j = 0; j < n && s.hasNextDouble(); j++){
				G[i][j] = s.nextDouble();
				if (i == j && G[i][j] != 0.0) {
					System.out.printf("Adjacency matrix contains self-loops.\n");
					return;
				}
				if (G[i][j] < 0.0) {
					System.out.printf("Adjacency matrix contains negative values.\n");
					return;
				}
				if (j < i && G[i][j] != G[j][i]) {
					System.out.printf("Adjacency matrix is not symmetric.\n");
					return;
				}
				valuesRead++;
			}
		}

		if (valuesRead < n*n){
			System.out.printf("Adjacency matrix for the graph contains too few values.\n");
			return;
		}
        boolean pvk = PrimVsKruskal(G);
        System.out.printf("Does Prim MST = Kruskal MST? %b\n", pvk);
    }
}
