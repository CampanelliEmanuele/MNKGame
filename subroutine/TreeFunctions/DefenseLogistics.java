package subroutine.TreeFunctions;

import mnkgame.MNKBoard;
import mnkgame.MNKCell;
import mnkgame.MNKCellState;

public class DefenseLogistics {

	public DefenseLogistics () {}
    
    private static final int D_length = 10;              // Array contenente le principali variabili della funzione assegnaValoreABFoglia
	
	// Indici delle variabili dell'array
	private static final int i_righe = 0;                // RIGHE    (i = M - 1)
	private static final int j_colonne = 1;              // COLONNE  (j = N - 1)
	private static final int k = 2;						 // Valore K della board
	private static final int ec_counter = 5;			 // Conta quante celle dello stesso tipo si susseguono, si resetta dopo due AB_freeCell consecutive (ha 1 AB_freeCell di margine perchè si fa un controllo per K-1 simboli)
	private static final int fc_counter = 6;		 	 // AB_freeCell che si susseguono
	private static final int priority_i = 8;			 // Coordinata (locale alla board del nodo) i della cella da difendere
	private static final int priority_j = 9;			 // Coordinata (locale alla board del nodo) j della cella da difendere

	private static boolean checkDefense (TreeNode in_node) {
		/**
		 * Invocazione: Controllo per interruzione di altri funzioni.
		 * 
		 * Funzione: Ritorna true se il nodo ha una cella da difendere, false altrimenti,
		 */
		if (in_node.getPriority_i() >= 0 && in_node.getPriority_j() >= 0)
			return true;
		else
			return false;
	}
	
	private static void D_editVar (int[] in_D_vars) {
		/**
		 * Invocazione: Durante il controllo di un set.
		 * 
		 * Funzione: Ad ogni nuovo controllo su una nuova board, resetta tali variabili per:
		 */
		in_D_vars[ec_counter] = 0;	// Tenere il conto dei simboli nel nuovo set in analisi
		in_D_vars[fc_counter] = 0;
		in_D_vars[priority_i] = -1;	// Si resettano le coordinate locali della cella da difendere
		in_D_vars[priority_j] = -1;
	}
	
	private static void D_enemyCell (int[] in_D_vars, TreeNode in_nodo) {
		/**
		 * Invocazione: Quando si incombe in una cella nemica durante il controllo di un set.
		 * 
		 * Funzione: Modifica delle variabili dell'array.
		 */
		in_D_vars[ec_counter]++;															// Si aumenta il conteggio delle celle (del player in analisi) in serie
		if (in_D_vars[ec_counter] >= in_D_vars[k]) in_D_vars[ec_counter] = 1;				// Se ec_counter ha valori "sballati" (ovvero maggiori di k) si pone a 1
		if (in_D_vars[ec_counter] == in_D_vars[k] - 1 && in_D_vars[priority_i] >= 0) 		// Controllo per vedere se è stata trovata la cella da difendere
			in_nodo.setPriorityCell(in_D_vars[priority_i], in_D_vars[priority_j]);
		in_D_vars[fc_counter] = 0;															// Reset del counter delle AB_freeCell
	}
	
	private static void D_freeCell (int[] in_D_vars, TreeNode in_node, int in_i, int in_j) {
		/**
		 * Invocazione: Quando si incombe in una cella libera durante il controllo di un set.
		 * 
		 * Funzione: Modifica delle variabili dell'array.
		 */
		in_D_vars[fc_counter]++;
		if (in_D_vars[fc_counter] > 1)								// Se vi sono 2 o più celle libere affiancate
			in_D_vars[ec_counter] = 0;								// Si resetta ec_counter
		
		in_D_vars[priority_i] = in_i;								// Si salvano localmente le coordinate della cella in questione,
																	// qualora ci sarò necessità di difendere
		in_D_vars[priority_j] = in_j;
		
		if (in_D_vars[ec_counter] == in_D_vars[k] - 1) {			// Controllo per capire se è una cella da difendere
			in_node.setPriorityCell(in_D_vars[priority_i], in_D_vars[priority_j]);
			in_D_vars[ec_counter] = 0;								// Si resetta ec_counter
		}
	}
	
