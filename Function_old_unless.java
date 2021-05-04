import mnkgame.*;
import subroutine.Colors;
import subroutine.TreeNode;

public class Function_old_unless {

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 3
	// Da spostare a livelli più globali, magari agli inizi della partita questo valore sarà più alto in modo tale da considerare come vincenti più situazioni, poi con l'avanzare della partita essa verrà incrementata per esserr più selettivi nelle condizioni di vittoria (ad esempio aumentando il valore di n ogni tot livelli dell'albero)
	private static int preWinLimit;   // Limite per decretare quando un strike si può reputare una situazione prossima alla vittoria

	private static final int lunghezza = 20;             // Array contenenti le principali variabili (intere) della funzione assegnaValoreABFoglia
	// Indici delle variabili nell'array
	private static final int i_righe = 0;                // RIGHE    (i = M - 1)
	private static final int j_colonne = 1;              // COLONNE  (j = N - 1)
	private static final int k = 2;                      // SERIE
	private static final int tmp = 5;
	private static final int alpha = 6;
	private static final int beta = 7;
	// Variabili per lo scorrimento delle varie righe/colonne/diagonali
	private static final int strike = 10;                 // Numero di simboli (identici) consecutivi (in determinate condizioni si resetta)
	private static final int maxStrike = 11;              // Massimo valore di strike registrato durante l'analisi della riga/colonna/diagonale in questione
	private static final int localWinSituations = 12;     // Situazioni vicine alla vittoria nella riga/colonna/diagonale in analisi
	private static final int globalWinSituations = 13;    // Situazione vicine alla vittoria nella tabella di gioco in questione
	// Variabili che funzionizionano allo stesso modo di strike e maxStrike, solo che non si resettano nel caso freecell.
	private static final int k_less_1 = 14;               // Variabile per controllare se un giocatore è prossimo alla vittoria
	private static final int max_k_less_1 = 15;           //
	// Variabili contenenti le coordinate della cella da difendere
	private static final int firstFreeCell = 16;          // true == 1 ; false == 0
	private static final int defense_i = 17;              //
	private static final int defense_j = 18;              //

	
	// 3.1
	// Funzione richiamata una volta finito di controllare un set
	private static void editVar (int[] in_vars, boolean in_noEnemy) {
	    System.out.println ("editVars: PRE reset: in_vars[k_less_1]: " + in_vars[k_less_1] + " ; in_vars[max_k_less_1]: " + in_vars[max_k_less_1]);
	    if (in_noEnemy) in_vars[tmp]++;
	    // Se non vi sono registrate situazioni prossime alla vittoria ma solo strike sufficientemente lunghi si incrementa tmp
	    if (in_vars[localWinSituations] == 0 && in_vars[maxStrike] > (int)(in_vars[k] / 2)) in_vars[tmp] += 2;
	    in_vars[strike] = 0;
	    in_vars[maxStrike] = 0;
	    if (in_vars[k_less_1] > in_vars[max_k_less_1]) in_vars[max_k_less_1] = in_vars[k_less_1];
	    in_vars[k_less_1] = 0;
	    in_vars[firstFreeCell] = 1;
	    System.out.println ("editVars: POST reset: in_vars[k_less_1]: " + in_vars[k_less_1] + " ; in_vars[max_k_less_1]: " + in_vars[max_k_less_1]);
	
	    if (in_vars[localWinSituations] > 0) {
	      in_vars[tmp] += 2;
	      in_vars[localWinSituations] = 0;
	    }
	    if (in_vars[max_k_less_1] == in_vars[k] - 1) {
	      System.out.println ("in_vars[max_k_less_1]: " + in_vars[max_k_less_1]);
	      in_vars[tmp] = Integer.MAX_VALUE - 1;       // Se un player sta per vincere si setta tmp = +inf - 1
	    }
	    //System.out.println ("in_vars[tmp]: " + in_vars[tmp]);
  }

