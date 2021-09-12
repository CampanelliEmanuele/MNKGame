package subroutine.TreeFunctions;

import mnkgame.MNKBoard;
import mnkgame.MNKCell;
import mnkgame.MNKCellState;
import mnkgame.MNKGameState;

public class AttackLogistics {

    private static int preWinLimit;                       // Limite per decretare quando un strike si può reputare una situazione prossima alla vittoria
    private static int strikeLimit = 3;

    private static final int AB_length = 15;              // Lunghezza dell'array contenente le principali variabili
    // Indici delle variabili nell'array
    private static final int i_righe = 0;                 // Righe della board in analisi (i = M - 1)
    private static final int j_colonne = 1;               // Colonne della board in analisi (j = N - 1)
    private static final int k = 2;                       // Valore K della board in analisi
    private static final int tmp = 5;
    private static final int alpha = 6;
    private static final int beta = 7;
    private static final int priority_i = 8;		 	  // Coordinata (locale alla board del nodo) i della cella da difendere
    private static final int priority_j = 9;		      // Coordinata (locale alla board del nodo) j della cella da difendere
    // Variabili per lo scorrimento delle varie righe/colonne/diagonali
    private static final int strike = 10;                 // Numero di simboli (identici) consecutivi (in determinate condizioni si resetta)
    private static final int maxStrike = 11;              // Massimo valore di strike registrato durante l'analisi della riga/colonna/diagonale in questione
    private static final int localWinSituations = 12;     // Situazioni vicine alla vittoria nella riga/colonna/diagonale in analisi
    private static final int globalWinSituations = 13;    // Situazione vicine alla vittoria nella tabella di gioco in questione
    
    public AttackLogistics () {}

    private static void AB_editVar (int[] in_AB_vars, boolean in_noEnemy) {
        /**
		 * Invocazione: Durante il controllo di un set.
		 * 
		 * Funzione: Ad ogni nuovo controllo su una nuova board, si fanno dei
         *           controlli per modificare i punteggi.
		 */
        if (in_noEnemy)
            in_AB_vars[tmp] += 1;
        if (in_AB_vars[localWinSituations] == 0 &&              // Se non vi sono registrate situazioni prossime alla vittoria
            in_AB_vars[maxStrike] > (int)(in_AB_vars[k] / 2))   // ma solo strike sufficientemente lunghi
            in_AB_vars[tmp] += 2;                                   // si incrementa tmp
        in_AB_vars[strike] = 0;
        in_AB_vars[maxStrike] = 0;
    
        if (in_AB_vars[localWinSituations] > 0) {
            in_AB_vars[tmp] += 2;
            in_AB_vars[localWinSituations] = 0;
        }
    }

    private static void AB_currenPlayerCell (int[] in_AB_vars, TreeNode in_nodo) {
		/**
		 * Invocazione: Quando si incombe in una di Slow_Unmade durante il controllo di un set.
		 * 
		 * Funzione: Modifica delle variabili dell'array.
		 */
        in_AB_vars[tmp] += 2 + in_AB_vars[strike];
        in_AB_vars[strike] += 1;
        if (in_AB_vars[strike] > in_AB_vars[maxStrike])
            in_AB_vars[maxStrike] = in_AB_vars[strike];
    }

    private static void AB_freeCell (int[] in_AB_vars, TreeNode in_node, int in_i, int in_j) {
        /**
		 * Invocazione: Quando si incombe in una cella libera durante il controllo di un set.
		 * 
		 * Funzione: Modifica delle variabili dell'array.
		 */
        in_AB_vars[tmp] += 1;
        if (in_AB_vars[strike] > 0 && in_AB_vars[strike] >= preWinLimit) {
            in_AB_vars[localWinSituations]++;
            in_AB_vars[globalWinSituations]++;
            in_AB_vars[tmp] += 1;
            in_AB_vars[strike] = 0;
        }
    }

    private static void AB_enemyCell (int[] in_AB_vars) {
		/**
		 * Invocazione: Quando si incombe in una cella nemica durante il controllo di un set.
		 * 
		 * Funzione: Modifica delle variabili dell'array.
		 */
        in_AB_vars[tmp] -= 2;
        in_AB_vars[strike] = 0;
    }

