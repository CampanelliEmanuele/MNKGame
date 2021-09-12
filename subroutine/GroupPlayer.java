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
				//System.out.print("Inizio creazione albero - ");
				tree.createTree(radice, 2, first);												// Crea il livello sottostante
				//System.out.print("Albero creato\nAssegnamento valori AB - ");
	
				attackFunctions.vaiAlleFoglie(radice, botState);								// Assegna i valori AB
				if (radice.getPriority_i() >= 0) { 												// Se c'è una cella da difendere
                    //System.out.println("Limite di tempo scaduto - Marcamento randomico");
					return new MNKCell (radice.getPriority_i(), radice.getPriority_j(), botState);
				}	
				//System.out.print("Valori AB assegnati\nInizio scelta percorso - ");
	
				TreeNode winCell = algoritms.sceltaPercorso(radice, true, botState);			// Nodo con la mossa da ritornare
				//System.out.println("Scelta percorso terminata\n");
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

}
