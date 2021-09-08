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
		Functions tmpFunctions = new Functions();				// Creazione dell'oggetto per l'uso delle funzioni
		Algoritms algoritms = new Algoritms(first);							// Creazione dell'oggetto per l'uso delle funzioni
		Stampa stampa = new Stampa();
		MNKCellState botState = MNKCellState.P2; if (first) botState = MNKCellState.P1;

		if (MC.length == 0)																	// PRIMO TURNO
			return new MNKCell (0, 0, MNKCellState.P1);
		else if (MC.length == 1) {																// SECONDO TURNO
			if (M == N && M % 2 == 0) {																// Caso di MNK: 44K - 66K
				// L'avversario ha la prima mossa e marca la cella: (0,0) || (M-1,N-1) --> Allora noi marchiamo (M/2, N/2 - 1) (zona centrale 
				if ((MC[0].i == 0 && MC[0].j == 0) || (MC[0].i == M-1 && MC[0].j == N-1)) 			// Se l'avversario marca l'angolo ASX o l'angolo in BDX
					return new MNKCell (M/2, N/2 - 1, MNKCellState.P2);						// Noi marchiamo il centro
					// 2' + G  <-- ????
				
				// L'avversario ha la prima mossa e marca la cella: (0,N-1) || (M-1,0) --> Allora noi marchiamo (M/2, N/2)
				else if ((MC[0].i == 0 && MC[0].j == N-1) || (MC[0].i == M-1 && MC[0].j == 0)) 	// Se l'avversario marca l'angolo BSX o l'angolo in ADX
					return new MNKCell (M/2, N/2, MNKCellState.P2);							// Noi marchiamo il centro
				// Se l'avversario non marca un angolo
				else {
					if ((MC[0].i != M/2 && MC[0].j != N/2 - 1)) 									// Se NON HA marcato la cella in BSX del quadratino centrale
						return new MNKCell (M/2, N/2 - 1, MNKCellState.FREE);				// La marca il bot
					else 																			// Se ha marcato la cella in BSX del quadratino centrale
						return new MNKCell (M/2, N/2, MNKCellState.FREE);					// Altrimenti marca quella affianco
				}
			}
			else {
				// Caso di MNK: 33K - 55K + 34K - 62K
				if (MC[0].i == (int) M/2 && MC[0].j == (int) N/2)  								// Se l'avversario HA marcato il centro
					return new MNKCell (0, 0, MNKCellState.FREE);							// Allora noi marchiamo un'angolo
				else 																				// Se invece l'avversario NON HA marcato il centro
					return new MNKCell ((int) M/2, (int) N/2, MNKCellState.FREE);			// Allora noi marchiam un angolo
			}

		}
		// Fine if primi due turni
		else {	// Se si è oltre il secondo turno
			if (FC.length == 1) return FC[0];

			MNKCell[] tmpMC_ = B.getMarkedCells();
			if (MC.length > 0) 				// Si fanno i nuovi marcamenti
				for (int el = tmpMC_.length; el < MC.length; el++)
					B.markCell(MC[el].i,MC[el].j);
			// A questa riga di codice, la board B è aggiornata alla situazione attuale della partita
			
			TreeNode radice = new TreeNode (B);					// Nodo contenente la stituzione di gioco attuale
			
			// ////////////////////////
			
			if (FC.length <= -10) {
				// DA TESTARE //tmpFunctions.createTree(radice, 18, first);		// Crea il nodo sottostante
				tmpFunctions.createTree(radice, FC.length + 1, first);			// Crea il nodo sottostante
				
				// Modifica i colori dell'albero sottostante
				if (botState == MNKCellState.P1) algoritms.minMax(radice.getPrimoFiglio(), botState, MNKCellState.P2);
				else algoritms.minMax(radice.getPrimoFiglio(), botState, MNKCellState.P1);
				
				TreeNode primoFiglio = radice.getPrimoFiglio();
				while (primoFiglio != null) {
					System.out.println("pos: " + primoFiglio.getListPosition() + " Color: " + primoFiglio.getColor());
					primoFiglio = primoFiglio.getNext();
				}

				stampa.printMoleColor (radice);
				
				TreeNode winCell = algoritms.sceltaPercorso(radice, false, botState);
				MNKCell[] tmpMC = winCell.getMNKBoard().getMarkedCells();
				System.out.println("minMax");
				System.out.println("BotState: " + botState + " ; Colore radice: " + radice.getColor());
				//System.out.println("NODO VINCENTE ##########################################################");
				winCell.printNodeInfo();// System.out.println("minMax - Cella vincente: " + "(" + tmp.i + "," + tmp.j + ")");
				return tmpMC[tmpMC.length - 1];
				
			}
			else {
				tmpFunctions.createTree(radice, 2, first);								// Crea il nodo sottostante
				tmpFunctions.vaiAlleFoglie(radice, botState);							// Assegna i valori AB
				TreeNode winCell = algoritms.sceltaPercorso(radice, true, botState);
				MNKCell[] tmpMC = winCell.getMNKBoard().getMarkedCells();
				
				if (winCell.getBeta() == Integer.MAX_VALUE) 								// Se c'è una vittoria imminente
					return tmpMC[tmpMC.length - 1]; 										// Marca la cella vincente
				else {																	// Se non c'è una vittoria imminente
					tmpFunctions.defenseCell(radice, botState);
					if (radice.getPriority_i() >= 0) 										// Se c'è una cella da difendere
						return new MNKCell (radice.getPriority_i(), radice.getPriority_j(), botState); // Marca la cella da difendere
					else {														    		// Se non c'è una ella da difendere
						tmpFunctions.quickMark (radice, first);
						if (radice.getPriority_i() >= 0) 									// Se c'è un marcamento veloce possibile
							return new MNKCell (radice.getPriority_i(), radice.getPriority_j(), botState); // Fa il marcamento rapido
						else															// Se non c'è un marcamento rapido possibile
							return tmpMC[tmpMC.length - 1];
					}
				}
				
				
				
				
				
				/*
				tmpFunctions.defenseCell(radice, botState);
				tmpFunctions.createTree(radice, 2, first);		// Crea il nodo sottostante
				tmpFunctions.vaiAlleFoglie(radice, botState);	// Assegna i valori AB
				TreeNode winCell = algoritms.sceltaPercorso(radice, true, botState);
				
				if (winCell.getBeta() == Integer.MAX_VALUE || radice.getPriority_i() < 0) {
					MNKCell[] tmpMC = winCell.getMNKBoard().getMarkedCells();
					MNKCell tmp = tmpMC[tmpMC.length - 1];
					return tmp;
				} else {
					MNKCell[] tmpMC = radice.getMNKBoard().getMarkedCells();
					MNKCell tmp = new MNKCell (radice.getPriority_i(), radice.getPriority_j(), botState);
					return tmp;
				}
				*/
				
				
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

	/*
	public static void main (String[] args) {
		Functions tmpFunctions = new Functions();				// Creazione dell'oggetto per l'uso delle funzioni
		Algoritms algoritms = new Algoritms(first);							// Creazione dell'oggetto per l'uso delle funzioni
		Stampa stampa = new Stampa();

		System.out.println("È partito!");

		// K piccolo: + tempo assegnaValoreABFoglia (controlli diagonali)
		//						- tempo creazione albero
		// K grande:  - ...
		// 						+ ...
		// Preferenza del K piccolo

		M = 5;											// Parte da rimuovere in futuro
		N = 5;											// Parte da rimuovere in futuro
		K = 4;											// Parte da rimuovere in futuro
		B = new MNKBoard (M,N,K);

		MNKCell[] FC = B.getFreeCells();				// Parte da rimuovere in futuro
		
		//B.markCell (FC[0].i, FC[0].j);	// 11
		//B.markCell (FC[1].i, FC[1].j);	// 21
		//B.markCell (FC[2].i, FC[2].j);	// 01
		//B.markCell (FC[3].i, FC[3].j);	// 12
		
		//B.markCell (FC[4].i, FC[4].j);	// 20
		
		//B.markCell (FC[5].i, FC[5].j);	// 10
		//B.markCell (FC[6].i, FC[6].j);	// 22
		B.markCell(2, 3);
		B.markCell(0, 3);
		
		B.markCell(4, 0);
		B.markCell(1, 1);
		
		B.markCell(2, 2);
		B.markCell(1, 4);
		
		B.markCell(3, 4);
		B.markCell(3, 0);
		
		B.markCell(2, 0);
		//B.markCell(2, 4);
		
		//B.markCell(2, 1); \\ enemy win
		
		// ___________________________________________________________________________________________
		// Codice del selectCell
		
		TreeNode radice = new TreeNode (B);					// Nodo contenente la stituzione di gioco attuale
		first = false;
		MNKCellState botState = MNKCellState.P2; if (first) botState = MNKCellState.P1;
		
		if (FC.length <= -10) {
			// DA TESTARE //tmpFunctions.createTree(radice, 18, first);		// Crea il nodo sottostante
			tmpFunctions.createTree(radice, FC.length + 1, first);			// Crea il nodo sottostante
			
			// Modifica i colori dell'albero sottostante
			if (botState == MNKCellState.P1) algoritms.minMax(radice.getPrimoFiglio(), botState, MNKCellState.P2);
			else algoritms.minMax(radice.getPrimoFiglio(), botState, MNKCellState.P1);
			
			TreeNode primoFiglio = radice.getPrimoFiglio();
			while (primoFiglio != null) {
				System.out.println("pos: " + primoFiglio.getListPosition() + " Color: " + primoFiglio.getColor());
				primoFiglio = primoFiglio.getNext();
			}

			stampa.printMoleColor (radice);
			
			TreeNode winCell = algoritms.sceltaPercorso(radice, false, botState);
			MNKCell[] tmpMC = winCell.getMNKBoard().getMarkedCells();
			MNKCell tmp = tmpMC[tmpMC.length - 1];
			System.out.println("minMax");
			System.out.println("BotState: " + botState + " ; Colore radice: " + radice.getColor());
			//System.out.println("NODO VINCENTE ##########################################################");
			winCell.printNodeInfo();// System.out.println("minMax - Cella vincente: " + "(" + tmp.i + "," + tmp.j + ")");
			//return tmp;
			
		}
		else {
			tmpFunctions.defenseCell(radice, botState);
			tmpFunctions.createTree(radice, 2, first);		// Crea il nodo sottostante
			tmpFunctions.vaiAlleFoglie(radice, botState);	// Assegna i valori AB
			TreeNode winCell = algoritms.sceltaPercorso(radice, true, botState);
			
			if (winCell.getBeta() == Integer.MAX_VALUE || radice.getPriority_i() < 0) {
				MNKCell[] tmpMC = winCell.getMNKBoard().getMarkedCells();
				MNKCell tmp = tmpMC[tmpMC.length - 1];
				System.out.println(winCell.getMNKBoard().gameState() + " ; " + tmp.toString());
				//System.out.println("NODO VINCENTE ##########################################################"); winCell.printNodeInfo(); System.out.println("AB - Cella vincente: " + "(" + tmp.i + "," + tmp.j + ")");
				//return tmp;
			} else {
				MNKCell[] tmpMC = radice.getMNKBoard().getMarkedCells();
				MNKCell tmp = new MNKCell (radice.getPriority_i(), radice.getPriority_j(), botState);
				System.out.println(winCell.getMNKBoard().gameState() + " ; " + tmp.toString());
				//System.out.println("NODO VINCENTE ##########################################################"); radice.printNodeInfo(); System.out.println("Difesa: " + "(" + tmp.i + "," + tmp.j + ")");
				//return tmp;
			}
			
		}
		
		// //////////////////
		

	}
	// fine main
	*/
	
}
// fine class GroupPlayer
