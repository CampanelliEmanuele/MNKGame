package mnkgame;

public class TreeFunctions {

  public TreeFunctions () {}

  // 2
  // Funzione che va alle foglie dell'albero (e ne assegna il valore tramite un'altra funzione) + il relativo codice di invocazione
  public void vaiAlleFoglie (TreeNode in_primoFiglio, boolean in_first) {     // Preso il padre, visiterà l'albero (in qualsiasi modo) fino ad arrivare alle foglie ed attribuire ad esse un valore che sarà utilizzato dall'algoritmo alpha beta pruning
    while (in_primoFiglio != null) {                        // Per ogni figlio del nodo padre a cui è stata apllicata la funzione
      if (in_primoFiglio.getPrimoFiglio() == null)          // Se è una foglia
        assegnaValoreABFoglia (in_primoFiglio, in_first);   // Assegna i valori alpha e beta
      else {                                                  // Se non è una foglia
          vaiAlleFoglie (in_primoFiglio.getPrimoFiglio(), in_first);  // Si applica la funzione nei sotto-alberi
      }
      in_primoFiglio = in_primoFiglio.getNext();
    }
  }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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



//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  // 3
  // Da spostare a livelli più globali, magari agli inizi della partita questo valore sarà più alto in modo tale da considerare come vincenti più situazioni, poi con l'avanzare della partita essa verrà incrementata per esserr più selettivi nelle condizioni di vittoria (ad esempio aumentando il valore di n ogni tot livelli dell'albero)
  public static int preWinLimit;   // Limite per decretare quando uno strike si può reputare una situazione prossima alla vittoria

  public static final int lunghezza = 15;             // Array contenenti le principali variabili (intere) della funzione assegnaValoreABFoglia
  // Indici delle variabili nell'array
  public static final int i_righe = 0;                // RIGHE    (i = M - 1)
  public static final int j_colonne = 1;              // COLONNE  (j = N - 1)
  public static final int k = 2;                      // SERIE
  public static final int tmp = 5;
  public static final int alpha = 6;
  public static final int beta = 7;
  // Variabili per lo scorrimento delle varie righe/colonne/diagonali
  public static final int strike = 10;                 // Numero di simboli (identici) consecutivi (in determinate condizioni si resetta)
  public static final int maxStrike = 11;              // Massimo valore di strike registrato durante l'analisi della riga/colonna/diagonale in questione
  public static final int localWinSituations = 12;     // Situazioni vicine alla vittoria nella riga/colonna/diagonale in analisi
  public static final int globalWinSituations = 13;    // Situazione vicine alla vittoria nella tabella di gioco in questione

  // 3.1
  public static void editVar (int[] in_vars, boolean in_noEnemy) {
    if (in_noEnemy) in_vars[tmp]++;
    // Se non vi sono registrate situazioni prossime alla vittoria ma solo strike sufficientemente lunghi si incrementa tmp
    if (in_vars[localWinSituations] == 0 && in_vars[maxStrike] > (int)(in_vars[k] / 2)) in_vars[tmp] += 2;
    in_vars[maxStrike] = 0;
    in_vars[strike] = 0;
    if (in_vars[localWinSituations] > 0) {
      in_vars[tmp] += 2;
      in_vars[localWinSituations] = 0;
    }
    //System.out.println ("in_vars[tmp]: " + in_vars[tmp]);
  }

  // 3.2
  public static void currenPlayerCell (int[] in_vars) {
    //System.out.println ("currenPlayerCell tmp +2");
    in_vars[tmp] += 2;
    in_vars[strike] += 1;
    if (in_vars[strike] > in_vars[maxStrike]) in_vars[maxStrike] = in_vars[strike];
  }

  public static void freeCell (int[] in_vars) {
    //System.out.println ("freeCell tmp +1");
    in_vars[tmp] += 1;
    if (in_vars[strike] > 0 && in_vars[strike] >= preWinLimit) {
      in_vars[localWinSituations]++;
      in_vars[globalWinSituations]++;
      in_vars[tmp]++;
      in_vars[strike] = 0;
    }
  }

  public static void enemyCell (int[] in_vars) {
    //System.out.println ("enemyCell tmp -1");
    in_vars[tmp]--;
    in_vars[strike] = 0;
  }