    private static boolean checkTime (TreeNode in_foglia, long in_start) {
		/**
		 * Invocazione: Periodicamente invocata durante i controlli di un set.
		 * 
		 * Funzione: Se non c'è abbastanza tempo per lo svolgimento della
         *           funzione si interrompe il tutto per un più rapido marcamento
         *           pseudo-casuale.
		 */
        if ((System.currentTimeMillis()-in_start)/10.0 > 10*(99.0/100.0)) {
            MNKCell[] FC = in_foglia.getMNKBoard().getFreeCells();
            in_foglia.setPriority_i(FC[0].i);
            in_foglia.setPriority_j(FC[0].j);
            return true;
        } else
            return false;
    }
    
    public void vaiAlleFoglie (TreeNode in_primoFiglio, MNKCellState in_botState) {
        /**
         * Invocazione: Subito dopo aver creato l'albero.
         * 
         * Funzione: Va alle foglie dell'albero passato e ne decreta i valori alpha e
         * 			 beta tramite la funzione assegnaValoreABFoglia.
         */
        while (in_primoFiglio != null) {									// Per ogni figlio del nodo padre a cui è stata apllicata la funzione
            long start = System.currentTimeMillis();
            if (in_primoFiglio.getPrimoFiglio() == null)                       	// Se è una foglia
                assegnaValoreABFoglia (in_primoFiglio, in_botState);               	// Assegna i valori alpha e beta
            else                                                                // Altrimenti
                vaiAlleFoglie (in_primoFiglio.getPrimoFiglio(), in_botState);        // Chiamata ricorsiva sui sotto-alberi
            if (checkTime (in_primoFiglio, start)) return;
            in_primoFiglio = in_primoFiglio.getNext();
        }
    }

