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
	private int initialDepthLimit = 1;
	private int M;
	private int N;
	private int K;

	public GroupPlayer () {}

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
		if (MC.length <= 2) {
			if (first && FC.length == M * N) return MNKCell (0, 0, MNKCellState.FREE);	// Se siamo i primi a giocare si marca un angolo
	 	  else {		// Se siamo il secondo a giocare
					if (M == N && M % 2 == 0) {		// 44K - 66K
						// L'avversario marca un angolo e noi marchiamo il centro
						// Prima mossa avversaria --> (0,0) || (M-1,N-1) --> Marchiamo (M/2, N/2 - 1)
						if ((MC[0].i == 0 && MC[0].j == 0) || (MC[0].i == M-1 && MC[0].j == N-1)) return MNKCell ((int) M/2, (int) N/2 - 1, MNKCellState.FREE);		// 2' + G
						// Prima mossa avversaria --> (0,N-1) || (M-1,0) --> Marchiamo (M/2, N/2)
						else if ((MC[0].i == 0 && MC[0].j == N-1) || (MC[0].i == M-1 && MC[0].j == 0)) return MNKCell ((int) M/2, (int) N/2, MNKCellState.FREE);	// 2' + G
						else {		// 2' + N
							if ((MC[0].i != (int) M/2 && MC[0].j != (int) N/2 - 1))) return MNKCell ((int) M/2, (int) N/2 - 1, MNKCellState.FREE);	// Se non ha marcato la cella in basso a sx del quadratino centrale, marcala
							else return MNKCell ((int) M/2, (int) N/2 - 1, MNKCellState.FREE);	// Altrimenti marca quella affianco
						}
					} else {//else if (M == N && M % 2 == 1) {	// 33K - 55K + 34K - 62K
						// Se l'avversaio non ha marcato il centro, lo marchiamo noi
						if (MC[0].i != (int) M/2 && MC[0].j != (int) N/2) return MNKCell ((int) M/2, (int) N/2, MNKCellState.FREE);		// 2' + G
						// Se invece l'avversario ha marcato il centro, noi marchiam un angolo
						else return MNKCell (0, 0, MNKCellState.FREE);		// 2' + N
					}
			}
		}
		// FIne if

		else {	// Se si è oltre il secondo turno
			TreeNode radice = new TreeNode (this.B, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
			boolean oneShot = true;

			for (int depth = 0; depth < initialDepthLimit; depth++) {
				for (int times = 0; times < FC.length; times++) {

					if (oneShot) {
						TreeNode primoFiglio = new TreeNode (this.B, radice, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
						oneShot = false;
					}
					TreeNode figlio = new TreeNode (this.B, radice, null, primoFiglio, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
				}
			}

		}
		// Fine else


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

	public static void main (String[] args) {
		System.out.println("È partito!");
	}

}