	public void defenseCell (TreeNode in_foglia, MNKCellState in_botState) {
		/**
		 * Invocazione: Per controllare se un nodo ha una board con una cella da marcare
		 * 				onde evitare la sconfitta.
		 * 
		 * Funzione: Controlla tramite i set, se vi sono celle da marcare
		 * 			 per evitare l'imminente vittoria avversaria.
		 */
		MNKBoard board = in_foglia.getMNKBoard();
		MNKCell[] MC = in_foglia.getMNKBoard().getMarkedCells();        // Nelle posizioni 0,2,4,... vi sono le mosse del P1,
																		// nelle posizioni 1,3,5,... vi quelle del P2
		int[] D_vars = new int[D_length]; for (int i = 3; i < D_length; i++) D_vars[i] = 0;
		D_vars[i_righe] = board.M - 1;
		D_vars[j_colonne] = board.N - 1;
		D_vars[k] = board.K;
		D_vars[priority_i] = -1;
		D_vars[priority_j] = -1;
		
		int start = 0; MNKCellState enemyState = MNKCellState.P1;
		
		if (in_botState == MNKCellState.P1) {
			start = 1;
			enemyState = MNKCellState.P2;
		}
		for (int pos = start; pos < MC.length; pos += 2) {
			int i_MC = MC[pos].i;   								// Coordinata i (riga) della cella in analisi
			int j_MC = MC[pos].j;   								// Coordinata j (colonna) della cella in analisi
			
			// CONTROLLO: set orizzontale
			for (int c = 0; c <= D_vars[j_colonne]; c++) {     		// Controllo della i-esima riga (da sx verso dx)
				if (checkDefense(in_foglia)) return;
				else {
					if (board.cellState(i_MC, c) == enemyState)
						D_enemyCell (D_vars, in_foglia);
					else if (board.cellState(i_MC, c) == MNKCellState.FREE)
						D_freeCell (D_vars, in_foglia, i_MC, c);
					else if (board.cellState(i_MC, c) != enemyState && board.cellState(i_MC, c) != MNKCellState.FREE)
						D_editVar (D_vars);
					else
						System.out.println ("ERRORE - Funzione: PriorityCell - DOVE: Controllo orizzontale");
				}
			}
			D_editVar (D_vars);
			
			// CONTROLLO: set verticale
			for (int r = 0; r <= D_vars[i_righe]; r++) {     			// Controllo della j-esima colonna (dall'alto verso il basso)
				if (checkDefense(in_foglia)) return;
				else {
					if (board.cellState(r, j_MC) == enemyState)
						D_enemyCell (D_vars, in_foglia);
					else if (board.cellState(r, j_MC) == MNKCellState.FREE)
						D_freeCell (D_vars, in_foglia, r, j_MC);
					else if (board.cellState(r, j_MC) != enemyState && board.cellState(r, j_MC) != MNKCellState.FREE)
						D_editVar (D_vars);
					else
						System.out.println ("ERRORE - Funzione: PriorityCell - DOVE: Controllo verticale");
				}
			}
			D_editVar (D_vars);
			
			// CONTROLLO: set diagonale
			if (D_vars[i_righe] + 1 >= D_vars[k] && D_vars[j_colonne] + 1 >= D_vars[k]) {   // Se la mappa permette la creazione di set diagonali
				int move = 0;     										// Variabile per lo scorrimento verso la posizione di partenza dei set diagonali
			
				// SCORRIMENTO: Verso in alto a sx
				while (i_MC - move > 0 && j_MC - move > 0) { move++; }	// Si incrementa move fino a quando sottratto alle coordinate i e j
																		// non ci si ritrova in (i - move, j - move = 0)
				int start_i = i_MC - move;
				int start_j = j_MC - move;
			
				// Data la posizione più in alto a sx possibile (rispetto alla cella in analisi), controlla è in una posizione tale per cui
				// vi sia almeno un set di lunghezza vars[k]
				if (start_i + D_vars[k] - 1 <= D_vars[i_righe] && start_j + D_vars[k] - 1 <= D_vars[j_colonne]) {
					// CONTROLLO: da in alto a sx fino in basso a dx
					for (move = 0; start_i + move < start_i + D_vars[k] && start_j + move < start_j + D_vars[k]; move++) {
						if (checkDefense(in_foglia)) return;
						else {
							if (board.cellState(start_i + move, start_j + move) == enemyState)
								D_enemyCell (D_vars, in_foglia);
							else if (board.cellState(start_i + move, start_j + move) == MNKCellState.FREE)
								D_freeCell(D_vars, in_foglia, start_i + move, start_j + move);
							else if (board.cellState(start_i + move, start_j + move) != enemyState &&
									 board.cellState(start_i + move, start_j + move) != MNKCellState.FREE)
								D_editVar (D_vars);
							else
								System.out.println ("ERRORE - FUNZIONE: PriorityCell - DOVE: Controllo diagonale: alto sx --> basso dx");
						}
					}
					D_editVar (D_vars);	
				}
			
				// SCORRIMENTO: Verso in alto a dx
				for (move = 0; i_MC - move > 0 && j_MC + move < D_vars[j_colonne]; move++) {}

				start_i = i_MC - move;
				start_j = j_MC + move;
				
				if (start_i + D_vars[k] - 1 <= D_vars[i_righe] && start_j - D_vars[k] + 1 >= 0) {
					// CONTROLLO: Da in alto a dx fino in basso a sx
					for (move = 0; start_i + move < start_i + D_vars[k] && start_j - move >= 0; move++) {
						if (checkDefense(in_foglia)) return;
						else {
							if (board.cellState(start_i + move, start_j - move) == enemyState)
								D_enemyCell (D_vars, in_foglia);
							else if (board.cellState(start_i + move, start_j - move) == MNKCellState.FREE)
								D_freeCell (D_vars, in_foglia, start_i + move, start_j - move);
							else if (board.cellState(start_i + move, start_j - move) != enemyState &&
									 board.cellState(start_i + move, start_j - move) != MNKCellState.FREE)
								D_editVar (D_vars);
							else
								System.out.println ("ERRORE - FUNZIONE: PriorityCell - DOVE: Controllo diagonale: alto dx --> basso sx");
						}
					}
					D_editVar (D_vars);	
				}
			}
		}
	}

}
