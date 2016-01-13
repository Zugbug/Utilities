/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Predicate;

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
    static <T extends Comparable<T>> boolean between(T lower, T src, T upper) {

        return src.compareTo(lower) + src.compareTo(upper) == 0;
    }

    public static Iterable<Integer> range(int s, int e, int o) {

        return (o == 0) ? new Iterable<Integer>() {
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
        }
                : (o > 0) ? () -> new Iterator<Integer>() {
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
                } : () -> new Iterator<Integer>() {
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
                };
    }

    public static Iterable<Integer> range(int s, int e) {
        return range(s, e, 1);
    }

    public static <T> boolean matchAny(Predicate<? super T> match, T... targets) {
        if (targets.length == 0) {
            return false;
        }
        for (T par : targets) {
            if (match.test(par)) {
                return true;
            }
        }
        return false;
    }

    public static boolean eitherMatch(String tar, String... matches) {
        return matchAny(tar::equalsIgnoreCase, matches);
    }

    public static <T> boolean matchAll(Predicate<? super T> match, T... targets) {
        return Arrays.asList(targets).stream().allMatch(match);
    }
}
