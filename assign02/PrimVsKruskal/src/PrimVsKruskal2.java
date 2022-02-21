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
 import java.util.*;
 import java.io.File;

//Do not change the name of the PrimVsKruskal class
public class PrimVsKruskal2{

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

		/* Build the MST by Prim's and the MST by Kruskal's */
		/* (You may add extra methods if necessary) */
		
		/* ... Your code here ... */
		double weight;
		EdgeWeightedGraph graph = new EdgeWeightedGraph(n);
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n;j++) {
				if(i != j && G[i][j] != 0) {
					weight = G[i][j];
					Edge newEdge = new Edge(i, j, weight);
					graph.addEdge(newEdge);
				}
			}
		}
		//Implement PrimMST 
		PrimMST newPrimMST = new PrimMST(graph);
		List<Edge> primlist = new ArrayList<Edge>();
		for(Edge e: newPrimMST.edges()) {
			StdOut.println(e);
			primlist.add(e);
		}
		//print out the weight of prim
		StdOut.printf("%.5f\n", newPrimMST.weight());
		
		//Implement KruskalMST
		KruskalMST newkruskalmst = new KruskalMST(graph);
		List<Edge> kruskallist = new ArrayList<Edge>();
		for(Edge e: newkruskalmst.edges()) {
			StdOut.println(e);
			kruskallist.add(e);
			//StdOut.println(kruskallist+"***");
		}
		StdOut.printf("%.5f\n", newkruskalmst.weight());
		
		boolean pvk = true;
		/* ... Your code here ... */
		if(newPrimMST.weight() != newkruskalmst.weight()) {
			pvk = false;
		}
		
		//compare
		for(int i = 0; i < primlist.size(); i++){
			Edge primEdge = primlist.get(i);
			int primVertex1 = primEdge.either();
			int primVertex2 = primEdge.other(primVertex1);
			for(int j = 0; j < primlist.size(); j++){
				Edge kruskalEdge = kruskallist.get(j);
				int kruskalVertex1 = kruskalEdge.either();
				int kruskalVertex2 = kruskalEdge.other(kruskalVertex1);
				if ((primVertex1 == kruskalVertex1 && primVertex2 == kruskalVertex2)
					|| (primVertex1 == kruskalVertex2 && primVertex2 == kruskalVertex1)) {
				
					break;
				}	 
				else if(j == primlist.size() - 1){
					return pvk = false;
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