	// 3.2: aumenta valori quando trova cella del player che stai analizzando
	private static void currenPlayerCell (int[] in_vars) {
		//System.out.println ("currenPlayerCell tmp +2");
	    System.out.println ("currenPlayerCell PRE: in_vars[k_less_1]: " + in_vars[k_less_1] + " ; in_vars[max_k_less_1]: " + in_vars[max_k_less_1]);
	    in_vars[tmp] += 2;
	    in_vars[strike] += 1;
	    in_vars[k_less_1] += 1;
	    if (in_vars[strike] > in_vars[maxStrike]) in_vars[maxStrike] = in_vars[strike];
	    System.out.println ("currenPlayerCell POST: in_vars[k_less_1]: " + in_vars[k_less_1] + " ; in_vars[max_k_less_1]: " + in_vars[max_k_less_1]);
	}

	private static void freeCell (int[] in_vars, int in_defense_i, int in_defense_j) {//System.out.println ("freeCell tmp +1");
		System.out.println ("freeCell PRE: in_vars[firstFreeCell]: " + in_vars[firstFreeCell]);
	    in_vars[tmp] += 1;
	    if (in_vars[strike] > 0 && in_vars[strike] >= preWinLimit) {
	    	in_vars[localWinSituations]++;
	    	in_vars[globalWinSituations]++;
	    	in_vars[tmp]++;
	    	in_vars[strike] = 0;
	    }
	
	    if (in_vars[firstFreeCell] == 1) {
	    	in_vars[defense_i] = in_defense_i;
	    	in_vars[defense_j] = in_defense_j;
	    	in_vars[firstFreeCell] = 0;
	    }
	    System.out.println ("freeCell POST: in_vars[firstFreeCell]: " + in_vars[firstFreeCell]);
	  }

	private static void enemyCell (int[] in_vars) {
	    //System.out.println ("enemyCell tmp -1");
	    System.out.println ("enemyCell: reset: in_vars[k_less_1]: " + in_vars[k_less_1]);
	    in_vars[tmp]--;
	    in_vars[strike] = 0;
	    in_vars[k_less_1] = 0;
	}

