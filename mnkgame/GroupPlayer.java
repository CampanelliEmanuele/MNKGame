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
		if (in_depthLimit > 1) {
			if (in_padre.getMNKBoard().gameState() == MNKGameState.OPEN) {				// Se in_depthLimit > 1 --> Si crea un'altro livello
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
					TreeNode primoFiglio = new TreeNode (tmpB, in_padre, true, null);		// Si crea il primo figlio
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
						TreeNode figlio = new TreeNode (tmp2B, in_padre, false, prev);
						prev.setNext (figlio);											// Il fratello prev è ora collegato al suo nuovo fratello

						prev = figlio;															// Il nuovo figlio è ora il prev (ovvero l'ultimo figlio creato)
						solve4 (figlio, tmp2B, in_depthLimit - 1);
						//tmp2B.unmarkCell ();												// Si smarca la cella in questione
					}

					in_padre = in_padre.getNext();
				}

			}
			// Fine if
			else if (in_padre.getMNKBoard().gameState() == MNKGameState.WINP1) {
				if (first) in_padre.setColor(Colors.GREEN);
				else in_padre.setColor(Colors.RED);
			}
			else if (in_padre.getMNKBoard().gameState() == MNKGameState.WINP2) {
				if (first) in_padre.setColor(Colors.RED);
				else in_padre.setColor(Colors.GREEN);
			}
			else in_padre.setColor(Colors.GREY);
		}

	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// 6

	public static void printInfo (TreeNode in_node, int in_level) {
		System.out.println ("------------------------------------------");
		if (in_node.getMNKBoard().gameState() != MNKGameState.OPEN) {
			System.out.println ("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		}
		System.out.println ("LIVELLO: " + in_level);
		System.out.println ("NIQ: " + in_node);
		in_node.printNodeInfo();
		in_node.printMCInfo();
		System.out.println ("------------------------------------------");
	}

	// Stampa le informazioni di ogni nodo dell'albero
	public static void printSolve (TreeNode in_padre, int in_level) {		// in_level rappresenta il livello del nodo in_padre
		if (in_padre != null) {
			while (in_padre != null) {								// Per ogni fratello (e padre compreso) si crea il sottoalbero
			  printInfo (in_padre, in_level);
				if (in_padre.getPrimoFiglio() != null) printSolve (in_padre.getPrimoFiglio(), in_level + 1);
				in_padre = in_padre.getNext();
			}
		}
		System.out.println ("Nodo null");
	}

	/**
	 * Stampa foglie: 								printSolve2 (Nodo, true,  node_level, n)   --> n
	 * Stampa albero intero: 					printSolve2 (Node, false, node_level, n)   --> n < 1
	 * Stampa albero primi n-livelli: printSolve2 (Node, false, node_level, n)   --> n >= 1
	 */
	public static void printSolve2 (TreeNode in_padre, boolean in_onlyLeaf, int in_level, int in_limit) {		// in_level rappresenta il livello del nodo in_padre
		if (in_padre != null) {												// Se si passa un nodo
			if (in_onlyLeaf) {													// Se si vuole stampare solo le foglie
				while (in_padre != null) {								// Per ogni fratello (e padre compreso) si richiama la funzione
					if (in_padre.getPrimoFiglio() == null) printInfo (in_padre, in_level); // Se è una foglia fa la stampa
					if (in_padre.getPrimoFiglio() != null) printSolve2 (in_padre.getPrimoFiglio(), true, in_level + 1, -1);	// Si richiama
					in_padre = in_padre.getNext();
				}
			}
			// end if in_onlyLeaf
			else {										// Se non si vuole stampare solo le foglie ma bensì tutto/parte dell'albero
				if (in_limit >= 1) {		// Caso in cui si  vuole stampare solo fino ad un certo livello in_limit
					while (in_padre != null) {
						printInfo (in_padre, in_level);
						if (in_padre.getPrimoFiglio() != null) printSolve2 (in_padre.getPrimoFiglio(), false, in_level + 1, in_limit - 1);
						in_padre = in_padre.getNext();
					}
				} else {								// Se in_limit <= 0 (ovvero se si vuole stamapre tutto l'albero)
					while (in_padre != null) {
						printInfo (in_padre, in_level);
						if (in_padre.getPrimoFiglio() != null) printSolve2 (in_padre.getPrimoFiglio(), false, in_level + 1, -1);
						in_padre = in_padre.getNext();
					}
				}
			}
			// end else in_onlyLeaf
		}

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
		B.markCell (0,2);	// p1
		B.markCell (0,0);
		B.markCell (0,1);	// p1
		B.markCell (2,0);
		B.markCell (0,2);	// p1 --> B.gameState == WINP1
		*/

		//B.markCell (2,2);	// p1

		TreeNode radice = new TreeNode (B);
		TreeFunctions tmpTreeFunctions = new TreeFunctions();
		Algoritms algoritms = new Algoritms(first);

		System.out.println("Avvio la creazione dell'albero...");
		System.out.println("Global B: " + B + "\n");
		solve4 (radice, B, 3);

		System.out.println("albero creato!");
		tmpTreeFunctions.vaiAlleFoglie(radice, true);
		//printSolve2(radice, false, 0, -1);		// La radice è in cime all'albero --> ergo livello 0
		System.out.println("");
		System.out.println("");
		System.out.println("");


		// Funzione da applicare solo alle foglie primoFiglio !!!
		algoritms.bigSolve (radice, true);		// Si passa true perchè è il nostro turno nel nodo radice
		printSolve2(radice, false, 0, -1);		// La radice è in cime all'albero --> ergo livello 0

		//System.out.println(algoritms.sceltaPercorso (true, radice, 2));


		/*
		B = new MNKBoard (2,2,2);
		changeBoard (B);
		*/
	}
}




Java - GroupPlayer.java:228
È partito!
Avvio la creazione dell'albero...
Global B: mnkgame.MNKBoard@4a574795

albero creato!


CASE: myTurn
A: 13 ; B: 29
NODO: B: 29 ; A13
A: 27 ; B: 30
NODO: B: 30 ; A27
A: 13 ; B: 29
NODO: B: 29 ; A13
A: 25 ; B: 30
NODO: B: 30 ; A25
A: 16 ; B: 27
NODO: B: 27 ; A16
A: 16 ; B: 27
NODO: B: 27 ; A16
------------------------------------------
LIVELLO: 0
NIQ: mnkgame.TreeNode@38af3868
------------------------------------------
Padre: null
Board: mnkgame.MNKBoard@4a574795
Primo figlio: mnkgame.TreeNode@77459877
List position: -1
Next: null
Prev: null
alpha: -2147483648
beta: -2147483648
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
------------------------------------------
------------------------------------------
LIVELLO: 1
NIQ: mnkgame.TreeNode@77459877
------------------------------------------
Padre: mnkgame.TreeNode@38af3868
Board: mnkgame.MNKBoard@5b2133b1
Primo figlio: mnkgame.TreeNode@72ea2f77
List position: 0
Next: mnkgame.TreeNode@33c7353a
Prev: null
alpha: 27
beta: -2147483648
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(0,1) : P1
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@72ea2f77
------------------------------------------
Padre: mnkgame.TreeNode@77459877
Board: mnkgame.MNKBoard@681a9515
Primo figlio: null
List position: 0
Next: mnkgame.TreeNode@3af49f1c
Prev: null
alpha: 13
beta: 29
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(0,1) : P1
(1,2) : P2
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@3af49f1c
------------------------------------------
Padre: mnkgame.TreeNode@77459877
Board: mnkgame.MNKBoard@19469ea2
Primo figlio: null
List position: 1
Next: mnkgame.TreeNode@13221655
Prev: mnkgame.TreeNode@72ea2f77
alpha: 27
beta: 30
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(0,1) : P1
(2,0) : P2
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@13221655
------------------------------------------
Padre: mnkgame.TreeNode@77459877
Board: mnkgame.MNKBoard@2f2c9b19
Primo figlio: null
List position: 2
Next: mnkgame.TreeNode@31befd9f
Prev: mnkgame.TreeNode@3af49f1c
alpha: 13
beta: 29
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(0,1) : P1
(1,0) : P2
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@31befd9f
------------------------------------------
Padre: mnkgame.TreeNode@77459877
Board: mnkgame.MNKBoard@1c20c684
Primo figlio: null
List position: 3
Next: mnkgame.TreeNode@1fb3ebeb
Prev: mnkgame.TreeNode@13221655
alpha: 25
beta: 30
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(0,1) : P1
(2,2) : P2
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@1fb3ebeb
------------------------------------------
Padre: mnkgame.TreeNode@77459877
Board: mnkgame.MNKBoard@548c4f57
Primo figlio: null
List position: 4
Next: mnkgame.TreeNode@1218025c
Prev: mnkgame.TreeNode@31befd9f
alpha: 16
beta: 27
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(0,1) : P1
(0,0) : P2
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@1218025c
------------------------------------------
Padre: mnkgame.TreeNode@77459877
Board: mnkgame.MNKBoard@816f27d
Primo figlio: null
List position: 5
Next: null
Prev: mnkgame.TreeNode@1fb3ebeb
alpha: 16
beta: 27
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(0,1) : P1
(0,2) : P2
------------------------------------------
------------------------------------------
LIVELLO: 1
NIQ: mnkgame.TreeNode@33c7353a
------------------------------------------
Padre: mnkgame.TreeNode@38af3868
Board: mnkgame.MNKBoard@87aac27
Primo figlio: mnkgame.TreeNode@3e3abc88
List position: 1
Next: mnkgame.TreeNode@6ce253f1
Prev: mnkgame.TreeNode@77459877
alpha: -2147483648
beta: -2147483648
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(1,2) : P1
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@3e3abc88
------------------------------------------
Padre: mnkgame.TreeNode@33c7353a
Board: mnkgame.MNKBoard@53d8d10a
Primo figlio: null
List position: 0
Next: mnkgame.TreeNode@e9e54c2
Prev: null
alpha: 18
beta: 32
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(1,2) : P1
(0,1) : P2
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@e9e54c2
------------------------------------------
Padre: mnkgame.TreeNode@33c7353a
Board: mnkgame.MNKBoard@65ab7765
Primo figlio: null
List position: 1
Next: mnkgame.TreeNode@1b28cdfa
Prev: mnkgame.TreeNode@3e3abc88
alpha: 29
beta: 32
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(1,2) : P1
(2,0) : P2
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@1b28cdfa
------------------------------------------
Padre: mnkgame.TreeNode@33c7353a
Board: mnkgame.MNKBoard@eed1f14
Primo figlio: null
List position: 2
Next: mnkgame.TreeNode@7229724f
Prev: mnkgame.TreeNode@e9e54c2
alpha: 13
beta: 28
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(1,2) : P1
(1,0) : P2
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@7229724f
------------------------------------------
Padre: mnkgame.TreeNode@33c7353a
Board: mnkgame.MNKBoard@4c873330
Primo figlio: null
List position: 3
Next: mnkgame.TreeNode@119d7047
Prev: mnkgame.TreeNode@1b28cdfa
alpha: 25
beta: 30
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(1,2) : P1
(2,2) : P2
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@119d7047
------------------------------------------
Padre: mnkgame.TreeNode@33c7353a
Board: mnkgame.MNKBoard@776ec8df
Primo figlio: null
List position: 4
Next: mnkgame.TreeNode@4eec7777
Prev: mnkgame.TreeNode@7229724f
alpha: 21
beta: 32
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(1,2) : P1
(0,0) : P2
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@4eec7777
------------------------------------------
Padre: mnkgame.TreeNode@33c7353a
Board: mnkgame.MNKBoard@3b07d329
Primo figlio: null
List position: 5
Next: null
Prev: mnkgame.TreeNode@119d7047
alpha: 19
beta: 30
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(1,2) : P1
(0,2) : P2
------------------------------------------
------------------------------------------
LIVELLO: 1
NIQ: mnkgame.TreeNode@6ce253f1
------------------------------------------
Padre: mnkgame.TreeNode@38af3868
Board: mnkgame.MNKBoard@41629346
Primo figlio: mnkgame.TreeNode@404b9385
List position: 2
Next: mnkgame.TreeNode@6d311334
Prev: mnkgame.TreeNode@33c7353a
alpha: -2147483648
beta: -2147483648
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(2,0) : P1
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@404b9385
------------------------------------------
Padre: mnkgame.TreeNode@6ce253f1
Board: mnkgame.MNKBoard@682a0b20
Primo figlio: null
List position: 0
Next: mnkgame.TreeNode@3d075dc0
Prev: null
alpha: 15
beta: 34
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(2,0) : P1
(0,1) : P2
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@3d075dc0
------------------------------------------
Padre: mnkgame.TreeNode@6ce253f1
Board: mnkgame.MNKBoard@214c265e
Primo figlio: null
List position: 1
Next: mnkgame.TreeNode@448139f0
Prev: mnkgame.TreeNode@404b9385
alpha: 12
beta: 33
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(2,0) : P1
(1,2) : P2
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@448139f0
------------------------------------------
Padre: mnkgame.TreeNode@6ce253f1
Board: mnkgame.MNKBoard@7cca494b
Primo figlio: null
List position: 2
Next: mnkgame.TreeNode@7ba4f24f
Prev: mnkgame.TreeNode@3d075dc0
alpha: 10
beta: 31
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(2,0) : P1
(1,0) : P2
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@7ba4f24f
------------------------------------------
Padre: mnkgame.TreeNode@6ce253f1
Board: mnkgame.MNKBoard@3b9a45b3
Primo figlio: null
List position: 3
Next: mnkgame.TreeNode@7699a589
Prev: mnkgame.TreeNode@448139f0
alpha: 21
beta: 32
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(2,0) : P1
(2,2) : P2
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@7699a589
------------------------------------------
Padre: mnkgame.TreeNode@6ce253f1
Board: mnkgame.MNKBoard@58372a00
Primo figlio: null
List position: 4
Next: mnkgame.TreeNode@4dd8dc3
Prev: mnkgame.TreeNode@7ba4f24f
alpha: 16
beta: 32
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(2,0) : P1
(0,0) : P2
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@4dd8dc3
------------------------------------------
Padre: mnkgame.TreeNode@6ce253f1
Board: mnkgame.MNKBoard@6d03e736
Primo figlio: null
List position: 5
Next: null
Prev: mnkgame.TreeNode@7699a589
alpha: 16
beta: 32
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(2,0) : P1
(0,2) : P2
------------------------------------------
------------------------------------------
LIVELLO: 1
NIQ: mnkgame.TreeNode@6d311334
------------------------------------------
Padre: mnkgame.TreeNode@38af3868
Board: mnkgame.MNKBoard@568db2f2
Primo figlio: mnkgame.TreeNode@378bf509
List position: 3
Next: mnkgame.TreeNode@5fd0d5ae
Prev: mnkgame.TreeNode@6ce253f1
alpha: -2147483648
beta: -2147483648
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(1,0) : P1
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@378bf509
------------------------------------------
Padre: mnkgame.TreeNode@6d311334
Board: mnkgame.MNKBoard@2d98a335
Primo figlio: null
List position: 0
Next: mnkgame.TreeNode@16b98e56
Prev: null
alpha: 18
beta: 34
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(1,0) : P1
(0,1) : P2
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@16b98e56
------------------------------------------
Padre: mnkgame.TreeNode@6d311334
Board: mnkgame.MNKBoard@7ef20235
Primo figlio: null
List position: 1
Next: mnkgame.TreeNode@27d6c5e0
Prev: mnkgame.TreeNode@378bf509
alpha: 13
beta: 28
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(1,0) : P1
(1,2) : P2
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@27d6c5e0
------------------------------------------
Padre: mnkgame.TreeNode@6d311334
Board: mnkgame.MNKBoard@4f3f5b24
Primo figlio: null
List position: 2
Next: mnkgame.TreeNode@15aeb7ab
Prev: mnkgame.TreeNode@16b98e56
alpha: 27
beta: 32
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(1,0) : P1
(2,0) : P2
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@15aeb7ab
------------------------------------------
Padre: mnkgame.TreeNode@6d311334
Board: mnkgame.MNKBoard@7b23ec81
Primo figlio: null
List position: 3
Next: mnkgame.TreeNode@6acbcfc0
Prev: mnkgame.TreeNode@27d6c5e0
alpha: 27
beta: 34
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(1,0) : P1
(2,2) : P2
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@6acbcfc0
------------------------------------------
Padre: mnkgame.TreeNode@6d311334
Board: mnkgame.MNKBoard@5f184fc6
Primo figlio: null
List position: 4
Next: mnkgame.TreeNode@3feba861
Prev: mnkgame.TreeNode@15aeb7ab
alpha: 19
beta: 32
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(1,0) : P1
(0,0) : P2
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@3feba861
------------------------------------------
Padre: mnkgame.TreeNode@6d311334
Board: mnkgame.MNKBoard@5b480cf9
Primo figlio: null
List position: 5
Next: null
Prev: mnkgame.TreeNode@6acbcfc0
alpha: 21
beta: 34
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(1,0) : P1
(0,2) : P2
------------------------------------------
------------------------------------------
LIVELLO: 1
NIQ: mnkgame.TreeNode@5fd0d5ae
------------------------------------------
Padre: mnkgame.TreeNode@38af3868
Board: mnkgame.MNKBoard@6f496d9f
Primo figlio: mnkgame.TreeNode@723279cf
List position: 4
Next: mnkgame.TreeNode@10f87f48
Prev: mnkgame.TreeNode@6d311334
alpha: -2147483648
beta: -2147483648
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(2,2) : P1
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@723279cf
------------------------------------------
Padre: mnkgame.TreeNode@5fd0d5ae
Board: mnkgame.MNKBoard@b4c966a
Primo figlio: null
List position: 0
Next: mnkgame.TreeNode@2f4d3709
Prev: null
alpha: 15
beta: 34
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(2,2) : P1
(0,1) : P2
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@2f4d3709
------------------------------------------
Padre: mnkgame.TreeNode@5fd0d5ae
Board: mnkgame.MNKBoard@4e50df2e
Primo figlio: null
List position: 1
Next: mnkgame.TreeNode@1d81eb93
Prev: mnkgame.TreeNode@723279cf
alpha: 10
beta: 31
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(2,2) : P1
(1,2) : P2
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@1d81eb93
------------------------------------------
Padre: mnkgame.TreeNode@5fd0d5ae
Board: mnkgame.MNKBoard@7291c18f
Primo figlio: null
List position: 2
Next: mnkgame.TreeNode@34a245ab
Prev: mnkgame.TreeNode@2f4d3709
alpha: 21
beta: 32
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(2,2) : P1
(2,0) : P2
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@34a245ab
------------------------------------------
Padre: mnkgame.TreeNode@5fd0d5ae
Board: mnkgame.MNKBoard@7cc355be
Primo figlio: null
List position: 3
Next: mnkgame.TreeNode@6e8cf4c6
Prev: mnkgame.TreeNode@1d81eb93
alpha: 12
beta: 33
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(2,2) : P1
(1,0) : P2
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@6e8cf4c6
------------------------------------------
Padre: mnkgame.TreeNode@5fd0d5ae
Board: mnkgame.MNKBoard@12edcd21
Primo figlio: null
List position: 4
Next: mnkgame.TreeNode@34c45dca
Prev: mnkgame.TreeNode@34a245ab
alpha: 16
beta: 32
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(2,2) : P1
(0,0) : P2
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@34c45dca
------------------------------------------
Padre: mnkgame.TreeNode@5fd0d5ae
Board: mnkgame.MNKBoard@52cc8049
Primo figlio: null
List position: 5
Next: null
Prev: mnkgame.TreeNode@6e8cf4c6
alpha: 16
beta: 32
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(2,2) : P1
(0,2) : P2
------------------------------------------
------------------------------------------
LIVELLO: 1
NIQ: mnkgame.TreeNode@10f87f48
------------------------------------------
Padre: mnkgame.TreeNode@38af3868
Board: mnkgame.MNKBoard@5b6f7412
Primo figlio: mnkgame.TreeNode@27973e9b
List position: 5
Next: mnkgame.TreeNode@312b1dae
Prev: mnkgame.TreeNode@5fd0d5ae
alpha: -2147483648
beta: -2147483648
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(0,0) : P1
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@27973e9b
------------------------------------------
Padre: mnkgame.TreeNode@10f87f48
Board: mnkgame.MNKBoard@7530d0a
Primo figlio: null
List position: 0
Next: mnkgame.TreeNode@27bc2616
Prev: null
alpha: 15
beta: 36
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(0,0) : P1
(0,1) : P2
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@27bc2616
------------------------------------------
Padre: mnkgame.TreeNode@10f87f48
Board: mnkgame.MNKBoard@3941a79c
Primo figlio: null
List position: 1
Next: mnkgame.TreeNode@506e1b77
Prev: mnkgame.TreeNode@27973e9b
alpha: 15
beta: 38
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(0,0) : P1
(1,2) : P2
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@506e1b77
------------------------------------------
Padre: mnkgame.TreeNode@10f87f48
Board: mnkgame.MNKBoard@4fca772d
Primo figlio: null
List position: 2
Next: mnkgame.TreeNode@9807454
Prev: mnkgame.TreeNode@27bc2616
alpha: 27
beta: 37
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(0,0) : P1
(2,0) : P2
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@9807454
------------------------------------------
Padre: mnkgame.TreeNode@10f87f48
Board: mnkgame.MNKBoard@3d494fbf
Primo figlio: null
List position: 3
Next: mnkgame.TreeNode@1ddc4ec2
Prev: mnkgame.TreeNode@506e1b77
alpha: 13
beta: 36
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(0,0) : P1
(1,0) : P2
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@1ddc4ec2
------------------------------------------
Padre: mnkgame.TreeNode@10f87f48
Board: mnkgame.MNKBoard@133314b
Primo figlio: null
List position: 4
Next: mnkgame.TreeNode@b1bc7ed
Prev: mnkgame.TreeNode@9807454
alpha: 25
beta: 35
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(0,0) : P1
(2,2) : P2
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@b1bc7ed
------------------------------------------
Padre: mnkgame.TreeNode@10f87f48
Board: mnkgame.MNKBoard@7cd84586
Primo figlio: null
List position: 5
Next: null
Prev: mnkgame.TreeNode@1ddc4ec2
alpha: 18
beta: 36
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(0,0) : P1
(0,2) : P2
------------------------------------------
------------------------------------------
LIVELLO: 1
NIQ: mnkgame.TreeNode@312b1dae
------------------------------------------
Padre: mnkgame.TreeNode@38af3868
Board: mnkgame.MNKBoard@30dae81
Primo figlio: mnkgame.TreeNode@1b2c6ec2
List position: 6
Next: null
Prev: mnkgame.TreeNode@10f87f48
alpha: -2147483648
beta: -2147483648
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(0,2) : P1
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@1b2c6ec2
------------------------------------------
Padre: mnkgame.TreeNode@312b1dae
Board: mnkgame.MNKBoard@4edde6e5
Primo figlio: null
List position: 0
Next: mnkgame.TreeNode@70177ecd
Prev: null
alpha: 15
beta: 36
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(0,2) : P1
(0,1) : P2
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@70177ecd
------------------------------------------
Padre: mnkgame.TreeNode@312b1dae
Board: mnkgame.MNKBoard@1e80bfe8
Primo figlio: null
List position: 1
Next: mnkgame.TreeNode@66a29884
Prev: mnkgame.TreeNode@1b2c6ec2
alpha: 13
beta: 36
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(0,2) : P1
(1,2) : P2
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@66a29884
------------------------------------------
Padre: mnkgame.TreeNode@312b1dae
Board: mnkgame.MNKBoard@4769b07b
Primo figlio: null
List position: 2
Next: mnkgame.TreeNode@cc34f4d
Prev: mnkgame.TreeNode@70177ecd
alpha: 27
beta: 35
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(0,2) : P1
(2,0) : P2
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@cc34f4d
------------------------------------------
Padre: mnkgame.TreeNode@312b1dae
Board: mnkgame.MNKBoard@17a7cec2
Primo figlio: null
List position: 3
Next: mnkgame.TreeNode@65b3120a
Prev: mnkgame.TreeNode@66a29884
alpha: 15
beta: 38
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(0,2) : P1
(1,0) : P2
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@65b3120a
------------------------------------------
Padre: mnkgame.TreeNode@312b1dae
Board: mnkgame.MNKBoard@6f539caf
Primo figlio: null
List position: 4
Next: mnkgame.TreeNode@79fc0f2f
Prev: mnkgame.TreeNode@cc34f4d
alpha: 25
beta: 37
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(0,2) : P1
(2,2) : P2
------------------------------------------
------------------------------------------
LIVELLO: 2
NIQ: mnkgame.TreeNode@79fc0f2f
------------------------------------------
Padre: mnkgame.TreeNode@312b1dae
Board: mnkgame.MNKBoard@50040f0c
Primo figlio: null
List position: 5
Next: null
Prev: mnkgame.TreeNode@65b3120a
alpha: 18
beta: 36
Valore: -100
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(0,2) : P1
(0,0) : P2
------------------------------------------
[Finished in 1.321s]
