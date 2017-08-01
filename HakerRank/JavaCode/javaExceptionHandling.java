import java.io.*;
import java.util.*;

public class Solution {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
         Scanner scan = new Scanner(System.in);
        try{
          int n1 = scan.nextInt();
          int n2 = scan.nextInt();
          int res=n1/n2;
            System.out.print(res);
        }
          catch (ArithmeticException aex) {
        	System.out.println(aex);
        } catch (InputMismatchException imex) {
        	System.out.println(imex.getClass().getName());
        }
       // catch (ArithmeticException | InputMismatchException  e) {
       //   System.out.println(e.getClass().getName());
       // }
    }
}
