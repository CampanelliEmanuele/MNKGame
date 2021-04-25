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

		TreeNode radice = new TreeNode (B, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
		TreeFunctions tmpTreeFunctions = new TreeFunctions();
		Algoritms algoritms = new Algoritms(first);

		System.out.println("Avvio la creazione dell'albero...");
		System.out.println("Global B: " + B + "\n");
		solve4 (radice, B, 2);

		System.out.println("albero creato!");
		tmpTreeFunctions.vaiAlleFoglie(radice, true);
		//printSolve2(radice, false, 0, -1);		// La radice è in cime all'albero --> ergo livello 0
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");


		// Funzione da applicare solo alle foglie primoFiglio !!!
		algoritms.bigSolve (radice.getPrimoFiglio(), false);			// Si passa true perchè è il nostro turno
		printSolve2(radice, false, 0, -1);		// La radice è in cime all'albero --> ergo livello 0

		//System.out.println(algoritms.sceltaPercorso (true, radice, 2));


		/*
		B = new MNKBoard (2,2,2);
		changeBoard (B);
		*/
	}
}


Java - GroupPlayer.java:313
È partito!
Avvio la creazione dell'albero...
Global B: mnkgame.MNKBoard@4a574795

albero creato!






NODO: B: 32 ; A6
NODO: B: 34 ; A8
NODO: B: 36 ; A5
NODO: B: 36 ; A8
NODO: B: 36 ; A5
NODO: B: 41 ; A8
NODO: B: 41 ; A8
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
beta: 41
Valore: 0
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
Primo figlio: null
List position: 0
Next: mnkgame.TreeNode@72ea2f77
Prev: null
alpha: 6
beta: 32
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
NIQ: mnkgame.TreeNode@72ea2f77
------------------------------------------
Padre: mnkgame.TreeNode@38af3868
Board: mnkgame.MNKBoard@33c7353a
Primo figlio: null
List position: 1
Next: mnkgame.TreeNode@681a9515
Prev: mnkgame.TreeNode@77459877
alpha: 8
beta: 34
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
NIQ: mnkgame.TreeNode@681a9515
------------------------------------------
Padre: mnkgame.TreeNode@38af3868
Board: mnkgame.MNKBoard@3af49f1c
Primo figlio: null
List position: 2
Next: mnkgame.TreeNode@19469ea2
Prev: mnkgame.TreeNode@72ea2f77
alpha: 5
beta: 36
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
NIQ: mnkgame.TreeNode@19469ea2
------------------------------------------
Padre: mnkgame.TreeNode@38af3868
Board: mnkgame.MNKBoard@13221655
Primo figlio: null
List position: 3
Next: mnkgame.TreeNode@2f2c9b19
Prev: mnkgame.TreeNode@681a9515
alpha: 8
beta: 36
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
NIQ: mnkgame.TreeNode@2f2c9b19
------------------------------------------
Padre: mnkgame.TreeNode@38af3868
Board: mnkgame.MNKBoard@31befd9f
Primo figlio: null
List position: 4
Next: mnkgame.TreeNode@1c20c684
Prev: mnkgame.TreeNode@19469ea2
alpha: 5
beta: 36
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
NIQ: mnkgame.TreeNode@1c20c684
------------------------------------------
Padre: mnkgame.TreeNode@38af3868
Board: mnkgame.MNKBoard@1fb3ebeb
Primo figlio: null
List position: 5
Next: mnkgame.TreeNode@548c4f57
Prev: mnkgame.TreeNode@2f2c9b19
alpha: 8
beta: 41
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
NIQ: mnkgame.TreeNode@548c4f57
------------------------------------------
Padre: mnkgame.TreeNode@38af3868
Board: mnkgame.MNKBoard@1218025c
Primo figlio: null
List position: 6
Next: null
Prev: mnkgame.TreeNode@1c20c684
alpha: 8
beta: 41
Valore: 0
Colore: WHITE
------------------------------------------
CELLE MARCATE + STATO
(1,1) : P1
(2,1) : P2
(0,2) : P1
------------------------------------------
[Finished in 1.049s]
