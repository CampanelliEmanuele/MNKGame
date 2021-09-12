import mnkgame.*;
import subroutine.TreeFunctions.*;


public class uselessFunctions {
    
    /*
	public static void main (String[] args) {
		Functions tree = new Functions();				// Creazione dell'oggetto per l'uso delle funzioni
		Algoritms algoritms = new Algoritms(first);							// Creazione dell'oggetto per l'uso delle funzioni
		Stampa stampa = new Stampa();

		System.out.println("È partito!");

		// K piccolo: + tempo assegnaValoreABFoglia (controlli diagonali)
		//						- tempo creazione albero
		// K grande:  - ...
		// 						+ ...
		// Preferenza del K piccolo

		M = 5;											// Parte da rimuovere in futuro
		N = 5;											// Parte da rimuovere in futuro
		K = 4;											// Parte da rimuovere in futuro
		B = new MNKBoard (M,N,K);

		MNKCell[] FC = B.getFreeCells();				// Parte da rimuovere in futuro
		
		//B.markCell (FC[0].i, FC[0].j);	// 11
		//B.markCell (FC[1].i, FC[1].j);	// 21
		//B.markCell (FC[2].i, FC[2].j);	// 01
		//B.markCell (FC[3].i, FC[3].j);	// 12
		
		//B.markCell (FC[4].i, FC[4].j);	// 20
		
		//B.markCell (FC[5].i, FC[5].j);	// 10
		//B.markCell (FC[6].i, FC[6].j);	// 22
		B.markCell(2, 3);
		B.markCell(0, 3);
		
		B.markCell(4, 0);
		B.markCell(1, 1);
		
		B.markCell(2, 2);
		B.markCell(1, 4);
		
		B.markCell(3, 4);
		B.markCell(3, 0);
		
		B.markCell(2, 0);
		//B.markCell(2, 4);
		
		//B.markCell(2, 1); \\ enemy win
		
		// ___________________________________________________________________________________________
		// Codice del selectCell
		
		TreeNode radice = new TreeNode (B);					// Nodo contenente la stituzione di gioco attuale
		first = false;
		MNKCellState botState = MNKCellState.P2; if (first) botState = MNKCellState.P1;
		
		if (FC.length <= -10) {
			// DA TESTARE //tree.createTree(radice, 18, first);		// Crea il nodo sottostante
			tree.createTree(radice, FC.length + 1, first);			// Crea il nodo sottostante
			
			// Modifica i colori dell'albero sottostante
			if (botState == MNKCellState.P1) algoritms.minMax(radice.getPrimoFiglio(), botState, MNKCellState.P2);
			else algoritms.minMax(radice.getPrimoFiglio(), botState, MNKCellState.P1);
			
			TreeNode primoFiglio = radice.getPrimoFiglio();
			while (primoFiglio != null) {
				System.out.println("pos: " + primoFiglio.getListPosition() + " Color: " + primoFiglio.getColor());
				primoFiglio = primoFiglio.getNext();
			}

			stampa.printMoleColor (radice);
			
			TreeNode winCell = algoritms.sceltaPercorso(radice, false, botState);
			MNKCell[] tmpMC = winCell.getMNKBoard().getMarkedCells();
			MNKCell tmp = tmpMC[tmpMC.length - 1];
			System.out.println("minMax");
			System.out.println("BotState: " + botState + " ; Colore radice: " + radice.getColor());
			//System.out.println("NODO VINCENTE ##########################################################");
			winCell.printNodeInfo();// System.out.println("minMax - Cella vincente: " + "(" + tmp.i + "," + tmp.j + ")");
			//return tmp;
			
		}
		else {
			tree.defenseCell(radice, botState);
			tree.createTree(radice, 2, first);		// Crea il nodo sottostante
			tree.vaiAlleFoglie(radice, botState);	// Assegna i valori AB
			TreeNode winCell = algoritms.sceltaPercorso(radice, true, botState);
			
			if (winCell.getBeta() == Integer.MAX_VALUE || radice.getPriority_i() < 0) {
				MNKCell[] tmpMC = winCell.getMNKBoard().getMarkedCells();
				MNKCell tmp = tmpMC[tmpMC.length - 1];
				System.out.println(winCell.getMNKBoard().gameState() + " ; " + tmp.toString());
				//System.out.println("NODO VINCENTE ##########################################################"); winCell.printNodeInfo(); System.out.println("AB - Cella vincente: " + "(" + tmp.i + "," + tmp.j + ")");
				//return tmp;
			} else {
				MNKCell[] tmpMC = radice.getMNKBoard().getMarkedCells();
				MNKCell tmp = new MNKCell (radice.getPriority_i(), radice.getPriority_j(), botState);
				System.out.println(winCell.getMNKBoard().gameState() + " ; " + tmp.toString());
				//System.out.println("NODO VINCENTE ##########################################################"); radice.printNodeInfo(); System.out.println("Difesa: " + "(" + tmp.i + "," + tmp.j + ")");
				//return tmp;
			}
			
		}
		
		// //////////////////
		

	}
	// fine main
	*/

