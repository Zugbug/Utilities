/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 *
 * @author zugbug
 */
public class Logic {

    static public String caesar(String orig, int offset) {
        Function<Integer, Character> begin = (s) -> (Character.isUpperCase(s) ? 'A' : 'a');
        return orig.chars().map(s -> begin.apply(s) + wrap(s - begin.apply(s), 26, offset)).mapToObj(s -> (char) s).map(Object::toString).reduce((a, b) -> a + b).orElse("");
    }

    static public int wrap(int src, int range, int offset) {
        return (range + src + offset) % range;

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
                .reduce((a, b) -> a + "" + b)
                .orElse("");
    }

    /**
     * compares as lower < src < upper;
     *
     * @param <T>
     * @param src
     * @param lower
     * @param upper
     * @return
     */
    public static <T extends Comparable<T>> boolean between(T lower, T src, T upper) {
        return src.compareTo(lower) + src.compareTo(upper) == 0;
    }

    public static <T> T[][] make2DArray(Class<T> clazz, int... dim) {
        return make2DArray(clazz, null, dim);
    }

    public static <T> T[][] make2DArray(Class<T> clazz, T defaultVal, int... dim) {
        T[][] ret = (T[][]) Array.newInstance(clazz, dim);
        for (T[] ts : ret) {
            for (int i = 0; i < ts.length; i++) {
                ts[i] = defaultVal;
            }
        }
        return ret;
    }

    public static Iterable<Integer> range(int s, int e, int o) {
        if (e - s >= 0 && o < 0 || s - e >= 0 && o > 0) {
            return () -> new Iterator<Integer>() {
                @Override
                public boolean hasNext() {
                    return false;
                }

                @Override
                public Integer next() {
                    return 0;
                }
            };
        }
        return (o > 0) ? () -> new Iterator<Integer>() {
            int index;

            {
                index = s - o;
            }

            @Override
            public boolean hasNext() {
                return (index + o) < e;
            }

            @Override
            public Integer next() {
                return index = index + o;
            }
        } : (o < 0) ? () -> new Iterator<Integer>() {
            int index;

            {
                index = s - o;
            }

            @Override
            public boolean hasNext() {
                return (index - o) > e;
            }

            @Override
            public Integer next() {
                return index = index - o;
            }
        } : new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                return new Iterator<Integer>() {
                    @Override
                    public boolean hasNext() {
                        return false;
                    }

                    @Override
                    public Integer next() {
                        return 0;
                    }
                };
            }
        };
    }

    public static Iterable<Integer> range(int s, int e) {
        return range(s, e, (s > e) ? 1 : -1);
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
