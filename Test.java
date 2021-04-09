public class Test {

  public static void c(int[] a) {
    a[0]++;
  }

  public static void main (String[] args) {
    int[] a = new int[3];
    a[0] = 9;

    c(a);

    System.out.println(a[0] + " " + a[2]);
  }
}
