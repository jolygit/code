/*
Dothraki are planning an attack to usurp King Robert's throne. King Robert learns of this conspiracy from Raven and plans to lock the single door through which the enemy can enter his kingdom.

door

But, to lock the door he needs a key that is an anagram of a certain palindrome string.

The king has a string composed of lowercase English letters. Help him figure out whether any anagram of the string can be a palindrome or not.

Input Format

A single line which contains the input string.

Constraints

 length of string 
Each character of the string is a lowercase English letter.
Output Format

A single line which contains YES or NO in uppercase.

Sample Input 0

aaabbbb
Sample Output 0

YES
Explanation 0

A palindrome permutation of the given string is bbaaabb. 

Sample Input 1

cdefghmnopqrstuvw
Sample Output 1
 */
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    static String gameOfThrones(String s1){
        // Complete this function
         HashMap<Character,Integer> h1=new HashMap<Character,Integer>();
        for(int i=0;i<s1.length();i++){
            Character c=s1.charAt(i);
            if(h1.containsKey(c)){
                Integer val=h1.get(c);
                h1.put(c,val+1);
            }
            else{
               h1.put(c,1); 
            }
        }
        Iterator it1 = h1.entrySet().iterator();
        int cntOdd=0;
        while (it1.hasNext()) {
         Map.Entry pair1 = (Map.Entry)it1.next();
         Character c1=(Character)pair1.getKey() ;
         Integer   v1=(Integer)pair1.getValue();
            if(v1%2==1){
                cntOdd++;
            }
          }
        if(cntOdd==0 || (cntOdd==1 && s1.length()%2==1)){
            return "YES";
        }
        else{
            return "NO";
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String s = in.next();
        String result = gameOfThrones(s);
        System.out.println(result);
    }
}
