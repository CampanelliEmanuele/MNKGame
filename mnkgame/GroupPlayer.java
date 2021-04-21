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
	private static final int LIMIT = 18;

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
	public static void solve4 (TreeNode in_padre, MNKBoard in_B, int in_depthLimit) {
		if (in_depthLimit > 1 && in_padre.getMNKBoard().gameState() == MNKGameState.OPEN) {				// Se in_depthLimit > 1 --> Si crea un'altro livello
			//System.out.println("Stato: generazione - Livello: " + (5 - in_depthLimit) + " - Local B: " + in_B);

			MNKCell[] FC = in_padre.getMNKBoard().getFreeCells();
			MNKCell[] MC = in_padre.getMNKBoard().getMarkedCells();
			//MNKBoard tmpB = in_B;
			MNKBoard tmpB = new MNKBoard (M,N,K);
			for (int e = 0; e < MC.length; e++) {
				//if (tmpB.gameState() != MNKGameState.OPEN) continue;
				tmpB.markCell (MC[e].i, MC[e].j);
			}

			//System.out.println("Local B: " + in_B);

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

		/* K piccolo: + tempo assegnaValoreABFoglia (controlli diagonali)
									- tempo creazione albero
			 K grande:  - ...
			 						+ ...
			 Preferenza del K piccolo
		*/
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
		*/

		//B.markCell (2,2);	// p1

		TreeNode radice = new TreeNode (B, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
		TreeFunctions tmpTreeFunctions = new TreeFunctions();

		System.out.println("Avvio la creazione dell'albero...");
		System.out.println("Global B: " + B + "\n");
		solve4 (radice, B, 2);

		System.out.println("albero creato!");
		tmpTreeFunctions.assegnaValoreABFoglia(radice, true);
		printSolve(radice, 0);		// La radice è in cime all'albero --> ergo livello 0

		/*
		B = new MNKBoard (2,2,2);
		changeBoard (B);
		*/
	}
}


/*
Java - GroupPlayer.java:116
È partito!
Avvio la creazione dell'albero...
Global B: mnkgame.MNKBoard@452b3a41

albero creato!
AVVIO - FUNZIONE: assegnaValoreABFoglia - Controllo diagonale in esecuzione.
-----------------------------------------------------------------------------
start_i: 0 ; start_j: 0 ; vars[k]: 3
control_i: start_i + vars[k] - 1 = 2
control_j: start_j + vars[k] - 1 = 2
-----------------------------------------------------------------------------
ASX -> BDX: start_i: 0 ; start_j: 0 ; move: 0
ASX -> BDX: start_i: 0 ; start_j: 0 ; move: 1
ASX -> BDX: start_i: 0 ; start_j: 0 ; move: 2
SC ADX: start_i: 0 ; start_j: 0 ; move: 1
AVVIO - FUNZIONE: assegnaValoreABFoglia - Controllo diagonale in esecuzione.
SC ADX: start_i: 1 ; start_j: 0 ; move: 1
------------------------------------------
LIVELLO: 0
NIQ: mnkgame.TreeNode@72ea2f77
------------------------------------------
Padre: null
Board: mnkgame.MNKBoard@452b3a41
Primo figlio: mnkgame.TreeNode@33c7353a
List position: -1
Next: null
Prev: null
alpha: 5
beta: 11
Valore: 0
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
------------------------------------------
------------------------------------------
LIVELLO: 1
NIQ: mnkgame.TreeNode@33c7353a
------------------------------------------
Padre: mnkgame.TreeNode@72ea2f77
Board: mnkgame.MNKBoard@1c20c684
Primo figlio: null
List position: 0
Next: mnkgame.TreeNode@1fb3ebeb
Prev: null
alpha: -2147483648
beta: 2147483647
Valore: 0
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(0,1) : P1
------------------------------------------
------------------------------------------
LIVELLO: 1
NIQ: mnkgame.TreeNode@1fb3ebeb
------------------------------------------
Padre: mnkgame.TreeNode@72ea2f77
Board: mnkgame.MNKBoard@548c4f57
Primo figlio: null
List position: 1
Next: mnkgame.TreeNode@1218025c
Prev: mnkgame.TreeNode@33c7353a
alpha: -2147483648
beta: 2147483647
Valore: 0
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(1,2) : P1
------------------------------------------
------------------------------------------
LIVELLO: 1
NIQ: mnkgame.TreeNode@1218025c
------------------------------------------
Padre: mnkgame.TreeNode@72ea2f77
Board: mnkgame.MNKBoard@816f27d
Primo figlio: null
List position: 2
Next: mnkgame.TreeNode@87aac27
Prev: mnkgame.TreeNode@1fb3ebeb
alpha: -2147483648
beta: 2147483647
Valore: 0
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(2,0) : P1
------------------------------------------
------------------------------------------
LIVELLO: 1
NIQ: mnkgame.TreeNode@87aac27
------------------------------------------
Padre: mnkgame.TreeNode@72ea2f77
Board: mnkgame.MNKBoard@3e3abc88
Primo figlio: null
List position: 3
Next: mnkgame.TreeNode@6ce253f1
Prev: mnkgame.TreeNode@1218025c
alpha: -2147483648
beta: 2147483647
Valore: 0
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(1,0) : P1
------------------------------------------
------------------------------------------
LIVELLO: 1
NIQ: mnkgame.TreeNode@6ce253f1
------------------------------------------
Padre: mnkgame.TreeNode@72ea2f77
Board: mnkgame.MNKBoard@53d8d10a
Primo figlio: null
List position: 4
Next: mnkgame.TreeNode@e9e54c2
Prev: mnkgame.TreeNode@87aac27
alpha: -2147483648
beta: 2147483647
Valore: 0
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(2,2) : P1
------------------------------------------
------------------------------------------
LIVELLO: 1
NIQ: mnkgame.TreeNode@e9e54c2
------------------------------------------
Padre: mnkgame.TreeNode@72ea2f77
Board: mnkgame.MNKBoard@65ab7765
Primo figlio: null
List position: 5
Next: mnkgame.TreeNode@1b28cdfa
Prev: mnkgame.TreeNode@6ce253f1
alpha: -2147483648
beta: 2147483647
Valore: 0
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(0,0) : P1
------------------------------------------
------------------------------------------
LIVELLO: 1
NIQ: mnkgame.TreeNode@1b28cdfa
------------------------------------------
Padre: mnkgame.TreeNode@72ea2f77
Board: mnkgame.MNKBoard@eed1f14
Primo figlio: null
List position: 6
Next: null
Prev: mnkgame.TreeNode@e9e54c2
alpha: -2147483648
beta: 2147483647
Valore: 0
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(0,2) : P1
------------------------------------------
Nodo null
Nodo null
[Finished in 1.555s]
superf4brizio-VirtualBox superf4brizio 01:00:03 am
*/
