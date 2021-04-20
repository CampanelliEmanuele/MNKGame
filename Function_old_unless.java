public class Function_old_unless {


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

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

  //}

}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public static void solve1 (TreeNode in_padre, int in_depthLimit) {
  if (in_depthLimit > 1) {				// Se in_depthLimit > 1 --> Si crea un'altro livello
    MNKCell[] FC = in_padre.getMNKBoard().getFreeCells();
    B.markCell (FC[0].i, FC[0].j);		  		// Temporaneo marcamento della prima cella
    TreeNode primoFiglio = new TreeNode (B, in_padre, true, null, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);		// Si crea il primo figlio
    B.unmarkCell ();												// Si smarca la prima cella
    in_padre.setPrimoFiglio(primoFiglio);		// Si setta il primo figlio del nodo padre

    //TreeNode head = in_padre;
    while (in_padre != null) {								// Per ogni fratello (e padre compreso) si crea il sottoalbero
      TreeNode prev = primoFiglio;						// Prev creato uguale al primoFiglio
      for (int e = 1; e < FC.length; e++) {
        B.markCell (FC[e].i, FC[e].j);			// Temporaneo marcamento della cella
        TreeNode figlio = new TreeNode (B, in_padre, false, prev, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
        prev.setNext (figlio);							// Il fratello prev è ora collegato al suo nuovo fratello

        prev = figlio;											// Il nuovo figlio è ora il prev (ovvero l'ultimo figlio creato)
        B.unmarkCell ();										// Si smarca la cella in questione
      }

      solve1 (primoFiglio, --in_depthLimit);
      in_padre = in_padre.getNext();
    }
    //return head;
  } //else return head;	// Altrimenti ritorna il nodo dato in input

}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public static void solve2 (TreeNode in_padre, MNKBoard in_B, int in_depthLimit) {
  if (in_depthLimit > 1) {				// Se in_depthLimit > 1 --> Si crea un'altro livello
    MNKCell[] FC = in_padre.getMNKBoard().getFreeCells();
    //MNKBoard tmpB = in_B;
    System.out.println("Local B: " + B);

    while (in_padre != null) {								// Per ogni fratello (e padre compreso) si crea il sottoalbero
      in_B.markCell (FC[0].i, FC[0].j);		  		// Temporaneo marcamento della prima cella
      TreeNode primoFiglio = new TreeNode (in_B, in_padre, true, null, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);		// Si crea il primo figlio
      in_padre.setPrimoFiglio(primoFiglio);		// Si setta il primo figlio del nodo padre

      solve2 (primoFiglio, in_B, in_depthLimit - 1);
      in_B.unmarkCell ();												// Si smarca la prima cella

      TreeNode prev = primoFiglio;						// Prev creato uguale al primoFiglio
      for (int e = 1; e < FC.length; e++) {
        in_B.markCell (FC[e].i, FC[e].j);			// Temporaneo marcamento della cella
        TreeNode figlio = new TreeNode (in_B, in_padre, false, prev, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
        prev.setNext (figlio);							// Il fratello prev è ora collegato al suo nuovo fratello

        prev = figlio;											// Il nuovo figlio è ora il prev (ovvero l'ultimo figlio creato)
        solve2 (figlio, in_B, in_depthLimit - 1);
        in_B.unmarkCell ();										// Si smarca la cella in questione
      }

      in_padre = in_padre.getNext();
    }
  } //else return head;	// Altrimenti ritorna il nodo dato in input

}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// Experiment
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

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public static void solve3 (TreeNode in_padre, MNKBoard in_B, int in_depthLimit) {
  if (in_depthLimit > 1) {				// Se in_depthLimit > 1 --> Si crea un'altro livello
    System.out.println("Stato: generazione - Livello: " + (5 - in_depthLimit) + " - Local B: " + in_B);

    MNKCell[] FC = in_padre.getMNKBoard().getFreeCells();
    MNKCell[] MC = in_padre.getMNKBoard().getMarkedCells();
    MNKBoard tmpB = new MNKBoard (M,N,K);
    for (int e = 0; e < MC.length; e++) {
      tmpB.markCell (MC[e].i, MC[e].j);
    }

    while (in_padre != null) {											// Per ogni fratello (e padre compreso) si crea il sottoalbero
      tmpB.markCell (FC[0].i, FC[0].j);		  				// Temporaneo marcamento della prima cella
      TreeNode primoFiglio = new TreeNode (tmpB, in_padre, true, null, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);		// Si crea il primo figlio
      in_padre.setPrimoFiglio(primoFiglio);					// Si setta il primo figlio del nodo padre

      solve3 (primoFiglio, tmpB, in_depthLimit - 1);
      //tmpB.unmarkCell ();													// Si smarca la prima cella
      TreeNode prev = primoFiglio;									// Prev creato uguale al primoFiglio

      for (int e = 1; e < FC.length; e++) {					// Ciclo per la creazione dei nodi di un livello
        MNKBoard tmp2B = new MNKBoard (M,N,K);			// Crea una nuova board per ogni nodo del livello in questione
        for (int el = 0; el < MC.length; el++) {
          tmp2B.markCell (MC[el].i, MC[el].j);
        }

        tmp2B.markCell (FC[e].i, FC[e].j);					// Temporaneo marcamento della cella
        TreeNode figlio = new TreeNode (tmp2B, in_padre, false, prev, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
        prev.setNext (figlio);											// Il fratello prev è ora collegato al suo nuovo fratello

        prev = figlio;															// Il nuovo figlio è ora il prev (ovvero l'ultimo figlio creato)
        solve3 (figlio, tmp2B, in_depthLimit - 1);
        //tmp2B.unmarkCell ();												// Si smarca la cella in questione
      }
      in_padre = in_padre.getNext();
    }
  }

}
