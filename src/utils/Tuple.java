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
 * @param <X> Left element type
 * @param <Y> Right element type
 */
public class Tuple<X, Y> {

    public static <T, Y> JSONObject toJSON(T a, Y b) {
        return Tuple.toJSON(Tuple.of(a, b));
    }

    public static JSONObject toJSON(Tuple... ts) {
        JSONObject ret = new JSONObject();
        for (Tuple t : ts) {
            t.consume(ret::put);
        }
        return ret;
    }

    public static Tuple<String, String> splitOnce(String src, String split) {
        return Tuple.of(src.split(split, 2));
    }

    public static Tuple<String, String> split(String src, String split) {
        return Tuple.of(src.split(split));
    }

    public static <X, Y> Tuple<X, Y> of(X l, Y r) {
        return new Tuple<>(l, r);
    }

    public static <X> Tuple<X, X> of(X... array) {
        if (array.length != 2) {
            throw new RuntimeException("Array must contain exactly 2 elements, had: " + Arrays.deepToString(array));
        }
        return Tuple.of(array[0], array[1]);
    }

    public static <B> Tuple<List<B>, List<B>> of(B[] l, B[] r) {
        return (Tuple<List<B>, List<B>>) of(Arrays.asList(r), Arrays.asList(l));
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

    public List<Tuple<X, Y>> asList() {
        return Tuple.listFrom((Tuple<? extends Collection<X>, ? extends Collection<Y>>) this);
    }

    public static <X> double samePairsPercentage(List<Tuple<X, X>> tar) {
        return samePairsPercentage(tar.stream());
    }

    public static <X> double samePairsPercentage(Stream<Tuple<X, X>> tar) {
        return tar.map(tuple -> tuple.map((X left, X right) -> (left.equals(right)) ? 1 : 0))
            .collect(Collectors.averagingDouble(s -> s));
    }
    private final X left;
    private final Y right;

    private Tuple(X l, Y r) {
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

    public void consume(BiConsumer<X, Y> b) {
        b.accept(left, right);
    }

    public X left() {
        return this.left;
    }

    public Y right() {
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

    public <T> Tuple<T, Y> mapLeft(Function<? super X, ? extends T> mapper) {
        return Tuple.of(mapper.apply(left), right);
    }

    public <T> Tuple<X, T> mapRight(Function<? super Y, ? extends T> mapper) {
        return Tuple.of(left, mapper.apply(right));
    }

    @Override
    public String toString() {
        return left().toString() + ":" + right().toString();
    }

}