	protected static void assegnaValoreABFoglia (TreeNode in_foglia, MNKCellState in_botState) {
	    // set: È quel "pezzo" di celle lungo tutta la linea (orizzontale, verticale o diagonale) che viene analizzato di votla in volta
	
	    //------------------------------------------------------------------------------------------
	
	    if (in_foglia.getMNKBoard().gameState() == MNKGameState.OPEN) {
	    	MNKCellState currentPlayer = MNKCellState.P1;
	
	    	MNKBoard board = in_foglia.getMNKBoard();     // Essendo la variabile MNKCellState[][] protetta, dovrebbe essere accessibile da questo programma
	    	MNKCell[] MC = in_foglia.getMNKBoard().getMarkedCells();        // Nelle posizioni 0,2,4,... vi sono le mosse del P1, nelle posizioni 1,3,5,... vi sono le mosse del P2
	
	    	boolean noEnemy = true;                       // Variabile che tiene conto della presenza di nemici in un set
	    	boolean primoControllo = true;                // Si fanno due controlli (uno per ogni player), sarà true solo durante il primo di essi
	
	    	preWinLimit = (int)((board.K / 3) * 2);       // preWin è pari ad un terzo di K
	    	int[] vars = new int[lunghezza]; for (int i = 3; i < lunghezza; i++) vars[i] = 0;
	    	vars[i_righe] = board.M - 1;
	    	vars[j_colonne] = board.N - 1;
	    	vars[k] = board.K;
	    	vars[firstFreeCell] = 1;        // firstFreeCell = true
	
	      // Si esegue la valutazione per attribuire un valore ad entrambi i simboli alpha e beta di ogni giocatore
	    	for (int alphaBeta = 0; alphaBeta < 2; alphaBeta++) {
	
	    		for (int pos = alphaBeta; pos < MC.length; pos += 2) {    // La prima volta scorre le pos del P1: 0,2,4,..., la seconda volta le posizioni del P2: 1,3,5,...
	    			int i_MC = MC[pos].i;   // Coordinata i (riga) della cella in analisi
	    			int j_MC = MC[pos].j;   // Coordinata j (colonna) della cella in analisi
	    			//System.out.println ("[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]");
	    			//System.out.println ("CELLA SET: (" + i_MC + "," + j_MC + ")");
	
	    			// Controllo set orizzontale
	    			System.out.println ("AVVIO - Controllo orizzontale");
	    			for (int c = 0; c <= vars[j_colonne]; c++) {     // Controllo della i-esima riga (da sx verso dx)
    					//System.out.println("cella guardata: (" + i_MC + "," + c + ")" );
	    				if (board.cellState(i_MC, c) == currentPlayer) currenPlayerCell (vars);
	    				else if (board.cellState(i_MC, c) == MNKCellState.FREE) freeCell (vars, i_MC, c);
	    				else if (board.cellState(i_MC, c) != currentPlayer && board.cellState(i_MC, c) != MNKCellState.FREE) { enemyCell (vars); noEnemy = false; }
	    				else System.out.println ("ERRORE - Funzione: assegnaValoreABFoglia - DOVE: Controllo orizzontale - R.104 circa");
	    			}
	    			editVar (vars, noEnemy);
	    			noEnemy = true;
	
	    			// Controllo riga in verticale
	    			System.out.println ("AVVIO - Controllo verticale");
	    			for (int r = 0; r <= vars[i_righe]; r++) {     // Controllo della j-esima colonna (dall'alto verso il basso)
	    				if (board.cellState(r, j_MC) == currentPlayer) currenPlayerCell (vars);
	    				else if (board.cellState(r, j_MC) == MNKCellState.FREE) freeCell (vars, r, j_MC);
	    				else if (board.cellState(r, j_MC) != currentPlayer && board.cellState(r, j_MC) != MNKCellState.FREE) { enemyCell (vars); noEnemy = false; }
	    				else System.out.println ("ERRORE - Funzione: assegnaValoreABFoglia - DOVE: Controllo verticale - R.114 circa");
	    			}
	    			editVar (vars, noEnemy);
	    			noEnemy = true;
	
	    			// Controllo diagonale
	    			if (vars[i_righe] + 1 >= vars[k] && vars[j_colonne] + 1 >= vars[k]) {   // Se è possibile la creazione di diagonali
	    				System.out.println ("AVVIO - Controllo diagonale");
	    				int move = 0;     // Variabile per lo scorrimento verso la posizione di partenza dei set diagonali, parte da 1 perchè da un valore minore di 1 si fanno incrementi inutili
	
	    				// Scorrimento verso in alto a sx
	    				while (i_MC - move > 0 && j_MC - move > 0) { move++; }      // Si incrementa move fino a quando sottratto alle coordinate i e j non ci si ritrova in (i - move, j - move = 0)
	    				int start_i = i_MC - move;
	    				int start_j = j_MC - move;
	
	    				// Data la posizione più in alto a sx possibile (rispetto alla cella in analisi), controlla è in una posizione tale per cui vi sia almeno un set di lunghezza vars[k]
	    				if (start_i + vars[k] - 1 <= vars[i_righe] && start_j + vars[k] - 1 <= vars[j_colonne]) {
	    					//System.out.println("control_i: start_i + vars[k] - 1 = " + (start_i + vars[k] - 1));
	    					//System.out.println("control_j: start_j + vars[k] - 1 = " + (start_j + vars[k] - 1));
	
	    					// Da in alto a sx fino in basso a dx
	    					//System.out.println ("AVVIO - ASX -> BDX - start_i: " + start_i + " ; start_j: " + start_j + " ; move: " + move);
	    					for (move = 0; start_i + move < start_i + vars[k] && start_j + move < start_j + vars[k]; move++) {
	    						if (board.cellState(start_i + move, start_j + move) == currentPlayer) currenPlayerCell (vars);
	    						else if (board.cellState(start_i + move, start_j + move) == MNKCellState.FREE) freeCell(vars, start_i + move, start_j + move);
	    						else if (board.cellState(start_i + move, start_j + move) != currentPlayer && board.cellState(start_i + move, start_j + move) != MNKCellState.FREE) { enemyCell (vars); noEnemy = false; }
	    						else System.out.println ("ERRORE - FUNZIONE: assegnaValoreABFoglia - DOVE: Controllo diagonale: alto sx --> basso dx - R.134 circa");
	    					}
	    					editVar (vars, noEnemy);
	    					noEnemy = true;
	    				}
	
	    				// Scorrimento verso in alto a dx
	    				for (move = 0; i_MC - move > 0 && j_MC + move < vars[j_colonne]; move++) {}
	    				start_i = i_MC - move;
	    				start_j = j_MC + move;
	
	    				if (start_i + vars[k] - 1 <= vars[i_righe] && start_j - vars[k] + 1 >= 0) {
	    					//System.out.println("control_i: start_i + vars[k] - 1 = " + (start_i + vars[k] - 1));
	    					//System.out.println("control_j: start_j - vars[k] + 1 = " + (start_j - vars[k] + 1));
	
	    					// Da in alto a dx fino in basso a sx
	    					//System.out.println ("AVVIO - ADX -> BSX - start_i: " + start_i + " ; start_j: " + start_j + " ; move: " + move);
	    					for (move = 0; start_i + move < start_i + vars[k] && start_j - move >= 0; move++) {
	    						if (board.cellState(start_i + move, start_j - move) == currentPlayer){
	    							currenPlayerCell (vars);
	    							//System.out.println("Controllo diagonale ADX -> BSX");
	    						}
	    						else if (board.cellState(start_i + move, start_j - move) == MNKCellState.FREE) freeCell (vars, start_i + move, start_j - move);
	    						else if (board.cellState(start_i + move, start_j - move) != currentPlayer && board.cellState(start_i + move, start_j - move) != MNKCellState.FREE) { enemyCell (vars); noEnemy = false; }
	    						else System.out.println ("ERRORE - FUNZIONE: assegnaValoreABFoglia - DOVE: Controllo diagonale: alto dx --> basso sx - R.149 circa");
	    					}
	    					editVar (vars, noEnemy);
	    					noEnemy = true;
	    				}
	    				//System.out.println ("tmp: " + vars[tmp]);
	    			}
	    			// end if - controllo diagonale
	    			else {  // Se il controllo diagonale non può essere eseguito a causa dei valori di M,N e K
	    				System.out.println("NO DIAGONAL SET - FUNZIONE: assegnaValoreABFoglia - Valori MNK non consoni per set diagonali.");
	    			}
	    			// end if - else
	
	    		}
	    		// end for
	
	    		if (primoControllo) {                                               // Se si stanno analizzando le celle del P1 (primo controllo, 0,2,4,...)
	    			if (in_botState == MNKCellState.P1) vars[beta] = vars[tmp];       // Se il bot è p1 (ha iniziato per primo) --> *Primo controllo* beta = tmp
	    			else vars[alpha] = vars[tmp];                                     // Altrimenti se il bot iniziato per secondo --> *Primo controllo* alpha = tmp
	     	    primoControllo = false;
	    		} else {                                                            // Altrimenti, se si controllano le celle del P2 (second ocontrollo, 1,3,5,...)
	    			if (in_botState == MNKCellState.P2) vars[beta] = vars[tmp];       // Se il bot è P1 (tmp definti dalle celle di P2) --> *Secondo controllo* alpha = tmp
	    			else vars[alpha] = vars[tmp];                                     // Altrimenti se il boi inizia per secondo (e si è al secondo controllo) --> *Secondo controllo* beta = tmp
	    		}
	
	    		currentPlayer = MNKCellState.P2;
	
	    		//System.out.println("RESET DI TMP");
	    		vars[tmp] = 0;
	    	}
	    	// end for
	
	    	in_foglia.setAlpha(vars[alpha]);
	    	in_foglia.setBeta(vars[beta]);
	    	// Aggiungere var per minmax
	
	
	    	if (in_foglia.getAlpha() == Integer.MAX_VALUE - 1) { // Se l'avversario sta per vincere e noi no
	    		in_foglia.setDefense_i(vars[defense_i]);  // Ritorniamo le coordinare della cella per bloccare la vittoria nemica
	    		in_foglia.setDefense_j(vars[defense_j]);
	    	} else {                                    // Altrimenti ritorniamo coordinate irrilevanti
	    		in_foglia.setDefense_i(-1);
	    		in_foglia.setDefense_j(-1);
	    	}
	
	    }
	    // Fine if board OPEN

	    else {
	    	int alpha = in_foglia.getAlpha();
	    	int beta = in_foglia.getBeta();
	    	MNKGameState winState = in_foglia.getMNKBoard().gameState();
	    	if ((winState == MNKGameState.WINP1 && in_botState == MNKCellState.P1) || (winState == MNKGameState.WINP2 && in_botState == MNKCellState.P2)) {      // VITTORIA
	    		in_foglia.setColor(Colors.GREEN);
	    		in_foglia.setBeta(Integer.MAX_VALUE);
	    		//in_foglia.setAlpha(alpha - 100000);
	    	}
	    	else if ((winState == MNKGameState.WINP2 && in_botState == MNKCellState.P1) || (winState == MNKGameState.WINP1 && in_botState == MNKCellState.P2)) { // SCONFITTA
	    		in_foglia.setColor(Colors.RED);
	    		System.out.println("aoaoaoaoaoaoaoaoaoaooaaooaoaoaaoaoaoaoaoaoaaoaoo");
	    		System.out.println("winState: " + winState + " ; in_botState: " + in_botState);
	    		//in_foglia.setBeta(beta - 100000);
	    		in_foglia.setAlpha(Integer.MAX_VALUE);
	    	}
	    	else if (in_foglia.getMNKBoard().gameState() == MNKGameState.DRAW) {              // PAREGGIO
	    		in_foglia.setColor (Colors.GREY);
	    	}
	
	    }
	    // Fine else board OPEN
	
	    //------------------------------------------------------------------------------------------
	
	    System.out.println("llllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllll");

	}
	// Fine funzione assegnaValoreABFoglia
  
	
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void assegnaValoreABFoglia (TreeNode in_foglia, MNKCellState in_botState) {
		  // set: È quel "pezzo" di celle lungo tutta la linea (orizzontale, verticale o diagonale) che viene analizzato di votla in volta

		  //------------------------------------------------------------------------------------------

		  if (in_foglia.getMNKBoard().gameState() == MNKGameState.OPEN) {
		    MNKCellState currentPlayer = MNKCellState.P1;

		    MNKBoard board = in_foglia.getMNKBoard();     // Essendo la variabile MNKCellState[][] protetta, dovrebbe essere accessibile da questo programma
		    MNKCell[] MC = in_foglia.getMNKBoard().getMarkedCells();        // Nelle posizioni 0,2,4,... vi sono le mosse del P1, nelle posizioni 1,3,5,... vi sono le mosse del P2

		    boolean noEnemy = true;                       // Variabile che tiene conto della presenza di nemici in un set
		    boolean primoControllo = true;                // Si fanno due controlli (uno per ogni player), sarà true solo durante il primo di essi

		    preWinLimit = (int)((board.K / 3) * 2);       // preWin è pari ad un terzo di K
		    int[] vars = new int[lunghezza]; for (int i = 3; i < lunghezza; i++) vars[i] = 0;
		    vars[i_righe] = board.M - 1;
		    vars[j_colonne] = board.N - 1;
		    vars[k] = board.K;

		    // Si esegue la valutazione per attribuire un valore ad entrambi i simboli alpha e beta di ogni giocatore
		    for (int alphaBeta = 0; alphaBeta < 2; alphaBeta++) {

		      for (int pos = alphaBeta; pos < MC.length; pos += 2) {    // La prima volta scorre le pos del P1: 0,2,4,..., la seconda volta le posizioni del P2: 1,3,5,...
		        int i_MC = MC[pos].i;   // Coordinata i (riga) della cella in analisi
		        int j_MC = MC[pos].j;   // Coordinata j (colonna) della cella in analisi
		        //System.out.println ("[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]");
		        //System.out.println ("CELLA SET: (" + i_MC + "," + j_MC + ")");

		        // Controllo set orizzontale
		        //System.out.println ("AVVIO - Controllo orizzontale");
		        for (int c = 0; c <= vars[j_colonne]; c++) {     // Controllo della i-esima riga (da sx verso dx)
		          if (board.cellState(i_MC, c) == currentPlayer) currenPlayerCell (vars);
		          else if (board.cellState(i_MC, c) == MNKCellState.FREE) freeCell (vars);
		          else if (board.cellState(i_MC, c) != currentPlayer && board.cellState(i_MC, c) != MNKCellState.FREE) { enemyCell (vars); noEnemy = false; }
		          else System.out.println ("ERRORE - Funzione: assegnaValoreABFoglia - DOVE: Controllo orizzontale - R.104 circa");
		        }
		        editVar (vars, noEnemy);
		        noEnemy = true;

		        // Controllo riga in verticale
		        //System.out.println ("AVVIO - Controllo verticale");
		        for (int r = 0; r <= vars[i_righe]; r++) {     // Controllo della j-esima colonna (dall'alto verso il basso)
		          if (board.cellState(r, j_MC) == currentPlayer) currenPlayerCell (vars);
		          else if (board.cellState(r, j_MC) == MNKCellState.FREE) freeCell (vars);
		          else if (board.cellState(r, j_MC) != currentPlayer && board.cellState(r, j_MC) != MNKCellState.FREE) { enemyCell (vars); noEnemy = false; }
		          else System.out.println ("ERRORE - Funzione: assegnaValoreABFoglia - DOVE: Controllo verticale - R.114 circa");
		        }
		        editVar (vars, noEnemy);
		        noEnemy = true;

		        // Controllo diagonale
		        if (vars[i_righe] + 1 >= vars[k] && vars[j_colonne] + 1 >= vars[k]) {   // Se è possibile la creazione di diagonali
		          //System.out.println ("AVVIO - Controllo diagonale");
		          int move = 0;     // Variabile per lo scorrimento verso la posizione di partenza dei set diagonali, parte da 1 perchè da un valore minore di 1 si fanno incrementi inutili

		          // Scorrimento verso in alto a sx
		          while (i_MC - move > 0 && j_MC - move > 0) { move++; }      // Si incrementa move fino a quando sottratto alle coordinate i e j non ci si ritrova in (i - move, j - move = 0)
		          int start_i = i_MC - move;
		          int start_j = j_MC - move;

		          // Data la posizione più in alto a sx possibile (rispetto alla cella in analisi), controlla è in una posizione tale per cui vi sia almeno un set di lunghezza vars[k]
		          if (start_i + vars[k] - 1 <= vars[i_righe] && start_j + vars[k] - 1 <= vars[j_colonne]) {
		            //System.out.println("control_i: start_i + vars[k] - 1 = " + (start_i + vars[k] - 1));
		            //System.out.println("control_j: start_j + vars[k] - 1 = " + (start_j + vars[k] - 1));

		            // Da in alto a sx fino in basso a dx
		            //System.out.println ("AVVIO - ASX -> BDX - start_i: " + start_i + " ; start_j: " + start_j + " ; move: " + move);
		            for (move = 0; start_i + move < start_i + vars[k] && start_j + move < start_j + vars[k]; move++) {
		              if (board.cellState(start_i + move, start_j + move) == currentPlayer) currenPlayerCell (vars);
		              else if (board.cellState(start_i + move, start_j + move) == MNKCellState.FREE) freeCell(vars);
		              else if (board.cellState(start_i + move, start_j + move) != currentPlayer && board.cellState(start_i + move, start_j + move) != MNKCellState.FREE) { enemyCell (vars); noEnemy = false; }
		              else System.out.println ("ERRORE - FUNZIONE: assegnaValoreABFoglia - DOVE: Controllo diagonale: alto sx --> basso dx - R.134 circa");
		            }
		            editVar (vars, noEnemy);
		            noEnemy = true;
		          }

		          // Scorrimento verso in alto a dx
		          for (move = 0; i_MC - move > 0 && j_MC + move < vars[j_colonne]; move++) {}
		          start_i = i_MC - move;
		          start_j = j_MC + move;

		          if (start_i + vars[k] - 1 <= vars[i_righe] && start_j - vars[k] + 1 >= 0) {
		            //System.out.println("control_i: start_i + vars[k] - 1 = " + (start_i + vars[k] - 1));
		            //System.out.println("control_j: start_j - vars[k] + 1 = " + (start_j - vars[k] + 1));

		            // Da in alto a dx fino in basso a sx
		            //System.out.println ("AVVIO - ADX -> BSX - start_i: " + start_i + " ; start_j: " + start_j + " ; move: " + move);
		            for (move = 0; start_i + move < start_i + vars[k] && start_j - move >= 0; move++) {
		              if (board.cellState(start_i + move, start_j - move) == currentPlayer) currenPlayerCell (vars);
		              else if (board.cellState(start_i + move, start_j - move) == MNKCellState.FREE) freeCell (vars);
		              else if (board.cellState(start_i + move, start_j - move) != currentPlayer && board.cellState(start_i + move, start_j - move) != MNKCellState.FREE) { enemyCell (vars); noEnemy = false; }
		              else System.out.println ("ERRORE - FUNZIONE: assegnaValoreABFoglia - DOVE: Controllo diagonale: alto dx --> basso sx - R.149 circa");
		            }
		            editVar (vars, noEnemy);
		            noEnemy = true;
		          }
		          //System.out.println ("tmp: " + vars[tmp]);
		        }
		        // end if - controllo diagonale
		        else {  // Se il controllo diagonale non può essere eseguito a causa dei valori di M,N e K
		          System.out.println("NO DIAGONAL SET - FUNZIONE: assegnaValoreABFoglia - Valori MNK non consoni per set diagonali.");
		        }
		        // end if - else

		      }
		      // end for

		      if (primoControllo) {                                               // Se si stanno analizzando le celle del P1 (primo controllo, 0,2,4,...)
		        if (in_botState == MNKCellState.P1) vars[beta] = vars[tmp];       // Se il bot è p1 (ha iniziato per primo) --> *Primo controllo* beta = tmp
		        else vars[alpha] = vars[tmp];                                     // Altrimenti se il bot iniziato per secondo --> *Primo controllo* alpha = tmp
		        primoControllo = false;
		      } else {                                                            // Altrimenti, se si controllano le celle del P2 (second ocontrollo, 1,3,5,...)
		        if (in_botState == MNKCellState.P2) vars[beta] = vars[tmp];       // Se il bot è P1 (tmp definti dalle celle di P2) --> *Secondo controllo* alpha = tmp
		        else vars[alpha] = vars[tmp];                                     // Altrimenti se il boi inizia per secondo (e si è al secondo controllo) --> *Secondo controllo* beta = tmp
		      }

		      currentPlayer = MNKCellState.P2;

		      //System.out.println("RESET DI TMP");
		      vars[tmp] = 0;
		    }
		    // end for

		    in_foglia.setAlpha(vars[alpha]);
		    in_foglia.setBeta(vars[beta]);
		    // Aggiungere var per minmax

		  }
		  // Fine if board OPEN

		  else {
		    int alpha = in_foglia.getAlpha();
		    int beta = in_foglia.getBeta();
		    MNKGameState winState = in_foglia.getMNKBoard().gameState();
		    if ((winState == MNKGameState.WINP1 && in_botState == MNKCellState.P1) || (winState == MNKGameState.WINP2 && in_botState == MNKCellState.P2)) {      // VITTORIA
		      in_foglia.setColor(Colors.GREEN);
		      in_foglia.setBeta(Integer.MAX_VALUE);
		      //in_foglia.setAlpha(alpha - 100000);
		    }
		    else if ((winState == MNKGameState.WINP2 && in_botState == MNKCellState.P1) || (winState == MNKGameState.WINP1 && in_botState == MNKCellState.P2)) { // SCONFITTA
		      in_foglia.setColor(Colors.RED);
		      System.out.println("aoaoaoaoaoaoaoaoaoaooaaooaoaoaaoaoaoaoaoaoaaoaoo");
		      System.out.println("winState: " + winState + " ; in_botState: " + in_botState);
		      //in_foglia.setBeta(beta - 100000);
		      in_foglia.setAlpha(Integer.MAX_VALUE);
		    }
		    else if (in_foglia.getMNKBoard().gameState() == MNKGameState.DRAW) {              // PAREGGIO
		      in_foglia.setColor (Colors.GREY);
		    }
		  }

		  //------------------------------------------------------------------------------------------


		}
		// Fine funzione assegnaValoreABFoglia
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	  // 7 (?)
	  // Funzione che ritorna ogni foglia primoFiglio
	  public static TreeNode ritornaFogliePF (TreeNode in_radice) {
	    while (in_radice != null) {                              // Per ogni fratello
	      if (in_radice.getPrimoFiglio() == null) {              // Se il nodo in questione non ha figli
	        return in_radice;                                    // Lo si ritorna in quanto foglia
	      } else {                                               // Se invece ha dei figli
	        ritornaFogliePF(in_radice.getPrimoFiglio());  // Si scorre albero verso il basso
	      }
	      System.out.println ("in_radice_ " + in_radice);
	      in_radice = in_radice.getNext();
	    }
	    System.out.println ("ao");
	    return null;
	  }

