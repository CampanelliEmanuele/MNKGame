package subroutine;

import mnkgame.MNKGameState;
import subroutine.TreeFunctions.TreeNode;
import subroutine.TreeFunctions.Colors;

public class Stampa {

    protected Stampa () {}

    protected static void printInfo (TreeNode in_node, int in_level) {
  		System.out.println ("------------------------------------------");
  		if (in_node.getMNKBoard().gameState() != MNKGameState.OPEN)
  			System.out.println ("WINNER_WINNER_WINNER_WINNER_WINNER_WINNER_WINNER_WINNER_WINNER_WINNER_WINNER_WINNER_WINNER_WINNER_WINNER_WINNER");
  		System.out.println ("LIVELLO: " + in_level);
  		System.out.println ("NIQ: " + in_node);
  		in_node.printNodeInfo();
  		System.out.println ("------------------------------------------");
  	}

    protected void printTree (TreeNode in_padre, boolean in_onlyLeaf, int in_level, int in_limit) {		// in_level rappresenta il livello del nodo in_padre
		/**
		 * Stampa foglie: 								printTree (Nodo, true,  node_level, n)  --> n
		 * Stampa albero intero: 					printTree (Node, false, node_level, n)  --> n < 1
		 * Stampa albero primi n-livelli: printTree (Node, false, node_level, n)  --> n >= 1
		 * in_limit: è il livello a cui fermare la stampa
		 * in_level: è il livello di partenza della stampa
		 */
		if (in_padre != null) {												// Se si passa un nodo
  			if (in_onlyLeaf) {													// Se si vuole stampare solo le foglie
  				while (in_padre != null) {								// Per ogni fratello (e padre compreso) si richiama la funzione
  					if (in_padre.getPrimoFiglio() == null)				// Se è una foglia fa la stampa
  						printInfo (in_padre, in_level); 
  					if (in_padre.getPrimoFiglio() != null)				// Altrimenti si richiama
  						printTree (in_padre.getPrimoFiglio(), true, in_level + 1, -1);	
  					in_padre = in_padre.getNext();
  				}
  			} else {										// Se non si vuole stampare solo le foglie ma bensì tutto/parte dell'albero
  				if (in_limit >= 1) {		// Caso in cui si  vuole stampare solo fino ad un certo livello in_limit
  					while (in_padre != null) {
  						printInfo (in_padre, in_level);
  						if (in_padre.getPrimoFiglio() != null)
  							printTree (in_padre.getPrimoFiglio(), false, in_level + 1, in_limit - 1);
  						in_padre = in_padre.getNext();
  					}
  				} else {								// Se in_limit <= 0 (ovvero se si vuole stamapre tutto l'albero)
  					while (in_padre != null) {
  						printInfo (in_padre, in_level);
  						if (in_padre.getPrimoFiglio() != null)
  							printTree (in_padre.getPrimoFiglio(), false, in_level + 1, -1);
  						in_padre = in_padre.getNext();
  					}
  				}
  			}
  		}

  	}
	
	public void printMoleColor (TreeNode in_padre) {
		if (in_padre != null) {
  			while (in_padre != null) {
  				if (in_padre.getColor() != Colors.GREEN && in_padre.getColor() != Colors.RED) {
  					System.out.println("Vittoria: " + in_padre.getMNKBoard().gameState());
  					System.out.println("Nodo: " + in_padre);
  					System.out.println("Colore: " + in_padre.getColor());
  				}
  				if (in_padre.getPrimoFiglio() != null)
  					printMoleColor (in_padre.getPrimoFiglio());		// Se ha dei figli si scende
  				in_padre = in_padre.getNext();
  			}
  		}
	}

}
