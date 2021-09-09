package subroutine;

import mnkgame.*;
import mnkgame.MNKCell;
import mnkgame.MNKCellState;
import mnkgame.MNKGameState;

public class TreeFunctions {

	protected TreeFunctions () {}
	
	/**
	 * La funzioni principali di codesto file sono defenseCell ed assegnaValoreABFoglia, le quali fanno uso
	 * di un array contenente le principali variabili. Il resto dei metodi della classe vertono sulla
	 * modifiche di quest'ultime.
	 * 
	 * Per memorizzare i sottoalberi si utilizza una struttura dati dinamica omogena lineare.
	 */

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	protected void quickMark (TreeNode in_radice, boolean in_first) {
		int start = 1; if (in_first) start = 0;
		MNKCell[] MC = in_radice.getMNKBoard().getMarkedCells();
		
		// Si scorrono le celle marcate fino a quando non se ne trova una con k spazi liberi
		for (int pos = start; pos < MC.length; pos += 2) {
			int i_MC = MC[pos].i;
			int j_MC = MC[pos].j;
			int M = in_radice.getMNKBoard().M;
			int N = in_radice.getMNKBoard().N;
			int K = in_radice.getMNKBoard().K;
			int strike = 0;
			
			for (int c = 0; c <= N - 1; c++) {     	// Controllo della i-esima riga (da sx verso dx)
				
			}
		}
	
	}
	*/

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	protected void createTree (TreeNode in_padre, int in_depthLimit, boolean in_first) {
		/**
		 * Invocazione: ???
		 * 
		 * Funzione: Crea in_depthLimit livelli di un albero avente come nodi dei TreeNode.
		 * 			 L'albero è implementato tramite puntatori ai nodi primoFiglio e fratelli.
		 */
		if (in_depthLimit > 1) {													// Se in_depthLimit > 1 --> Si crea un'altro livello
			if (in_padre.getMNKBoard().gameState() == MNKGameState.OPEN) {
				MNKCell[] FC = in_padre.getMNKBoard().getFreeCells();
				MNKCell[] MC = in_padre.getMNKBoard().getMarkedCells();
				int M = in_padre.getMNKBoard().M;
				int N = in_padre.getMNKBoard().N;
				int K = in_padre.getMNKBoard().K;
				MNKBoard tmpB = new MNKBoard(M,N,K);
				for (int e = 0; e < MC.length; e++)
					tmpB.markCell (MC[e].i, MC[e].j);
				
				while (in_padre != null) {												// Per ogni fratello (e padre compreso) si crea il sottoalbero
					tmpB.markCell (FC[0].i, FC[0].j);		  								// Temporaneo marcamento della prima cella
					TreeNode primoFiglio = new TreeNode(tmpB, in_padre, true, null);		// Si crea il primo figlio
					in_padre.setPrimoFiglio(primoFiglio);									// Si setta il primo figlio del nodo padre

					createTree (primoFiglio, in_depthLimit - 1, in_first);
					TreeNode prev = primoFiglio;											// Prev creato uguale al primoFiglio

					for (int e = 1; e < FC.length; e++) {									// Ciclo per la creazione dei nodi di un livello
						MNKBoard tmp2B = new MNKBoard (M,N,K);									// Crea una nuova board per ogni nodo del livello in questione
						for (int el = 0; el < MC.length; el++)
							tmp2B.markCell (MC[el].i, MC[el].j);

						tmp2B.markCell (FC[e].i, FC[e].j);										// Temporaneo marcamento della cella
						TreeNode figlio = new TreeNode (tmp2B, in_padre, false, prev);
						prev.setNext (figlio);													// Il fratello prev è ora collegato al suo nuovo fratello

						prev = figlio;															// Il nuovo figlio è ora il prev (ovvero l'ultimo figlio creato)
						createTree (figlio, in_depthLimit - 1, in_first);
					}
					in_padre = in_padre.getNext();
				}
			} else if (in_padre.getMNKBoard().gameState() == MNKGameState.WINP1) {
				if (in_first)
					in_padre.setColor(Colors.GREEN);
				else
					in_padre.setColor(Colors.RED);
			} else if (in_padre.getMNKBoard().gameState() == MNKGameState.WINP2) {
				if (in_first)
					in_padre.setColor(Colors.RED);
				else
					in_padre.setColor(Colors.GREEN);
			} else
				in_padre.setColor(Colors.RED);
		}
	}
 
}
