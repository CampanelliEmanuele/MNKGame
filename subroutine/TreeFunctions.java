package subroutine;

import mnkgame.*;
import mnkgame.MNKCell;
import mnkgame.MNKCellState;
import mnkgame.MNKGameState;

public class TreeFunctions {

	protected TreeFunctions () {}
	
	// Per memorizzare i sottoalberi verrà utilizzata una struttura dati dinamica omogena lineare

	// 5
	protected static void createTree (TreeNode in_padre, int in_depthLimit, boolean in_first) {
		if (in_depthLimit > 1) {
			if (in_padre.getMNKBoard().gameState() == MNKGameState.OPEN) {					// Se in_depthLimit > 1 --> Si crea un'altro livello
				//System.out.println("Stato: generazione - Livello: " + (5 - in_depthLimit) + " - Local B: " + in_padre.getMNKBoard());
				MNKCell[] FC = in_padre.getMNKBoard().getFreeCells();
				MNKCell[] MC = in_padre.getMNKBoard().getMarkedCells();
				int M = in_padre.getMNKBoard().M;
				int N = in_padre.getMNKBoard().N;
				int K = in_padre.getMNKBoard().K;
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

					createTree (primoFiglio, in_depthLimit - 1, in_first);
					//tmpB.unmarkCell ();													// Si smarca la prima cella
					TreeNode prev = primoFiglio;											// Prev creato uguale al primoFiglio

					for (int e = 1; e < FC.length; e++) {									// Ciclo per la creazione dei nodi di un livello
						MNKBoard tmp2B = new MNKBoard (M,N,K);								// Crea una nuova board per ogni nodo del livello in questione
						for (int el = 0; el < MC.length; el++) tmp2B.markCell (MC[el].i, MC[el].j);

						tmp2B.markCell (FC[e].i, FC[e].j);									// Temporaneo marcamento della cella
						TreeNode figlio = new TreeNode (tmp2B, in_padre, false, prev);
						prev.setNext (figlio);												// Il fratello prev è ora collegato al suo nuovo fratello

						prev = figlio;														// Il nuovo figlio è ora il prev (ovvero l'ultimo figlio creato)
						createTree (figlio, in_depthLimit - 1, in_first);
					}

					in_padre = in_padre.getNext();
				}

			}
			// Fine if
			else if (in_padre.getMNKBoard().gameState() == MNKGameState.WINP1) {
				if (in_first) in_padre.setColor(Colors.GREEN);
				else in_padre.setColor(Colors.RED);
			}
			else if (in_padre.getMNKBoard().gameState() == MNKGameState.WINP2) {
				if (in_first) in_padre.setColor(Colors.RED);
				else in_padre.setColor(Colors.GREEN);
			}
			else in_padre.setColor(Colors.GREY);
		}

	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  	// 2
  	// Funzione che va alle foglie dell'albero (e ne assegna il valore tramite un'altra funzione) + il relativo codice di invocazione
  	protected void vaiAlleFoglie (TreeNode in_primoFiglio, MNKCellState in_botState) {     // Preso il padre, visiterà l'albero (in qualsiasi modo) fino ad arrivare alle foglie ed attribuire ad esse un valore che sarà utilizzato dall'algoritmo alpha beta pruning
  		while (in_primoFiglio != null) {                                                  // Per ogni figlio del nodo padre a cui è stata apllicata la funzione
  			if (in_primoFiglio.getPrimoFiglio() == null)                                    // Se è una foglia
  				assegnaValoreABFoglia (in_primoFiglio, in_botState);                          // Assegna i valori alpha e beta
  			else {                                                                          // Se non è una foglia
  				vaiAlleFoglie (in_primoFiglio.getPrimoFiglio(), in_botState);               // Si applica la funzione nei sotto-alberi
  			}
  			in_primoFiglio = in_primoFiglio.getNext();
  		}
  	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
	// Variabili contenenti le coordinate della cella da difendere
	private static final int cp_counter = 15;				// Conta quante celle dello stesso tipo si susseguono, si resetta dopo due freeCell consecutive (ha 1 freeCell di margine perchè si fa un controllo per K-1 simboli)
	private static final int fc_counter = 16;				// Tiene conto delle freeCell che si susseguono
	private static final int defense_i = 17;				// Coordinata i della cella da difendere (locale)
	private static final int defense_j = 18;				// Coordinata j della cella da difendere (locale)

	/* Pseudo-codice per la difesa di una cella
	If (currentPlayer)
		cp_counter ++
		If (cp_counter >= K) cp_counter = 1
		If (cp_counter == K-1 && vars[defense_i] >= 0) 		-> A
			Nodo.setDefense_i(vars[defense_i])
			Nodo.setDefense_j(vars[defense_j])
		fc_counter = 0
	Else if (freeCell)
		fc_counter++
		If (fc_counter > 1) cp_counter = 0                	-> B/C
		vars[defense_i] = cella_in_questione.i
		vars[defense_j] = cella_in_questione.j
		If (in_vars[cp_counter] == in_vars[k] - 1)
			Nodo.setDefense_i(vars[defense_i])
			Nodo.setDefense_j(vars[defense_j])
			cp_counter = 0                                   -> B
	Else if (enemyCell)
		cp_counter = 0
		fc_counter = 0
		vars[defense_i] = -1
		vars[defense_j] = -1
	*/
	
	// 3.1
	// Funzione richiamata una volta finito di controllare un set
	private static void editVar (int[] in_vars, boolean in_noEnemy) {
	    if (in_noEnemy) in_vars[tmp]++;
	    // Se non vi sono registrate situazioni prossime alla vittoria ma solo strike sufficientemente lunghi si incrementa tmp
	    if (in_vars[localWinSituations] == 0 && in_vars[maxStrike] > (int)(in_vars[k] / 2)) in_vars[tmp] += 2;
	    in_vars[strike] = 0;
	    in_vars[maxStrike] = 0;
	
	    if (in_vars[localWinSituations] > 0) {
	      in_vars[tmp] += 2;
	      in_vars[localWinSituations] = 0;
	    }
	    
	    // Ad ogni nuovo controllo, bisogna resettare tali variabili per:
	    in_vars[cp_counter] = 0;	// Tenere il conto dei simboli nel nuovo set in analisi
	    in_vars[fc_counter] = 0;
	    in_vars[defense_i] = -1;	// Si resettano le coordinate locali della cella da difendere
	    in_vars[defense_j] = -1;
	}

	// 3.2: aumenta valori quando trova cella del player che stai analizzando
	private static void currenPlayerCell (int[] in_vars, TreeNode in_nodo) {
		//System.out.println ("currenPlayerCell tmp +2");
	    in_vars[tmp] += 2;
	    in_vars[strike] += 1;
	    if (in_vars[strike] > in_vars[maxStrike]) in_vars[maxStrike] = in_vars[strike];

	    in_vars[cp_counter]++;														// Si aumenta il conteggio delle celle (del player in analisi) in serie
		if (in_vars[cp_counter] >= in_vars[k]) in_vars[cp_counter] = 1;				// Se cp_counter ha valori "sballati" si pone a 1
		if (in_vars[cp_counter] == in_vars[k] - 1 && in_vars[defense_i] >= 0) {		// Controllo per vedere è stata trovata la cella da difendere
			in_nodo.setDefense_i(in_vars[defense_i]);
			in_nodo.setDefense_j(in_vars[defense_j]);
		}
		in_vars[fc_counter] = 0;				// Reset del counter delle freeCell
	}

	private static void freeCell (int[] in_vars, TreeNode in_node, int in_i, int in_j) {
		//System.out.println ("freeCell tmp +1");
	    in_vars[tmp] += 1;
	    if (in_vars[strike] > 0 && in_vars[strike] >= preWinLimit) {
	    	in_vars[localWinSituations]++;
	    	in_vars[globalWinSituations]++;
	    	in_vars[tmp]++;
	    	in_vars[strike] = 0;
	    }
	    
	    
		in_vars[fc_counter]++;
		if (in_vars[fc_counter] > 1) in_vars[cp_counter] = 0;	// Se vi sono 2 o più celle libere affiancate si resetta cp_counter
		
		in_vars[defense_i] = in_i;								// Si salvano localmente le coordinate della cella in questione
		in_vars[defense_j] = in_j;

		if (in_vars[cp_counter] == in_vars[k] - 1) {			// Controllo per capire se è una cella da difendere
			in_node.setDefense_i(in_vars[defense_i]);
			in_node.setDefense_j(in_vars[defense_j]);
			in_vars[cp_counter] = 0;							// Una volta salvato nel nodo le coordinate da difendere, si resetta cp_counter
		}
	}

	private static void enemyCell (int[] in_vars) {
	    //System.out.println ("enemyCell tmp -1");
	    in_vars[tmp]--;
	    in_vars[strike] = 0;
	    
	    in_vars[cp_counter] = 0;
		in_vars[fc_counter] = 0;
		in_vars[defense_i] = -1;
		in_vars[defense_j] = -1;
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
	    	vars[defense_i] = -1;
	    	vars[defense_j] = -1;
	
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
	    				if (board.cellState(i_MC, c) == currentPlayer) currenPlayerCell (vars, in_foglia);
	    				else if (board.cellState(i_MC, c) == MNKCellState.FREE) freeCell (vars, in_foglia, i_MC, c);
	    				else if (board.cellState(i_MC, c) != currentPlayer && board.cellState(i_MC, c) != MNKCellState.FREE) { enemyCell (vars); noEnemy = false; }
	    				else System.out.println ("ERRORE - Funzione: assegnaValoreABFoglia - DOVE: Controllo orizzontale - R.104 circa");
	    			}
	    			editVar (vars, noEnemy);
	    			noEnemy = true;
	
	    			// Controllo riga in verticale
	    			System.out.println ("AVVIO - Controllo verticale");
	    			for (int r = 0; r <= vars[i_righe]; r++) {     // Controllo della j-esima colonna (dall'alto verso il basso)
	    				if (board.cellState(r, j_MC) == currentPlayer) currenPlayerCell (vars, in_foglia);
	    				else if (board.cellState(r, j_MC) == MNKCellState.FREE) freeCell (vars, in_foglia, r, j_MC);
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
	    						if (board.cellState(start_i + move, start_j + move) == currentPlayer) currenPlayerCell (vars, in_foglia);
	    						else if (board.cellState(start_i + move, start_j + move) == MNKCellState.FREE) freeCell(vars, in_foglia, start_i + move, start_j + move);
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
	    						if (board.cellState(start_i + move, start_j - move) == currentPlayer) currenPlayerCell (vars, in_foglia);
	    						else if (board.cellState(start_i + move, start_j - move) == MNKCellState.FREE) freeCell (vars, in_foglia, start_i + move, start_j - move);
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
  
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	  
	// Ritorna il nodo migliore tra i nodi del livello sottostante a quello del padre
	protected static TreeNode sceltaPercorso_1LV (TreeNode in_padre) {

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
	  
  
  
  /*
  protected static TreeNode defense (TreeNode in_padre, MNKGameState in_botState) {
    if (in_botState == MNKCellState.P1) int pos = 0;
    else if (in_botState == MNKCellState.P2) int pos = 1;

    int i_MC = MC[pos].i;   // Coordinata i (riga) della cella in analisi
    int j_MC = MC[pos].j;   // Coordinata j (colonna) della cella in analisi
    //System.out.println ("[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]");
    //System.out.println ("CELLA SET: (" + i_MC + "," + j_MC + ")");
    MNKBoard board = in_foglia.getMNKBoard();     // Essendo la variabile MNKCellState[][] protetta, dovrebbe essere accessibile da questo programma
    int i = board.M;
    int j = board.N;
    int k = board.K;
    int counter = 0;

    // Controllo set orizzontale
    //System.out.println ("AVVIO - Controllo orizzontale");
    for (int c = 0; c <= j; c++) {     // Controllo della i-esima righe (da sx verso dx)
      if (board.cellState(i_MC, c) == in_botState) counter++;
      else if (board.cellState(i_MC, c) == MNKCellState.FREE)
      else if (board.cellState(i_MC, c) != in_botState && board.cellState(i_MC, c) != MNKCellState.FREE)
      else System.out.println ("ERRORE - Funzione: assegnaValoreABFoglia - DOVE: Controllo orizzontale - R.104 circa");
    }

    // Controllo riga in verticale
    //System.out.println ("AVVIO - Controllo verticale");
    for (int r = 0; r <= vars[i_righe]; r++) {     // Controllo della j-esima colonna (dall'alto verso il basso)
      if (board.cellState(r, j_MC) == in_botState)
      else if (board.cellState(r, j_MC) == MNKCellState.FREE)
      else if (board.cellState(r, j_MC) != in_botState && board.cellState(r, j_MC) != MNKCellState.FREE)
      else System.out.println ("ERRORE - Funzione: assegnaValoreABFoglia - DOVE: Controllo verticale - R.114 circa");
    }

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
            if (board.cellState(start_i + move, start_j + move) == in_botState)
            else if (board.cellState(start_i + move, start_j + move) == MNKCellState.FREE)
            else if (board.cellState(start_i + move, start_j + move) != in_botState && board.cellState(start_i + move, start_j + move) != MNKCellState.FREE)
            else System.out.println ("ERRORE - FUNZIONE: assegnaValoreABFoglia - DOVE: Controllo diagonale: alto sx --> basso dx - R.134 circa");
          }
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
            if (board.cellState(start_i + move, start_j - move) == in_botState)
            else if (board.cellState(start_i + move, start_j - move) == MNKCellState.FREE)
            else if (board.cellState(start_i + move, start_j - move) != in_botState && board.cellState(start_i + move, start_j - move) != MNKCellState.FREE)
            else System.out.println ("ERRORE - FUNZIONE: assegnaValoreABFoglia - DOVE: Controllo diagonale: alto dx --> basso sx - R.149 circa");
          }
        }
        //System.out.println ("tmp: " + vars[tmp]);
      }
      // end if - controllo diagonale
      else {
        //System.out.println("NO DIAGONAL SET - FUNZIONE: assegnaValoreABFoglia - Valori MNK non consoni per set diagonali.");
      }
      // end if - else

    }
    // end for
  }
  */

}
// Fine class TreeFunctions
