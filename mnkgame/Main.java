package mnkgame;

public class Main {

    // Per memorizzare i sottoalberi verrà utilizzata una struttura dati dinamica omogena lineare

    // 1
    // Funzione che si occupa della creazione dell'albero
    public static void creaSottoAlbero (TreeNode in_alberoPadre, MNKBoard in_boardPadre, MNKCell[] FC) {
        for (int k = 0; k < FC.length; k++) {               // Crea un nuovo sotto-albero per ogni cella libera (rappresentazione di ogni possible mossa)
        MNKBoard tmpBoard = in_boardPadre;                // Creazione della nuova board
        tmpBoard.markCell (FC[k].i, FC[k].j);             // Marcamento della board appena creata
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
    // Funzione che decreta il valore alpha e beta del nodo;MC: LinkedList; FC: HashSet
    public static void assegnaValoreABFoglia (TreeNode in_foglia) {

      MNKCell[] FC = in_foglia.getMNKBoard().getFreeCells();
      MNKCell[] MC = in_foglia.getMNKBoard().getMarkedCells();      // Nelle posizioni 0,2,4,... vi sono le mosse del P!, nelle posizioni 1,3,5,... vi sono le mosse del P2
      MNKCellState[][] tabellaGioco = in_foglia.getMNKBoard().B;    // Essendo la variabile MNKCellState[][] protetta, dovrebbe essere accessibile da questo programma

      int righe = in_foglia.getMNKBoard().M;     // RIGHE
      int colonne = in_foglia.getMNKBoard().N;   // COLONNE
      int k = in_foglia.getMNKBoard().K;         // SERIE

      MNKCellState simbolPlayer = P2;
      if (this.first) simbolPlayer = P1;
      
      int alpha = 0;
      int beta = 0;

      if (simbolPlayer == P1) {
        for (int pos = 0; pos < MC.length; pos += 2) {
          int rigaEl = MC[pos].i;
          int colonneEl = MC[pos].j;
          for (int c = 0; c < colonne; c++) {     // Controllo della riga (da sx verso dx)
            if (tabellaGioco[rigaEl][c].state == simbolPlayer) beta += 2;
            if (tabellaGioco[rigaEl][c].state == FREE) beta += 1;
            if (tabellaGioco[rigaEl][c].state != simbolPlayer && tabellaGioco[rigaEl][c].state != FREE) { beta = 0; break; }
          }
        }
        for (int i = 1; i < MC.length; i += 2) {
          // Si prende la cella marcata che ci interessa e si crea il set tramite essa
          
        } 

      } else { //simbolPlayer == P2
        for (int i = 1; i < MC.length; i += 2) {
          // Si prende la cella marcata che ci interessa e si crea il set tramite essa
        
        }
        for (int i = 0; i < MC.length; i += 2) {
        // Si prende la cella marcata che ci interessa e si crea il set tramite essa
          for (int wet = 0; wet < k; wet++) {


          }
        }
      }

      
      

      /*
      if (k > righe && k <= colonne) {            // Vittoria possibile solo in orizzontale
        for (int i = 0; i < righe; i++) {
          for (int j = 0; j <= colonne - k; j++) {
            //cella(i, j);
            createSet(0, ...);
          }
        }

      } else if (k > colonne && k <= righe) {     // Vittoria possibile solo in verticale
        for (int i = 0; i < colonne; i++) {
          for (int j = 0; j <= righe - k; j++) {
            //cella(i, j);
            createSet (1, ...);
          }
        }
      
      } else if (k <= righe && k <= colonne) {        // Vittoria possibile in diagonale, verticale ed orizzontale
        MNKCell lastMarkedCell = MC[MC.length - 1];   // Con il -1 si accede all'ultimo elemento
        
        for (int i = 0; i <= righe - k ; i++) {
          for (int j = 0; j <= colonne - k; j++) {
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
