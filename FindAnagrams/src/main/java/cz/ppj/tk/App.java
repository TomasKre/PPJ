package cz.ppj.tk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class App {

    public static ArrayList<String> anagrams;

    public static ArrayList<String> FindAnagrams(String strIn) {
        anagrams = new ArrayList<>();
        char[] tmp = strIn.toCharArray();
        Arrays.sort(tmp);
        strIn = new String(tmp);
        FindAnagram(strIn,"");
        return anagrams;
    }

    private static void FindAnagram(String input, String prefix) {
        if (input.length() < 1) {
            anagrams.add(prefix + input);
        }
        Set<Character> setOfChars = new HashSet<>();
        for (int i = 0; i < input.length(); i++) {
            if(setOfChars.contains(input.charAt(i))) {
                continue;
            } else {
                setOfChars.add(input.charAt(i));
            }
            String temp;
            if (i < input.length() - 1) {
                temp = input.substring(0, i) + input.substring(i + 1);
            } else {
                temp = input.substring(0, i);
            }
            FindAnagram(temp, prefix + input.charAt(i));
        }
    }

    public static void PrintStringArrayList(ArrayList<String> list) {
        for (String str : list) {
            System.out.println(str);
        }
    }
}
