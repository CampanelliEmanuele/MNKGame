package subroutine;

import mnkgame.MNKCellState;
import subroutine.TreeFunctions.*;

public class Algoritms {

	protected Algoritms ( ) {}
  
	Tree tree = new Tree();
	DefenseLogistics defenseFunctions = new DefenseLogistics();
	AttackLogistics attackFunctions = new AttackLogistics();
  
	/*
	 * MAX:  BT == S -> RED
	 * min:  BT != S -> GREEN
	 */
	// Non si parte dalla radice ma dal primoFiglio dell'albero
	// Se ultimo nodo è GREEN -> Corretto
	// Se ultimo nodo è RED   -> assegna WHITE all'ultimo nodos
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
			//else in_padre.setColor(Colors.GREY);
			else in_foglia.setColor(Colors.RED);
		}
		*/
		while (in_foglia != null) {
			if (in_foglia.getPrimoFiglio() != null) {
				if (in_S == MNKCellState.P1)
					minMax (in_foglia.getPrimoFiglio(), in_BT, MNKCellState.P2);			
				else if (in_S == MNKCellState.P2)
					minMax (in_foglia.getPrimoFiglio(), in_BT, MNKCellState.P1);
			} 
			if (in_foglia.getNext() != null) {			
				if (in_BT != in_S && in_foglia.getColor() == Colors.GREEN)
					in_foglia.getPadre().setColor(in_foglia.getColor());	
				else if (in_BT == in_S && in_foglia.getColor() == Colors.RED)
					in_foglia.getPadre().setColor(in_foglia.getColor());
			} 
			else if (in_foglia.getNext() == null && in_foglia.getPadre().getColor() == Colors.WHITE)
				in_foglia.getPadre().setColor(in_foglia.getColor());
			in_foglia = in_foglia.getNext();
		}
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
			float maxBeta = Float.MIN_VALUE;
			while (primoFiglio != null) {								// Scorre tutti i figli e tira fuori quello col beta maggiore di tutti (winNode)
				if (primoFiglio.getBeta() > maxBeta) {
					maxBeta = primoFiglio.getBeta();
					winNode = primoFiglio;
				} else if (primoFiglio.getBeta() == maxBeta)
					if (primoFiglio.getAlpha() > winNode.getAlpha()) winNode = primoFiglio;
				primoFiglio = primoFiglio.getNext();
			}
			return winNode;
		} else {
			while (primoFiglio != null) {
				if (primoFiglio.getColor() == Colors.GREEN)
					return primoFiglio;
				primoFiglio = primoFiglio.getNext();
			}
      
			// Se non ha trovato nodi di colore verde
			primoFiglio = in_padre.getPrimoFiglio();
			while (primoFiglio != null) {
				attackFunctions.assegnaValoreABFoglia(primoFiglio, in_botState);
				primoFiglio = primoFiglio.getNext();
			}
			defenseFunctions.defenseCell(in_padre, in_botState);
			
			if (in_padre.getPriority_i() < 0)
				return sceltaPercorso (in_padre, true, in_botState);
			else {
				in_padre.getMNKBoard().markCell(in_padre.getPriority_i(), in_padre.getPriority_j());
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
