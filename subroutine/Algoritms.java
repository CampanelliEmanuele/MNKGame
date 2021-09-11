package subroutine;

import mnkgame.MNKCellState;
import subroutine.TreeFunctions.*;

public class Algoritms {

	protected Algoritms () {}
  
	Tree tree = new Tree();
	DefenseLogistics defenseFunctions = new DefenseLogistics();
	AttackLogistics attackFunctions = new AttackLogistics();
  
	protected TreeNode sceltaPercorso (TreeNode in_padre, boolean in_takeMaxBeta, MNKCellState in_botState) {
		/**
		 * Invocazione: Dopo aver creato l'albero, è usata per decretare il nodo contente la mossa migliore.
		 * 
		 * Funzione: Prende in input il nodo padre, un booleano per capire se bisogna prenere il massimo
		 * 			 valore di beta o meno, ed un altro booleano che indica lo stato di Slow_Unmade.
		 */
		TreeNode primoFiglio = in_padre.getPrimoFiglio();			// Serve per lo scorrimento dei fratelli
		TreeNode winNode = primoFiglio;								// Nodo da ritornare
		
		if (in_takeMaxBeta) {
			float maxBeta = Float.MIN_VALUE;
			while (primoFiglio != null) {												// Scorre tutti i figli e tira fuori quello col beta maggiore di tutti (winNode)
				if (primoFiglio.getBeta() > maxBeta) {									// Se un nuovo maxBeta si aggiorna il nodo vincente
					maxBeta = primoFiglio.getBeta();
					winNode = primoFiglio;
				} else if (primoFiglio.getBeta() == maxBeta)							// Se si hanno valori identici di maxBeta
					if (primoFiglio.getAlpha() > winNode.getAlpha()) winNode = primoFiglio;	// Si sceglie il nodo con l'alpha minore
				primoFiglio = primoFiglio.getNext();
			}
			return winNode;
		} else {																	// Se non bisogna ritornare il nodo col beta maggiore
			while (primoFiglio != null) {												// Si scorre il livello
				if (primoFiglio.getColor() == Colors.GREEN)									// Se c'è un nodo vincente 
					return primoFiglio;															// Lo si ritorna
				primoFiglio = primoFiglio.getNext();
			}
      
			// Se non ha trovato nodi di colore verde
			primoFiglio = in_padre.getPrimoFiglio();
			while (primoFiglio != null) {
				attackFunctions.assegnaValoreABFoglia(primoFiglio, in_botState);		// Si assegnagno i valori alpha e beta ai nodi del livello
				primoFiglio = primoFiglio.getNext();
			}
			defenseFunctions.defenseCell(in_padre, in_botState);						// Si cerca una possibile cella da difendere
			
			if (in_padre.getPriority_i() < 0)											// Se non ci sono celle da difendere
				return sceltaPercorso (in_padre, true, in_botState);						// Si fa la chiamata ricorsiva abilitando la ricerca del maxBeta
			else {
				in_padre.getMNKBoard().markCell(in_padre.getPriority_i(), in_padre.getPriority_j());
				return in_padre;
			}
		}
	}
	
	

}
