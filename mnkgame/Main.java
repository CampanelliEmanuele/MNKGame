package mnkgame;

public class Main {

    // Per memorizzare i sottoalberi verrà utilizzata una struttura dati dinamica omogena lineare

    public static void creaSottoAlbero (TreeNode in_alberoPadre, MNKBoard in_boardPadre, MNKCell[] FC) {
        for (int k = 0; k < FC.length; k++) {               // Crea un nuovo sotto-albero per ogni cella libera (rappresentazione di ogni possible mossa)
        MNKBoard tmpBoard = in_boardPadre;                // Creazione della nuova board
        tmpBoard.markCell (FC[k].i, FC[k].j);             // Marcamento della board appena creata
        TreeNode nuovoSottoAlbero = new TreeNode (tmpBoard, in_alberoPadre);    // Creazione del sottoalbero con la nuova board
        }
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
