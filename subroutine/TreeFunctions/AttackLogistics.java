package subroutine.TreeFunctions;

import mnkgame.MNKBoard;
import mnkgame.MNKCell;
import mnkgame.MNKCellState;
import mnkgame.MNKGameState;

public class AttackLogistics {
    
	public AttackLogistics () {}

    public void vaiAlleFoglie (TreeNode in_primoFiglio, MNKCellState in_botState) {
        /**
       * Invocazione: ???
       * 
       * Funzione: Va alle foglie dell'albero passato e ne decreta i valori alpha e
       * 			 beta tramite un'altra funzione
       */
      while (in_primoFiglio != null) {									// Per ogni figlio del nodo padre a cui è stata apllicata la funzione
            if (in_primoFiglio.getPrimoFiglio() == null)                       	// Se è una foglia
                assegnaValoreABFoglia (in_primoFiglio, in_botState);               	// Assegna i valori alpha e beta
            else                                                                // Altrimenti
                vaiAlleFoglie (in_primoFiglio.getPrimoFiglio(), in_botState);        // Chiamata ricorsiva sui sotto-alberi
            in_primoFiglio = in_primoFiglio.getNext();
        }
    }
    
  // Da spostare a livelli più globali, magari agli inizi della partita questo valore sarà più alto in modo tale da considerare come vincenti più situazioni, poi con l'avanzare della partita essa verrà incrementata per esserr più selettivi nelle condizioni di vittoria (ad esempio aumentando il valore di n ogni tot livelli dell'albero)
  private static int preWinLimit;   // Limite per decretare quando un strike si può reputare una situazione prossima alla vittoria

  private static final int AB_length = 15;             // Array contenenti le principali variabili (intere) della funzione assegnaValoreABFoglia
  // Indici delle variabili nell'array
  private static final int i_righe = 0;                // RIGHE    (i = M - 1)
  private static final int j_colonne = 1;              // COLONNE  (j = N - 1)
  private static final int k = 2;
  private static final int tmp = 5;
  private static final int alpha = 6;
  private static final int beta = 7;
  private static final int priority_i = 8;			// Coordinata (locale alla board del nodo) i della cella da difendere
  private static final int priority_j = 9;			// Coordinata (locale alla board del nodo) j della cella da difendere
  // Variabili per lo scorrimento delle varie righe/colonne/diagonali
  private static final int strike = 10;                 // Numero di simboli (identici) consecutivi (in determinate condizioni si resetta)
  private static final int maxStrike = 11;              // Massimo valore di strike registrato durante l'analisi della riga/colonna/diagonale in questione
  private static final int localWinSituations = 12;     // Situazioni vicine alla vittoria nella riga/colonna/diagonale in analisi
  private static final int globalWinSituations = 13;    // Situazione vicine alla vittoria nella tabella di gioco in questione
  // Variabili contenenti le coordinate della cella da difendere
  
  // 3.1
  // Funzione richiamata una volta finito di controllare un set
  private static void AB_editVar (int[] in_AB_vars, boolean in_noEnemy) {
      if (in_noEnemy)
          in_AB_vars[tmp] += 1;
      // Se non vi sono registrate situazioni prossime alla vittoria ma solo strike sufficientemente lunghi si incrementa tmp
      if (in_AB_vars[localWinSituations] == 0 && in_AB_vars[maxStrike] > (int)(in_AB_vars[k] / 2))
          in_AB_vars[tmp] += 2;
      in_AB_vars[strike] = 0;
      in_AB_vars[maxStrike] = 0;
  
      if (in_AB_vars[localWinSituations] > 0) {
        in_AB_vars[tmp] += 2;
        in_AB_vars[localWinSituations] = 0;
      }
  }

  // 3.2: aumenta valori quando trova cella del player che stai analizzando
  private static void AB_currenPlayerCell (int[] in_AB_vars, TreeNode in_nodo) {
      in_AB_vars[tmp] += 2 + in_AB_vars[strike];
      in_AB_vars[strike] += 1;
      if (in_AB_vars[strike] > in_AB_vars[maxStrike])
          in_AB_vars[maxStrike] = in_AB_vars[strike];
  }

  private static void AB_freeCell (int[] in_AB_vars, TreeNode in_node, int in_i, int in_j) {
      in_AB_vars[tmp] += 1;
      if (in_AB_vars[strike] > 0 && in_AB_vars[strike] >= preWinLimit) {
          in_AB_vars[localWinSituations]++;
          in_AB_vars[globalWinSituations]++;
          in_AB_vars[tmp] += 1;
          in_AB_vars[strike] = 0;
      }
  }

  private static void AB_enemyCell (int[] in_AB_vars) {
      in_AB_vars[tmp] -= 2;
      in_AB_vars[strike] = 0;
  }
  
