package mnkgame;

import java.lang.Math;
import java.util.Random;

public class GroupPlayer implements MNKPlayer {

	private MNKBoard B;

	private MNKGameState myWin;

	private MNKGameState yourWin;
	private int TIMEOUT;
	private Random rand;
	private static boolean first;
	//private MNKCellState botState;
	private int M;
	private int N;
	private int K;

	public GroupPlayer() {}

	public void initPlayer (int in_M, int in_N, int in_K, boolean in_first, int timeout_in_sec) {
		rand = new Random (System.currentTimeMillis());
		Board = new MNKBoard (in_M, in_N, in_K);
		myWin = in_first ? MNKGameState.WINP1 : MNKGameState.WINP2; // Vittoria del bot
		yourWin = first ? MNKGameState.WINP2 : MNKGameState.WINP1;  // Vittoria dell'avversario
		M = in_M;
		N = in_N;
		K = in_K;
		first = in_first;
		TIMEOUT = timeout_in_sec;
	}

	/*
	 * Deve ritornare la cella in cui mettere il segno
	 */
	public MNKCell selectCell (MNKCell[] FC, MNKCell[] MC) {
		// if - else per la prima mossa
		if (first && FC.length == M * N) return MNKCell (0, 0, MNKCellState.FREE);	// Se siamo i primi a giocare
 	  else {		// Se siamo il secondo a giocare
				// Prima mossa avversaria --> (0,0) || (M-1,N-1) --> Marchiamo (M/2, N/2 - 1)
				if ((MC[0].i == 0 && MC[0].j == 0) || (MC[0].i == M-1 && MC[0].j == N-1)) return MNKCell ((int) M/2, (int) N/2 - 1, MNKCellState.FREE);
				// Prima mossa avversaria --> (0,N-1) || (M-1,0) --> Marchiamo (M/2, N/2)
				else if ((MC[0].i == 0 && MC[0].j == N-1) || (MC[0].i == M-1 && MC[0].j == 0)) return MNKCell ((int) M/2, (int) N/2, MNKCellState.FREE);
				else {	// Prima mossa avversaria non fatta sugli angoli
					return MNKCell (0, 0, MNKCellState.FREE);
				}
		}




		/*	DA CAPIRE DOVE METTERE
		if (nodoInQuestione.getPrimoFiglio() != null) vaiAlleFoglie (nodoInQuestione.getPrimoFiglio());
		else assegnaValoreABFoglia (nodoInQuestione.getPrimoFiglio());
		*/



	}

	public String playerName () {
		return "Slow_Unmade";				// Lento_Sfatto
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// Per memorizzare i sottoalberi verrà utilizzata una struttura dati dinamica omogena lineare

	// 1
	// Crea un nuovo sotto-albero per ogni cella libera (rappresentazione di ogni possible mossa)
	public void creaSottoAlbero (TreeNode in_radice, int depthLimit) {
		MNKCell[] FC = in_radice.getMNKBoard().getFreeCells();			// Serve per marcare la nuova posizione sulla board
		MNKCell[] MC = in_radice.getMNKBoard().getMarkedCells();		// Serve per marcare le precedenti posizioni sulla board
		int m = in_radice.getMNKBoard().M;
		int n = in_radice.getMNKBoard().N;
		int k = in_radice.getMNKBoard().K;

		for (int x = 0; x < FC.length; x++) {
			MNKBoard newMNKBoard = new MNKBoard(m, n, k);           // Creazione della nuova board
			for (int y = 0; y < MC.length; y++) {                   // Marcamento delle celle precedenti
				newMNKBoard.markCell (MC[y].i, MC[y].j);
			}
			newMNKBoard.markCell (FC[x].i, FC[x].j);                // "Nuovo marcamento"
			TreeNode nuovoSottoAlbero = new TreeNode (newMNKBoard, in_radice, null, null, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE);                         // Creazione del sottoalbero con la nuova board
		}
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////





//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static void main (String[] args) {
		System.out.println("È partito!");
	}

}
