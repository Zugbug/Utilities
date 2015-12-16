/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.Iterator;

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

    private static Iterable<Integer> range(int s, int e, int o) {
        return () -> new Iterator<Integer>() {
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
        };
    }

    public static Iterable<Integer> range(int s, int e) {
        return range(s, e, 1);
    }
}