	  /*
	  public static TreeNode ritornaFogliePF2 (TreeNode in_radice) {
	    //while (in_radice != null) {                              // Per ogni fratello
	      if (in_radice.getPrimoFiglio() == null) {              // Se il nodo in questione non ha figli
	        return in_radice;                                    // Lo si ritorna in quanto foglia
	      } else {                                               // Se invece ha dei figli
	        ritornaFogliePF2(in_radice.getPrimoFiglio());  // Si scorre albero verso il basso
	      }
	    System.out.println ("in_radice_ " + in_radice);
	      //in_radice = in_radice.getNext();
	    System.out.println ("ao");
	    return in_radice.getNext();
	  }
	  */

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
  // 1
  // Crea un nuovo sotto-albero per ogni cella libera (rappresentazione di ogni possible mossa)
  public void creaSottoAlbero (TreeNode in_radice, int depthLimit) {
    MNKCell[] FC = in_radice.getMNKBoard().getFreeCells();			// Serve per marcare la nuova posizione sulla board
    MNKCell[] MC = in_radice.getMNKBoard().getMarkedCells();		// Serve per marcare le precedenti posizioni sulla board
    int m = in_radice.getMNKBoard().M;
    int n = in_radice.getMNKBoard().N;
    int k = in_radice.getMNKBoard().K;

    for (int x = 0; x < FC.length; x++) {
      MNKBoard newMNKBoard = new MNKBoard(m, n, k);           // Creazione della nuova board
      for (int y = 0; y < MC.length; y++) {                   // Marcamento delle celle precedenti
        newMNKBoard.markCell (MC[y].i, MC[y].j);
      }
      newMNKBoard.markCell (FC[x].i, FC[x].j);                // "Nuovo marcamento"
      TreeNode nuovoSottoAlbero = new TreeNode (newMNKBoard, in_radice, null, null, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE);                         // Creazione del sottoalbero con la nuova board
    }
  }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  // Funzione inutile perchè la funzione createTree consete di crare un numero a piacere di livelli (1 compreso)
  public static void createTree_1LV (TreeNode in_padre) {
		if (in_padre.getMNKBoard().gameState() == MNKGameState.OPEN) {				// Se in_depthLimit > 1 --> Si crea un'altro livello
			MNKCell[] FC = in_padre.getMNKBoard().getFreeCells();
			MNKCell[] MC = in_padre.getMNKBoard().getMarkedCells();

			MNKBoard B_padre = in_padre.getMNKBoard();

			MNKBoard newB = new MNKBoard (M,N,K);
			for (int e = 0; e < MC.length; e++) {
				newB.markCell (MC[e].i, MC[e].j);
			}

			newB.markCell(FC[0].i, FC[0].j);
			TreeNode figlioMaggiore = new TreeNode(newB, in_padre, true, null);
			in_padre.setPrimoFiglio(figlioMaggiore);
			TreeNode prev = figlioMaggiore;

			for (int e = 0; e < FC.length; e++) {					// Ciclo per la creazione dei nodi di un livello
				MNKBoard newB2 = new MNKBoard (M,N,K);			// Crea una nuova board per ogni nodo del livello in questione
				for (int el = 0; el < MC.length; el++) {
					newB2.markCell (MC[el].i, MC[el].j);
				}

				newB2.markCell (FC[e].i, FC[e].j);					// Temporaneo marcamento della cella
				TreeNode figlio = new TreeNode (newB2, in_padre, false, prev);
				prev.setNext (figlio);											// Il fratello prev è ora collegato al suo nuovo fratello

				prev = figlio;															// Il nuovo figlio è ora il prev (ovvero l'ultimo figlio creato)
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

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// CREDoCstampi le MC di una MNKBoard
	public static void printMC (MNKBoard in_board) {
	  MNKCell[] FC = in_board.getFreeCells();
	  MNKCell[] MC = in_board.getMarkedCells();
	  for (int e = 0; e < MC.length; e++) {
	    System.out.println("MC[" + e + "] -> (" + MC[e].i + "," + MC[e].j + ")");
	  }
	  System.out.println("");
	  if (in_board.getFreeCells().length == 0) System.out.println("fine");
	  else {
	    in_board.markCell(in_board.getFreeCells()[0].i, in_board.getFreeCells()[0].j);
	    printMC (B);
	  }
	}

}