    public void assegnaValoreABFoglia (TreeNode in_foglia, MNKCellState in_botState) {
        /**
		 * Invocazione: Dopo la creazione dell'albero, viene invocata dalla funzione vaiAlleFoglie.
		 * 
		 * Funzione: Dato un nodo, ne analizza la board e attribuisce dei vali alpha e beta al
         *           nodo in base alla situazione di gioco.
		 */
        if (in_foglia.getMNKBoard().gameState() == MNKGameState.OPEN) {
            MNKCellState currentPlayer = MNKCellState.P1;
    
            MNKBoard board = in_foglia.getMNKBoard();
            MNKCell[] MC = board.getMarkedCells();        	// Posizioni: 0,2,4,... -> mosse del P1; Pposizioni: 1,3,5,... -> mosse del P2
    
            boolean primoControllo = true;                	// Si fanno due controlli (uno per ogni player), sarà true solo durante il primo di essi

            preWinLimit = (int)(board.K / strikeLimit);
            int[] AB_vars = new int[AB_length];
            AB_vars[i_righe] = board.M - 1;
            AB_vars[j_colonne] = board.N - 1;
            AB_vars[k] = board.K;
            AB_vars[priority_i] = -1;
            AB_vars[priority_j] = -1;
    
            // Si esegue la valutazione per attribuire un valore ad entrambi i simboli alpha e beta di ogni giocatore
            for (int alphaBeta = 0; alphaBeta < 2; alphaBeta++) {
                long start = System.currentTimeMillis();

                for (int pos = alphaBeta; pos < MC.length; pos += 2) {		// La prima volta scorre le pos del P1: 0,2,4,..., la seconda volta le posizioni del P2: 1,3,5,...
                    boolean noEnemy = true;                       	        // Variabile che tiene conto della presenza di nemici in un set
                    int i_MC = MC[pos].i;   								// Coordinata i (riga) della cella in analisi
                    int j_MC = MC[pos].j;   								// Coordinata j (colonna) della cella in analisi
                    
			        // CONTROLLO: set orizzontale
                    for (int c = 0; c <= AB_vars[j_colonne]; c++) {     	// Controllo della i-esima riga (da sx verso dx)
                        if (board.cellState(i_MC, c) == currentPlayer)
                            AB_currenPlayerCell (AB_vars, in_foglia);
                        else if (board.cellState(i_MC, c) == MNKCellState.FREE)
                            AB_freeCell (AB_vars, in_foglia, i_MC, c);
                        else if (board.cellState(i_MC, c) != currentPlayer && board.cellState(i_MC, c) != MNKCellState.FREE) {
                            AB_enemyCell (AB_vars);
                            noEnemy = false;
                        }
                    }
                    if (checkTime (in_foglia, start)) return;
                    AB_editVar (AB_vars, noEnemy);
                    noEnemy = true;
                    
			        // CONTROLLO: set verticale
                    for (int r = 0; r <= AB_vars[i_righe]; r++) {     		// Controllo della j-esima colonna (dall'alto verso il basso)
                        if (board.cellState(r, j_MC) == currentPlayer)
                            AB_currenPlayerCell (AB_vars, in_foglia);
                        else if (board.cellState(r, j_MC) == MNKCellState.FREE)
                            AB_freeCell (AB_vars, in_foglia, r, j_MC);
                        else if (board.cellState(r, j_MC) != currentPlayer && board.cellState(r, j_MC) != MNKCellState.FREE) {
                            AB_enemyCell (AB_vars);
                            noEnemy = false;
                        }
                    }
                    if (checkTime (in_foglia, start)) return;
                    AB_editVar (AB_vars, noEnemy);
                    noEnemy = true;
    
			        // CONTROLLO: set diagonale
                    if (AB_vars[i_righe] + 1 >= AB_vars[k] && AB_vars[j_colonne] + 1 >= AB_vars[k]) {   // Se la mappa percmette la creazione di set diagonali
                        int move = 0;     													// Variabile per lo scorrimento verso la posizione di partenza dei set diagonali
    
                        // Scorrimento verso in alto a sx
                        while (i_MC - move > 0 && j_MC - move > 0)
                            move++;       		// Si incrementa move fino a quando sottratto alle coordinate i e j non ci si ritrova in (i - move, j - move = 0)
                        int start_i = i_MC - move;
                        int start_j = j_MC - move;
                        
                        // Data la posizione più in alto a sx possibile (rispetto alla cella in analisi), controlla è in una posizione tale per cui
                        // vi sia almeno un set di lunghezza vars[k]
                        if (start_i + AB_vars[k] - 1 <= AB_vars[i_righe] && start_j + AB_vars[k] - 1 <= AB_vars[j_colonne]) {
					        // CONTROLLO: da in alto a sx fino in basso a dx
                            for (move = 0; start_i + move < start_i + AB_vars[k] && start_j + move < start_j + AB_vars[k]; move++) {
                                if (board.cellState(start_i + move, start_j + move) == currentPlayer)
                                    AB_currenPlayerCell (AB_vars, in_foglia);
                                else if (board.cellState(start_i + move, start_j + move) == MNKCellState.FREE)
                                    AB_freeCell (AB_vars, in_foglia, start_i + move, start_j + move);
                                else if (board.cellState(start_i + move, start_j + move) != currentPlayer && board.cellState (start_i + move, start_j + move) != MNKCellState.FREE) {
                                    AB_enemyCell (AB_vars);
                                    noEnemy = false;
                                }
                            }
                            if (checkTime (in_foglia, start)) return;
                            AB_editVar (AB_vars, noEnemy);
                            noEnemy = true;
                        }
    
				        // SCORRIMENTO: Verso in alto a dx
                        for (move = 0; i_MC - move > 0 && j_MC + move < AB_vars[j_colonne]; move++) {}
                        start_i = i_MC - move;
                        start_j = j_MC + move;
    
                        if (start_i + AB_vars[k] - 1 <= AB_vars[i_righe] && start_j - AB_vars[k] + 1 >= 0) {
					        // CONTROLLO: Da in alto a dx fino in basso a sx
                            for (move = 0; start_i + move < start_i + AB_vars[k] && start_j - move >= 0; move++) {
                                if (board.cellState(start_i + move, start_j - move) == currentPlayer)
                                    AB_currenPlayerCell (AB_vars, in_foglia);
                                else if (board.cellState(start_i + move, start_j - move) == MNKCellState.FREE)
                                    AB_freeCell (AB_vars, in_foglia, start_i + move, start_j - move);
                                else if (board.cellState(start_i + move, start_j - move) != currentPlayer && board.cellState(start_i + move, start_j - move) != MNKCellState.FREE) {
                                    AB_enemyCell (AB_vars); 
                                    noEnemy = false;
                                }
                            }
                            if (checkTime (in_foglia, start)) return;
                            AB_editVar (AB_vars, noEnemy);
                            noEnemy = true;
                        }
                    }
                    if (checkTime (in_foglia, start)) return;
                }
                if (primoControllo) {                                        // Se si stanno analizzando le celle del P1 (primo controllo, 0,2,4,...)
                    if (in_botState == MNKCellState.P1)							// Se il bot è p1 (ha iniziato per primo) --> *Primo controllo* beta = tmp
                        AB_vars[beta] = AB_vars[tmp];
                    else														// Altrimenti se il bot iniziato per secondo --> *Primo controllo* alpha = tmp
                        AB_vars[alpha] = AB_vars[tmp];
                    primoControllo = false;
                } else {                                                     // Altrimenti, se si controllano le celle del P2 (second ocontrollo, 1,3,5,...)
                    if (in_botState == MNKCellState.P2)     					// Se il bot è P1 (tmp definti dalle celle di P2) --> *Secondo controllo* alpha = tmp
                        AB_vars[beta] = AB_vars[tmp];
                    else                                     					// Altrimenti se il boi inizia per secondo (e si è al secondo controllo) --> *Secondo controllo* beta = tmp
                        AB_vars[alpha] = AB_vars[tmp];
                }
                currentPlayer = MNKCellState.P2;
                AB_vars[tmp] = 0;
            }
            in_foglia.setAlpha(AB_vars[alpha]);
            in_foglia.setBeta(AB_vars[beta]);
        } else {
            MNKGameState winState = in_foglia.getMNKBoard().gameState();
            if ((winState == MNKGameState.WINP1 && in_botState == MNKCellState.P1) ||
                (winState == MNKGameState.WINP2 && in_botState == MNKCellState.P2)) {      		// VITTORIA
                in_foglia.setColor(Colors.GREEN);
                in_foglia.setBeta(Integer.MAX_VALUE);
            } else if ((winState == MNKGameState.WINP2 && in_botState == MNKCellState.P1) ||
                    (winState == MNKGameState.WINP1 && in_botState == MNKCellState.P2)) { 		// SCONFITTA
                in_foglia.setColor(Colors.RED);
                in_foglia.setAlpha(Integer.MAX_VALUE);
            }
        }
    }

