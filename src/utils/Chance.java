/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.Collection;
import java.util.Optional;

/**
 * A collection of static methods about probabilities.
 * @author zugbug
 */
public class Chance {
    /**
     * Quick and dirty method for a percentage based probability.
     * @param i percentage of times an event should occur
     * @return boolean that should return i% of the time
     */
    public static boolean percent(int i) {
        return (Math.random() * 100) < i;
    }
    /**
     * Returns a random element of the given collection, wrapped in an optional.
     * @param <T> type of elements in this collection
     * @param c the collection from which to pick an element
     * @return an optional of the element from the collection
     */
    public static <T> Optional<T> randomElement(Collection<T> c) {
        return (c.isEmpty())
                ? Optional.empty()
                : Optional.of((T) c.toArray()[((int) (Math.random() * c.size()))]);
    }
    /**
     * Quick and dirty method for returning a random float within a range.
     * @param low lower bound, inclusive
     * @param high upper bound, exclusive
     * @return the floating point number in the rage.
     */
    public static float range(float low, float high) {
        return (float) (low + Math.random() * (high - low));
    }
    /**
     * Quick and dirty method for returning a random double within a range.
     * @param low lower bound, inclusive
     * @param high upper bound, exclusive
     * @return the double point number in the rage.
     */
    public static double range(double low, double high) {
        return (low + Math.random() * (high - low));
    }
}
