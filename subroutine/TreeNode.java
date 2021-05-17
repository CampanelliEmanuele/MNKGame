package subroutine;

import mnkgame.MNKBoard;
import mnkgame.MNKCell;

public class TreeNode {

  private MNKBoard B;           // L'informazione rappresentata dal nodo

  private TreeNode padre;       // Collegamento con il padre
  private TreeNode primoFiglio; // Collegamento col primo dei vari sottoalberi
  private TreeNode next;        // Rappresenta il collegamento con l'elemento successivo
  private TreeNode prev;        // Rappresenta il collegamento con l'elemento precedente

  private int listPosition;     // Rappresenta l'indice dell'elemento nella lista di alberi
  private float alpha;
  private float beta;
  private float val;
  private Colors color;
  private int priority_i;
  private int priority_j;
  
  // Variabili (a occhio) più sacrificabili: listPosition, val e color

  // Costruttore per il nodo padre, la radice dell'albero
  protected TreeNode (MNKBoard in_B) {
    this.B = in_B;
    this.padre = null;
    this.primoFiglio = null;
    this.next = null;
    this.prev = null;
    this.listPosition = -1;
    this.alpha = Integer.MIN_VALUE;
    this.beta = Integer.MIN_VALUE;
    this.val = -100;
    this.color = Colors.WHITE;
    this.priority_i = -1;
    this.priority_j = -1;
  }

  // Costruttore di un figlio
  protected TreeNode (MNKBoard in_B, TreeNode in_padre, boolean in_primoFiglio, TreeNode in_prev) {
    this.B = in_B;
    this.padre = in_padre;
    this.primoFiglio = null;    // Nodo primoFiglio che SI USA SOLO PER IL PADRE !!!
    this.next = null;
    if (in_primoFiglio) {
      this.prev = null;
      this.listPosition = 0;
    } else {
      this.prev = in_prev;
      this.listPosition = in_prev.getListPosition() + 1;
    }
    this.alpha = Integer.MIN_VALUE;
    this.beta = Integer.MIN_VALUE;
    this.val = -100;
    this.color = Colors.WHITE;
    this.priority_i = -1;
    this.priority_j = -1;
  }

  protected void printNodeInfo () {
    System.out.println ("------------------------------------------");
    System.out.println ("Padre: " + this.padre);
    System.out.println ("Board: " + this.B);
    System.out.println ("MC.length: " + this.B.getMarkedCells().length);
    System.out.println ("FC.length: " + this.B.getFreeCells().length);
    System.out.println ("Primo figlio: " + this.primoFiglio);
    System.out.println ("List position: " + this.listPosition);
    System.out.println ("Next: " + this.next);
    System.out.println ("Prev: " + this.prev);
    System.out.println ("alpha: " + this.alpha + "\nbeta: " + this.beta);
    System.out.println ("Valore: " + this.val);
    System.out.println ("Colore: " + this.color);
    System.out.println ("priority_i: " + this.priority_i);
    System.out.println ("priority_j: " + this.priority_j);
    printMCInfo ();
    System.out.println ("------------------------------------------");
  }

  protected void printMCInfo () {
    MNKCell[] MC = this.B.getMarkedCells();
    System.out.println ("CELLE MARCATE + STATO");
    for (int pos = 0; pos < MC.length; pos++) {
      System.out.println ("(" + MC[pos].i + "," + MC[pos].j + ") : " + MC[pos].state);
    }
  }

  protected MNKBoard getMNKBoard () { return this.B; }
  protected void setMNKBoard (MNKBoard in_MNKBoard) { this.B = in_MNKBoard; }

  protected TreeNode getPadre () { return this.padre; }
  protected void setPadre (TreeNode in_padre) { this.padre = in_padre; }

  protected TreeNode getPrimoFiglio () { return this.primoFiglio; }
  protected void setPrimoFiglio (TreeNode in_primoFiglio) { this. primoFiglio = in_primoFiglio; }

  protected TreeNode getNext () { return this.next; }
  protected void setNext (TreeNode in_next) { this.next = in_next; }

  protected TreeNode getPrev () { return this.prev; }
  protected void setPrev (TreeNode in_prev) { this.prev = in_prev; }

  protected int getListPosition () { return this.listPosition; }
  protected void setListPosition (int in_listPosition) { this.listPosition = in_listPosition; }

  protected float getAlpha () { return this.alpha; }
  protected void setAlpha (float in_alpha) { this.alpha = in_alpha; }

  protected float getBeta () { return this.beta; }
  protected void setBeta (float in_beta) { this.beta = in_beta; }

  protected float getVal () { return this.val; }
  protected void setVal (float in_val) { this.val = in_val; }

  protected Colors getColor () { return this.color; }
  protected void setColor (Colors in_color) { this.color = in_color; }

  protected int getPriority_i () { return this.priority_i; }
  protected void setPriority_i (int in_defense_i) { this.priority_i = in_defense_i; }

  protected int getPriority_j () { return this.priority_j; }
  protected void setPriority_j (int in_defense_j) { this.priority_j = in_defense_j; }
  
  protected void setPriorityCell (int in_i, int in_j) {
	  this.priority_i = in_i;
	  this.priority_j = in_j;
  }

}
