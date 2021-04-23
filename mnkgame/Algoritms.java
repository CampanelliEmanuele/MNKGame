package mnkgame;

public class Algoritms {

  public boolean first;
  public Algoritms (boolean in_first) { first = in_first; }

  TreeFunctions treeFunctions = new TreeFunctions();

  // 4

  /*
	//MINIMAX
	public int minimax (TreeNode in_currentNode, boolean myNode){
		int eval;
		if(in_currentNode.getPrimoFiglio() == null){
			treeFunctions.assegnaValoreABFoglia(in_currentNode, first);
		  return in_currentNode.getVal();
		} else if (myNode == true){
			eval = Integer.MIN_VALUE;
			TreeNode child = in_currentNode.getPrimoFiglio();
			TreeNode fratello = child.getNext();
			while(fratello.getNext() != null){
				while (in_currentNode.getPrimoFiglio() != null) {
					eval = Math.min(eval, minimax(child, false));
					in_currentNode = in_currentNode.getPrimoFiglio();
				}
				fratello = fratello.getNext();
			}
			return eval;
		} else {
      TreeNode child = in_currentNode.getPrimoFiglio();
			TreeNode fratello = child.getNext();
			eval = Integer.MAX_VALUE;
			while(fratello.getNext() != null){
				while (in_currentNode.getPrimoFiglio() != null) {
					eval = Math.max(eval, minimax(child, true));
					in_currentNode = in_currentNode.getPrimoFiglio();
				}
				fratello = fratello.getNext();
			}
			return eval;
		}
	}
  */

  public void vaiAlleFoglieV2 (TreeNode in_primoDeiFigli, boolean in_first, int in_depth) {     // Preso il padre, visiterà l'albero (in qualsiasi modo) fino ad arrivare alle foglie ed attribuire ad esse un valore che sarà utilizzato dall'algoritmo alpha beta pruning
    while (in_primoDeiFigli != null) {                        // Per ogni figlio del nodo padre a cui è stata apllicata la funzione
      if (in_primoDeiFigli.getPrimoFiglio() == null)          // Se è una foglia
        //assegnaValoreABFoglia (in_primoDeiFigli, in_first);   // Assegna i valori alpha e beta
      else {                                                  // Se non è una foglia
          vaiAlleFoglie (in_primoDeiFigli.getPrimoFiglio(), in_first, in_depth++);  // Si applica la funzione nei sotto-alberi
      }
      in_primoDeiFigli = in_primoDeiFigli.getNext();
    }
  }

  /*
  public Color minimax(TreeNode in_padre, boolean myNode){
    //Siamo alle foglie dell'albero
    if(in_padre.getPrimoFiglio() == null) return in_padre.getColor();

    //È un livello da massimizzare
    else if(myNode){
      foreach(figlio in figli di in_padre){
        if(figlio.getColor() == Colors.GREEN)
        myColor =
      }
    }
    //Livello da minimizzare
    else {

    }
  }
  */

  //ALPHA-BETA V2
  public int alphaBetaPruning(TreeNode in_padre, boolean myNode, int in_depth, int alpha, int beta) {
    //scorre fino  alle foglie, ma deve tenere traccia della profindità a cui si è
    /*
    while (in_currentNode != null) {                        // Per ogni figlio del nodo padre a cui è stata apllicata la funzione
			if (in_primoDeiFigli.getPrimoFiglio() == null)          // Se è una foglia
				//assegnaValoreABFoglia (in_primoDeiFigli, in_first);   // Assegna i valori alpha e beta
			else {                                                  // Se non è una foglia
					vaiAlleFoglie (in_primoDeiFigli.getPrimoFiglio(), in_first);  // Si applica la funzione nei sotto-alberi
			}
			in_primoDeiFigli = in_primoDeiFigli.getNext();
		}


		1) vai alle foglie
		2) cerca il max valore il questione (A/B) tra i fratelli
		3) Assegna il valore max dei figli, al in_padre
		4) Ripetere fino a qunado non ritorni "in cima" (alla radice)
		*/

    if (myNode) {
			if (in_depth == 0 || in_padre.getPrimoFiglio() == null) return in_padre.getBeta();


    }
    else {
			if (in_depth == 0 || in_padre.getPrimoFiglio() == null) return in_padre.getAlpha();

    }
  }