  public void assegnaValoreABFoglia (TreeNode in_foglia, MNKCellState in_botState) {
      /* 
       * set: È quel "pezzo" di celle lungo tutta la linea (orizzontale, verticale o diagonale) che viene analizzato di votla in volta
       * 
       * currentPlayer parte settata .P1 perchè prima si analizzano i simboli di P1, poi viene settato .P2 per controllare i simboli
       * dell'altro player, mentre inbotState viene utilizzata per capire se assegnare tmp ad alpha o beta a fine controllo analisi della tabella.
      */
      
      if (in_foglia.getMNKBoard().gameState() == MNKGameState.OPEN) {
          MNKCellState currentPlayer = MNKCellState.P1;
  
          MNKBoard board = in_foglia.getMNKBoard();     	// Essendo la variabile MNKCellState[][] protetta, dovrebbe essere accessibile da questo programma
          MNKCell[] MC = board.getMarkedCells();        	// Nelle posizioni 0,2,4,... vi sono le mosse del P1, nelle posizioni 1,3,5,... vi sono le mosse del P2
  
          boolean noEnemy = true;                       	// Variabile che tiene conto della presenza di nemici in un set
          boolean primoControllo = true;                	// Si fanno due controlli (uno per ogni player), sarà true solo durante il primo di essi
  
          preWinLimit = (int)(board.K / 3);
          int[] AB_vars = new int[AB_length];
          AB_vars[i_righe] = board.M - 1;
          AB_vars[j_colonne] = board.N - 1;
          AB_vars[k] = board.K;
          AB_vars[priority_i] = -1;
          AB_vars[priority_j] = -1;
  
          // Si esegue la valutazione per attribuire un valore ad entrambi i simboli alpha e beta di ogni giocatore
          for (int alphaBeta = 0; alphaBeta < 2; alphaBeta++) {
  
              for (int pos = alphaBeta; pos < MC.length; pos += 2) {		// La prima volta scorre le pos del P1: 0,2,4,..., la seconda volta le posizioni del P2: 1,3,5,...
                  int i_MC = MC[pos].i;   								// Coordinata i (riga) della cella in analisi
                  int j_MC = MC[pos].j;   								// Coordinata j (colonna) della cella in analisi
  
                  // Controllo set orizzontale
                  for (int c = 0; c <= AB_vars[j_colonne]; c++) {     	// Controllo della i-esima riga (da sx verso dx)
                      if (board.cellState(i_MC, c) == currentPlayer)
                          AB_currenPlayerCell (AB_vars, in_foglia);
                      else if (board.cellState(i_MC, c) == MNKCellState.FREE)
                          AB_freeCell (AB_vars, in_foglia, i_MC, c);
                      else if (board.cellState(i_MC, c) != currentPlayer && board.cellState(i_MC, c) != MNKCellState.FREE) {
                          AB_enemyCell (AB_vars);
                          noEnemy = false;
                      }
                      else
                          System.out.println ("ERRORE - Funzione: assegnaValoreABFoglia - DOVE: Controllo orizzontale");
                  }
                  AB_editVar (AB_vars, noEnemy);
                  noEnemy = true;
  
                  // Controllo riga in verticale
                  for (int r = 0; r <= AB_vars[i_righe]; r++) {     			// Controllo della j-esima colonna (dall'alto verso il basso)
                      if (board.cellState(r, j_MC) == currentPlayer)
                          AB_currenPlayerCell (AB_vars, in_foglia);
                      else if (board.cellState(r, j_MC) == MNKCellState.FREE)
                          AB_freeCell (AB_vars, in_foglia, r, j_MC);
                      else if (board.cellState(r, j_MC) != currentPlayer && board.cellState(r, j_MC) != MNKCellState.FREE) {
                          AB_enemyCell (AB_vars);
                          noEnemy = false;
                      }
                      else
                          System.out.println ("ERRORE - Funzione: assegnaValoreABFoglia - DOVE: Controllo verticale");
                  }
                  AB_editVar (AB_vars, noEnemy);
                  noEnemy = true;
  
                  // Controllo diagonale
                  if (AB_vars[i_righe] + 1 >= AB_vars[k] && AB_vars[j_colonne] + 1 >= AB_vars[k]) {   // Se la mappa percmette la creazione di set diagonali
                      int move = 0;     													// Variabile per lo scorrimento verso la posizione di partenza dei set diagonali
  
                      // Scorrimento verso in alto a sx
                      while (i_MC - move > 0 && j_MC - move > 0)
                          move++;       		// Si incrementa move fino a quando sottratto alle coordinate i e j non ci si ritrova in (i - move, j - move = 0)
                      int start_i = i_MC - move;
                      int start_j = j_MC - move;
  
                      // Data la posizione più in alto a sx possibile (rispetto alla cella in analisi), controlla è in una posizione tale per cui vi sia almeno un set di lunghezza AB_vars[k]
                      if (start_i + AB_vars[k] - 1 <= AB_vars[i_righe] && start_j + AB_vars[k] - 1 <= AB_vars[j_colonne]) {
                          // Da in alto a sx fino in basso a dx
                          for (move = 0; start_i + move < start_i + AB_vars[k] && start_j + move < start_j + AB_vars[k]; move++) {
                              if (board.cellState(start_i + move, start_j + move) == currentPlayer)
                                  AB_currenPlayerCell (AB_vars, in_foglia);
                              else if (board.cellState(start_i + move, start_j + move) == MNKCellState.FREE)
                                  AB_freeCell (AB_vars, in_foglia, start_i + move, start_j + move);
                              else if (board.cellState(start_i + move, start_j + move) != currentPlayer && board.cellState (start_i + move, start_j + move) != MNKCellState.FREE) {
                                  AB_enemyCell (AB_vars);
                                  noEnemy = false;
                              }
                              else
                                  System.out.println ("ERRORE - FUNZIONE: assegnaValoreABFoglia - DOVE: Controllo diagonale: alto sx --> basso dx");
                          }
                          AB_editVar (AB_vars, noEnemy);
                          noEnemy = true;
                      }
  
                      // Scorrimento verso in alto a dx
                      for (move = 0; i_MC - move > 0 && j_MC + move < AB_vars[j_colonne]; move++) {}
                      start_i = i_MC - move;
                      start_j = j_MC + move;
  
                      if (start_i + AB_vars[k] - 1 <= AB_vars[i_righe] && start_j - AB_vars[k] + 1 >= 0) {
                          // Da in alto a dx fino in basso a sx
                          for (move = 0; start_i + move < start_i + AB_vars[k] && start_j - move >= 0; move++) {
                              if (board.cellState(start_i + move, start_j - move) == currentPlayer)
                                  AB_currenPlayerCell (AB_vars, in_foglia);
                              else if (board.cellState(start_i + move, start_j - move) == MNKCellState.FREE)
                                  AB_freeCell (AB_vars, in_foglia, start_i + move, start_j - move);
                              else if (board.cellState(start_i + move, start_j - move) != currentPlayer && board.cellState(start_i + move, start_j - move) != MNKCellState.FREE) {
                                  AB_enemyCell (AB_vars); 
                                  noEnemy = false;
                              }
                              else
                                  System.out.println ("ERRORE - FUNZIONE: assegnaValoreABFoglia - DOVE: Controllo diagonale: alto dx --> basso sx");
                          }
                          AB_editVar (AB_vars, noEnemy);
                          noEnemy = true;
                      }
                  }
                  // end if - controllo diagonale
                  else   	    						// Se il controllo diagonale non può essere eseguito a causa dei valori di M,N e K
                      System.out.println("NO DIAGONAL SET - FUNZIONE: assegnaValoreABFoglia - Valori MNK non consoni per set diagonali.");
                  // end if - else
  
              }
              // end for
  
              if (primoControllo) {                                               		// Se si stanno analizzando le celle del P1 (primo controllo, 0,2,4,...)
                  if (in_botState == MNKCellState.P1)											// Se il bot è p1 (ha iniziato per primo) --> *Primo controllo* beta = tmp
                      AB_vars[beta] = AB_vars[tmp];
                  else																		// Altrimenti se il bot iniziato per secondo --> *Primo controllo* alpha = tmp
                      AB_vars[alpha] = AB_vars[tmp];
                  primoControllo = false;
              } else {                                                            		// Altrimenti, se si controllano le celle del P2 (second ocontrollo, 1,3,5,...)
                  if (in_botState == MNKCellState.P2)     								  	// Se il bot è P1 (tmp definti dalle celle di P2) --> *Secondo controllo* alpha = tmp
                      AB_vars[beta] = AB_vars[tmp];
                  else                                     									// Altrimenti se il boi inizia per secondo (e si è al secondo controllo) --> *Secondo controllo* beta = tmp
                      AB_vars[alpha] = AB_vars[tmp];
              }
              
              currentPlayer = MNKCellState.P2;
  
              AB_vars[tmp] = 0;
          }
          // end for
  
          in_foglia.setAlpha(AB_vars[alpha]);
          in_foglia.setBeta(AB_vars[beta]);
  
      }
      // Fine if board OPEN

      else {
          MNKGameState winState = in_foglia.getMNKBoard().gameState();
          if ((winState == MNKGameState.WINP1 && in_botState == MNKCellState.P1) ||
              (winState == MNKGameState.WINP2 && in_botState == MNKCellState.P2)) {      		// VITTORIA
              in_foglia.setColor(Colors.GREEN);
              //System.out.println("CELLA DI VITTORIA PER IL BOT");
              in_foglia.setBeta(Integer.MAX_VALUE);
          }
          else if ((winState == MNKGameState.WINP2 && in_botState == MNKCellState.P1) ||
                  (winState == MNKGameState.WINP1 && in_botState == MNKCellState.P2)) { 		// SCONFITTA
              in_foglia.setColor(Colors.RED);
              //System.out.println("CELLA DI VITTORIA AVVERSARIA");
              System.out.println("winState: " + winState + " ; in_botState: " + in_botState);
              in_foglia.setAlpha(Integer.MAX_VALUE);
          }
          else if (in_foglia.getMNKBoard().gameState() == MNKGameState.DRAW) {              	// PAREGGIO
              in_foglia.setColor (Colors.GREY);
              //System.out.println("CELLA DI PAREGGIO");
          }
  
      }
      // Fine else board OPEN
      
  }
  // Fine funzione assegnaValoreABFoglia

}
