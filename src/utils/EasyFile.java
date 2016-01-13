/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Optional;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 *
 * @author zugbug
 */
public class EasyFile {
    
    interface OutputLambdaStream {

        void write(int b);

        default OutputStream get() {
            return new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    OutputLambdaStream.this.write(b);
                }
            };
        }
    }


    @FunctionalInterface
    interface ExceptionReturner<T> {

        T operate() throws Exception;

        default Optional<T> tryReturn() {
            try {
                return Optional.of(this.operate());
            } catch (Exception e) {
                return Optional.empty();
            }
        }
    }

    @FunctionalInterface
    interface ExceptionConsumer<T> {

        void operate(T val) throws Exception;

        default void tryConsume(T val) {
            try {
                operate(val);
            } catch (Exception e) {
                System.err.println("could not consume " + val + ": " + e.getMessage());
            }
        }

    }

    public static String read(File file) {
        return StreamSupport.stream(((Iterable<String>) ()
                -> ((ExceptionReturner<Scanner>) () -> new Scanner(file).useDelimiter("\n"))
                .tryReturn().orElse(new Scanner(""))).spliterator(), false)
                .reduce((a, b) -> a + "\n" + b).orElse("");
    }

    public static void write(File tar, String msg) throws FileNotFoundException {
        writeStream(new FileOutputStream(tar), msg);
    }

    public static void append(File tar, String msg) throws FileNotFoundException {
        writeStream(new FileOutputStream(tar, true), msg);
    }

    public static void prepend(File tar, String msg) throws FileNotFoundException {
        writeStream(new FileOutputStream(tar, false), msg);
    }

    public static void writeStream(OutputStream out, String msg) throws FileNotFoundException {
        msg.chars().boxed().map(s -> (char) s.intValue()).forEach(((ExceptionConsumer<Character>) out::write)::tryConsume);
    }
    
}
