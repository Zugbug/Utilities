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

    /**
     * Capitalises the string.
     *
     * @param word or sentence to capitalise
     * @return the word parameter with first letter capital and rest lowercase
     */
    public static String capitalise(String word) {
        return (word.length() > 0) ? Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase() : "";
    }

    /**
     * Maps each character of src to be the full-width representation.
     *
     * @see https://en.wikipedia.org/wiki/Halfwidth_and_fullwidth_forms
     * @param src source string
     * @return string replaces with full-width characters
     */
    public static String fatString(String src) {
        return src.chars().boxed().map((Integer s) -> (char) s.intValue()).map((Character s) -> (32 < s && s < 126) ? fatChar(s) : s).map(Object::toString).reduce((String a, String b) -> a + b).orElse("");
    }

    /**
     * Maps the character to a full-width one.
     *
     * @param src source char
     * @return full-width'd source char
     */
    public static char fatChar(char src) {
        return (char) (src - 'a' + 65365 - 20);
    }

    /**
     * Capitalises each word in the given string, normalises so that the rest is
     * lowercase.
     *
     * @param src
     * @return
     */
    public static String capitaliseEachWord(String src) {
        return (src.length() == 0) ? "" : Arrays.asList(src.split(" ")).stream().filter((String s) -> (s != null && !"".equals(s))).map(StringUtils::capitalise).reduce((String a, String b) -> a + " " + b).orElse("");
    }

    public static String alignLines(String src, String align) {
        int numberOfSplits = 1 + (src.length() - src.replace(align, "").length()) / align.length();
        final String[][] splittedBits = new String[src.split("\n").length][numberOfSplits]; //lines x lengths
        for (int i = 0; i < src.split("\n").length; i++) {
            splittedBits[i] = src.split("\n")[i].split(align);
        }
        final int[] longests = new int[Stream.of(splittedBits).mapToInt(s -> s.length).max().getAsInt()];
        for (String[] splittedBit : splittedBits) {
            for (int j = 0; j < splittedBit.length; j++) {
                longests[j] = Math.max(longests[j], splittedBit[j].length());
            }
        }
        StringBuilder[] lines = Stream.generate(StringBuilder::new).limit(src.split("\n").length).toArray((i) -> new StringBuilder[i]);
        for (int i = 0; i < splittedBits.length; i++) {//every line
            for (int j = 0; j < splittedBits[i].length; j++) {//every bit
                lines[i].append(align).append(pad(splittedBits[i][j], longests[j] - splittedBits[i][j].length()));
            }
        }
        return Stream.of(lines).map(Object::toString).map(s -> s.substring(align.length())).reduce((a, b) -> a + "\n" + b).orElse("");
    }

    public static String pad(String src, int n, char side) {
        if (n == 0) {
            return src;
        }
        String pad = Stream.generate(() -> " ").limit(Math.abs(n)).reduce((a, b) -> a + b).orElse(" ");
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
        return Stream.of(q).reduce((a,b)->a+"\n"+b).orElse("");
    }
}