	public static void bigSolve (TreeNode in_padre, bool myNode) {
		if (in_padre.getPrimoFiglio() != null) bigSolve (inpadre.getPrimoFiglio(), !myNode);
		else {
			int maxTmp = Integer.MIN_VALUE;
			while (in_padre != null) {
				if (in_padre.getPrimoFiglio() != null) bigSolve (in_padre.getPrimoFiglio(), !myNode);
				if (myNode) {
					if (in_padre.getAlpha() > maxTmp) maxTmp = in_padre.getAlpha();
				} else {
					if (in_padre.getBeta() > maxTmp) maxTmp = in_padre.getBeta();
				}

				if (in_padre.getPrimoFiglio() == null) {
					if (myNode) in_padre.setVal(in_padre.getBeta());
					else in_padre.setVal(in_padre.getAlpha());
				}
				In_padre = in_padre.getNext();
			}
			if (in_padre.getPadre() != NULL) in_padre.getPadre().setVal(tmpMax);
		}

	}




  /*
	ALPHA-BETA PRUNING
	public int alphabetaPruning (TreeNode in_currentNode, int alpha, int beta, boolean isMax, int depth) {
		System.out.println("inizio alphabeta");
    System.out.println("depth: " + depth);

    if(depth == 0){
      System.out.println("depth = 0");
			int max = Integer.MIN_VALUE;
      while(in_currentNode != null){
  			if(in_currentNode.getBeta() > max) max = in_currentNode.getBeta();
        in_currentNode.getNext();
        System.out.println("max: " + max);
        return max;
      }
    }


	  //Nodo da massimizzare
	  if (isMax) {
        TreeNode child = in_currentNode.getPrimoFiglio();
        TreeNode fratello = child.getNext();
	      int best = Integer.MIN_VALUE;
        System.out.println("Entrare nel ciclo alphabeta");
				//for (TreeNode child : in_padre.getPrimoFiglio()){
					//for(TreeNode fratello: child.getNext()){
				while(fratello.getNext() != null){
					while (in_currentNode.getPrimoFiglio() != null) {
						int valore = alphabetaPruning (child, alpha, beta, false, depth--);
						best = Math.max (best, valore);
						alpha = Math.max (best, alpha);
						if (beta <= alpha) break;
						in_currentNode = in_currentNode.getPrimoFiglio();
					}
					fratello = fratello.getNext();
				}
        System.out.println("best: " + best);
	      return best;
	  } else { //Nodo da minimizzare
        TreeNode child = in_currentNode.getPrimoFiglio();
        TreeNode fratello = child.getNext();
	      int best = Integer.MAX_VALUE;
        System.out.println("Entrare nel ciclo alphabeta");
				//for (TreeNode child : in_padre.getPrimoFiglio()){
					//for(TreeNode fratello: child.getNext()){
				while(fratello.getNext() != null){
					while (in_currentNode.getPrimoFiglio() != null) {
	          int valore = alphabetaPruning (child, alpha, beta, true, depth--);
	          best = Math.min (best, valore);
	          beta = Math.min (best, beta);
	          if (beta <= alpha) break;
						in_currentNode = in_currentNode.getPrimoFiglio();
					}
					fratello = fratello.getNext();
	      }
        System.out.println("best: " + best);
	      return best;
	  }
	}
  */

/*
	//CREARE SCELTA DEL PERCORSO
	public TreeNode sceltaPercorso (boolean isMaximizing, TreeNode in_padre, int in_depth) {
    System.out.println("in_padre: " + in_padre);
	  if (in_padre.getPrimoFiglio() == null){
        System.out.println("in_padre selezionato");
        return in_padre;
    }

	  TreeNode fratelloMaggiore = in_padre.getPrimoFiglio();
	  TreeNode winner = fratelloMaggiore;  // Si utilizza un costruttore specifico
	  int punteggioVincente = winner.getVal();

    //System.out.println("Entrare nel ciclo");

	  while (in_padre.getPrimoFiglio() != null) {
	    while (fratelloMaggiore.getNext() != null) {
        //System.out.println("Dentro ciclo");
	      if (isMaximizing) {
	        int movimento = alphabetaPruning (fratelloMaggiore, Integer.MIN_VALUE, Integer.MAX_VALUE, true, in_depth);
	        if (movimento > punteggioVincente){
	          punteggioVincente = movimento;
	          winner.setVal(punteggioVincente);
	          }
	        } else {
	          int movimento = alphabetaPruning (fratelloMaggiore, Integer.MIN_VALUE, Integer.MAX_VALUE, false, in_depth);
	          if (movimento < punteggioVincente) {
	            punteggioVincente = movimento;
	            winner.setVal(punteggioVincente);
	          }
	        }
	        fratelloMaggiore = fratelloMaggiore.getNext();
	      }
	    in_padre = in_padre.getPrimoFiglio();
	  }
    //System.out.println(winner);
	  return winner;
	}
  */

  public TreeNode sceltaPercorso()

}
