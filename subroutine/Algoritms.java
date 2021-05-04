package subroutine;

public class Algoritms {

  protected Algoritms (boolean in_first) { first = in_first; }

  private static boolean first;
  
  TreeFunctions treeFunctions = new TreeFunctions();

  // BIG SOLVE SI APPLICA SOLO ALLE FOGLIE DEL PRIMO FIGLIO !!!

  protected static void bigSolve2 (TreeNode in_primoFiglio, boolean myTurn) {
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
