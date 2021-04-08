package mnkgame;

import javax.swing.tree.TreeNode;

public class Main {

    // Per memorizzare i sottoalberi verrà utilizzata una struttura dati dinamica omogena lineare

    // 1
    // Funzione che si occupa della creazione dell'albero
    public static void creaSottoAlbero (TreeNode in_alberoPadre, MNKBoard in_boardPadre, MNKCell[] FC) {
        for (int k = 0; k < FC.length; k++) {               // Crea un nuovo sotto-albero per ogni cella libera (rappresentazione di ogni possible mossa)
        MNKBoard tmpBoard = in_boardPadre;                  // Creazione della nuova board
        tmpBoard.markCell (FC[k].i, FC[k].j);               // Marcamento della board appena creata
        TreeNode nuovoSottoAlbero = new TreeNode (tmpBoard, in_alberoPadre);    // Creazione del sottoalbero con la nuova board
        }
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 2
    // Funzione che va alle foglie dell'albero (e ne assegna il valore tramite un'altra funzione) + il relativo codice di invocazione
    // Pezzo di codice da mettere nel "main", prima di richiamare la funzione sottostante vaiAlleFoglie
    if (nodoInQuestione.getPrimoFiglio() != null)
        vaiAlleFoglie (nodoInQuestione.getPrimoFiglio());
    else
        assegnaValoreABFoglia (nodoInQuestione);

    public static void vaiAlleFoglie (TreeNode in_padre) {    // Preso il padre, visiterà l'albero (in qualsiasi modo) fino ad arrivare alle foglie ed attribuire ad esse un valore che sarà utilizzato dall'algoritmo alpha beta pruning
      while (in_padre != null){
        if (in_padre.getPrimoFiglio() == null)  // Se è una foglia
          assegnaValoreABFoglia (in_padre);
        else {                                  // Altrimenti, se non è una foglia
            vaiAlleFoglie (in_padre.getPrimoFiglio());  // Si applica la funzione nei sotto-alberi
            //assegnaValoreABFoglia(in_padre);    // APPUNTO PER IL FUTURO SVILUPPO DELL'ALGORITMO ALPHA BETA, CANCELLARE QUESTA RIGA UNA VOLTA IMPLEMENTATO L'ALGORIMTO
        }
        in_padre = in_padre.getNext();
      }
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 3

    // Da spostare a liveli più globali, magari agli inizi della partita questo valore sarà più alto in modo tale da considerare come vincenti più situazioni, poi con l'avanzare della partita essa verrà incrementata per esserr più selettivi nelle condizioni di vittoria (ad esempio aumentando il valore di n ogni tot livelli dell'albero)
    public static int preWinLimit = -1 /* parteIntera (il n% di K) VALORE DA DECRETARE*/;   // Limite per decretare quando uno strike si può reputare una situazione prossima alla vittoria

    // 3.1

    public static void editVar (boolean in_noEnemy, int in_beta, int in_localWinSituations, int in_maxStrike, int in_k) {
        if (in_noEnemy) in_beta++;
        // Se non vi sono registrate situazioni prossime alla vittoria ma solo strike sufficiente lunghi si incrementa in_beta
        if (in_localWinSituations == 0 && in_maxStrike >= (int)(in_k / 4)) in_beta += in_maxStrike / 4;
        in_noEnemy = true;
        in_maxStrike = 0;
        strike = 0;
        in_localWinSituations = 0;
    }

    // Funzione che decreta il valore alpha e beta del nodo;MC: LinkedList; FC: HashSet
    public static void assegnaValoreABFoglia (TreeNode in_foglia) {

        MNKCell[] FC = in_foglia.getMNKBoard().getFreeCells();
        MNKCell[] MC = in_foglia.getMNKBoard().getMarkedCells();      // Nelle posizioni 0,2,4,... vi sono le mosse del P!, nelle posizioni 1,3,5,... vi sono le mosse del P2
        MNKCellState[][] tabellaGioco = in_foglia.getMNKBoard().B;    // Essendo la variabile MNKCellState[][] protetta, dovrebbe essere accessibile da questo programma

        int M_righe = in_foglia.getMNKBoard().M;   int i_righe   = M_righe - 1;      // RIGHE
        int N_colonne = in_foglia.getMNKBoard().N; int j_colonne = N_colonne - 1;    // COLONNE
        int k = in_foglia.getMNKBoard().K;                                           // SERIE

        MNKCellState playerState = P2;
        if (this.first) playerState = P1;

        int alpha = 0;
        int beta = 0;

        boolean noEnemy = true;

        int strike = 0;                 // Tiene conto del numero di simboli (identici) consecutivi (in determinate condizioni si resetta)
        int maxStrike = 0;              // Massimo valore di strike registrato nell'analisi della riga/colonna/diagonale in analisi
        int localWinSituations = 0;     // Tiene conto del numero di situazioni vicine alla vittoria nella riga/colonna/diagonale in analisi
        int globalWinSituations = 0;    // Tiene conto del numero di situazione vicine alla vittoria nella tabella di gioco in questione



        // Controllo dei nostri simboli
        for (int pos = 0; pos < MC.length; pos += 2) {
            int i_MC = MC[pos].i;   // Coordinata i (riga) della cella in analisi
            int j_MC = MC[pos].j;   // Coordinata j (colonna) della cella in analisi



            // Controllo riga in orizzontale
            for (int c = 0; c < j_colonne; c++) {     // Controllo della i-esima righe (da sx verso dx)
                if (tabellaGioco[i_MC][c].state == playerState) {
                    beta += 2;
                    strike++;
                    if (strike > maxStrike) maxStrike = strike;
                }
                if (tabellaGioco[i_MC][c].state == FREE) {
                    beta += 1;
                    if (strike > 0 && strike >= preWinLimit) {
                        localWinSituations++;
                        globalWinSituations++;
                        strike = 0;
                    }
                }
                if (tabellaGioco[i_MC][c].state != playerState && tabellaGioco[i_MC][c].state != FREE) {
                    beta--;
                    strike = 0;
                    noEnemy = false;
                }
                /*
                if (tabellaGioco[i_MC][c].state == playerState) beta += 2;
                if (tabellaGioco[i_MC][c].state == FREE) beta += 1;
                if (tabellaGioco[i_MC][c].state != playerState && tabellaGioco[i_MC][c].state != FREE) {
                    beta--;
                    noEnemy = false;
                }
                if (noEnemy) beta++;
                */
            }

            editVar (in_noEnemy, in_beta, in_localWinSituations, in_maxStrike, in_k);







            // Controllo riga in verticale
            for (int r = 0; r < i_righe; r++) {     // Controllo della j-esima colonna (dall'alto verso il basso)
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
                /*
                if (tabellaGioco[r][j_MC].state == playerState) beta += 2;
                if (tabellaGioco[r][j_MC].state == FREE) beta += 1;
                if (tabellaGioco[r][j_MC].state != playerState && tabellaGioco[r][j_MC].state != FREE) {
                    beta--;
                    noEnemy = false;
                }
                if (noEnemy) beta++;
                */
            }

            editVar (in_noEnemy, in_beta, in_localWinSituations, in_maxStrike, in_k);




            // Controllo diagonale
            if (M_righe < k && N_colonne < k) {
                System.out.println("Controllo diagonale eseguito.");
                // Scorrimento verso in alto a sx
                for (int tmp = 1; i_righe - tmp >= 0 && j_colonne - tmp >= 0; tmp++) {
                    if (tabellaGioco[i_righe - tmp][j_colonne - tmp].state == playerState) beta += 2;
                    if (tabellaGioco[i_righe - tmp][j_colonne - tmp].state == FREE) beta += 1;
                    if (tabellaGioco[i_righe - tmp][j_colonne - tmp].state != playerState && tabellaGioco[i_righe - tmp][j_colonne - tmp].state != FREE) {
                        beta--;
                        noEnemy = false;
                    }
                    if (noEnemy) beta++;
                }
                noEnemy = true;


                // Discesa fino in basso a dx
                for (int tmp = 1; i_righe + tmp < M_righe && j_righe + tmp < N_righe; tmp++) {
                    if (tabellaGioco[i_righe + tmp][j_colonne + tmp].state == playerState)  {
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
                }
                if (noEnemy) beta++;
                noEnemy = true;



                // Scorrimento verso in alto a dx
                for (int tmp = 1; i_righe - tmp < M_righe && j_righe + tmp < N_righe; tmp++) {
                    if (tabellaGioco[i_righe - tmp][j_colonne + tmp].state == playerState) beta += 2;
                    if (tabellaGioco[i_righe - tmp][j_colonne + tmp].state == FREE) beta += 1;
                    if (tabellaGioco[i_righe - tmp][j_colonne + tmp].state != playerState && tabellaGioco[i_righe - tmp][j_colonne + tmp].state != FREE) {
                        beta--;
                        noEnemy = false;
                    }
                    if (noEnemy) beta++;
                }
                noEnemy = true;

                // Discesa fino in basso a sx
                for (int tmp = 1; i_righe + tmp >= 0 && j_righe - tmp < N_righe; tmp++) {
                    if (tabellaGioco[i_righe + tmp][j_colonne - tmp].state == playerState) beta += 2;
                    if (tabellaGioco[i_righe + tmp][j_colonne - tmp].state == FREE) beta += 1;
                    if (tabellaGioco[i_righe + tmp][j_colonne - tmp].state != playerState && tabellaGioco[i_righe + tmp][j_colonne - tmp].state != FREE) {
                        beta--;
                        noEnemy = false;
                    }
                    if (noEnemy) beta++;
                }
                noEnemy = true;



            } else {
                System.out.println("controllo diagonale Impossibile.");
            }

            editVar (in_noEnemy, in_beta, in_localWinSituations, in_maxStrike, in_k);


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

    public static MNKCell[] createSet (int in_setDirection, MNKBoard in_board, MNKCellState in_simbol2, int in_k, int in_riga, int in_colonna) {    // s2 è il simbolo avversario
      // in_setDirection = 1  --> set VERTICALE
      // in_setDirection = 0  --> set ORIZZONTALE
      // in_setDirection = -1 --> set DIAGONALE

      int righe = in_board.M;     // RIGHE
      int colonne = in_board.N;   // COLONNE
      int k = in_board.K;         // SERIE
      int x = 0;                  // Indice dell'array ritornato

      boolean noEnemySimbol = false;

      MNKCell[] set = new MNKCell[in_k];

      // Usare le istruzioni throw per il controllo del valore in_setDirection
      // Usare le istruzioni throw per il controllo del valore in_setDirection
      // Usare le istruzioni throw per il controllo del valore in_setDirection
      // Usare le istruzioni throw per il controllo del valore in_setDirection
      // Usare le istruzioni throw per il controllo del valore in_setDirection
      // Usare le istruzioni throw per il controllo del valore in_setDirection
      // Usare le istruzioni throw per il controllo del valore in_setDirection
      // Usare le istruzioni throw per il controllo del valore in_setDirection

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

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// 4

// MINIMAX e ALPHA-BETA PRUNING

public int minimax(TreeNode in_padre, int alpha, int beta, bool isMax, int depth){
  if (in_padre.isLeaf() || depth == 0) return in_padre.val;
  //Nodo da massimizzare
  else if(isMax){
      int best = Integer.MIN_VALUE;
      for(TreeNode child : in_padre.getPrimoFiglio()){
        for(TreeNode fratello : in_padre.getPrimoFiglio.next()){
          int valore = minimax(fratello, alpha, beta, false, depth--);
          best = Math.max(best, valore);
          alpha = Math.max(best, alpha);
          if(beta <= alpha) break;
        }
      }
      return best;
  } else { //Nodo da minimizzare
      int best = Integer.MAX_VALUE;
      for(TreeNode child : in_padre.getPrimoFiglio()){
        for(TreeNode fratello : in_padre.getPrimoFiglio.next()){
          int valore = minimax(fratello, alpha, beta, true, depth--);
          best = Math.min(best, valore);
          beta = Math.min(best, beta);
          if(beta <= alpha) break;
        }
      return best;
      }
  }
}

//CREARE SCELTA DEL PERCORSO
public TreeNode sceltaPercorso(bool isMaximizing, TreeNode in_padre){

  if(isMaximizing){
    //ritorno il nodo da scegliere
    in_padre.alpha = minimax(in_padre, Integer.MIN_VALUE, Integer.MAX_VALUE, isMaximizing, 3);
  } else {

  }

}




//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void main(String[] args) {

        MNKBoard bp = new MNKBoard(3, 3, 3);
        TreeNode padre = new TreeNode(bp);

        MNKBoard bf1 = new MNKBoard(3, 3, 3);
        TreeNode figlio1 = new TreeNode(bf1, padre);

        MNKBoard bf2 = new MNKBoard(3, 3, 3);
        TreeNode figlio2 = new TreeNode(bf2, padre);

        // System.out.println("loool");
    }
}
