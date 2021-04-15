package mnkgame;

public class Algoritms {

  // 4
	// MINIMAX e ALPHA-BETA PRUNING
	public int alphabetaPruning (TreeNode in_currentNode, int alpha, int beta, boolean isMax, int depth) {
	  if (in_currentNode.getPrimoFiglio() == null || depth == 0) {
			assegnaValoreABFoglia(in_padre);
		  return in_currentNode.getVal();
	  }
		TreeNode child = in_currentNode.getPrimoFiglio();
		TreeNode fratello = child.getNext();
	  //Nodo da massimizzare
	  if (isMax) {
	      int best = Integer.MIN_VALUE;
				//for (TreeNode child : in_padre.getPrimoFiglio()){
					//for(TreeNode fratello: child.getNext()){
				while(fratello.getNext() != null){
					while (in_currentNode.getPrimoFiglio() != null) {
						int valore = alphabetaPruning (child, alpha, beta, false, depth--);
						best = Math.max (best, valore);
						alpha = Math.max (best, alpha);
						if (beta <= alpha) break;
						fratello = fratello.getNext();
					}
					child = child.getPrimoFiglio();
				}
	      return best;
	  } else { //Nodo da minimizzare
	      int best = Integer.MAX_VALUE;
				//for (TreeNode child : in_padre.getPrimoFiglio()){
					//for(TreeNode fratello: child.getNext()){
				while(fratello.getNext() != null){
					while (in_currentNode.getPrimoFiglio() != null) {
	          int valore = alphabetaPruning (child, alpha, beta, true, depth--);
	          best = Math.min (best, valore);
	          beta = Math.min (best, beta);
	          if (beta <= alpha) break;
						fratello = fratello.getNext();
	        }
					child = child.getPrimoFiglio();
	      }
	      return best;
	  }
	}

	//CREARE SCELTA DEL PERCORSO
	public TreeNode sceltaPercorso (boolean isMaximizing, TreeNode in_padre, int in_depth) {

	  if (in_padre.getPrimoFiglio() == null)
	    return in_padre;

	  TreeNode fratelloMaggiore = in_padre.getPrimoFiglio();
	  TreeNode winner = fratelloMaggiore;  // Si utilizza un costruttore specifico
	  int punteggioVincente = winner.getVal();

	  while (in_padre.getPrimoFiglio() != null) {
	    while (fratelloMaggiore.getNext() != null) {
	      if (isMaximizing) {
	        int movimento = alphabetaPruning (fratelloMaggiore, Integer.MIN_VALUE, Integer.MAX_VALUE, false, in_depth);
	        if (movimento > punteggioVincente){
	          punteggioVincente = movimento;
	          winner.setVal(punteggioVincente);
	          }
	        } else {
	          int movimento = alphabetaPruning (fratelloMaggiore, Integer.MIN_VALUE, Integer.MAX_VALUE, true, in_depth);
	          if (movimento < punteggioVincente) {
	            punteggioVincente = movimento;
	            winner.setVal(punteggioVincente);
	          }
	        }
	        fratelloMaggiore = fratelloMaggiore.getNext();
	      }
	    in_padre = in_padre.getPrimoFiglio();
	  }
	  return winner;
	}

}
