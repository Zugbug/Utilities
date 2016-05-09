/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 *
 * @author zugbug
 */
public class StringUtils {

    public static void main(String[] args) throws IOException {
        String render = Stream.of("THAT'S A BIG FONT".split("\n"))
            .flatMap(Stream::of).map(s -> renderBigLetters(s, 25, 2)).reduce((a, b) -> a + "\n\n\n\n" + b).orElse("");

        System.out.println(render);
        System.out.println("");
        System.out.println("");
        System.out.println("");
        render = Stream.of("...FOR YOU".split("\n"))
            .flatMap(Stream::of).map(s -> renderBigLetters(s, 20, 1)).reduce((a, b) -> a + "\n\n\n\n" + b).orElse("");
        System.out.println(StringUtils.preppendTo2DString(render, "\t\t\t\t\t\t"));

    }

    public static String renderBigLetters(String src, int scale, int ramp) {
        Font f = new Font("Comic Sans MS", Font.PLAIN, 256);
        char[] colourRamp;
        switch (ramp) {
            case 0:
                colourRamp = " .xX".toCharArray();
                break;
            case 1:
                colourRamp = " ░▒▓█".toCharArray();
                break;
            case 2:
                colourRamp = " .:-=+*#%@".toCharArray();
                break;
            case 3:
                colourRamp = new StringBuilder("$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\\|()1{}[]?-_+~<>i!lI;:,\"^`'. ").reverse().toString().toCharArray();
                break;
            default:
                colourRamp = " x".toCharArray();
        }
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = img.createGraphics();
        Rectangle2D w = g.getFontMetrics(f).getStringBounds(src, g);
        img = new BufferedImage((int) w.getWidth(), (int) w.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        g = img.createGraphics();
        g.setColor(Color.WHITE);
        g.setFont(f);
        g.drawString(src, 0, (int) w.getHeight());
//        ImageIO.write(img, "png", new File("image.png"));
        Image scaled = img.getScaledInstance(-1, scale, Image.SCALE_SMOOTH);
        scaled.flush();
        int scaled_X = scaled.getWidth(null);
        int scaled_Y = scaled.getHeight(null);
        BufferedImage small = new BufferedImage(scaled_X, scaled_Y, BufferedImage.TYPE_BYTE_GRAY);
        small.getGraphics().drawImage(scaled, 0, 0, null);
        small.flush();
//        ImageIO.write(small, "png", new File("scaled.png"));
        Raster data = small.getData();
        DataBuffer buffer = data.getDataBuffer();
        StringBuilder sb = new StringBuilder();
        int[][] matrix = new int[scaled_Y][scaled_X];
        for (int j = 0; j < buffer.getSize(); j++) {
            int nu = buffer.getElem(j);
            matrix[j / scaled_X][j % scaled_X] = nu;
        }
        int range = Stream.of(matrix).flatMapToInt(IntStream::of).max().getAsInt() - Stream.of(matrix).flatMapToInt(IntStream::of).min().getAsInt();
        float rangeSteps = (float) range / (colourRamp.length - 1);
        for (int[] matrix1 : matrix) {
            for (int y = 0; y < matrix1.length; y++) {
                sb.append(colourRamp[(int) Math.round(matrix1[y] / rangeSteps)]);
            }
            sb.append("\n");
        }
        return Stream.of(sb.toString().split("\n")).filter(s -> !s.replace(" ", "").isEmpty()).reduce((a, b) -> a + "\n" + b).orElse("");

    }

    public static String appendTo2DString(String src, String c) {
        return appendToSplitted(src, c, "\n");
    }

    public static String appendToSplitted(String src, String c, String split) {
        return Stream.of(src.split(split)).map(s -> s.concat(c)).reduce((a, b) -> a + split + b).orElse("");
    }

    public static String preppendToSplitted(String src, String c, String split) {
        return Stream.of(src.split(split)).map(s -> c.concat(s)).reduce((a, b) -> a + split + b).orElse("");
    }

    public static String preppendTo2DString(String src, String c) {
        return preppendToSplitted(src, c, "\n");
    }

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
            splittedBits[i] = src.split("\n")[i].split(Pattern.quote(align));
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
        return Stream.of(q).reduce((a, b) -> a + "\n" + b).orElse("");
    }
}
