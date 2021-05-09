package subroutine;

import mnkgame.*;

public class Algoritms {

	private boolean first;

	protected Algoritms (boolean in_first) { this.first = in_first; }
  
	TreeFunctions treeFunctions = new TreeFunctions();
  
	/*
	 * MAX:  BT == S
	 * min:  BT != S 
	 */
	// Non si parte dalla radice ma dal primoFiglio dell'albero
	public void minMax (TreeNode in_foglia, MNKCellState in_BT, MNKCellState in_S) {
		while (in_foglia != null) {																	// Per ogni fratello
			if (in_foglia.getPrimoFiglio() != null) {												// Se non è una foglia
				if (in_S == MNKCellState.P1) minMax (in_foglia.getPrimoFiglio(), in_BT, MNKCellState.P2);			// Si scende di livello
				else minMax (in_foglia.getPrimoFiglio(), in_BT, MNKCellState.P1);									// Si scende di livello
			}
			if (in_foglia.getNext() == null && in_foglia.getPadre().getColor() == Colors.WHITE) in_foglia.getPadre().setColor(in_foglia.getColor());							// Se è l'ultimo figlio, il padre ne eredita il colore
			else {																											// Altrimenti
				if (in_BT != in_S && in_foglia.getColor() == Colors.GREEN) in_foglia.getPadre().setColor(Colors.GREEN);		// Se è un livello di min ed il nodo ha colore GREEN, si colora il padre
				else if (in_BT == in_S && in_foglia.getColor() == Colors.RED) in_foglia.getPadre().setColor(Colors.RED);	// Se e un livello di MAX ed il nodo ha colore RED, si colora il padre
			}
			in_foglia = in_foglia.getNext();
		}
	}
  
