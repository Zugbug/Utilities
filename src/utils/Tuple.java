/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
     * Makes a simple JSONObject with key a and value b.
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
     *
     * @param <L> type for the left element
     * @param <R> type for the right element
     * @param left element
     * @param right element
     * @return
     */
    public static <L, R> Tuple<L, R> of(L left, R right) {
        return new Tuple<>(left, right);
    }

    public static <L, R> List<Tuple<L, R>> zip(L[] left, R[] right) {
        Iterator<L> l = Arrays.asList(left).iterator();
        Iterator<R> r = Arrays.asList(right).iterator();
        ArrayList<Tuple<L, R>> ret = new ArrayList();
        while (l.hasNext() && r.hasNext()) {
            ret.add(Tuple.of(l.next(), r.next()));
        }
        return ret;
    }

    /**
     * Creates a tuple, from pairs of elements in the array. The array MUST be
     * of even length. If the length of the array is longer than 2, then the
     * result is a tuple made up of two lists, where the even elements are on
     * the left and the odd elements are on the right. Otherwise returns a
     * simple tuple with first element on the left and second on the right.
     *
     * @exception RuntimeException is thrown if the length of the array is not
     * of even length
     * @param <T> type for the elements of the array and both sides of the tuple
     * @param array from which the elements are created
     * @return the tuple object representing the array
     */
    public static <T> Tuple<T, T> of(T... array) {
        if (array.length == 2) {
            return Tuple.of(array[0], array[1]);
        }
        if (array.length % 2 != 0) {
            throw new RuntimeException("Array must contain an even number elements, had: " + Arrays.deepToString(array));
        } else {
            List lefts = new ArrayList(), rights = new ArrayList();
            IntStream.range(0, array.length).forEach((int value) -> {
                ((value % 2 == 0) ? lefts : rights).add(array[value]);
            });
            return (Tuple<T, T>) Tuple.of(lefts, rights);
        }

    }

    /**
     * !WARNING: HERE BE DRAGONS. Instead make a tuple of two lists and then use
     * {@link utils.Tuple#asList() asList method}.
     *
     * @param <L>
     * @param <R>
     * @param t
     * @return
     */
    private static <L, R> List<Tuple<L, R>> listFrom(Tuple<? extends Collection<L>, ? extends Collection<R>> t) {
//        return listFromStreamTuples(t.mapLeft(Collection::stream).mapRight(Collection::stream));
        return t.map((BiFunction<Collection<L>, Collection<R>, List<Tuple<L, R>>>) (Collection<L> t1, Collection<R> u) -> {
            List<L> lefts = new ArrayList<>(t1);
            List<R> rights = new ArrayList<>(u);
            int diff;
            Collection smaller = ((diff = rights.size() - lefts.size()) < 0) ? rights : lefts;
            smaller.addAll(Stream.generate(() -> null)
                .limit(Math.abs(diff))
                .collect(Collectors.toList()));
            List<Tuple<L, R>> ret = new ArrayList();
            for (int i = 0; i < lefts.size(); i++) {
                ret.add(new Tuple(lefts.get(i), rights.get(i)));
            }
            return ret;
        });
    }

    /**
     *
     * @return
     */
    public List<Tuple<L, R>> asList() {
        return Tuple.listFrom((Tuple<? extends Collection<L>, ? extends Collection<R>>) this);
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

    /**
     * Feeds the two values into a function. Analogous to a
     * {@link Collection#forEach(java.util.function.Consumer) forEach}.
     *
     * @param b function that accepts two values
     */
    public void consume(BiConsumer<L, R> b) {
        b.accept(left, right);
    }

    /**
     * Swaps the two elements. Right becomes left and vice versa.
     *
     * @return new tuple with left values of current right and right value of
     * current left
     */
    public Tuple<R, L> swap() {
        return Tuple.of(right, left);
    }

    /**
     * Returns the left element of this tuple.
     *
     * @return left element
     */
    public L left() {
        return this.left;
    }

    /**
     * Return the right element of this tuple.
     *
     * @return right element
     */
    public R right() {
        return this.right;
    }

    /**
     * Collects the two elements of the tuple into a new value. Result is no
     * longer a tuple!
     *
     * @param <T> type of the result of the mapping operation
     * @param mapper function that takes two elements and returns one
     * @return new the result of the mapping operation
     */
    public <T> T map(BiFunction<? super L, ? super R, ? extends T> mapper) {
        return mapper.apply((L) left, (R) right);
    }

    /**
     * Applies a function to both values. Assumes that both of the values are of
     * the same type.
     *
     * @param <T> type of both of the original values
     * @param <R> type of both of the resultant values
     * @param mapper function that is applied to both the left and the right
     * element
     * @return new tuple where both of the values are of the original tuple,
     * with the function applied to them
     */
    public <T, R> Tuple<R, R> map(Function<? super T, ? extends R> mapper) {
        return (Tuple<R, R>) Tuple.of(mapper.apply((T) left), mapper.apply((T) right));
    }

    /**
     * Applies a function to the left value. The values are allowed to not be
     * the same.
     *
     * @param <T> type of the resultant left element
     * @param mapper function that is applied to only left element
     * @return new tuple with the original right element and the new left
     * element
     */
    public <T> Tuple<T, R> mapLeft(Function<? super L, ? extends T> mapper) {
        return Tuple.of(mapper.apply(left), right);
    }

    /**
     * Applies a function to the right value. The values are allowed to not be
     * the same.
     *
     * @param <T> type of the resultant right element
     * @param mapper function that is applied to only right element
     * @return new tuple with the original left element and the new right
     * element
     */
    public <T> Tuple<L, T> mapRight(Function<? super R, ? extends T> mapper) {
        return Tuple.of(left, mapper.apply(right));
    }

    /**
     * Applies a function to a pair of left values of this and other tuple, and
     * a pair of right values of this and other tuple, producing a new tuple.
     * Must be that all 4 elements, left & right of this AND left & right of
     * that ARE OF THE SAME TYPE.
     *
     * @param <T> original type of the elements of this tuple and other tuple
     * @param <X> resultant type of the elements of the produced tuple
     * @param that other tuple with which the mapping is done
     * @param mapper function that is applied to the two right elements and the
     * two left elements. Separately
     * @return new tuple with the mapped left and right values.
     */
    public <T, X> Tuple<X, X> mapWith(Tuple<T, T> that, BiFunction<T, T, X> mapper) {
        return Tuple.of(mapper.apply((T) this.left(), that.left()), mapper.apply((T) this.right(), that.right()));
    }

    @Override
    public String toString() {
        return left().toString() + ":" + right().toString();
    }

}
