/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.akinevz.utils;

/**
 *
 * @author zugbug
 */
public class Throwing {
    /**
     * Function that returns false if the supplier throws an exception.
     * @param s function to test
     * @return whether the function doesn't throw an exception
     */
    public static boolean passes(Throwing.Supplier s){
        try {
            s.get();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @see Function
     * @param <T>
     * @param <R>
     */
    @FunctionalInterface
    public interface Function<T, R> extends java.util.function.Function<T, R> {

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

    @FunctionalInterface
    public interface Predicate<T> extends java.util.function.Predicate<T> {

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

    @FunctionalInterface
    public interface BiFunction<X, Y, Z> extends java.util.function.BiFunction<X, Y, Z> {

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
    public interface Supplier<R> extends java.util.function.Supplier<R> {

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
    public interface Consumer<T> extends java.util.function.Consumer<T> {

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
}