	// in_nodo è il nipote (primoFiglio del primoFiglio)
	public int greenNodeCounter (TreeNode in_nodo) {
		int greenCounter = 0;
		while (in_nodo != null) {
			if (in_nodo.getColor() == Colors.GREEN) greenCounter++;
			if (in_nodo.getPrimoFiglio() != null) greenCounter += greenNodeCounter (in_nodo.getPrimoFiglio());
			in_nodo = in_nodo.getNext();
		}
		return greenCounter;
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// Ritorna il nodo migliore tra i nodi del livello sottostante a quello del padre
	/*
	protected TreeNode sceltaPercorso_1LV (TreeNode in_padre) {
		TreeNode primoFiglio = in_padre.getPrimoFiglio();			// Serve per lo scorrimento dei fratelli
		TreeNode winNode = primoFiglio;												// Nodo ritornato
		int maxBeta = Integer.MIN_VALUE;

		while (primoFiglio != null) {								// Scorre tutti i figli e tira fuori quello col beta maggiore di tutti (winNode)
			if (primoFiglio.getBeta() > maxBeta) {
				maxBeta = primoFiglio.getBeta();
				winNode = primoFiglio;
			} else if (primoFiglio.getBeta() == maxBeta) {
				if (primoFiglio.getAlpha() > winNode.getAlpha()) winNode = primoFiglio;
			}
			primoFiglio = primoFiglio.getNext();
		}
		return winNode;
	}
	*/
	protected TreeNode sceltaPercorso (TreeNode in_padre, boolean in_takeMaxBeta, MNKCellState in_botState) {
		TreeNode primoFiglio = in_padre.getPrimoFiglio();			// Serve per lo scorrimento dei fratelli
		TreeNode winNode = primoFiglio;								// Nodo ritornato
		
		if (in_takeMaxBeta) {
			int maxBeta = Integer.MIN_VALUE;

			while (primoFiglio != null) {								// Scorre tutti i figli e tira fuori quello col beta maggiore di tutti (winNode)
				if (primoFiglio.getBeta() > maxBeta) {
					maxBeta = primoFiglio.getBeta();
					winNode = primoFiglio;
				} else if (primoFiglio.getBeta() == maxBeta) {
					if (primoFiglio.getAlpha() > winNode.getAlpha()) winNode = primoFiglio;
				}
				primoFiglio = primoFiglio.getNext();
			}
			return winNode;
		}
		
		else {
			while (primoFiglio != null) {
				if (primoFiglio.getColor() == Colors.GREEN) return primoFiglio;
				primoFiglio = primoFiglio.getNext();
			}
      
			// Se non ha trovato nodi di colore verde
			TreeFunctions tmpTreeFunctions = new TreeFunctions();
			
			primoFiglio = in_padre.getPrimoFiglio();
			while (primoFiglio != null) {
				tmpTreeFunctions.assegnaValoreABFoglia(primoFiglio, in_botState);
				primoFiglio = primoFiglio.getNext();
			}
			tmpTreeFunctions.defenseCell(in_padre, in_botState);
			
			if (in_padre.getDefense_i() < 0) return sceltaPercorso (in_padre, true, in_botState);
			else {
				in_padre.getMNKBoard().markCell(in_padre.getDefense_i(), in_padre.getDefense_j());
				return in_padre;
			}
			
		}
		
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// BIG SOLVE SI APPLICA SOLO ALLE FOGLIE DEL PRIMO FIGLIO !!!

	protected void bigSolve2 (TreeNode in_primoFiglio, boolean myTurn) {
	 	/*
    if (in_primoFiglio.getPrimoFiglio() != null) {              // Se il nodo in questione ha dei figli
      bigSolve2 (in_primoFiglio.getPrimoFiglio(), !myTurn);     // Si applica bigSolve al livello sottostante
		}
    */
    //else {                                                      // Se il nodo in questione non ha dei figli
      TreeNode tmpHead = in_primoFiglio;
			int maxTmp = Integer.MIN_VALUE;
      if (myTurn) {                                             // Se è il mio turno, dovrò passare al padre il miglior valore di alpha (in quanto il padre rappresenta la scelta per l'avversaio)
        System.out.println("CASE: myTurn");
        while (in_primoFiglio != null) {                        // Per ogni fratello della foglia
          System.out.println("A: " + in_primoFiglio.getAlpha() + " ; B: " + in_primoFiglio.getBeta());
          //if (in_primoFiglio.getBeta() <= in_primoFiglio.getAlpha()) {  //cutoff
          if (in_primoFiglio.getAlpha() < in_primoFiglio.getBeta()) {  //cutoff
            System.out.println("NODO: B: " + in_primoFiglio.getBeta() + " ; A" + in_primoFiglio.getAlpha());
            if (in_primoFiglio.getPrimoFiglio() != null) bigSolve2 (in_primoFiglio.getPrimoFiglio(), !myTurn);
            if (in_primoFiglio.getAlpha() > maxTmp) maxTmp = in_primoFiglio.getAlpha();
          }
          in_primoFiglio = in_primoFiglio.getNext();
        }
      } else {    //turno dell'avversario, si guarda alpha <= beta perchè ci si mette nei panni dell'altro giocatore
      System.out.println("CASE: not myTurn");
        while (in_primoFiglio != null) {
          System.out.println("NODO: B: " + in_primoFiglio.getBeta() + " ; A" + in_primoFiglio.getAlpha());
          //if (in_primoFiglio.getAlpha() <= in_primoFiglio.getBeta()) {   //cutoff
          if (in_primoFiglio.getBeta() < in_primoFiglio.getAlpha()) {  //cutoff
            if (in_primoFiglio.getPrimoFiglio() != null) bigSolve2 (in_primoFiglio.getPrimoFiglio(), !myTurn);
            if (in_primoFiglio.getBeta() > maxTmp) maxTmp = in_primoFiglio.getBeta();
          }
          in_primoFiglio = in_primoFiglio.getNext();
        }
      }

			if (tmpHead.getPadre() != null) {                                                         // Se non si è arrivati alla radice dell'albero
        if (myTurn) {                                                                           // Se è il mio turno
          if (tmpHead.getPadre().getAlpha() < maxTmp) tmpHead.getPadre().setAlpha(maxTmp);      // Si aggiorna il valore alpha del padre
        } else {                                                                                //turno avversario
          if (tmpHead.getPadre().getBeta() < maxTmp) tmpHead.getPadre().setBeta(maxTmp);        // Si aggiorna beta di in_primoFiglio
        }
        tmpHead.getPadre().setVal(maxTmp);
      }

    //}
    // fine else
	}
  // fine bigSolve

}