    /*
	 * MAX:  BT == S -> RED
	 * min:  BT != S -> GREEN
	 */
	// Non si parte dalla radice ma dal primoFiglio dell'albero
	// Se ultimo nodo è GREEN -> Corretto; se ultimo nodo è RED -> assegna WHITE all'ultimo nodo
	public void minMax (TreeNode in_foglia, MNKCellState in_BT, MNKCellState in_S) {
		/*
		if (in_foglia.getColor() == Colors.WHITE) {
			if (in_foglia.getMNKBoard().gameState() == MNKGameState.WINP1) {
				if (in_BT == MNKCellState.P1) in_foglia.setColor(Colors.GREEN);
				else in_foglia.setColor(Colors.RED);
			}
			else if (in_foglia.getMNKBoard().gameState() == MNKGameState.WINP2) {
				if (in_BT == MNKCellState.P1) in_foglia.setColor(Colors.RED);
				else in_foglia.setColor(Colors.GREEN);
			}
			else in_foglia.setColor(Colors.RED);
		}
		*/
		while (in_foglia != null) {													// Per ogni nodo di un livello
			if (in_foglia.getPrimoFiglio() != null) {									// Se non è una foglia si scende di livello cambiando stato
				if (in_S == MNKCellState.P1)
					minMax (in_foglia.getPrimoFiglio(), in_BT, MNKCellState.P2);			
				else if (in_S == MNKCellState.P2)
					minMax (in_foglia.getPrimoFiglio(), in_BT, MNKCellState.P1);
			}
			// Colorazione dell'albero secondo i criteri del minMax
			if (in_foglia.getNext() != null) {											// Se ha dei un almeno un fratello minore
				if (in_BT != in_S && in_foglia.getColor() == Colors.GREEN)					// Se lo stato di Slow_Unmade è diverso da quello del livello e la foglia è verde
					in_foglia.getPadre().setColor(in_foglia.getColor());						// Si colora il padre di verde
				else if (in_BT == in_S && in_foglia.getColor() == Colors.RED)				// Altrimenti, se lo stato di Slow_Unmade è uguale a quello del livello e la foglia è rossa
					in_foglia.getPadre().setColor(in_foglia.getColor());						// Si colora il padre di rosso
			} else if (in_foglia.getNext() == null &&									// Altrimeti, se è l'ultimo figlio
					   in_foglia.getPadre().getColor() == Colors.WHITE)					// e il padre è sprovvisto di colori
				in_foglia.getPadre().setColor(in_foglia.getColor());						// Si il padre avrà lo stesso colore del suo ultimo figlio
			in_foglia = in_foglia.getNext();
		}
	}

    // BIG SOLVE SI APPLICA SOLO ALLE FOGLIE DEL PRIMO FIGLIO !!!
	protected void bigSolve2 (TreeNode in_primoFiglio, boolean myTurn) {
        /*
        if (in_primoFiglio.getPrimoFiglio() != null) {              // Se il nodo in questione ha dei figli
          bigSolve2 (in_primoFiglio.getPrimoFiglio(), !myTurn);     // Si applica bigSolve al livello sottostante
            }
        */
        //else {                                                    // Se il nodo in questione non ha dei figli
        TreeNode tmpHead = in_primoFiglio;
        float maxTmp = Float.MIN_VALUE;
        if (myTurn) {                                             // Se è il mio turno, dovrò passare al padre il miglior valore di alpha (in quanto il padre rappresenta la scelta per l'avversaio)
            System.out.println("CASE: myTurn");
            while (in_primoFiglio != null) {                        // Per ogni fratello della foglia
            System.out.println("A: " + in_primoFiglio.getAlpha() + " ; B: " + in_primoFiglio.getBeta());
            //if (in_primoFiglio.getBeta() <= in_primoFiglio.getAlpha()) {  //cutoff
            if (in_primoFiglio.getAlpha() < in_primoFiglio.getBeta()) {  //cutoff
                System.out.println("NODO: B: " + in_primoFiglio.getBeta() + " ; A" + in_primoFiglio.getAlpha());
                if (in_primoFiglio.getPrimoFiglio() != null)
                    bigSolve2 (in_primoFiglio.getPrimoFiglio(), !myTurn);
                if (in_primoFiglio.getAlpha() > maxTmp)
                    maxTmp = in_primoFiglio.getAlpha();
            }
            in_primoFiglio = in_primoFiglio.getNext();
            }
        } else {    //turno dell'avversario, si guarda alpha <= beta perchè ci si mette nei panni dell'altro giocatore
            System.out.println("CASE: not myTurn");
            while (in_primoFiglio != null) {
                System.out.println("NODO: B: " + in_primoFiglio.getBeta() + " ; A" + in_primoFiglio.getAlpha());
                //if (in_primoFiglio.getAlpha() <= in_primoFiglio.getBeta()) {   //cutoff
                if (in_primoFiglio.getBeta() < in_primoFiglio.getAlpha()) {  //cutoff
                    if (in_primoFiglio.getPrimoFiglio() != null)
                        bigSolve2 (in_primoFiglio.getPrimoFiglio(), !myTurn);
                    if (in_primoFiglio.getBeta() > maxTmp)
                        maxTmp = in_primoFiglio.getBeta();
                }
                in_primoFiglio = in_primoFiglio.getNext();
            }
        }
        
        if (tmpHead.getPadre() != null) {                                                         // Se non si è arrivati alla radice dell'albero
            if (myTurn && tmpHead.getPadre().getAlpha() < maxTmp)                                   // Se è il mio turno e ...
            tmpHead.getPadre().setAlpha(maxTmp);      											// Si aggiorna il valore alpha del padre
            else if (tmpHead.getPadre().getBeta() < maxTmp)                                         // Altrimenti se è un turno avversario
            tmpHead.getPadre().setBeta(maxTmp);        											// Si aggiorna beta di in_primoFiglio
        }
    }
        
}
