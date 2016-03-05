/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 *
 * @author zugbug
 */
public class Logic {

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
        return range(s, e, 1);
    }

    public static <T> boolean matchAny(Predicate<? super T> match, T... targets) {
        return findAny(match, targets).isPresent();
    }

    public static <T> Optional<T> findAny(Predicate<? super T> match, T... targets) {
        for (T item : targets) {
            if (match.test(item)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    public static boolean matchEither(String tar, String... matches) {
        return matchAny(tar::equalsIgnoreCase, matches);
    }

    public static <T> boolean matchAll(Predicate<? super T> match, T... targets) {
        return Stream.of(targets).allMatch(match);
    }
}
