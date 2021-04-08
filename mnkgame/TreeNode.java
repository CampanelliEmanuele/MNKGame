package mnkgame;

public class TreeNode {

  private MNKBoard B;           // L'informazione rappresentata dal nodo

  private TreeNode padre;       // Collegamento con il padre
  private TreeNode primoFiglio; // Collegamento col primo dei vari sottoalberi
  private TreeNode next;        // Rappresenta il collegamento con l'elemento successivo
  private TreeNode prev;        // Rappresenta il collegamento con l'elemento precedente

  private int val;
  private int listPosition;     // Rappresenta l'indice dell'elemento nella lista di alberi
  private int alpha;
  private int beta;

  public TreeNode (MNKBoard in_B, TreeNode in_padre, TreeNode in_next, TreeNode in_prev, int in_listPosition, int in_alpha, int in_beta) {   // Costruttore per un nodo figlio di un padre
    this.B = in_B;
    this.padre = in_padre;
    this.primoFiglio = null;
    this.next = in_next;
    this.prev = in_prev;
    this.listPosition = in_listPosition;
    this.alpha = in_alpha;
    this.beta = in_beta;
  }

  public TreeNode (MNKBoard in_B, int in_alpha, int in_beta) {    // Costruttore per il nodo padre
    this.B = in_B;
    this.padre = null;
    this.primoFiglio = null;
    this.next = null;
    this.prev = null;
    this.listPosition = -1;
    this.alpha = Integer.MIN_VALUE;
    this.beta = Integer.MAX_VALUE;
  }

  public MNKBoard getMNKBoard () {
    return this.B;
  }

  public void setMNKBoard (MNKBoard in_MNKBoard) {
    this.B = in_MNKBoard;
  }

  public TreeNode getPadre () {
    return this.padre;
  }

  public void setPadre (TreeNode in_padre) {
    this.padre = in_padre;
  }

  public TreeNode getPrimoFiglio () {
    return this.primoFiglio;
  }

  public void setPrimoFiglio (TreeNode in_primoFiglio) {
    this. primoFiglio = in_primoFiglio;
  }

  public TreeNode getPrev (){
    return this.prev;
  }

  public void setPrev (TreeNode in_prev) {
    this.prev = in_prev;
  }

  public TreeNode getNext () {
    return this.next;
  }

  public void setNext (TreeNode in_next) {
    this.next = in_next;
  }

  public int getListPosition () {
    return this.listPosition;
  }

  public void setListPosition (int in_listPosition) {
    this.listPosition = in_listPosition;
  }

  public int getAlpha () {
    return this.alpha;
  }

  public void setAlpha (int in_alpha) {
    this.alpha = in_alpha;
  }

  public int getBeta () {
    return this.beta;
  }

  public void setBeta (int in_beta) {
    this.beta = in_beta;
  }

  public int getVal () {
    return this.val;
  }

  public void setVal (int in_val) {
    this.val = in_val;
  }

}
