/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.akinevz.utils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 *
 * @author zugbug
 */
public class Logic {

    /**
     * Simple Caesar crypt. Takes every character in a string and moves it's
     * codepoint by the given offset. Works for both uppercase and lowercase
     * ASCII English characters.
     *
     * @param source string
     * @param offset for each character
     * @return encrypted string
     */
    static public String caesar(String source, int offset) {
        Function<Integer, Character> alphabetStart = (s) -> (Character.isUpperCase(s) ? 'A' : 'a');
        return source.chars()
                .map(s -> alphabetStart.apply(s) + wrap(s - alphabetStart.apply(s), offset, 26))
                .mapToObj(s -> (char) s).map(Object::toString)
                .reduce((a, b) -> a + b).orElse("");
    }

    /**
     * Simple wrapping function. Offsets the source number, but keeps it within
     * the range by wrapping it back around.
     *
     * @param source number to start from
     * @param offset number to add to source
     * @param range number to constrain the output to
     * @return the number between 
     */
    static public int wrap(int source, int offset, int range) {
        return (range + source + offset) % range;

    }
    /**
     * Returns every Nth element of an array as a stream. 
     * @param <T> Type of the array
     * @param nth item in the array, starting with 0 offset
     * @param offset changes which is counted as the first item in the array
     * @param ts array containing elements
     * @return a stream of elements which satisfy this function
     */
    public static <T> Stream<T> everyNthElement(int nth, int offset, T... ts) {
        if (nth < 0) {
            throw new RuntimeException("NEGATIVE ITERATION NOT YET IMPLEMENTED");
        }
        return (nth < 1) ? Stream.of(ts) : Stream.iterate(offset, s -> nth + s).limit(ts.length / nth).map(s -> ts[s]);
    }
    /**
     * Returns every Nth element of an array as a stream. 
     * @param <T> Type of the array
     * @param nth item in the array, starting with 0 offset
     * @param ts array containing elements
     * @return a stream of elements which satisfy this function
     */
    public static <T> Stream<T> everyNthElement(int nth, T... ts) {
        return everyNthElement(nth, 0, ts);
    }
    
    static public String rot13(String entry) {
        return entry.chars().boxed().map(s -> (char) s.intValue())
                .map((Character t) -> {
                    if (!Character.isLetter(t)) {
                        return (int) t;
                    }
                    int offset;
                    if (Character.isUpperCase(t)) {
                        offset = 65;
                    } else {
                        offset = 97;
                    }
                    int start = ((int) t) - offset;
                    return (offset + ((start + 13) % 26));
                })
                .map(s -> (char) s.intValue() + "")
                .reduce((a, b) -> a  + b)
                .orElse("");
    }

    /**
     * Compares whether variable src lies between lower and upper;
     * ex: lower &lt src &lt upper
     * @param <T> type of the variables being compared. Must extend Comparable
     * @param src compared value
     * @param lower lower bound
     * @param upper upper bound
     * @return whether src lies between lower and upper
     */
    public static <T extends Comparable<T>> boolean between(T lower, T src, T upper) {
        return src.compareTo(lower) + src.compareTo(upper) == 0;
    }
    /**
     * Creates a 2d array with given dimensions
     * @param <T> type of the array's elements
     * @param clazz reference to the type of the elements
     * @param x dimension of the outermost array
     * @param y dimension of the innermost array
     * @return a 2d array, preinitialised with "null"
     */
    public static <T> T[][] make2DArray(Class<T> clazz, int x, int y) {
        return make2DArray(clazz, null, x,y);
    }

    public static <T> T[][] make2DArray(Class<T> clazz, T defaultVal, int x, int y) {
        T[][] ret = (T[][]) Array.newInstance(clazz, x,y);
        for (T[] ts : ret) {
            for (int i = 0; i < ts.length; i++) {
                ts[i] = defaultVal;
            }
        }
        return ret;
    }


    public static <T> boolean matchAny(Predicate<? super T> match, T... targets) {
        return findAny(match, targets).isPresent();
    }

    /**
     * Searches the iterable sequentially for the first instance where the
     * predicate returns true. Returns an optional of that value.
     *
     * @param <T> type of the value contained in the iterable
     * @param matcher predicate used to find the value
     * @param targets any iterable to be searched for the value
     * @return the wrapped value if present, otherwise empty optional
     */
    public static <T> Optional<T> findAny(Predicate<? super T> matcher, Iterable<T> targets) {
        for (T item : targets) {
            if (matcher.test(item)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    /**
     * Searches the iterable sequentially for the first instance where the
     * predicate returns true. Returns an optional of that value.
     *
     * @see Logic#findAny(java.util.function.Predicate, java.lang.Iterable)
     * @param <T> type of the value contained in the iterable
     * @param matcher predicate used to find the value
     * @param targets varargs array to be searched
     * @return the wrapped value if present, otherwise empty optional
     */
    public static <T> Optional<T> findAny(Predicate<? super T> matcher, T... targets) {
        return findAny(matcher, Arrays.asList(targets));
    }

    public static boolean matchEither(String tar, String... matches) {
        return matchAny(tar::equalsIgnoreCase, matches);
    }

    public static <T> boolean matchAll(Predicate<? super T> match, T... targets) {
        return Stream.of(targets).allMatch(match);
    }
}
