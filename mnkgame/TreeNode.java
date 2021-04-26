package mnkgame;

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
    this.val = -100;
    this.color = Colors.WHITE;
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
    this.val = -100;
    this.color = Colors.WHITE;
  }

  public void printNodeInfo () {
    System.out.println ("------------------------------------------");
    System.out.println ("Padre: " + this.padre);
    System.out.println ("Board: " + this.B);
    System.out.println ("Primo figlio: " + this.primoFiglio);
    System.out.println ("List position: " + this.listPosition);
    System.out.println ("Next: " + this.next);
    System.out.println ("Prev: " + this.prev);
    System.out.println ("alpha: " + this.alpha + "\nbeta: " + this.beta);
    System.out.println ("Valore: " + this.val);
    System.out.println ("Colore: " + this.color);
    System.out.println ("------------------------------------------");
  }

  public void printMCInfo () {
    MNKCell[] MC = this.B.getMarkedCells();
    System.out.println ("CELLE MARCATE + STATO");
    for (int pos = 0; pos < MC.length; pos++) {
      System.out.println ("(" + MC[pos].i + "," + MC[pos].j + ") : " + MC[pos].state);
    }
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

  public int getAlpha () { return this.alpha; }
  public void setAlpha (int in_alpha) { this.alpha = in_alpha; }

  public int getBeta () { return this.beta; }
  public void setBeta (int in_beta) { this.beta = in_beta; }

  public int getVal () { return this.val; }
  public void setVal (int in_val) { this.val = in_val; }

  public Colors getColor () { return this.color; }
  public void setColor (Colors in_color) { this.color = in_color; }

}
