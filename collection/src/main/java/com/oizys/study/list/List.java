package com.oizys.study.list;

import com.oizys.study.Collection;

/**
 * @author wyn
 * Created on 2025/8/6
 */
public interface List<E> extends Collection<E> {

    void add(E element);

    void add(E element, int index);

    E remove(int index);

    boolean remove(E element);

    E set(int index, E element);

    E get(int index);

    int size();

    void clear();
}
