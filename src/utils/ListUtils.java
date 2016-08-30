/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

/**
 *
 * @author zugbug
 */
public class ListUtils {
    /**
     * 
     * @param <T>
     * @param src
     * @param pos
     * @return
     * @throws Exception 
     */
    public static <T> List<T> swap(List<T> src, int pos) throws Exception {
        if (src.size() - Math.abs(pos) - 1 < 0) {
            throw new Exception("Index out of bounds - " + pos);
        }
        T temp = src.get(pos);
        src.set(pos, src.get(src.size() - pos - 1));
        src.set(src.size() - pos - 1, temp);
        return src;
    }

    public static <T> List<T> reverse(List<T> src) {
        int iters = src.size() / 2;
        for (int i = 0; i < iters; i++) {
            try {
                src = swap(src, i);
            } catch (Exception ex) {
            }
        }
        return src;
    }

    public static <T> List<T> shuffle(List<T> src, int maxDepth) {
        return shuffle(src, maxDepth, null);
    }

    public static <T> List<T> shuffle(List<T> src, int maxDepth, Random r) {
        while (maxDepth > 0) {
            if (src.size() == 1) {
                return src;
            }
            src = new ArrayList<>(src);
            Supplier<Double> next = (r == null) ? () -> Math.random() : () -> r.nextDouble();
            int a;
            int b;
            do {
                a = (int) (src.size() * next.get());
                b = (int) (src.size() * next.get());
                if (b < a) {
                    int t = b;
                    b = a;
                    a = t;
                }
            } while (a == b);
            List<T> sublist = new ArrayList<>(src.subList(a, b));
            src.removeAll(sublist);
            maxDepth = (int) (next.get() * (maxDepth));
            if (maxDepth > 0) {
                sublist = shuffle(sublist, maxDepth, r);
            }
            if (next.get() < 0.5) {
                sublist = reverse(sublist);
            }
            if (next.get() < 0.5) {
                src = reverse(src);
            }
            int replace = (int) (3 * next.get());
            if (sublist.size() == 1) {
                replace = 2;
            }
            if (src.size() == 1) {
                replace = 1;
            }
            int position;
            switch (replace) {
                case 0:
                    position = 0;
                    break;
                case 1:
                    position = a;
                    break;
                case 2:
                    position = src.size();
                    break;
                default:
                    System.err.println(replace + " is replace");
                    position = 0;
            }
            for (T t : sublist) {
                src.add(position, t);
            }
        }
        return src;
    }
    
}
