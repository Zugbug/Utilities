/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author zugbug
 */
public interface Mapping<T> {

    public T map(T val);

    public default T value(T val) {
        return this.map(val);
    }
}