  public static void assegnaValoreABFoglia (TreeNode in_foglia, boolean in_first) {
    // set: È quel "pezzo" di celle lungo tutta la linea (orizzontale, verticale o diagonale) che viene analizzato di votla in volta
    MNKCellState botState = MNKCellState.P2; if (in_first) botState = MNKCellState.P1;
    // DUBBIO 1.1 MNKCellState currentPlayer = botState;    // Per current player si intende il giocatore in analisi

    //------------------------------------------------------------------------------------------

    if (in_foglia.getMNKBoard().gameState() == MNKGameState.OPEN) {
      MNKCellState currentPlayer = MNKCellState.P1;

      MNKBoard board = in_foglia.getMNKBoard();     // Essendo la variabile MNKCellState[][] protetta, dovrebbe essere accessibile da questo programma
      MNKCell[] MC = in_foglia.getMNKBoard().getMarkedCells();        // Nelle posizioni 0,2,4,... vi sono le mosse del P1, nelle posizioni 1,3,5,... vi sono le mosse del P2

      boolean noEnemy = true;                       // Variabile che tiene conto della presenza di nemici in un set
      boolean primoControllo = true;                // Si fanno due controlli (uno per ogni player), sarà true solo durante il primo di essi

      preWinLimit = (int)((board.K / 3) * 2);
      int[] vars = new int[lunghezza]; for (int i = 3; i < lunghezza; i++) vars[i] = 0;
      vars[i_righe] = board.M - 1;
      vars[j_colonne] = board.N - 1;
      vars[k] = board.K;

      // Si esegue la valutazione per attribuire un valore ad entrambi i simboli alpha e beta di ogni giocatore
      for (int alphaBeta = 0; alphaBeta < 2; alphaBeta++) {

        for (int pos = alphaBeta; pos < MC.length; pos += 2) {    // La prima volta scorre le pos del player 0,2,4,..., la seconda volta le posizioni avversarie 1,3,5,...
          int i_MC = MC[pos].i;   // Coordinata i (riga) della cella in analisi
          int j_MC = MC[pos].j;   // Coordinata j (colonna) della cella in analisi
          //System.out.println ("[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]");
          //System.out.println ("CELLA SET: (" + i_MC + "," + j_MC + ")");

          // Controllo set orizzontale
          //System.out.println ("AVVIO - Controllo orizzontale");
          for (int c = 0; c <= vars[j_colonne]; c++) {     // Controllo della i-esima righe (da sx verso dx)
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
            else if (board.cellState(r, j_MC) != currentPlayer && board.cellState(r, j_MC) != MNKCellState.FREE) enemyCell (vars);
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
                else if (board.cellState(start_i + move, start_j + move) != currentPlayer && board.cellState(start_i + move, start_j + move) != MNKCellState.FREE) enemyCell(vars);
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
                else if (board.cellState(start_i + move, start_j - move) != currentPlayer && board.cellState(start_i + move, start_j - move) != MNKCellState.FREE) enemyCell (vars);
                else System.out.println ("ERRORE - FUNZIONE: assegnaValoreABFoglia - DOVE: Controllo diagonale: alto dx --> basso sx - R.149 circa");
              }
              editVar (vars, noEnemy);
              noEnemy = true;
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

        if (primoControllo) {                           // Se si stanno analizzando le celle del P1 (primo controllo, 0,2,4,...)
          if (in_first) vars[beta] = vars[tmp];         // Se il bot è p1 (ha iniziato per primo) --> *Primo controllo* beta = tmp
          else vars[alpha] = vars[tmp];                 // Altrimenti se il bot iniziato per secondo --> *Primo controllo* alpha = tmp
          primoControllo = false;
        } else {                                        // Altrimenti, se si controllano le celle del P2 (second ocontrollo, 1,3,5,...)
          if (in_first) vars[alpha] = vars[tmp];        // Se il bot è P1 (tmp definti dalle celle di P2) --> *Secondo controllo* alpha = tmp
          else vars[beta] = vars[tmp];                  // Altrimenti se il boi inizia per secondo (e si è al secondo controllo) --> *Secondo controllo* beta = tmp
        }

        // DUBBIO 1.2 if (botState == MNKCellState.P1) currentPlayer = MNKCellState.P2;   // Si cambia il giocatore a cui guardare i segni
        // DUBBIO 1.3 else currentPlayer = MNKCellState.P1;

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
      if (in_foglia.getMNKBoard().gameState() == MNKGameState.WINP1 && in_first) {      // VITTORIA
        in_foglia.setColor(Colors.GREEN);
        in_foglia.setBeta(Integer.MAX_VALUE);
        //in_foglia.setAlpha(alpha - 100000);
      }
      else if (in_foglia.getMNKBoard().gameState() == MNKGameState.WINP2 && in_first) { // SCONFITTA
        in_foglia.setColor(Colors.RED);
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


}
// Fine class TreeFunctions
