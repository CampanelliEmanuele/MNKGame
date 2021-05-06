package subroutine;

//import java.lang.Math;
import java.util.Random;

import mnkgame.*;

// javac -cp ".." *.java
// java -cp ".." mnkgame.MNKGame 3 3 3 subroutine.GroupPlayer
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
	 * Deve ritornare la cella da marcare
	 */
	public MNKCell selectCell (MNKCell[] FC, MNKCell[] MC) {
		TreeFunctions tmpTreeFunctions = new TreeFunctions();				// Creazione dell'oggetto per l'uso delle funzioni
		Algoritms algoritms = new Algoritms(first);							// Creazione dell'oggetto per l'uso delle funzioni
		Stampa stampa = new Stampa();

		if (MC.length == 0) {																	// PRIMO TURNO
				System.out.println("/////////////////////////////////////////////////////////////////////////////////////");
				System.out.println("PRIMA MOSSA A NOI - MC length: " + MC.length);
				MNKCell tmp = new MNKCell (0, 0, MNKCellState.P1);
				return tmp;
		}
		else if (MC.length == 1) {														// SECONDO TURNO
				System.out.println("/////////////////////////////////////////////////////////////////////////////////////");
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
			
			int count = 0;
			for(int i = 0; i < FC.length; i++) {count++;}
			
			
			System.out.println("");
			System.out.println("/////////////////////////////////////////////////////////////////////////////////////");
			System.out.println("ELSE CASE - MC length: " + MC.length);
			if (FC.length == 1) return FC[0];

			MNKCell[] tmpMC_ = B.getMarkedCells();
			if (MC.length > 0) {				// Si fanno i nuovi marcamenti
				for (int el = tmpMC_.length; el < MC.length; el++) {
					MNKCell c = MC[el];
					B.markCell(c.i,c.j);
				}
			}	// A questa riga di codice, la board B è aggiornata alla situazione attuale della partita
			
			TreeNode radice = new TreeNode (B);					// Nodo contenente la stituzione di gioco attuale
			MNKCellState botState = MNKCellState.P2; if (first) botState = MNKCellState.P1;
			
			if(count < 18) {
				algoritms.minMax(radice.getPrimoFiglio(), botState, botState);
			}
			
			// ////////////////////////
			tmpTreeFunctions.defenseCell(radice, botState);
			tmpTreeFunctions.createTree(radice, 2, first);		// Crea il nodo sottostante
			tmpTreeFunctions.vaiAlleFoglie(radice, botState);	// Assegna i valori AB
			TreeNode winCell = tmpTreeFunctions.sceltaPercorso_1LV(radice);
			if (winCell.getBeta() == Integer.MAX_VALUE || radice.getDefense_i() < 0) {
				MNKCell[] tmpMC = winCell.getMNKBoard().getMarkedCells();
				MNKCell tmp = tmpMC[tmpMC.length - 1];
				System.out.println("NODO VINCENTE #################################################################################");
				winCell.printNodeInfo();
				System.out.println("Cella vincente: " + "(" + tmp.i + "," + tmp.j + ")");
				return tmp;
			} else {
				MNKCell[] tmpMC = radice.getMNKBoard().getMarkedCells();
				MNKCell tmp = new MNKCell (radice.getDefense_i(), radice.getDefense_j(), tmpMC[tmpMC.length - 1].state);
				System.out.println("NODO VINCENTE #################################################################################");
				radice.printNodeInfo();
				System.out.println("Difesa: " + "(" + tmp.i + "," + tmp.j + ")");
				return tmp;
			}
			// ////////////////////////
			
		}
		// Fine else turno oltre il secondo
	}
	// Fine selectCell

	public String playerName () {
		return "Slow_Unmade";				// Lento_Sfatto
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
		//B.markCell (FC[0].i, FC[0].j);			// Parte da rimuovere in futuro
		//B.markCell (FC[1].i, FC[1].j);			// Parte da rimuovere in futuro
		B.markCell (1,1);
		B.markCell (0,0);
		B.markCell (1,0);
		
		
		// ___________________________________________________________________________________________
		// Codice del selectCell
		
		TreeNode radice = new TreeNode (B);					// Nodo contenente la stituzione di gioco attuale
		MNKCellState botState = MNKCellState.P2; if (first) botState = MNKCellState.P1;
		
		tmpTreeFunctions.defenseCell(radice, botState);
		tmpTreeFunctions.createTree(radice, 2, first);		// Crea il nodo sottostante
		tmpTreeFunctions.vaiAlleFoglie(radice, botState);	// Assegna i valori AB
		TreeNode winCell = tmpTreeFunctions.sceltaPercorso_1LV(radice);
		if (winCell.getBeta() == Integer.MAX_VALUE || radice.getDefense_i() < 0) {
			MNKCell[] tmpMC = winCell.getMNKBoard().getMarkedCells();
			MNKCell tmp = tmpMC[tmpMC.length - 1];
			System.out.println("NODO VINCENTE #################################################################################");
			winCell.printNodeInfo();
			System.out.println("Cella vincente: " + "(" + tmp.i + "," + tmp.j + ")");
			//return tmp;
		} else {// if (radice.getDefense_i() >= 0) {
			MNKCell[] tmpMC = radice.getMNKBoard().getMarkedCells();
			MNKCell tmp = new MNKCell (radice.getDefense_i(), radice.getDefense_j(), tmpMC[tmpMC.length - 1].state);
			System.out.println("NODO VINCENTE #################################################################################");
			radice.printNodeInfo();
			System.out.println("Difesa: " + "(" + tmp.i + "," + tmp.j + ")");
			//return tmp;
		}	
		

	}
	// fine main
	

}
// fine class GroupPlayer
