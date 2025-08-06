package com.oizys.study.list;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.IntFunction;

/**
 * @author wyn
 * Created on 2025/8/6
 */
public class LinkedList<E> implements List<E> {
    transient int size = 0;

    /**
     * Pointer to first node.
     */
    transient Node<E> head;

    /**
     * Pointer to last node.
     */
    transient Node<E> tail;


    @Override
    public void add(E element) {
        Node<E> newNode = new Node<>(element, null, tail);
        if (tail == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
        size++;
    }

    @Override
    public void add(E element, int index) {
        checkIndex( index);
        if (index == size) {
            add(element);
            return;
        }
        Node< E> oldNode = findNode(index);
        Node<E> newNode = new Node<>(element, oldNode, oldNode.prev);
        oldNode.prev.next = newNode;
        oldNode.prev = newNode;
        size++;
    }

    private Node<E> findNode(int index) {
        Node<E> result = null;
        if (index < size / 2) {
            result = head;
            for (int i = 0; i < index; i++) {
                result = result.next;
            }
        } else {
            result = tail;
            for (int i = size - 1; i > index; i--) {
                result = result.prev;
            }
        }
        return result;
    }

    @Override
    public E remove(int index) {
        checkIndex(index);
        return removeNode(findNode(index));
    }

    private E removeNode(Node<E> node) {
        Node<E> pre = node.prev;
        Node<E> next = node.next;
        if (pre == null) {
            head = next;
        } else {
            pre.next = next;
        }
        if (next == null) {
            tail = pre;
        } else {
            next.prev = pre;
        }
        node.prev = null;
        node.next = null;
        size--;
        return node.element;
    }

    @Override
    public boolean remove(E element) {
        Node<E> node = head;
        while (node != null) {
            if (Objects.equals(node.element, element)) {
                removeNode(node);
                return true;
            }
            node = node.next;
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        checkIndex( index);
        if (index == size) {
            add(element);
            return null;
        }
        Node< E> oldNode = findNode(index);
        Node<E> newNode = new Node<>(element, oldNode.next, oldNode.prev);
        oldNode.prev.next = newNode;
        oldNode.next.prev = newNode;
        return oldNode.element;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return findNode(index).element;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        head = null;
        size = 0;
    }

    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<E> x = head; x != null; x = x.next)
            result[i++] = x.element;
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            a = (T[])java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        int i = 0;
        Object[] result = a;
        for (Node<E> x = head; x != null; x = x.next)
            result[i++] = x.element;

        if (a.length > size)
            a[size] = null;

        return a;
    }

    @Override
    public Iterator<E> iterator() {
        return new LinkedListIterator<>();
    }

    static class LinkedListIterator<E> implements Iterator<E> {
        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public E next() {
            return null;
        }
    }

    static class Node<E> {
        E element;
        Node<E> next;
        Node<E> prev;

        public Node(E element, Node<E> next, Node<E> prev) {
            this.element = element;
            this.next = next;
            this.prev = prev;
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
    }
}
