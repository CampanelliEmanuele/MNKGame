package mnkgame;

public class Algoritms {

  public boolean first;
  public Algoritms (boolean in_first) { first = in_first; }

  TreeFunctions treeFunctions = new TreeFunctions();

  // 4

  /*
	//MINIMAX
	public int minimax (TreeNode in_currentNode, boolean myTurn){
		int eval;
		if(in_currentNode.getPrimoFiglio() == null){
			treeFunctions.assegnaValoreABFoglia(in_currentNode, first);
		  return in_currentNode.getVal();
		} else if (myTurn == true){
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



/*
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
*/



  /*
  public Color minimax(TreeNode in_padre, boolean myTurn){
    //Siamo alle foglie dell'albero
    if(in_padre.getPrimoFiglio() == null) return in_padre.getColor();

    //È un livello da massimizzare
    else if(myTurn){
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


  public static void prova (TreeNode p) {
    while (p != null) {
      System.out.println("kek");
      p=p.getNext();
    }
    if (p == null) System.out.println("ddddddddddd");
    System.out.println(p.getPrimoFiglio());
  }

	public static void bigSolve (TreeNode in_primoFiglio, boolean myTurn) {
    //Se non si è in una foglia scorre l'albero verso il basso
		if (in_primoFiglio.getPrimoFiglio() != null) {
      bigSolve (in_primoFiglio.getPrimoFiglio(), !myTurn);
		}
    else {                    //Assegna alpha o beta al nodo in_primoFiglio
      TreeNode tmpHead = in_primoFiglio;
			int maxTmp = Integer.MIN_VALUE;
      if (myTurn) {                                                                                   // Se è il mio turno, dovrò passare al padre il miglior valore di alpha (in quanto il padre rappresenta la scelta per l'avversaio)
        while (in_primoFiglio != null) {                                                                    // Per ogni fratello della foglia
          if (in_primoFiglio.getBeta() <= in_primoFiglio.getAlpha()) {  //cutoff
            System.out.println("NODO: B: " + in_primoFiglio.getBeta() + " ; A" + in_primoFiglio.getAlpha());
            if (in_primoFiglio.getPrimoFiglio() != null) bigSolve (in_primoFiglio.getPrimoFiglio(), !myTurn);
            if (in_primoFiglio.getAlpha() > maxTmp) maxTmp = in_primoFiglio.getAlpha();
          }
          in_primoFiglio = in_primoFiglio.getNext();
        }
      } else {    //turno dell'avversario, si guarda alpha <= beta perchè ci si mette nei panni dell'altro giocatore
        while (in_primoFiglio != null) {
          System.out.println("NODO: B: " + in_primoFiglio.getBeta() + " ; A" + in_primoFiglio.getAlpha());
          if (in_primoFiglio.getAlpha() <= in_primoFiglio.getBeta()) {   //cutoff
            if (in_primoFiglio.getPrimoFiglio() != null) bigSolve (in_primoFiglio.getPrimoFiglio(), !myTurn);
            if (in_primoFiglio.getBeta() > maxTmp) maxTmp = in_primoFiglio.getBeta();
          }

          in_primoFiglio = in_primoFiglio.getNext();
        }
      }

			if (tmpHead.getPadre() != null) {                                                            // Se non si è arrivati alla radice dell'albero
        if (myTurn) {                                                                               // Se è il mio turno
          if (tmpHead.getPadre().getAlpha() < maxTmp) {// Si aggiorna il valore alpha del padre
            tmpHead.getPadre().setAlpha(maxTmp);
          }
        } else {                                                                                    //turno avversario
          if (tmpHead.getPadre().getBeta() < maxTmp) {// Si aggiorna beta di in_primoFiglio
            tmpHead.getPadre().setBeta(maxTmp);
          }
        }
      }
    }
    // fine else
	}
  // fine bigSolve




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

  /*
  public TreeNode sceltaPercorso()
  */
}
