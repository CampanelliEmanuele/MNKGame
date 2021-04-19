package mnkgame;

import java.lang.Math;
import java.util.Random;

public class GroupPlayer implements MNKPlayer {

	private static MNKBoard B;

	private MNKGameState myWin;

	private MNKGameState yourWin;
	private int TIMEOUT;
	private Random rand;
	private static boolean first;
	//private MNKCellState botState;
	private int initialDepthLimit = 1;
	private static int M;
	private static int N;
	private static int K;

	public GroupPlayer () {}

	public void initPlayer (int in_M, int in_N, int in_K, boolean in_first, int timeout_in_sec) {
		rand = new Random (System.currentTimeMillis());
		B = new MNKBoard (in_M, in_N, in_K);
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
		/*
		if (MC.length <= 2) {
			if (first && FC.length == M * N) {
				MNKCell tmp = new MNKCell (0, 0, MNKCellState.FREE);
				return tmp;
				//return MNKCell (0, 0, MNKCellState.FREE);									// Se siamo i primi a giocare si marca un angolo
			}
			else {		// Se siamo il secondo a giocare
					if (M == N && M % 2 == 0) {		// 44K - 66K
						// L'avversario marca un angolo e noi marchiamo il centro
						// Prima mossa avversaria --> (0,0) || (M-1,N-1) --> Marchiamo (M/2, N/2 - 1)
						if ((MC[0].i == 0 && MC[0].j == 0) || (MC[0].i == M-1 && MC[0].j == N-1)) return MNKCell ((int) M/2, (int) N/2 - 1, MNKCellState.FREE);		// 2' + G
						// Prima mossa avversaria --> (0,N-1) || (M-1,0) --> Marchiamo (M/2, N/2)
						else if ((MC[0].i == 0 && MC[0].j == N-1) || (MC[0].i == M-1 && MC[0].j == 0)) return MNKCell ((int) M/2, (int) N/2, MNKCellState.FREE);	// 2' + G
						else {		// 2' + N
							if ((MC[0].i != (int) M/2 && MC[0].j != (int) N/2 - 1)) return MNKCell ((int) M/2, (int) N/2 - 1, MNKCellState.FREE);	// Se non ha marcato la cella in basso a sx del quadratino centrale, marcala
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
		*/
		if (MC.length <= 2) {
			if (first && FC.length == M * N) {
				MNKCell tmp = new MNKCell (0, 0, MNKCellState.FREE);
				return tmp;
			}
			else {		// Se siamo il secondo a giocare
					if (M == N && M % 2 == 0) {		// 44K - 66K
						// L'avversario marca un angolo e noi marchiamo il centro
						// Prima mossa avversaria --> (0,0) || (M-1,N-1) --> Marchiamo (M/2, N/2 - 1)
						if ((MC[0].i == 0 && MC[0].j == 0) || (MC[0].i == M-1 && MC[0].j == N-1)) {
							MNKCell tmp = new MNKCell ((int) M/2, (int) N/2 - 1, MNKCellState.FREE);		// 2' + G
							return tmp;
						}
						// Prima mossa avversaria --> (0,N-1) || (M-1,0) --> Marchiamo (M/2, N/2)
						else if ((MC[0].i == 0 && MC[0].j == N-1) || (MC[0].i == M-1 && MC[0].j == 0)) {
							MNKCell tmp = new MNKCell ((int) M/2, (int) N/2, MNKCellState.FREE);	// 2' + G
							return tmp;
						}
						else {		// 2' + N
							if ((MC[0].i != (int) M/2 && MC[0].j != (int) N/2 - 1)) {
								MNKCell tmp = new MNKCell ((int) M/2, (int) N/2 - 1, MNKCellState.FREE);	// Se non ha marcato la cella in basso a sx del quadratino centrale, marcala
								return tmp;
							}
							else {
								MNKCell tmp = new MNKCell ((int) M/2, (int) N/2 - 1, MNKCellState.FREE);	// Altrimenti marca quella affianco
								return tmp;
							}
						}
					} else {//else if (M == N && M % 2 == 1) {	// 33K - 55K + 34K - 62K
						// Se l'avversaio non ha marcato il centro, lo marchiamo noi
						if (MC[0].i != (int) M/2 && MC[0].j != (int) N/2) {
							MNKCell tmp = new MNKCell ((int) M/2, (int) N/2, MNKCellState.FREE);		// 2' + G
							return tmp;
						}
						// Se invece l'avversario ha marcato il centro, noi marchiam un angolo
						else {
							MNKCell tmp = new MNKCell (0, 0, MNKCellState.FREE);		// 2' + N
							return tmp;
						}

					}
			}
		}
		// FIne if

		else {	// Se si è oltre il secondo turno

				return FC[0];

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

	// 5

	// Deve ritornare "la radice / head" o funziona in automatico?

	public static void solve3 (TreeNode in_padre, MNKBoard in_B, int in_depthLimit) {
		if (in_depthLimit > 1) {				// Se in_depthLimit > 1 --> Si crea un'altro livello
			System.out.println("Stato: generazione - Livello: " + (5 - in_depthLimit) + " - Local B: " + in_B);

			MNKCell[] FC = in_padre.getMNKBoard().getFreeCells();
			MNKCell[] MC = in_padre.getMNKBoard().getMarkedCells();
			MNKBoard tmpB = new MNKBoard (M,N,K);
			for (int e = 0; e < MC.length; e++) {
				tmpB.markCell (MC[e].i, MC[e].j);
			}

			while (in_padre != null) {											// Per ogni fratello (e padre compreso) si crea il sottoalbero
				tmpB.markCell (FC[0].i, FC[0].j);		  				// Temporaneo marcamento della prima cella
				TreeNode primoFiglio = new TreeNode (tmpB, in_padre, true, null, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);		// Si crea il primo figlio
				in_padre.setPrimoFiglio(primoFiglio);					// Si setta il primo figlio del nodo padre

				solve3 (primoFiglio, tmpB, in_depthLimit - 1);
				//tmpB.unmarkCell ();													// Si smarca la prima cella
				TreeNode prev = primoFiglio;									// Prev creato uguale al primoFiglio

				for (int e = 1; e < FC.length; e++) {					// Ciclo per la creazione dei nodi di un livello
					MNKBoard tmp2B = new MNKBoard (M,N,K);			// Crea una nuova board per ogni nodo del livello in questione
					for (int el = 0; el < MC.length; el++) {
						tmp2B.markCell (MC[el].i, MC[el].j);
					}

					tmp2B.markCell (FC[e].i, FC[e].j);					// Temporaneo marcamento della cella
					TreeNode figlio = new TreeNode (tmp2B, in_padre, false, prev, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
					prev.setNext (figlio);											// Il fratello prev è ora collegato al suo nuovo fratello

					prev = figlio;															// Il nuovo figlio è ora il prev (ovvero l'ultimo figlio creato)
					solve3 (figlio, tmp2B, in_depthLimit - 1);
					//tmp2B.unmarkCell ();												// Si smarca la cella in questione
				}
				in_padre = in_padre.getNext();
			}
		}

	}






	public static void solve4 (TreeNode in_padre, MNKBoard in_B, int in_depthLimit) {
		if (in_depthLimit > 1 && in_padre.getMNKBoard().gameState() == MNKGameState.OPEN) {				// Se in_depthLimit > 1 --> Si crea un'altro livello
			System.out.println("Stato: generazione - Livello: " + (5 - in_depthLimit) + " - Local B: " + in_B);

			MNKCell[] FC = in_padre.getMNKBoard().getFreeCells();
			MNKCell[] MC = in_padre.getMNKBoard().getMarkedCells();
			//MNKBoard tmpB = in_B;
			MNKBoard tmpB = new MNKBoard (M,N,K);
			for (int e = 0; e < MC.length; e++) {
				//if (tmpB.gameState() != MNKGameState.OPEN) continue;
				tmpB.markCell (MC[e].i, MC[e].j);
			}

			System.out.println("Local B: " + in_B);

			while (in_padre != null) {											// Per ogni fratello (e padre compreso) si crea il sottoalbero
				//if (tmpB.gameState() != MNKGameState.OPEN) continue;
				tmpB.markCell (FC[0].i, FC[0].j);		  				// Temporaneo marcamento della prima cella
				TreeNode primoFiglio = new TreeNode (tmpB, in_padre, true, null, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);		// Si crea il primo figlio
				in_padre.setPrimoFiglio(primoFiglio);					// Si setta il primo figlio del nodo padre

				solve4 (primoFiglio, tmpB, in_depthLimit - 1);
				//tmpB.unmarkCell ();													// Si smarca la prima cella
				TreeNode prev = primoFiglio;									// Prev creato uguale al primoFiglio

				for (int e = 1; e < FC.length; e++) {					// Ciclo per la creazione dei nodi di un livello
					MNKBoard tmp2B = new MNKBoard (M,N,K);			// Crea una nuova board per ogni nodo del livello in questione
					for (int el = 0; el < MC.length; el++) {
						//if (tmp2B.gameState() != MNKGameState.OPEN) continue;
						tmp2B.markCell (MC[el].i, MC[el].j);
					}

				//	if (tmp2B.gameState() != MNKGameState.OPEN) continue;
					tmp2B.markCell (FC[e].i, FC[e].j);					// Temporaneo marcamento della cella
					TreeNode figlio = new TreeNode (tmp2B, in_padre, false, prev, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
					prev.setNext (figlio);											// Il fratello prev è ora collegato al suo nuovo fratello

					prev = figlio;															// Il nuovo figlio è ora il prev (ovvero l'ultimo figlio creato)
					solve4 (figlio, tmp2B, in_depthLimit - 1);
					//tmp2B.unmarkCell ();												// Si smarca la cella in questione
				}

				in_padre = in_padre.getNext();
			}
		}

	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// 6

	// Stampa le informazioni di ogni nodo dell'albero
	public static void printSolve (TreeNode in_padre, int in_level) {		// in_level rappresenta il livello del nodo in_padre
		if (in_padre != null) {
			while (in_padre != null) {								// Per ogni fratello (e padre compreso) si crea il sottoalbero
				System.out.println ("------------------------------------------");
				if (in_padre.getMNKBoard().gameState() != MNKGameState.OPEN) {
					System.out.println ("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
					System.out.println ("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
				}
				System.out.println ("LIVELLO: " + in_level);
				System.out.println ("NIQ: " + in_padre);
				in_padre.printNodeInfo();
				in_padre.printMCInfo();
				System.out.println ("------------------------------------------");
				if (in_padre.getPrimoFiglio() != null) printSolve (in_padre.getPrimoFiglio(), in_level + 1);
				in_padre = in_padre.getNext();
			}
		}
		System.out.println ("Nodo null");
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static void main (String[] args) {
		System.out.println("È partito!");

		M = 3;
		N = 3;
		K = 3;
		B = new MNKBoard (M,N,K);
		MNKCell[] FC = B.getFreeCells();
		B.markCell (FC[0].i, FC[0].j);
		B.markCell (FC[1].i, FC[1].j);

		/*
		B.markCell (0,0);	// p1
		B.markCell (1,0);
		B.markCell (0,1);	// p1
		B.markCell (2,0);
		B.markCell (0,2);	// p1 --> B.gameState == WINP1

		//B.markCell (2,2);	// p1
		*/

		TreeNode radice = new TreeNode (B, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
		System.out.println("Avvio la creazione dell'albero...");
		System.out.println("Global B: " + B + "\n");
		solve4 (radice, B, 6);

		System.out.println("albero creato!");
		printSolve(radice, 0);		// La radice è in cime all'albero --> ergo livello 0

		/*
		B = new MNKBoard (2,2,2);
		changeBoard (B);
		*/
	}
}
