public class tmp {

  // FUNZIONE INUTILIZZATA
  public static MNKCell[] createSet (int in_setDirection, MNKBoard in_board, MNKCellState in_simbol2, int in_k, int in_riga, int in_colonna) {    // s2 è il simbolo avversario
        // Usare le istruzioni throw per il controllo del valore in_setDirection !!!!!!!!

        // in_setDirection = 1  --> set VERTICALE
        // in_setDirection = 0  --> set ORIZZONTALE
        // in_setDirection = -1 --> set DIAGONALE

        int righe = in_board.M;     // RIGHE
        int colonne = in_board.N;   // COLONNE
        int k = in_board.K;         // SERIE
        int x = 0;                  // Indice dell'array ritornato

        boolean noEnemySimbol = false;

        MNKCell[] set = new MNKCell[in_k];

        switch (in_setDirection) {
          case 1: //VERTICALE
            for (int tmp = in_riga; tmp < in_k + in_riga; tmp++) {
              MNKCellState tmpMNKCellState = in_board.cellState(in_riga + x, in_colonna);
              if (tmpMNKCellState == in_simbol2.P2) {
                noEnemySimbol = true;
                break;
              }
              set[x] = MNKCell (in_riga + x, in_colonna, tmpCellState);
              x++;
            }
          break;

          case 0: //ORIZZONTALE
            for (int tmp = in_colonna; tmp < in_k + in_colonna; tmp++) {
              MNKCellState tmpMNKCellState = in_board.cellState(in_riga, in_colonna + x);
              if (tmpMNKCellState == in_simbol2.P2) {
                noEnemySimbol = true;
                break;
              }
              set[x] = MNKCell (in_riga, in_colonna + x, tmpCellState);
              x++;
            }
          break;

          case -1: //DIAGONALE
            for (int tmp = in_colonna; tmp < in_k + in_colonna; tmp++) {

            }
          break;

        }

        if (noEnemySimbol) return null;
        else return set;
      }


