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

    public static String alignLines(String src, String align) {
        String[] lines = src.split("\n");
        int splits = 1 + (src.length() - src.replace(align, "").length()) / align.length();
        final String[][] splittedBits = new String[lines.length][splits]; //lines x lengths
        for (int i = 0; i < lines.length; i++) {
            splittedBits[i] = lines[i].split(align);
        }
        final int[] longests = new int[Stream.of(splittedBits).mapToInt(s -> s.length).max().getAsInt()];
        for (String[] splittedBit : splittedBits) {
            for (int j = 0; j < splittedBit.length; j++) {
                longests[j] = Math.max(longests[j], splittedBit[j].length());
            }
        }
        StringBuilder[] line = Stream.generate(StringBuilder::new).limit(lines.length).toArray((int i) -> new StringBuilder[i]);
        for (int i = 0; i < splittedBits.length; i++) {//every line
            for (int j = 0; j < splittedBits[i].length; j++) {//every bit

                line[i].append(align).append(pad(splittedBits[i][j], longests[j] - splittedBits[i][j].length()));
            }
        }
        return Stream.of(line).map(Object::toString).map(s -> s.substring(align.length())).reduce((a, b) -> a + "\n" + b).orElse("");
    }

    public static String pad(String src, int n, char side) {
        if (n == 0) {
            return src;
        }
        String pad = Stream.generate(() -> " ").limit(n).reduce((a, b) -> a + b).orElse(" ");
        switch (Character.toLowerCase(side)) {
            case 'c':
                src = pad.substring(0, n / 2) + src + pad.substring(n / 2);
                break;
            case 'r':
                src = pad + src;
                break;
            case 'l':
            default:
                src = src + pad;
                break;
        }
        return src;
    }

    private static String pad(String string, int i) {
        return pad(string, i, 'l');
    }

    public static String flatten(String[] q) {
        StringBuilder sb = new StringBuilder();
        for (String string : q) {
            sb.append(string);
        }
        return sb.toString();
    }
}
