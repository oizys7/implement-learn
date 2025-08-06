package com.oizys.study;

import java.util.*;

/**
 * @author wyn
 * Created on 2025/8/6
 */
public class ArrayList<E> implements List<E> {
    private int size;
    private static final int DEFAULT_CAPACITY = 10;
    transient Object[] elementData;

    public ArrayList() {
        this.elementData = new Object[DEFAULT_CAPACITY];
    }

    @Override
    public void add(E element) {
        if (size == elementData.length) {
            elementData = grow();
        }
        elementData[size++] = element;
    }

    private Object[] grow() {
        Object[] newTable = new Object[elementData.length * 2];
        System.arraycopy(elementData, 0, newTable, 0, elementData.length);
        this.elementData = newTable;
        return newTable;
    }

    @Override
    public void add(E element, int index) {
        checkIndex( index);
        Object[] elementData = this.elementData;
        if (size == elementData.length) {
            elementData = grow();
        }
        System.arraycopy(elementData, index, elementData, index + 1, size - index);
        elementData[index] = element;
        size++;
    }

    @Override
    public E remove(int index) {
        E oldElement = elementDatum(index);
        System.arraycopy(elementData, index + 1, elementData, index, size - index - 1);
        size--;
        return oldElement;
    }

    @Override
    public boolean remove(E element) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(element, elementData[i])) {
                E removed = remove(i);
                return removed == null;
            }
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        checkIndex(index);
        E oldElement = elementDatum(index);
        elementData[ index] = element;
        return oldElement;
    }

    @Override
    public E get(int index) {
        checkIndex(index);
        return elementDatum(index);
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void clear() {
//        this.size = 0;
//        this.elementData = null;
        //复用数组内存：即使清空了元素，也保留这个数组，以便下次 add() 时直接使用，避免频繁创建新数组。
        //性能优化：如果每次 clear() 都把数组设为 null，下次 add() 时就得重新 new Object[DEFAULT_CAPACITY]，造成性能浪费。
        final Object[] es = elementData;
        for (int i = 0; i < size; i++) {
            es[i] = null;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new ArrayIterator();
    }

    class ArrayIterator implements Iterator<E> {

        private int cursor;
        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public E next() {
            if (cursor >= size) {
                throw new NoSuchElementException();
            }
            return elementDatum(cursor++);
        }
    }


    @SuppressWarnings("unchecked")
    private E elementDatum(int index) {
        return (E) elementData[index];
    }

    private void checkIndex(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
    }


}
