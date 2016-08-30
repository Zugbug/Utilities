/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import utils.Throwing.Consumer;
import utils.Throwing.Supplier;

/**
 *
 * @author zugbug
 */
public class EasyFile {

  
    public static String read(File file) throws FileNotFoundException {
        return stream(new Scanner(file).useDelimiter("\n"))
                .reduce((a, b) -> a + "\n" + b).orElse("");
    }

    public static Stream<String> stream(Scanner scanner) {
        return StreamSupport.stream(((Iterable<String>) ((Supplier<Scanner>) () -> scanner)::get)
                .spliterator(), false);
    }

    public static void append(File tar, String msg) throws FileNotFoundException {
        printStream(new FileOutputStream(tar, true), msg);
    }

    public static void write(File tar, String msg) throws FileNotFoundException {
        printStream(new FileOutputStream(tar, false), msg);
    }

    public static void printStream(OutputStream out, Object msg) {
        printStream(out, msg.toString());
    }

    public static void printlnStream(OutputStream out, Object msg) {
        printStream(out, msg.toString() + "\n");
    }

    public static void printlnStream(OutputStream out, String msg) {
        printStream(out, msg + "\n");
    }

    public static void printStream(OutputStream out, String msg) {
        msg.chars().boxed().map(s -> (char) s.intValue()).forEach((Consumer<Character>) out::write);
    }

}
