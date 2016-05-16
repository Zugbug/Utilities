/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.imageio.ImageIO;

/**
 *
 * @author zugbug
 */
public class StringUtils {

    private static String spaces(int n) {
        return Stream.generate(() -> " ").limit(Math.abs(n)).reduce((a, b) -> a + b).orElse(" ");
    }

    Font f = new Font("monospaced", Font.PLAIN, 256);

    Character[][] ramps = new Character[][]{
        new StringBuilder("$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\\|()1{}[]?-_+~<>i!lI;:,\"^`'. ").toString().chars().mapToObj(s -> (char) s).toArray(Character[]::new),
        new StringBuilder(" .:xX").reverse().toString().chars().mapToObj(s -> (char) s).toArray(Character[]::new),
        new StringBuilder(" .:░▒▓█").reverse().toString().chars().mapToObj(s -> (char) s).toArray(Character[]::new),
        new StringBuilder(" .:-=+*#%@").reverse().toString().chars().mapToObj(s -> (char) s).toArray(Character[]::new)

    };

    public static void main(String[] args) throws IOException {
//        String ret = blockFormat(20, "Hello world this is a long letter".split(" "));
//        System.out.println(ret);
        System.err.println(floatApart("1 2 3", 6));
    }

    public static String floatApart(String src, int maxWidth) {
        int extra;
        String[] bits = src.split(" ");
        if ((extra = maxWidth - flatten(bits).length()) <= 0) {
            return src;
        }
        System.err.println(extra);
        int amt = extra / (bits.length - 1);
        System.err.println(amt);
        return reduce(bits, new BinaryOperator<String>() {
            int tot = extra;

            @Override
            public String apply(String t, String u) {
                return t + spaces(((tot -= amt) >= amt) ? amt : tot) + u;
            }
        });
    }

    public static String reduce(String[] arr, BinaryOperator<String> fun) {
        String last = arr[0];
        for (int i = 1; i < arr.length; i++) {
            String next = arr[i];
            last = fun.apply(last, next);
        }
        return last;
    }

    public static String join(String j, String a, String b) {
        return (!a.isEmpty())
                ? (!b.isEmpty())
                        ? a + j + b
                        : a
                : (!b.isEmpty())
                        ? b
                        : "";
    }

    public static String blockFormat(int maxCharWidth, String... src) {
        StringBuilder sb = new StringBuilder();

        return sb.toString();
    }

    public static double analyseCharDensity(char c, Font f) {
        Canvas canvas = new Canvas();
        FontMetrics size = canvas.getFontMetrics(f);
        if (size.charWidth(c) == 0 || size.getAscent() == 0) {
            return 0;
        }
        BufferedImage bi = new BufferedImage(size.charWidth('W'), size.getAscent(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = bi.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, bi.getWidth(), bi.getHeight());
        g.setColor(Color.BLACK);
        g.setFont(f);
        g.drawString("" + c, 0, size.getAscent());
        bi.flush();
        DataBuffer data = bi.getData().getDataBuffer();
        return Stream.iterate(0, s -> s + 1).limit(data.getSize()).mapToInt(data::getElem).average().getAsDouble();
    }

    public static String renderBigLetters(String src, int scale, Character... ramp) {
        Font f = new Font("Comic Sans MS", Font.PLAIN, 256);
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = img.createGraphics();
        Rectangle2D w = g.getFontMetrics(f).getStringBounds(src, g);
        img = new BufferedImage((int) w.getWidth(), (int) (w.getHeight() - w.getY()), BufferedImage.TYPE_BYTE_GRAY);
        g = img.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        g.setColor(Color.BLACK);
        g.setFont(f);
        g.drawString(src, 0, (int) (w.getHeight()));
        return imageToText(scale, img, ramp);
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
        String pad = StringUtils.spaces(n);
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
        return Stream.of(q).collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
    }

    private static String imageToText(int scale, Image img, Character... ramps) {
        Image scaled = img.getScaledInstance(scale, -1, Image.SCALE_SMOOTH);
        scaled = scaled.getScaledInstance(scale, (int) (0.53 * scale), Image.SCALE_SMOOTH);
        scaled.flush();
        int sizeX = scaled.getWidth(null);
        int sizeY = (int) (scaled.getHeight(null));
        BufferedImage small = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_BYTE_GRAY);
        small.getGraphics().drawImage(scaled, 0, 0, null);
        small.flush();
        Raster data = small.getData();
        DataBuffer buffer = data.getDataBuffer();
        StringBuilder sb = new StringBuilder();
        int[][] matrix = new int[sizeY][sizeX];
        for (int j = 0; j < buffer.getSize(); j++) {
            int nu = buffer.getElem(j);
            matrix[j / sizeX][j % sizeX] = nu;
        }
        int max = Stream.of(matrix).flatMapToInt(IntStream::of).max().getAsInt();
        int min = Stream.of(matrix).flatMapToInt(IntStream::of).min().getAsInt();
        int range = 1 + max
                - min;
        for (int[] matrix1 : matrix) {
            for (int y = 0; y < matrix1.length; y++) {
                sb.append(
                        ramps[(int) ((matrix1[y] - min) * ((float) (ramps.length) / (range)))]
                );

            }
            sb.append("\n");
        }
        try {
            ImageIO.write(small, "png", new File("/home/zugbug/out.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }

    private static <T> List<T> posterise(T[] ramp, int max) {
        List<T> t = Arrays.asList(ramp);
        if (ramp.length <= max) {
            return t;
        }
        double step = (double) ramp.length / max;
        List<T> ret = new ArrayList<>();
        for (double i = 0; i < max * step; i += step) {
            System.err.println(t.get((int) Math.round(i)) + ":" + i);
            ret.add(t.get((int) Math.round(i)));
        }
        return ret;
    }
}