  // FUNZIONE OBSOLETA
  // Funzione che decreta il valore alpha e beta del nodo;MC: LinkedList; FC: HashSet
  public static void assegnaValoreABFoglia (TreeNode in_foglia) {

    /*
    int i_righe   = in_foglia.getMNKBoard().M - 1;    // RIGHE
    int j_colonne = in_foglia.getMNKBoard().N - 1;    // COLONNE
    int k = in_foglia.getMNKBoard().K;                // SERIE
    int strike = 0;
    int maxStrike = 0;
    int localWinSituations = 0;
    int globalWinSituations = 0;
    int alpha = 0;
    int beta = 0;
    */

    // FARE UN CONTROLLO GENERICO PER CORREGGERE ERRORI SEMANTICI E DI DISTRAZIONE

    MNKCellState playerState = P2;
    if (this.first) playerState = P1;

    boolean noEnemy = true;

    MNKCell[] FC = in_foglia.getMNKBoard().getFreeCells();
    MNKCell[] MC = in_foglia.getMNKBoard().getMarkedCells();      // Nelle posizioni 0,2,4,... vi sono le mosse del P!, nelle posizioni 1,3,5,... vi sono le mosse del P2
    MNKCellState[][] tabellaGioco = in_foglia.getMNKBoard().B;    // Essendo la variabile MNKCellState[][] protetta, dovrebbe essere accessibile da questo programma

    // Controllo dei simboli del player
    for (int pos = 0; pos < MC.length; pos += 2) {
      int i_MC = MC[pos].i;   // Coordinata i (riga) della cella in analisi
      int j_MC = MC[pos].j;   // Coordinata j (colonna) della cella in analisi

      // Controllo set orizzontale
      for (int c = 0; c < j_colonne; c++) {     // Controllo della i-esima righe (da sx verso dx)
        if (tabellaGioco[i_MC][c].state == playerState) playerCell (vars);
        if (tabellaGioco[i_MC][c].state == FREE) freeCell (vars);
        if (tabellaGioco[i_MC][c].state != playerState && tabellaGioco[i_MC][c].state != FREE) { enemyCell (vars); noEnemy = false; }

        /*
        if (tabellaGioco[i_MC][c].state == playerState) {
          beta += 2;
          strike++;
          if (strike > maxStrike) maxStrike = strike;
        }
        if (tabellaGioco[i_MC][c].state == FREE) {
          beta++;
          if (strike > 0 && strike >= preWinLimit) {
            localWinSituations++;
            globalWinSituations++;
            beta++;
            strike = 0;
          }
        }
        if (tabellaGioco[i_MC][c].state != playerState && tabellaGioco[i_MC][c].state != FREE) {    // Cella occupata dall'avversario
          beta--;
          strike = 0;
          noEnemy = false;
        }
        */
      }
      editVar (vars);
      in_noEnemy = true;

      // Controllo riga in verticale
      for (int r = 0; r < i_righe; r++) {     // Controllo della j-esima colonna (dall'alto verso il basso)
        if (tabellaGioco[r][j_MC].state == playerState) playerCell (vars);
        if (tabellaGioco[r][j_MC].state == FREE) freeCell (vars);
        if (tabellaGioco[r][j_MC].state != playerState && tabellaGioco[r][j_MC].state != FREE) enemyCell (vars);

        /*
        if (tabellaGioco[r][j_MC].state == playerState) {
          beta += 2;
          strike++;
          if (strike > maxStrike) maxStrike = strike;
        }
        if (tabellaGioco[r][j_MC].state == FREE) {
          beta += 1;
          if (strike > 0 && strike >= preWinLimit) {
            localWinSituations++;
            globalWinSituations++;
            strike = 0;
          }
        }
        if (tabellaGioco[r][j_MC].state != playerState && tabellaGioco[r][j_MC].state != FREE) {
          beta--;
          strike = 0;
          noEnemy = false;
        }
        */
      }
      editVar (vars);
      in_noEnemy = true;

      // Controllo diagonale
      if (M_righe >= k && N_colonne >= k) {   // Se è possibile la creazione di diagonali
        System.out.println("Controllo diagonale eseguito.");
        int tmp = 1;

        // Scorrimento verso in alto a sx
        for (tmp; i_MC - tmp > 0 && j_MC - tmp > 0; tmp++)    // Si incrementa tmp fino a quando sottratto alle coordinate i ed j non ci si ritrova in (i - tmp, j - tmp = 0)
        // while (i_righe - tmp > 0 && j_colonne - tmp > 0) { tmp++; }
        int start_i = i_MC - tmp;  // DA CONTROLLARE SE SERVE + 1 !!!!!!!!!!
        int start_j = j_MC - tmp;

        // Discesa fino in basso a dx
        for (tmp = 0; start_i + tmp <= start_i + vars[k] && start_j + tmp <= start_j + vars[k]; tmp++) {
          if (tabellaGioco[start_i + tmp][start_j + tmp].state == playerState) playerCell (vars);
          if (tabellaGioco[i_righe + tmp][j_colonne + tmp].state == FREE) freeCell(vars);
          if (tabellaGioco[i_righe + tmp][j_colonne + tmp].state != playerState && tabellaGioco[i_righe + tmp][j_colonne + tmp].state != FREE) enemyCell(vars);

          /*
          if (tabellaGioco[start_i + tmp][start_j + tmp].state == playerState) {
            beta += 2;
            strike++;
            if (strike > maxStrike) maxStrike = strike;
          }
          if (tabellaGioco[i_righe + tmp][j_colonne + tmp].state == FREE) {
            beta += 1;
            if (strike > 0 && strike >= preWinLimit) {
              localWinSituations++;
              globalWinSituations++;
              strike = 0;
            }
          }
          if (tabellaGioco[i_righe + tmp][j_colonne + tmp].state != playerState && tabellaGioco[i_righe + tmp][j_colonne + tmp].state != FREE) {
            beta--;
            strike = 0;
            noEnemy = false;
          }
          */
        }
        editVar (vars);
        noEnemy = true;

        // Scorrimento verso in alto a dx
        for (tmp = 1; i_MC - tmp < i_righe && j_MC + tmp < j_colonne; tmp++)    // Controllare il reset di tmp  !!!!!!!!!!!!!!!!!!!!
        // tmp = 1; while (i_righe - tmp < i_righe && j_colonne + tmp < j_colonne) { tmp++; }
        start_i = i_MC - tmp;  // DA CONTROLLARE SE SERVE + 1 !!!!!!!!!!
        start_j = j_MC - tmp;


        // Discesa fino in basso a sx
        for (int tmp = 1; start_i + tmp <= start_i + vars[k] && start_j - tmp <= 0; tmp++) {
          if (tabellaGioco[start_i + tmp][start_j - tmp].state == playerState) playerCell (vars);
          if (tabellaGioco[start_i + tmp][start_j - tmp].state == FREE) freeCell (vars);
          if (tabellaGioco[start_i + tmp][start_j - tmp].state != playerState && tabellaGioco[i_righe + tmp][j_colonne - tmp].state != FREE) enemyCell (vars):

          /*
          if (tabellaGioco[start_i + tmp][start_j - tmp].state == playerState) beta += 2;
          if (tabellaGioco[start_i + tmp][start_j - tmp].state == FREE) beta += 1;
          if (tabellaGioco[start_i + tmp][start_j - tmp].state != playerState && tabellaGioco[i_righe + tmp][j_colonne - tmp].state != FREE) {
            beta--;
            noEnemy = false;
          }
          */
        }
        editVar (vars);
        noEnemy = true;

        } else {
          System.out.println("controllo diagonale Impossibile.");
      }
    }



      // Contollo simboli avversari
      for (int pos = 1; i < MC.length; pos += 2) {
          int i_MC = MC[pos].i;
          int colonneEl = MC[pos].j;
          for (int c = 0; c < j_colonne; c++) {     // Controllo della riga (da sx verso dx)
              if (tabellaGioco[i_MC][c].state != playerState && tabellaGioco[i_MC][c].state != FREE) alpha = + 1;
              if (tabellaGioco[i_MC][c].state == FREE) alpha += 1;
              if (tabellaGioco[i_MC][c].state == playerState) alpha--;
          }
      }

      /*
      if (k > i_righe && k <= j_colonne) {            // Vittoria possibile solo in orizzontale
        for (int i = 0; i < i_righe; i++) {
          for (int j = 0; j <= j_colonne - k; j++) {
            //cella(i, j);
            createSet(0, ...);
          }
        }

      } else if (k > j_colonne && k <= i_righe) {     // Vittoria possibile solo in verticale
        for (int i = 0; i < colonne; i++) {
          for (int j = 0; j <= i_righe - k; j++) {
            //cella(i, j);
            createSet (1, ...);
          }
        }

      } else if (k <= i_righe && k <= j_colonne) {        // Vittoria possibile in diagonale, verticale ed orizzontale
        MNKCell lastMarkedCell = MC[MC.length - 1];   // Con il -1 si accede all'ultimo elemento

        for (int i = 0; i <= i_righe - k ; i++) {
          for (int j = 0; j <= j_colonne - k; j++) {
            createSet (-1, ...);
          }
        }

      } else {
        System.out.println ("Errore con il valore k. Funzione: assegnaValoreABFoglia");
      }
      */

  }

}
