/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 *
 * @author zugbug
 */
public class StringUtils {

    public static String appendTo2DString(String src, String c) {
        return appendToSplitted(src, c, "\n");
    }
    public static String appendToSplitted(String src, String c,String split) {
        return Stream.of(src.split(split)).map(s -> s.concat(c)).reduce((a, b) -> a + split + b).orElse("");
    }

    public static String capitalise(String word) {
        return (word.length() > 0) ? Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase() : "";
    }

    public static String fatString(String src) {
        return src.chars().boxed().map((Integer s) -> (char) s.intValue()).map((Character s) -> (32 < s && s < 126) ? fatChar(s) : s).map(Object::toString).reduce((String a, String b) -> a + b).orElse("");
    }

    public static char fatChar(char src) {
        return (char) (src - 'a' + 65365 - 20);
    }

    public static String capitaliseEachWord(String src) {
        return (src.length() == 0) ? "" : Arrays.asList(src.split(" ")).stream().filter((String s) -> (s != null && !"".equals(s))).map(StringUtils::capitalise).reduce((String a, String b) -> a + " " + b).orElse("");
    }

    public static float stringMatch(String toString, String toString0) {
        return stringMatch(toString, toString0, false);
    }

    public static String flattenToAscii(String string) {
        StringBuilder sb = new StringBuilder(string.length());
        for (char c : string.toCharArray()) {
            if (c <= '\u007F') {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static float stringMatch(String ths, String tht, boolean lastMatchFail) {
        ths = flattenToAscii(ths);
        tht = flattenToAscii(tht);
        if (lastMatchFail) {
            if (ths.endsWith(((Character) tht.charAt(tht.length() - 1)).toString())) {
                return 1;
            }
        } else if (ths.startsWith(((Character) tht.charAt(0)).toString())) {
            return 1;
        }
        List<Tuple<Character, Character>> lotoc = Tuple.listFromStreamTuples(Tuple.of(ths, tht)
            .map((String s) -> s.chars().mapToObj(elem -> (char) elem)));
        return (float) Tuple.samePairsPercentage(lotoc);

    }
}
