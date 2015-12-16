/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    

    public static boolean eitherMatch(String tar, String... matches) {
        return anyMatch(tar::equalsIgnoreCase, matches);
    }

    public static <T> boolean allMatch(Predicate<? super T> match, T... targets) {
        return Arrays.asList(targets).stream().allMatch(match);
    }

    public static <T> boolean anyMatch(Predicate<? super T> match, T... targets) {
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

}
