import subroutine.Colors;
import subroutine.TreeNode;

//import mnkgame.*;
//import subroutine.Colors;
//import subroutine.TreeNode;

public class Function_old_unless {

	/*
	 
	 
	/* Pseudo-codice per la difesa di una cella
	If (currentPlayer)
		cp_counter ++
		If (cp_counter >= K) cp_counter = 1
		If (cp_counter == K-1 && vars[defense_i] >= 0) 		-> A
			Nodo.setDefense_i(vars[defense_i])
			Nodo.setDefense_j(vars[defense_j])
		fc_counter = 0
	Else if (freeCell)
		fc_counter++
		If (fc_counter > 1) cp_counter = 0                	-> B/C
		vars[defense_i] = cella_in_questione.i
		vars[defense_j] = cella_in_questione.j
		If (in_vars[cp_counter] == in_vars[k] - 1)
			Nodo.setDefense_i(vars[defense_i])
			Nodo.setDefense_j(vars[defense_j])
			cp_counter = 0                                   -> B
	Else if (enemyCell)
		cp_counter = 0
		fc_counter = 0
		vars[defense_i] = -1
		vars[defense_j] = -1
	
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// Ritorna il numero di nodi verdi di un nodo rosso (funzione non testata)
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
	
	  // 7 (?)
	  // Funzione che ritorna ogni foglia primoFiglio
	  public static TreeNode ritornaFogliePF (TreeNode in_radice) {
	    while (in_radice != null) {                              // Per ogni fratello
	      if (in_radice.getPrimoFiglio() == null) {              // Se il nodo in questione non ha figli
	        return in_radice;                                    // Lo si ritorna in quanto foglia
	      } else {                                               // Se invece ha dei figli
	        ritornaFogliePF(in_radice.getPrimoFiglio());  // Si scorre albero verso il basso
	      }
	      System.out.println ("in_radice_ " + in_radice);
	      in_radice = in_radice.getNext();
	    }
	    System.out.println ("ao");
	    return null;
	  }

	  public static TreeNode ritornaFogliePF2 (TreeNode in_radice) {
	    //while (in_radice != null) {                              // Per ogni fratello
	      if (in_radice.getPrimoFiglio() == null) {              // Se il nodo in questione non ha figli
	        return in_radice;                                    // Lo si ritorna in quanto foglia
	      } else {                                               // Se invece ha dei figli
	        ritornaFogliePF2(in_radice.getPrimoFiglio());  // Si scorre albero verso il basso
	      }
	    System.out.println ("in_radice_ " + in_radice);
	      //in_radice = in_radice.getNext();
	    System.out.println ("ao");
	    return in_radice.getNext();
	  }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	  // Funzione inutile perchè la funzione createTree consete di crare un numero a piacere di livelli (1 compreso)
	  public static void createTree_1LV (TreeNode in_padre) {
		  if (in_padre.getMNKBoard().gameState() == MNKGameState.OPEN) {				// Se in_depthLimit > 1 --> Si crea un'altro livello
			MNKCell[] FC = in_padre.getMNKBoard().getFreeCells();
			MNKCell[] MC = in_padre.getMNKBoard().getMarkedCells();

			MNKBoard B_padre = in_padre.getMNKBoard();

			MNKBoard newB = new MNKBoard (M,N,K);
			for (int e = 0; e < MC.length; e++) {
				newB.markCell (MC[e].i, MC[e].j);
			}

			newB.markCell(FC[0].i, FC[0].j);
			TreeNode figlioMaggiore = new TreeNode(newB, in_padre, true, null);
			in_padre.setPrimoFiglio(figlioMaggiore);
			TreeNode prev = figlioMaggiore;

			for (int e = 0; e < FC.length; e++) {					// Ciclo per la creazione dei nodi di un livello
				MNKBoard newB2 = new MNKBoard (M,N,K);			// Crea una nuova board per ogni nodo del livello in questione
				for (int el = 0; el < MC.length; el++) {
					newB2.markCell (MC[el].i, MC[el].j);
				}

				newB2.markCell (FC[e].i, FC[e].j);					// Temporaneo marcamento della cella
				TreeNode figlio = new TreeNode (newB2, in_padre, false, prev);
				prev.setNext (figlio);											// Il fratello prev è ora collegato al suo nuovo fratello

				prev = figlio;															// Il nuovo figlio è ora il prev (ovvero l'ultimo figlio creato)
			}

		}
		// Fine if
		else if (in_padre.getMNKBoard().gameState() == MNKGameState.WINP1) {
			if (first) in_padre.setColor(Colors.GREEN);
			else in_padre.setColor(Colors.RED);
		}
		else if (in_padre.getMNKBoard().gameState() == MNKGameState.WINP2) {
			if (first) in_padre.setColor(Colors.RED);
			else in_padre.setColor(Colors.GREEN);
		}
		else in_padre.setColor(Colors.GREY);
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// CREDoCstampi le MC di una MNKBoard
	public static void printMC (MNKBoard in_board) {
	  MNKCell[] FC = in_board.getFreeCells();
	  MNKCell[] MC = in_board.getMarkedCells();
	  for (int e = 0; e < MC.length; e++) {
	    System.out.println("MC[" + e + "] -> (" + MC[e].i + "," + MC[e].j + ")");
	  }
	  System.out.println("");
	  if (in_board.getFreeCells().length == 0) System.out.println("fine");
	  else {
	    in_board.markCell(in_board.getFreeCells()[0].i, in_board.getFreeCells()[0].j);
	    printMC (B);
	  }
	}
	
	*/

}


