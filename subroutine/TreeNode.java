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
  private int alpha;
  private int beta;
  private int val;
  private Colors color;
  private int defense_i;
  private int defense_j;
  
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
    this.defense_i = -1;
    this.defense_j = -1;
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
    this.defense_i = -1;
    this.defense_j = -1;
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
    System.out.println ("Defense_i: " + this.defense_i);
    System.out.println ("Defense_j: " + this.defense_j);
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

  protected int getAlpha () { return this.alpha; }
  protected void setAlpha (int in_alpha) { this.alpha = in_alpha; }

  protected int getBeta () { return this.beta; }
  protected void setBeta (int in_beta) { this.beta = in_beta; }

  protected int getVal () { return this.val; }
  protected void setVal (int in_val) { this.val = in_val; }

  protected Colors getColor () { return this.color; }
  protected void setColor (Colors in_color) { this.color = in_color; }

  protected int getDefense_i () { return this.defense_i; }
  protected void setDefense_i (int in_defense_i) { this.defense_i = in_defense_i; }

  protected int getDefense_j () { return this.defense_j; }
  protected void setDefense_j (int in_defense_j) { this.defense_j = in_defense_j; }

}
