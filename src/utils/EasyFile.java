/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
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
    public interface ExceptionReturner<T> {

        T operate() throws Exception;

        default T tryReturn() {

            try {
                return this.operate();
            } catch (Exception e) {
                throw new RuntimeException("Could not operate: " + e.getMessage());
            }
        }
    }

    @FunctionalInterface
    public interface ExceptionConsumer<T> {

        void operate(T val) throws Exception;

        default void tryConsume(T val) {
            try {
                operate(val);
            } catch (Exception e) {
                throw new RuntimeException("Could not consume " + val + ": " + e.getMessage());
            }
        }

    }

    @FunctionalInterface
    public interface ThrowingSupplier<T> extends Supplier<T> {

        @Override
        default T get() {
            try {
                return getThrows();
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }

        T getThrows() throws Exception;
    }

    @FunctionalInterface
    public interface ThrowingFunction<T, Y> extends Function<T, Y> {

        @Override
        default Y apply(final T elem) {
            try {
                return applyThrows(elem);
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }

        Y applyThrows(T elem) throws Exception;
    }

    public static String read(File file) {
        return StreamSupport.stream(((Iterable<String>) ()
            -> ((ExceptionReturner<Scanner>) () -> new Scanner(file).useDelimiter("\n"))
            .tryReturn()).spliterator(), false)
            .reduce((a, b) -> a + "\n" + b).orElse("");
    }

    public static void append(File tar, String msg) throws FileNotFoundException {
        writeStream(new FileOutputStream(tar, true), msg);
    }

    public static void write(File tar, String msg) throws FileNotFoundException {
        writeStream(new FileOutputStream(tar, false), msg);
    }

    public static void writeStream(OutputStream out, String msg) {
        msg.chars().boxed().map(s -> (char) s.intValue()).forEach(((ExceptionConsumer<Character>) out::write)::tryConsume);
    }

}
