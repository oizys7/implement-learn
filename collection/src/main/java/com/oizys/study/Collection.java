package com.oizys.study;

import java.util.function.IntFunction;

public interface Collection<E> extends Iterable<E> {
    void add(E element);

    void add(E element, int index);

    E remove(int index);

    boolean remove(E element);

    E set(int index, E element);

    E get(int index);

    int size();

    boolean isEmpty();

    void clear();

    Object[] toArray();

    <T> T[] toArray(T[] a);
    default <T> T[] toArray(IntFunction<T[]> generator) {
        return toArray(generator.apply(0));
    }

}
