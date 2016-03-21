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
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author zugbug
 * @param <X> Left element type
 * @param <Y> Right element type
 */
public class Tuple<X, Y> {

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tuple) {
            Tuple conv = (Tuple) obj;
            if ((conv.left.equals(this.left) || conv.left.equals(this.right)) && 
                (conv.right.equals(this.right) || conv.right.equals(this.left))) {
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

    public static Tuple<String, String> split(String src, String split) {
        return Tuple.of(src.split(split));
    }

    private final X left;
    private final Y right;

    private Tuple(X l, Y r) {
        this.right = r;
        this.left = l;
    }

    public static <X, Y> Tuple<X, Y> of(X l, Y r) {
        return new Tuple<>(l, r);
    }

    public static <X> Tuple<X, X> of(X... array) {
        if (array.length != 2) {
            throw new RuntimeException("Array must contain only 2 elements");
        }
        return Tuple.of(array[0], array[1]);
    }

    public static <B> Tuple<List<B>, List<B>> of(B[] l, B[] r) {
        return (Tuple<List<B>, List<B>> )of(Arrays.asList(r), Arrays.asList(l));
    }

    public static <D extends Collection<V>, V> List<Tuple<V, V>> listFrom(Tuple<D, D> t) {
        return listFromStreamTuples(t.map((s) -> ((D) s).stream()));
    }
    
    public <D extends Collection<V>, V> List<Tuple<V, V>> asList() {
        return listFrom((Tuple<D,D>)this);
    }

    public static <E extends Stream, V> List<Tuple<V, V>> listFromStreamTuples(Tuple<E, E> t) {
        return t.consume((E too, E yoo) -> {
            List<Optional> ui = (List<Optional>) too.map(s -> Optional.of(s)).collect(Collectors.toList());
            List<Optional> ti = (List<Optional>) yoo.map(s -> Optional.of(s)).collect(Collectors.toList());
            int diff;
            List<Optional> smaller = ((diff = ti.size() - ui.size()) < 0) ? ti : ui;
            smaller.addAll(Stream.generate(Optional::empty)
                .limit(Math.abs(diff))
                .collect(Collectors.toList()));
            List<Tuple<V, V>> merged = new ArrayList();
            for (Iterator<Optional> iLong = ((ui.size() > ti.size()) ? ui : ti).iterator(),
                iShort = ((ui.size() > ti.size()) ? ti : ui).iterator(); iLong.hasNext();) {
                V ix = (V) iLong.next().orElse('_');
                V ux = (V) iShort.next().orElse('_');
                merged.add(Tuple.of(ix, ux));
            }
            return new ArrayList(merged);
        });
    }

    public X left() {
        return this.left;
    }

    public Y right() {
        return this.right;
    }

    public <X, Y, R> R consume(BiFunction<X, Y, R> mapper) {
        return (R) mapper.apply((X) left, (Y) right);
    }

    public <T, R> Tuple<R, R> map(Function<? super T, ? extends R> mapper) {
        if (left.getClass() != right.getClass()) {
            throw new RuntimeException("left.class is " + left.getClass().getName() + "but right.class is " + left.getClass().getName());
        }
        return (Tuple<R, R>) Tuple.of(mapper.apply((T) left), mapper.apply((T) right));
    }

    public <T extends X, R> Tuple<R, T> mapLeft(Function<? super T, ? extends R> mapper) {
        return Tuple.of(mapper.apply((T) left), (T) right);
    }

    public <T, R> Tuple<T, R> mapRight(Function<? super T, ? extends R> mapper) {
        return Tuple.of((T) left, mapper.apply((T) right));

    }

    public static <X> double samePairsPercentage(List<Tuple<X, X>> tar) {
        return samePairsPercentage(tar.stream());
    }

    public static <X> double samePairsPercentage(Stream<Tuple<X, X>> tar) {
        return tar.map(tuple -> tuple.consume((X left, X right) -> (left.equals(right)) ? 1 : 0))
            .collect(Collectors.averagingDouble(s -> s));
    }

    @Override
    public String toString() {
        return "Tuple" + "" + "[" + left().toString() + ":" + right().toString() + "]";
    }

}
