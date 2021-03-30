package mnkgame;

public class TreeNode {

    private TreeNode fogliaSx;
    private TreeNode fogliaDx;
    private MNKBoard B;
    
    public TreeNode (TreeNode in_fogliaSx, TreeNode in_fogliaDx, MNKBoard in_B) {
      this.fogliaSx = in_fogliaSx;
      this.fogliaDx = in_fogliaDx;
      this.B = in_B;
    }
    public TreeNode() {
    }
  
    public TreeNode getFogliaDx () {
      return this.fogliaDx;
    }
  
    public TreeNode getFogliaSx () {
      return this.fogliaSx;
    }
  
    public void setFogliaSx (TreeNode in_fogliaSx) {
      this.fogliaSx = in_fogliaSx; 
    }
  
    public void setFogliaDx (TreeNode in_fogliaDx) {
      this.fogliaDx = in_fogliaDx;
    }
  
    public MNKBoard getMNKBoard () {
      return this.B;
    }
  
    public void setMNKBoard (MNKBoard in_MNKBoard) {
      this.B = in_MNKBoard;
    }
    
  }
