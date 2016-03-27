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
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
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
    public interface ThrowingSupplier<R> extends Supplier<R> {

        @Override
        default public R get() {
            try {
                return getThrows();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        R getThrows() throws Exception;

    }

    @FunctionalInterface
    public interface ThrowingConsumer<T> extends Consumer<T> {

        @Override
        default public void accept(T t) {
            try {
                acceptThrows(t);
            } catch (Exception e) {
                throw new RuntimeException("Could not consume " + t + ": " + e.getMessage());
            }
        }

        void acceptThrows(T val) throws Exception;

    }


    @FunctionalInterface
    public interface ThrowingBiFunction<X, Y, Z> extends BiFunction<X, Y, Z> {

        @Override
        default public Z apply(X t, Y u) {
            try {
                return applyThrows(t, u);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        Z applyThrows(X t, Y u) throws Exception;

    }

    @FunctionalInterface
    public interface ThrowingPredicate<T> extends Predicate<T> {

        @Override
        default public boolean test(T t) {
            try {
                return testThrows(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public boolean testThrows(T t) throws Exception;

    }

    /**
     * @see Function
     * @param <T>
     * @param <R>
     */
    @FunctionalInterface
    public interface ThrowingFunction<T, R> extends Function<T, R> {

        @Override
        default R apply(final T elem) {
            try {
                return applyThrows(elem);
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }

        R applyThrows(T elem) throws Exception;
    }

    public static String read(File file) {
        return StreamSupport.stream(((Iterable<String>) ((ThrowingSupplier<Scanner>) () -> new Scanner(file).useDelimiter("\n"))::get)
                .spliterator(), false)
                .reduce((a, b) -> a + "\n" + b).orElse("");
    }

    public static void append(File tar, String msg) throws FileNotFoundException {
        writeStream(new FileOutputStream(tar, true), msg);
    }

    public static void write(File tar, String msg) throws FileNotFoundException {
        writeStream(new FileOutputStream(tar, false), msg);
    }

    public static void writeStream(OutputStream out, String msg) {
        msg.chars().boxed().map(s -> (char) s.intValue()).forEach((ThrowingConsumer<Character>) out::write);
    }

}
