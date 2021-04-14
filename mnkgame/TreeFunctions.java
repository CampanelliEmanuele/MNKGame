package mnkgame;

public class TreeFunctions {

  // 2
  // Funzione che va alle foglie dell'albero (e ne assegna il valore tramite un'altra funzione) + il relativo codice di invocazione
  public void vaiAlleFoglie (TreeNode in_primoDeiFigli) {    // Preso il padre, visiterà l'albero (in qualsiasi modo) fino ad arrivare alle foglie ed attribuire ad esse un valore che sarà utilizzato dall'algoritmo alpha beta pruning
    while (in_primoDeiFigli != null) {                        // Per ogni figlio del nodo padre su cui è stata invocata la funzione
      if (in_primoDeiFigli.getPrimoFiglio() == null)          // Se è una foglia
        assegnaValoreABFoglia (in_primoDeiFigli);
      else {                                                  // Se non è una foglia
          vaiAlleFoglie (in_primoDeiFigli.getPrimoFiglio());  // Si applica la funzione nei sotto-alberi
      }
      in_primoDeiFigli = in_primoDeiFigli.getNext();
    }
  }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  // 3
  // Da spostare a liveli più globali, magari agli inizi della partita questo valore sarà più alto in modo tale da considerare come vincenti più situazioni, poi con l'avanzare della partita essa verrà incrementata per esserr più selettivi nelle condizioni di vittoria (ad esempio aumentando il valore di n ogni tot livelli dell'albero)
  public static int preWinLimit = -1 /* parteIntera (il n% di K) VALORE DA DECRETARE*/;   // Limite per decretare quando uno strike si può reputare una situazione prossima alla vittoria

  public static final int lunghezza = 30;             // Array contenenti le principali variabili (intere) della funzione assegnaValoreABFoglia

  // Indici delle variabili nell'array
  public static final int i_righe = 0;                // RIGHE    (i = M - 1)
  public static final int j_colonne = 1;              // COLONNE  (j = N - 1)
  public static final int k = 2;                      // SERIE
  public static final int tmp = 10;
  public static final int alpha = 11;
  public static final int beta = 12;
  // Variabili per lo scorrimento delle varie righe/colonne/diagonali
  public static final int strike = 21;                 // Numero di simboli (identici) consecutivi (in determinate condizioni si resetta)
  public static final int maxStrike = 22;              // Massimo valore di strike registrato durante l'analisi della riga/colonna/diagonale in questione
  public static final int localWinSituations = 23;     // Situazioni vicine alla vittoria nella riga/colonna/diagonale in analisi
  public static final int globalWinSituations = 24;    // Situazione vicine alla vittoria nella tabella di gioco in questione

  // 3.1
  public static void editVar (int[] in_vars, boolean in_noEnemy) {
    if (in_noEnemy) in_vars[tmp]++;
    // Se non vi sono registrate situazioni prossime alla vittoria ma solo strike sufficiente lunghi si incrementa in_beta
    if (in_vars[localWinSituations] == 0 && in_vars[maxStrike] >= (int)(in_vars[k] / 4)) in_vars[beta] += in_vars[maxStrike] / 4;
    in_vars[maxStrike] = 0;
    in_vars[strike] = 0;
    in_vars[localWinSituations] = 0;
  }

  // 3.2
  public static void playerCell (int[] in_vars) {
    in_vars[tmp] += 2;
    in_vars[strike] += 1;
    if (in_vars[strike] > in_vars[maxStrike]) in_vars[maxStrike] = in_vars[strike];
  }

  public static void freeCell (int[] in_vars) {
    in_vars[tmp] += 1;
    if (in_vars[strike] > 0 && in_vars[strike] >= preWinLimit) {
      in_vars[localWinSituations]++;
      in_vars[globalWinSituations]++;
      in_vars[tmp]++;
      in_vars[strike] = 0;
    }
  }

  //public static void enemyCell (int in_beta, int in_strike, int in_maxStrike) {
  public static void enemyCell (int[] in_vars) {
    in_vars[tmp]--;
    in_vars[strike] = 0;
  }

