package subroutine;

//import java.lang.Math;
import java.util.Random;

import mnkgame.*;

// javac -cp ".." *.java
// java -cp ".." mnkgame.MNKGame 3 3 3 mnkgame.GroupPlayer
// rm -r *.class

public class GroupPlayer implements MNKPlayer {

	private static MNKBoard B;

	private MNKGameState myWin;

	private MNKGameState yourWin;
	private int TIMEOUT;
	private Random rand;
	private static boolean first;
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
		TreeFunctions tmpTreeFunctions = new TreeFunctions();		// Creazione dell'oggetto per l'uso delle funzioni
		Algoritms algoritms = new Algoritms(first);							// Creazione dell'oggetto per l'uso delle funzioni
		Stampa stampa = new Stampa();

		if (MC.length == 0) {																	// PRIMO TURNO
				System.out.println("////////////////////////////////////////////////////////////////");
				System.out.println("PRIMA MOSSA A NOI - MC length: " + MC.length);
				MNKCell tmp = new MNKCell (0, 0, MNKCellState.P2);
				return tmp;
		}
		else if (MC.length == 1) {														// SECONDO TURNO
				System.out.println("////////////////////////////////////////////////////////////////");
				System.out.println("SECONDA MOSSA A NOI - MC length: " + MC.length);
				if (M == N && M % 2 == 0) {		// Caso di MNK: 44K - 66K
						// L'avversario ha la prima mossa e marca la cella: (0,0) || (M-1,N-1) --> Allora noi marchiamo (M/2, N/2 - 1)
						if ((MC[0].i == 0 && MC[0].j == 0) || (MC[0].i == M-1 && MC[0].j == N-1)) {			// Se l'avversario marca l'angolo ASX o l'angolo in BDX
								MNKCell tmp = new MNKCell (M/2, N/2 - 1, MNKCellState.P2);									// Noi marchiamo il centro
								return tmp;			// 2' + G
						}
						// L'avversario ha la prima mossa e marca la cella: (0,N-1) || (M-1,0) --> Allora noi marchiamo (M/2, N/2)
						else if ((MC[0].i == 0 && MC[0].j == N-1) || (MC[0].i == M-1 && MC[0].j == 0)) {	// Se l'avversario marca l'angolo BSX o l'angolo in ADX
								MNKCell tmp = new MNKCell (M/2, N/2, MNKCellState.P2);												// Noi marchiamo il centro
								return tmp;
						}
						// Se l'avversario non marca un angolo
						else {
								if ((MC[0].i != M/2 && MC[0].j != N/2 - 1)) {											// Se NON HA marcato la cella in BSX del quadratino centrale
										MNKCell tmp = new MNKCell (M/2, N/2 - 1, MNKCellState.FREE);	// La marca il bot
										return tmp;
								}
								else {																														// Se HA marcato la cella in BSX del quadratino centrale
										MNKCell tmp = new MNKCell (M/2, N/2, MNKCellState.FREE);			// Altrimenti marca quella affianco
										return tmp;
								}
						}
				}
				else {	// Caso di MNK: 33K - 55K + 34K - 62K
						if (MC[0].i != (int) M/2 && MC[0].j != (int) N/2) {													// Se l'avversario NON HA marcato il centro
								MNKCell tmp = new MNKCell ((int) M/2, (int) N/2, MNKCellState.FREE);		// Lo marchiamo noi
								return tmp;
						}
						else {																											// Se invece l'avversario HA marcato il centro
								MNKCell tmp = new MNKCell (0, 0, MNKCellState.FREE);		// Allora noi marchiam un angolo
								return tmp;
						}
				}

		}
		// Fine if primi due turni

