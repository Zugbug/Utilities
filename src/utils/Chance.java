/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author zugbug
 */
public class Chance {

    public static boolean percent(int i) {
        return (Math.random() * 100) < i;
    }

    public static <E> Optional<E> randomElement(List<E> c) {
        return (c.isEmpty())
                ? Optional.empty()
                : Optional.of(c.get((int) (Math.random() * c.size())));
    }

    public static float range(float low, float high) {
        return (float) (low + Math.random() * (high - low));
    }

    public static float range(Tuple<Float, Float> s) {
        return range(s.left(), s.right());
    }

    


}
