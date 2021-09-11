package subroutine;

import mnkgame.MNKBoard;
import mnkgame.MNKCell;
import mnkgame.MNKCellState;
import mnkgame.MNKPlayer;
import subroutine.TreeFunctions.AttackLogistics;
import subroutine.TreeFunctions.DefenseLogistics;
import subroutine.TreeFunctions.Tree;
import subroutine.TreeFunctions.TreeNode;

public class GroupPlayer implements MNKPlayer {

	private static MNKBoard B;
	private static boolean first;
	private static int M;
	private static int N;
	private static final int EQUAL_LIMIT = 3500;

	public GroupPlayer () {}

	public void initPlayer (int in_M, int in_N, int in_K, boolean in_first, int timeout_in_sec) {
		B = new MNKBoard (in_M, in_N, in_K);
		M = in_M;
		N = in_N;
		first = in_first;
	}

	public MNKCell selectCell (MNKCell[] FC, MNKCell[] MC) {
		// Creazione degli oggetti per l'uso dei loro metodi
		Tree tree = new Tree();
		DefenseLogistics defenseFunctions = new DefenseLogistics();
		AttackLogistics attackFunctions = new AttackLogistics();
		Algoritms algoritms = new Algoritms();
		MNKCellState botState = MNKCellState.P2; if (first) botState = MNKCellState.P1;

		if (MC.length == 0)																	// PRIMO TURNO
			return new MNKCell (0, 0, MNKCellState.P1);
		else if (MC.length == 1) {															// SECONDO TURNO
			if (M == N && M % 2 == 0) {															// Caso di MNK: 44K - 66K
				// L'avversario ha la prima mossa e marca la cella: (0,0) || (M-1,N-1) --> Allora noi marchiamo (M/2, N/2 - 1) (zona centrale 
				if ((MC[0].i == 0 && MC[0].j == 0) || (MC[0].i == M-1 && MC[0].j == N-1)) 			// Se l'avversario marca l'angolo ASX o l'angolo in BDX
					return new MNKCell (M/2, N/2 - 1, MNKCellState.P2);									// Slow_Unmade marca il centro
				
				// L'avversario ha la prima mossa e marca la cella: (0,N-1) || (M-1,0) --> Allora noi marchiamo (M/2, N/2)
				else if ((MC[0].i == 0 && MC[0].j == N-1) || (MC[0].i == M-1 && MC[0].j == 0)) 		// Se l'avversario marca l'angolo BSX o l'angolo in ADX
					return new MNKCell (M/2, N/2, MNKCellState.P2);										// Slow_Unmade marca il centro
				else {																				// Se l'avversario non marca un angolo
					if ((MC[0].i != M/2 && MC[0].j != N/2 - 1)) 										// Se NON HA marcato la cella in BSX del quadratino centrale
						return new MNKCell (M/2, N/2 - 1, MNKCellState.FREE);								// La marca Slow_Unmade
					else 																				// Se ha marcato la cella in BSX del quadratino centrale
						return new MNKCell (M/2, N/2, MNKCellState.FREE);									// Altrimenti marca quella affianco
				}
			} else {																				// Caso di MNK: 33K - 55K + 34K - 62K
				if (MC[0].i == (int) M/2 && MC[0].j == (int) N/2)  									// Se l'avversario ha marcato il centro
					return new MNKCell (0, 0, MNKCellState.FREE);										// Allora noi marchiamo un'angolo
				else 																				// Se invece l'avversario NON HA marcato il centro
					return new MNKCell ((int) M/2, (int) N/2, MNKCellState.FREE);						// Allora noi marchiam un angolo
			}
		} else {																				// Se si è oltre il secondo turno
			//System.out.println("FC: " + FC.length);
			if (FC.length == 1) return FC[0];

			MNKCell[] tmpMC_ = B.getMarkedCells();
			if (MC.length > 0)
				for (int el = tmpMC_.length; el < MC.length; el++)
					B.markCell(MC[el].i,MC[el].j);
			TreeNode radice = new TreeNode (B);					// Nodo contenente la board aggiornata all'ultima mossa
			
			if (FC.length > EQUAL_LIMIT) {
				attackFunctions.basicMarking (B, botState);
				//System.out.println("Marcamento rapido - i: " + B.getMarkedCells()[B.getMarkedCells().length - 1].i + " ; j: " + B.getMarkedCells()[B.getMarkedCells().length - 1].j);
				return B.getMarkedCells()[B.getMarkedCells().length - 1];
			} else {
				System.out.print("Inizio creazione albero - ");
				tree.createTree(radice, 2, first);												// Crea il livello sottostante
				System.out.print("Albero creato\nAssegnamento valori AB - ");
	
				attackFunctions.vaiAlleFoglie(radice, botState);								// Assegna i valori AB
				if (radice.getPriority_i() >= 0) { 												// Se c'è una cella da difendere
                    System.out.println("Limite di tempo scaduto - Marcamento randomico");
					return new MNKCell (radice.getPriority_i(), radice.getPriority_j(), botState);
				}	
				System.out.print("Valori AB assegnati\nInizio scelta percorso - ");
	
				TreeNode winCell = algoritms.sceltaPercorso(radice, true, botState);			// Nodo con la mossa da ritornare
				System.out.println("Scelta percorso terminata\n");
				MNKCell[] tmpMC = winCell.getMNKBoard().getMarkedCells();
						
				if (winCell.getBeta() == Integer.MAX_VALUE) 									// Se c'è una vittoria imminente per Slow_Unmade
					return tmpMC[tmpMC.length - 1]; 												// Marca la cella vincente
				else {																			// Altrimenti
					defenseFunctions.defenseCell(radice, botState);
					if (radice.getPriority_i() >= 0) 												// Se c'è una cella da difendere
						return new MNKCell (radice.getPriority_i(), radice.getPriority_j(), botState); 	// Slow_Unmade la marca
					else 														    				// Altrimenti
						return tmpMC[tmpMC.length - 1];													// Ritorna l'ultima cella marcata del nodo vincente
				}
			}
		}
	}

	public String playerName () {
		return "Slow_Unmade";				// Lento_Sfatto
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	public static void main (String[] args) {
		Functions tree = new Functions();				// Creazione dell'oggetto per l'uso delle funzioni
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
			// DA TESTARE //tree.createTree(radice, 18, first);		// Crea il nodo sottostante
			tree.createTree(radice, FC.length + 1, first);			// Crea il nodo sottostante
			
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
			tree.defenseCell(radice, botState);
			tree.createTree(radice, 2, first);		// Crea il nodo sottostante
			tree.vaiAlleFoglie(radice, botState);	// Assegna i valori AB
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
