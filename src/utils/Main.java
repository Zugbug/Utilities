/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

/**
 *
 * @author zugbug
 */
public class Main {

    public static void main(String[] args) {
//        System.err.println("1<4<5");
//        System.err.println(1<4&&4<5);
//        System.err.println(Logic.between('a','b','x'));
//        System.err.println(EasyFile.getAll("h"));
//
//        Tuple<String, String> t = Tuple.of("hey", "mate");
//        System.err.println(t);
//        List<Tuple<Character, Character>> lotoc = Tuple.listFromStreamTuples(t.map((String s) -> s.chars().mapToObj(elem -> (char) elem)));
//        System.err.println(lotoc);
//        System.err.println(Tuple.samePairsPercentage(lotoc));
//
//        Integer[] one1 = {1, 2, 3, 4};
//        Integer[] two1 = {5, 2, 7, 4};
//
//        Tuple<List<Integer>, List<Integer>> t1 = Tuple.of(one1, two1);
//
//        System.err.println(t1);
//        
//
//        List<Tuple<Integer, Integer>> intTuples = Tuple.listFromStreamTuples(t1.map((Collection c) -> c.stream()));
//        System.err.println(intTuples);
//        System.err.println(Tuple.samePairsPercentage(intTuples));
//
//        Tuple<?, ?> mono = Tuple.of("Hey", 123);
//        System.err.println(mono
//                .operate((Object x, Number y) -> {
//            return x +":"+ y.doubleValue();
//        }));
//        System.err.println(MAKEBIG(new Scanner(System.in).nextLine())
//                .chars().boxed().map(s -> "" + (char) s.intValue())
//                .reduce((a, b) -> a + "\n" + b).orElse(""));
        for (Integer integer : utils.Logic.range(1, 2, -1)) {
            System.err.println(integer);
        }

    }

    private static int recadd(int[] things, int length) {
        return (length > 0) ? things[--length] + recadd(things, length) : 0;
    }

}
