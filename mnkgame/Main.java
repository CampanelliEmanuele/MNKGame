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
      MNKCell[] MC = in_foglia.getMNKBoard().getMarkedCells();
      MNKCellState[][] tabellaGioco = in_foglia.getMNKBoard().B;    // Essendo la variabile MNKCellState[][] protetta, dovrebbe essere accessibile da questo programma

      int righe = in_foglia.getMNKBoard().M;     // RIGHE
      int colonne = in_foglia.getMNKBoard().N;   // COLONNE
      int k = in_foglia.getMNKBoard().K;         // SERIE

      if (k > righe && k <= colonne) {            // Vittoria possibile solo in orizzontale
        for (int i = 0; i < righe; i++) {
          for (int j = 0; j < colonne; j++) {
            //cella(i, j);
          }
        }


        
      } else if (k > colonne && k <= righe) {     // Vittoria possibile solo in verticale
        for (int i = 0; i < righe; i++) {
          for (int j = 0; j < colonne; j++) {
            cella(i, j);
          }
        }
      
      } else if (k <= righe && k <= colonne) {        // Vittoria possibile in diagonale, verticale ed orizzontale
        MNKCell lastMarkedCell = MC[MC.length - 1];   // Con il -1 si accede all'ultimo elemento
        
        // Controllo orizzontale
        for (int i = 0; i < righe; i++) {
          for (int j = 0; j < colonne; j++) {
            MNKCell set = createSet (in_figlio, k, i, j);
            
          }
        }

        // Controllo verticale
        
        // Controllo diagonale
      } else {
        System.out.println ("Errore con il valore k. Funzione: assegnaValoreABFoglia");
      }
      
    }

    public static MNKCell[] createSet (MNKBoard in_board, int in_k, int in_riga, int in_colonna) {
      
      int righe = in_board.M;     // RIGHE
      int colonne = in_board.N;   // COLONNE
      int k = in_board.K;         // SERIE

      MNKCell[] set = new MNKCell[in_k];
      int x = 0;
      for (int tmp = in_colonna; tmp < in_k + in_colonna; tmp++) {
        set[x] = in_board.B[in_riga][tmp];  // tmp rappresenta la colonna
        set[x] = MNKCell (in_board.M, in_board.N, in_board.state);
        set[x] = MNKCell (in_riga, in_colonna + x, in_board.cellState(in_riga, in_colonna + x).state);
        set[x] = MNKCell (in_riga, in_colonna + x, in_board.state);
        x++;
      }
      return set;
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