		else {	// Se si è oltre il secondo turno
			System.out.println("");
			System.out.println("////////////////////////////////////////////////////////////////");
			System.out.println("ELSE CASE - MC length: " + MC.length);
			if (FC.length == 1) return FC[0];

			MNKCell[] tmpMC_ = B.getMarkedCells();

			System.out.println("MC date in input:");
			for (int el = 0; el < MC.length; el++) {
				System.out.println("MC[" + el + "]: " + "(" + MC[el].i + "," + MC[el].j + ")");
			}

			System.out.println("B.MC prima del marcamento:");
			for (int el = 0; el < tmpMC_.length; el++) {
				System.out.println("tmpMC_[" + el + "]: " + "(" + tmpMC_[el].i + "," + tmpMC_[el].j + ")");
			}

			// Si fanno i nuovi marcamenti
			if (MC.length > 0) {
				for (int el = tmpMC_.length; el < MC.length; el++) {
					MNKCell c = MC[el];
					B.markCell(c.i,c.j);
				}
			}
			tmpMC_ = B.getMarkedCells();		// Si aggiorna tmpMC_ con le nuove celle marcate

			System.out.println("B dopo del marcamento:");
			for (int el = 0; el < tmpMC_.length; el++) {
				System.out.println("tmpMC_[" + el + "]: " + "(" + tmpMC_[el].i + "," + tmpMC_[el].j + ")");
			}

			TreeNode radice = new TreeNode (B);													// radice dell'albero
			System.out.println("Avvio la creazione dell'albero...");
			//createTree_1LV (radice);
			createTree(radice,2);
			System.out.println("albero creato!");

			MNKCellState botState = MNKCellState.P2; if (first) botState = MNKCellState.P1;
			tmpTreeFunctions.vaiAlleFoglie(radice, botState);

			//algoritms.bigSolve2 (radice, true);			// Si passa true perchè è il nostro turno nel nodo radice
			stampa.printTree(radice, false, 0, -1);			// La radice è in cime all'albero --> ergo livello 0

			//System.out.println("");
			//System.out.println("Info cella scelta:");

			TreeNode winCell = sceltaPercorso_1LV(radice);
			MNKCell[] tmpMC = winCell.getMNKBoard().getMarkedCells();
			MNKCell[] tmpFC = winCell.getMNKBoard().getFreeCells();

			System.out.println("CELLA VINCENTE - MC:");
			for (int el = 0; el < tmpMC.length; el++) {
				System.out.println("tmpMC[" + el + "]: " + "(" + tmpMC[el].i + "," + tmpMC[el].j + ")");
			}
			winCell.printNodeInfo();

			/*
			for (int el = 0; el < tmpFC.length; el++) {
				System.out.println("tmpFC[" + el + "]: " + "(" + tmpFC[el].i + "," + tmpFC[el].j + ")");
			}
			*/
			MNKCell tmp = tmpMC[tmpMC.length - 1];
			System.out.println("Cella vincencte: " + "(" + tmp.i + "," + tmp.j + ")");
			return tmp;

		}
		// Fine else
	}
	// Fine selectCell

	public String playerName () {
		return "Slow_Unmade";				// Lento_Sfatto
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// Per memorizzare i sottoalberi verrà utilizzata una struttura dati dinamica omogena lineare

	
	// 5
	public static void createTree (TreeNode in_padre, int in_depthLimit) {
		if (in_depthLimit > 1) {
			if (in_padre.getMNKBoard().gameState() == MNKGameState.OPEN) {					// Se in_depthLimit > 1 --> Si crea un'altro livello
				//System.out.println("Stato: generazione - Livello: " + (5 - in_depthLimit) + " - Local B: " + in_padre.getMNKBoard());
				MNKCell[] FC = in_padre.getMNKBoard().getFreeCells();
				MNKCell[] MC = in_padre.getMNKBoard().getMarkedCells();
				MNKBoard tmpB = new MNKBoard (M,N,K);
				for (int e = 0; e < MC.length; e++) {
					tmpB.markCell (MC[e].i, MC[e].j);
				}
				//System.out.println("Local B: " + in_padre.getMNKBoard());

				while (in_padre != null) {													// Per ogni fratello (e padre compreso) si crea il sottoalbero
					//if (tmpB.gameState() != MNKGameState.OPEN) continue;
					tmpB.markCell (FC[0].i, FC[0].j);		  								// Temporaneo marcamento della prima cella
					TreeNode primoFiglio = new TreeNode (tmpB, in_padre, true, null);		// Si crea il primo figlio
					in_padre.setPrimoFiglio(primoFiglio);									// Si setta il primo figlio del nodo padre

					createTree (primoFiglio, in_depthLimit - 1);
					//tmpB.unmarkCell ();													// Si smarca la prima cella
					TreeNode prev = primoFiglio;											// Prev creato uguale al primoFiglio

					for (int e = 1; e < FC.length; e++) {									// Ciclo per la creazione dei nodi di un livello
						MNKBoard tmp2B = new MNKBoard (M,N,K);								// Crea una nuova board per ogni nodo del livello in questione
						for (int el = 0; el < MC.length; el++) tmp2B.markCell (MC[el].i, MC[el].j);

						tmp2B.markCell (FC[e].i, FC[e].j);									// Temporaneo marcamento della cella
						TreeNode figlio = new TreeNode (tmp2B, in_padre, false, prev);
						prev.setNext (figlio);												// Il fratello prev è ora collegato al suo nuovo fratello

						prev = figlio;														// Il nuovo figlio è ora il prev (ovvero l'ultimo figlio creato)
						createTree (figlio, in_depthLimit - 1);
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

	// Ritorna il nodo migliore tra i nodi del livello sottostante a quello del padre
	public static TreeNode sceltaPercorso_1LV (TreeNode in_padre) {

		TreeNode primoFiglio = in_padre.getPrimoFiglio();			// Serve per lo scorrimento dei fratelli
		TreeNode winNode = primoFiglio;												// Nodo ritornato
		int maxBeta = Integer.MIN_VALUE;

		while (primoFiglio != null) {								// Scorre tutti i figli e tira fuori quello col beta maggiore di tutti (winNode)
				if (primoFiglio.getBeta() > maxBeta) {
					maxBeta = primoFiglio.getBeta();
					winNode = primoFiglio;
				} else if (primoFiglio.getBeta() == maxBeta) {
						if (primoFiglio.getAlpha() > winNode.getAlpha()) winNode = primoFiglio;
				}
				primoFiglio = primoFiglio.getNext();
		}
		return winNode;
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static void main (String[] args) {
		TreeFunctions tmpTreeFunctions = new TreeFunctions();		// Creazione dell'oggetto per l'uso delle funzioni
		Algoritms algoritms = new Algoritms(first);							// Creazione dell'oggetto per l'uso delle funzioni
		Stampa stampa = new Stampa();

		System.out.println("È partito!");

		// K piccolo: + tempo assegnaValoreABFoglia (controlli diagonali)
		//						- tempo creazione albero
		// K grande:  - ...
		// 						+ ...
		// Preferenza del K piccolo

		M = 3;											// Parte da rimuovere in futuro
		N = 3;											// Parte da rimuovere in futuro
		K = 3;											// Parte da rimuovere in futuro
		B = new MNKBoard (M,N,K);

		MNKCell[] FC = B.getFreeCells();		// Parte da rimuovere in futuro
		B.markCell (FC[0].i, FC[0].j);			// Parte da rimuovere in futuro
		B.markCell (FC[1].i, FC[1].j);			// Parte da rimuovere in futuro



		TreeNode radice = new TreeNode (B);

		System.out.println("Avvio la creazione dell'albero...");
		System.out.println("Global B: " + B + "\n");
		//createTree_1LV (radice);
		createTree(radice,2);


		System.out.println("albero creato!");
		first = true;
		MNKCellState botState = MNKCellState.P2; if (first) botState = MNKCellState.P1;
		tmpTreeFunctions.vaiAlleFoglie(radice, botState);

		//algoritms.bigSolve2 (radice, true);		// Si passa true perchè è il nostro turno nel nodo radice
		stampa.printTree(radice, false, 0, -1);				// La radice è in cime all'albero --> ergo livello 0

		System.out.println("");
		System.out.println("");
		System.out.println("Cella scelta:" + sceltaPercorso_1LV(radice));

		TreeNode winCell = sceltaPercorso_1LV(radice);

		if (winCell.getDefense_i() >= 0 && winCell.getDefense_j() >= 0) {
			System.out.println("Cella da difendere: " + winCell.getDefense_i() + "," + winCell.getDefense_j());
		} else {
			MNKCell[] tmpMC = winCell.getMNKBoard().getMarkedCells();
			MNKCell[] tmpFC = winCell.getMNKBoard().getFreeCells();
			for (int el = 0; el < tmpMC.length; el++) {
				System.out.println("tmpMC[" + el + "]: " + "(" + tmpMC[el].i + "," + tmpMC[el].j + ")");
			}
			for (int el = 0; el < tmpFC.length; el++) {
				System.out.println("tmpFC[" + el + "]: " + "(" + tmpFC[el].i + "," + tmpFC[el].j + ")");
			}

			MNKCell tmp = tmpMC[tmpMC.length - 1];
			System.out.println("Cella vincente: " + "(" + tmp.i + "," + tmp.j + ")");
		}

	}

}
