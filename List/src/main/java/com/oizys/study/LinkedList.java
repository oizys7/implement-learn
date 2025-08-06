package com.oizys.study;

import java.util.Iterator;

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
    }

    private Node<E> findNode(int index) {
        Node<E> result = null;
        if (index < size / 2) {
            for (Node<E> node = head; node != null; node = node.next) {
                if (index == 0) {
                    result = node;
                    break;
                }
                index--;
            }

        } else {
            for (Node<E> node = tail; node != null; node = node.prev) {
                if (index == 0) {
                    result = node;
                    break;
                }
                index--;
            }
        }
        return result;
    }

    @Override
    public E remove(int index) {
        return null;
    }

    @Override
    public boolean remove(E element) {
        return false;
    }

    @Override
    public E set(int index, E element) {
        return null;
    }

    @Override
    public E get(int index) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void clear() {

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