    public void basicMarking (MNKBoard in_B, MNKCellState botState) {
        /**
		 * Invocazione: Qualora per motivi di tempo vi sia necessità di un rapido marcamento.
		 * 
		 * Funzione: Funge da marcamento casuale, ma è un pelo meglio di un marcamento randomico.
         */
        int start = 0; if (botState == MNKCellState.P2) start = 1;
        int rows = in_B.M - 1;
        int columns = in_B.N - 1;

        for (int el = start; el < in_B.getMarkedCells().length; el += 2) {
            int i_MC = in_B.getMarkedCells()[el].i;                        // Coordinata i (riga) della cella in analisi
            int j_MC = in_B.getMarkedCells()[el].j;   						// Coordinata j (colonna) della cella in analisi

            if (i_MC - 1 >= 0 &&
                in_B.cellState(i_MC - 1, j_MC) == MNKCellState.FREE) {       // ALTO
                in_B.markCell(i_MC - 1, j_MC);
                return;
            } else if (i_MC - 1 >= 0 && j_MC + 1 <= columns &&
                in_B.cellState(i_MC - 1, j_MC + 1) == MNKCellState.FREE) {   // ALTO DX
                in_B.markCell(i_MC - 1, j_MC + 1);
                return;
            } else if (j_MC + 1 <= columns &&
                in_B.cellState(i_MC, j_MC + 1) == MNKCellState.FREE) {       // DX
                in_B.markCell(i_MC, j_MC + 1);
                return;
            } else if (i_MC + 1 <= rows && j_MC + 1 <= columns &&
                in_B.cellState(i_MC + 1, j_MC + 1) == MNKCellState.FREE) {   // BASSO DX
                in_B.markCell(i_MC + 1, j_MC + 1);
                return;
            } else if (i_MC + 1 <= rows &&
                in_B.cellState(i_MC + 1, j_MC) == MNKCellState.FREE) {       // BASSO
                in_B.markCell(i_MC + 1, j_MC);
                return;
            } else if (i_MC + 1 <= rows && j_MC - 1 >= 0 &&
                in_B.cellState(i_MC + 1, j_MC - 1) == MNKCellState.FREE) {   // BASSO SX
                in_B.markCell(i_MC + 1, j_MC - 1);
                return;
            } else if (j_MC - 1 >= 0 && 
                in_B.cellState(i_MC, j_MC - 1) == MNKCellState.FREE) {       // SX
                in_B.markCell(i_MC, j_MC - 1);
                return;
            } else if (j_MC - 1 >= 0 && i_MC - 1 >= 0 && 
                in_B.cellState(i_MC - 1, j_MC - 1) == MNKCellState.FREE) {   // ALTO SX
                in_B.markCell(i_MC - 1, j_MC - 1);
                return;
            } else {
                MNKCell[] FC = in_B.getFreeCells();
                in_B.markCell(FC[0].i, FC[0].j);
                return;
            }
        }
    }
}
