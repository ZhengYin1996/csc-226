import edu.princeton.cs.algs4.*;

public class DFA_Construction {

	public static void main(String[] args) {
		String pat = "AACAAAB";
		int R = 3;
		int M = pat.length();
		
		int[][] dfa = new int[R][M];
		
		dfa[pat.charAt(0)-65][0] = 1;
		for (int x = 0, j = 1; j < M; ++j) {
			for (int c = 0; c < R; ++c) {
				dfa[c][j] = dfa[c][x];
			}
			dfa[pat.charAt(j)-65][j] = j + 1;
			x = dfa[pat.charAt(j)-65][x];
		}
		
		StdOut.println("   0 1 2 3 4 5 6");
		StdOut.print("A: ");
		for (int i = 0; i < M; ++i) {
			StdOut.print(dfa['A'-65][i] + " ");
		}
		StdOut.println();
		StdOut.print("B: ");
		for (int i = 0; i < M; ++i) {
			StdOut.print(dfa['B'-65][i] + " ");
		}
		StdOut.println();
		StdOut.print("C: ");
		for (int i = 0; i < M; ++i) {
			StdOut.print(dfa['C'-65][i] + " ");
		}
		
		pat = "ABABABAB";
		M = pat.length();
		R = 2;
		
		dfa = new int[R][M];
		
		dfa[pat.charAt(0)-65][0] = 1;
		for (int x = 0, j = 1; j < M; ++j) {
			for (int c = 0; c < R; ++c) {
				dfa[c][j] = dfa[c][x];
			}
			dfa[pat.charAt(j)-65][j] = j + 1;
			x = dfa[pat.charAt(j)-65][x];
		}
		
		StdOut.println();
		StdOut.println("**************");
		StdOut.println();
		StdOut.println("   0 1 2 3 4 5 6 7");
		StdOut.print("A: ");
		for (int i = 0; i < M; ++i) {
			StdOut.print(dfa['A'-65][i] + " ");
		}
		StdOut.println();
		StdOut.print("B: ");
		for (int i = 0; i < M; ++i) {
			StdOut.print(dfa['B'-65][i] + " ");
		}

	}

}
