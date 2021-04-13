package mnkgame;



public class Main {

    public static void main (String[] args) {

        MNKBoard bp = new MNKBoard (3, 3, 3);
        TreeNode padre = new TreeNode (bp);

        MNKBoard bf1 = new MNKBoard (3, 3, 3);
        TreeNode figlio1 = new TreeNode (bf1, padre);

        MNKBoard bf2 = new MNKBoard (3, 3, 3);
        TreeNode figlio2 = new TreeNode (bf2, padre);

        // System.out.println("loool");
    }
}