  public static void assegnaValoreABFoglia (TreeNode in_foglia) {
    /* CONTROLLARE:
      - Errori di distrazione
      - Errori semantici
      - Cicli for -> Codice nelle parentesi tonde
    */
    MNKCellState botState = MNKCellState.P2; if (first) botState = MNKCellState.P1;
    MNKCellState currentPlayer = botState;

    MNKCell[] MC = in_foglia.getMNKBoard().getMarkedCells();      // Nelle posizioni 0,2,4,... vi sono le mosse del P!, nelle posizioni 1,3,5,... vi sono le mosse del P2
    //MNKCellState[][] tabellaGioco = in_foglia.getMNKBoard().B;    // Essendo la variabile MNKCellState[][] protetta, dovrebbe essere accessibile da questo programma
    MNKBoard tabellaGioco = in_foglia.getMNKBoard();    // Essendo la variabile MNKCellState[][] protetta, dovrebbe essere accessibile da questo programma

    boolean noEnemy = true;
    boolean primoControllo = true;

    int[] vars = new int[lunghezza]; for (int i = 0; i < lunghezza; i++) vars[i] = 0;

    // Si esegue la valutazione per attribuire un valore ad entrambi i simboli alpha e beta
    for (int alphaBeta = 0; alphaBeta < 2; alphaBeta++) {
      for (int pos = alphaBeta; pos < MC.length; pos += 2) {    // La prima volta scorre le pos del player 0,2,4,..., la seconda volta le posizioni avversarie 1,3,5,...
        int i_MC = MC[pos].i;   // Coordinata i (riga) della cella in analisi
        int j_MC = MC[pos].j;   // Coordinata j (colonna) della cella in analisi

        // Controllo set orizzontale
        for (int c = 0; c < j_colonne; c++) {     // Controllo della i-esima righe (da sx verso dx)
          //if (tabellaGioco[i_MC][c].state == currentPlayer) playerCell (vars);
          //if (tabellaGioco[i_MC][c].state == MNKCellState.FREE) freeCell (vars);
          //if (tabellaGioco[i_MC][c].state != currentPlayer && tabellaGioco[i_MC][c].state != MNKCellState.FREE) { enemyCell (vars); noEnemy = false; }
          if (tabellaGioco.cellState(i_MC, c) == currentPlayer) playerCell (vars);
          if (tabellaGioco.cellState(i_MC, c) == MNKCellState.FREE) freeCell (vars);
          if (tabellaGioco.cellState(i_MC, c) != currentPlayer && tabellaGioco.cellState(i_MC, c) != MNKCellState.FREE) { enemyCell (vars); noEnemy = false; }
        }
        editVar (vars, noEnemy);
        noEnemy = true;

        // Controllo riga in verticale
        for (int r = 0; r < i_righe; r++) {     // Controllo della j-esima colonna (dall'alto verso il basso)
          //if (tabellaGioco[r][j_MC].state == currentPlayer) playerCell (vars);
          //if (tabellaGioco[r][j_MC].state == MNKCellState.FREE) freeCell (vars);
          //if (tabellaGioco[r][j_MC].state != currentPlayer && tabellaGioco[r][j_MC].state != MNKCellState.FREE) enemyCell (vars);

          if (tabellaGioco.cellState(r, j_MC) == currentPlayer) playerCell (vars);
          if (tabellaGioco.cellState(r, j_MC) == MNKCellState.FREE) freeCell (vars);
          if (tabellaGioco.cellState(r, j_MC) != currentPlayer && tabellaGioco.cellState(r, j_MC) != MNKCellState.FREE) enemyCell (vars);
        }
        editVar (vars, noEnemy);
        noEnemy = true;

        // Controllo diagonale
        if (vars[i_righe] + 1 >= k && vars[j_colonne] + 1 >= k) {   // Se è possibile la creazione di diagonali
          System.out.println("Controllo diagonale eseguito.");
          int move = 1;

          // Scorrimento verso in alto a sx
          for (move = 1; i_MC - move > 0 && j_MC - move > 0; move++) {}  // Si incrementa move fino a quando sottratto alle coordinate i ed j non ci si ritrova in (i - move, j - move = 0)
          int start_i = i_MC - move;  // DA CONTROLLARE SE SERVE + 1 !!!!!!!!!!
          int start_j = j_MC - move;

          // Discesa fino in basso a dx
          for (move = 1; start_i + move <= start_i + vars[k] && start_j + move <= start_j + vars[k]; move++) {
            //if (tabellaGioco[start_i + move][start_j + move].state == currentPlayer) playerCell (vars);
            //if (tabellaGioco[i_righe + move][j_colonne + move].state == MNKCellState.FREE) freeCell(vars);
            //if (tabellaGioco[i_righe + move][j_colonne + move].state != currentPlayer && tabellaGioco[i_righe + move][j_colonne + move].state != MNKCellState.FREE) enemyCell(vars);
            if (tabellaGioco.cellState(start_i + move, start_j + move) == currentPlayer) playerCell (vars);
            if (tabellaGioco.cellState(start_i + move, start_j + move) == MNKCellState.FREE) freeCell(vars);
            if (tabellaGioco.cellState(start_i + move, start_j + move) != currentPlayer && tabellaGioco.cellState(start_i + move, start_j + move) != MNKCellState.FREE) enemyCell(vars);
          }
          editVar (vars, noEnemy);
          noEnemy = true;

          // Scorrimento verso in alto a dx
          for (move = 1; i_MC - move < i_righe && j_MC + move < j_colonne; move++)    // Controllare il reset di move  !!!!!!!!!!!!!!!!!!!!
          start_i = i_MC - move;  // DA CONTROLLARE SE SERVE + 1 !!!!!!!!!!
          start_j = j_MC - move;

          // Discesa fino in basso a sx
          for (move = 1; start_i + move <= start_i + vars[k] && start_j - move <= 0; move++) {
            //if (tabellaGioco[start_i + move][start_j - move].state == currentPlayer) playerCell (vars);
            //if (tabellaGioco[start_i + move][start_j - move].state == MNKCellState.FREE) freeCell (vars);
            //if (tabellaGioco[start_i + move][start_j - move].state != currentPlayer && tabellaGioco[i_righe + move][j_colonne - move].state != MNKCellState.FREE) enemyCell (vars);
            if (tabellaGioco.cellState(start_i + move, start_j - move) == currentPlayer) playerCell (vars);
            if (tabellaGioco.cellState(start_i + move, start_j - move) == MNKCellState.FREE) freeCell (vars);
            if (tabellaGioco.cellState(start_i + move, start_j - move) != currentPlayer && tabellaGioco.cellState(start_i + move, start_j - move) != MNKCellState.FREE) enemyCell (vars);
          }
          editVar (vars, noEnemy);
          noEnemy = true;

          }
        else {
          System.out.println("controllo diagonale Impossibile.");
        }
        // end if - else
      }
      // end for

      if (primoControllo) {                           // Se si stanno analizzando le celle del P1 (primo controllo, 0,2,4,...)
        if (first) vars[beta] = vars[tmp];       // Se il bot è p1 (ha iniziato per primo) --> *Primo controllo* beta = tmp
        else vars[alpha] = vars[tmp];                 // Altrimenti se il bot iniziato per secondo --> *Primo controllo* alpha = tmp
        primoControllo = false;
      } else {                                        // Altrimenti, se si controllano le celle del P2 (second ocontrollo, 1,3,5,...)
        if (first) vars[alpha] = vars[tmp];      // Se il bot è P1 (tmp definti dalle celle di P2) --> *Secondo controllo* alpha = tmp
        else vars[beta] = vars[tmp];                  // Altrimenti se il boi inizia per secondo (e si è al secondo controllo) --> *Secondo controllo* beta = tmp
      }

      if (botState == MNKCellState.P1) currentPlayer = MNKCellState.P2;   // Si cambia il giocatore a cui guardare i segni
      else currentPlayer = MNKCellState.P1;

      vars[tmp] = 0;
    }
    // end for
  }

}
