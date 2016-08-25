/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.json.simple.JSONObject;

/**
 *
 * @author zugbug
 * @param <L> Left element type
 * @param <R> Right element type
 */
public class Tuple<L, R> {

    /**
     * Makes a JSONObject with key a and value b.
     *
     * @param <K> type for key
     * @param <V> type for value
     * @param key the key
     * @param value the value
     * @return
     */
    public static <K, V> JSONObject toJSON(K key, V value) {
        return Tuple.toJSON(Tuple.of(key, value));
    }

    /**
     * Creates a complex JSONObject from multiple Tuples, with each Tuple's left
     * being the key and right being the value.
     *
     * @param tuples the pairs of key-values from which this JSONObject is
     * created
     * @return the JSONObject
     */
    public static JSONObject toJSON(Tuple... tuples) {
        JSONObject ret = new JSONObject();
        for (Tuple t : tuples) {
            t.consume(ret::put);
        }
        return ret;
    }

    /**
     * Splits the string into an array with length of 2, and assigns the first
     * element to be the key and second to be the value.
     *
     * @param src string to split
     * @param split string which separates the first element from the second
     * @return
     */
    public static Tuple<String, String> split(String src, String split) {
        return Tuple.of(src.split(split, 2));
    }
    /**
     * Creates a tuple from two elements.
     * @param <L> type for the left element
     * @param <R> type for the right element
     * @param left element
     * @param right element
     * @return 
     */
    public static <L, R> Tuple<L, R> of(L left, R right) {
        return new Tuple<>(left, right);
    }
    /**
     * Creates a tuple, with both elements of same type, from an array. The array MUST be 2 elements long.
     * @exception RuntimeException is thrown if the length of the array is not equal to two
     * @param <T> type for the elements of the array and both sides of the tuple
     * @param array from which the elements are created
     * @return the tuple object representing the array
     */
    public static <T> Tuple<T, T> of(T... array) {
        if (array.length != 2) {
            throw new RuntimeException("Array must contain exactly 2 elements, had: " + Arrays.deepToString(array));
        }
        return Tuple.of(array[0], array[1]);
    }
   

    public static <X, Y> List<Tuple<X, Y>> listFrom(Tuple<? extends Collection<X>, ? extends Collection<Y>> t) {
//        return listFromStreamTuples(t.mapLeft(Collection::stream).mapRight(Collection::stream));
        return t.map((BiFunction<Collection<X>, Collection<Y>, List<Tuple<X, Y>>>) (Collection<X> t1, Collection<Y> u) -> {
            List<X> lefts = new ArrayList<>(t1);
            List<Y> rights = new ArrayList<>(u);
            int diff;
            Collection smaller = ((diff = rights.size() - lefts.size()) < 0) ? rights : lefts;
            smaller.addAll(Stream.generate(() -> null)
                    .limit(Math.abs(diff))
                    .collect(Collectors.toList()));
            List<Tuple<X, Y>> ret = new ArrayList();
            for (int i = 0; i < lefts.size(); i++) {
                ret.add(new Tuple(lefts.get(i), rights.get(i)));
            }
            return ret;
        });
    }

    public List<Tuple<L, R>> asList() {
        return Tuple.listFrom((Tuple<? extends Collection<L>, ? extends Collection<R>>) this);
    }

    public static <X> double samePairsPercentage(List<Tuple<X, X>> tar) {
        return samePairsPercentage(tar.stream());
    }

    public static <X> double samePairsPercentage(Stream<Tuple<X, X>> tar) {
        return tar.map(tuple -> tuple.map((X left, X right) -> (left.equals(right)) ? 1 : 0))
                .collect(Collectors.averagingDouble(s -> s));
    }
    private final L left;
    private final R right;

    private Tuple(L l, R r) {
        this.right = r;
        this.left = l;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tuple) {
            Tuple conv = (Tuple) obj;
            if ((conv.left.equals(this.left) || conv.left.equals(this.right))
                    && (conv.right.equals(this.right) || conv.right.equals(this.left))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (Objects.hashCode(this.left) + Objects.hashCode(this.right));
        return hash;
    }

    public void consume(BiConsumer<L, R> b) {
        b.accept(left, right);
    }

    public L left() {
        return this.left;
    }

    public R right() {
        return this.right;
    }

    public <X, Y, R> R map(BiFunction<? super X, ? super Y, ? extends R> mapper) {
        return mapper.apply((X) left, (Y) right);
    }

    public <T, R> Tuple<R, R> map(Function<? super T, ? extends R> mapper) {
        if (left.getClass() != right.getClass()) {
            throw new RuntimeException("left.class is " + left.getClass().getName() + "but right.class is " + left.getClass().getName());
        }
        return (Tuple<R, R>) Tuple.of(mapper.apply((T) left), mapper.apply((T) right));
    }

    public <T> Tuple<T, R> mapLeft(Function<? super L, ? extends T> mapper) {
        return Tuple.of(mapper.apply(left), right);
    }

    public <T> Tuple<L, T> mapRight(Function<? super R, ? extends T> mapper) {
        return Tuple.of(left, mapper.apply(right));
    }

    @Override
    public String toString() {
        return left().toString() + ":" + right().toString();
    }

}
