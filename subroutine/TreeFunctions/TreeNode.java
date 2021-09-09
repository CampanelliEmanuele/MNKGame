package subroutine.TreeFunctions;

import mnkgame.MNKBoard;
import mnkgame.MNKCell;

public class TreeNode {

  private MNKBoard B;           // L'informazione rappresentata dal nodo

  private TreeNode padre;       // Collegamento con il padre
  private TreeNode primoFiglio; // Collegamento col primo dei vari sottoalberi
  private TreeNode next;        // Rappresenta il collegamento con l'elemento successivo
  private TreeNode prev;        // Rappresenta il collegamento con l'elemento precedente

  private int listPosition;     // Rappresenta l'indice dell'elemento nella lista di alberi (usata solo per il debug)
  private float alpha;
  private float beta;
  private Colors color;
  private int priority_i;
  private int priority_j;
  
  // Costruttore per il nodo padre, la radice dell'albero
  public TreeNode (MNKBoard in_B) {
    this.B = in_B;
    this.padre = null;
    this.primoFiglio = null;
    this.next = null;
    this.prev = null;
    this.listPosition = -1;
    this.alpha = Integer.MIN_VALUE;
    this.beta = Integer.MIN_VALUE;
    this.color = Colors.WHITE;
    this.priority_i = -1;
    this.priority_j = -1;
  }

  // Costruttore di un figlio
  public TreeNode (MNKBoard in_B, TreeNode in_padre, boolean in_primoFiglio, TreeNode in_prev) {
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
    this.color = Colors.WHITE;
    this.priority_i = -1;
    this.priority_j = -1;
  }

  public void printNodeInfo () {
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
    System.out.println ("Colore: " + this.color);
    System.out.println ("priority_i: " + this.priority_i);
    System.out.println ("priority_j: " + this.priority_j);
    printMCInfo ();
    System.out.println ("------------------------------------------");
  }

  public void printMCInfo () {
    MNKCell[] MC = this.B.getMarkedCells();
    System.out.println ("CELLE MARCATE + STATO");
    for (int pos = 0; pos < MC.length; pos++)
      System.out.println ("(" + MC[pos].i + "," + MC[pos].j + ") : " + MC[pos].state);
  }

  public MNKBoard getMNKBoard () { return this.B; }
  public void setMNKBoard (MNKBoard in_MNKBoard) { this.B = in_MNKBoard; }

  public TreeNode getPadre () { return this.padre; }
  public void setPadre (TreeNode in_padre) { this.padre = in_padre; }

  public TreeNode getPrimoFiglio () { return this.primoFiglio; }
  public void setPrimoFiglio (TreeNode in_primoFiglio) { this. primoFiglio = in_primoFiglio; }

  public TreeNode getNext () { return this.next; }
  public void setNext (TreeNode in_next) { this.next = in_next; }

  public TreeNode getPrev () { return this.prev; }
  public void setPrev (TreeNode in_prev) { this.prev = in_prev; }

  public int getListPosition () { return this.listPosition; }
  public void setListPosition (int in_listPosition) { this.listPosition = in_listPosition; }

  public float getAlpha () { return this.alpha; }
  public void setAlpha (float in_alpha) { this.alpha = in_alpha; }

  public float getBeta () { return this.beta; }
  public void setBeta (float in_beta) { this.beta = in_beta; }

  public Colors getColor () { return this.color; }
  public void setColor (Colors in_color) { this.color = in_color; }

  public int getPriority_i () { return this.priority_i; }
  public void setPriority_i (int in_defense_i) { this.priority_i = in_defense_i; }

  public int getPriority_j () { return this.priority_j; }
  public void setPriority_j (int in_defense_j) { this.priority_j = in_defense_j; }
  
  public void setPriorityCell (int in_i, int in_j) {
	  this.priority_i = in_i;
	  this.priority_j = in_j;
  }

}
