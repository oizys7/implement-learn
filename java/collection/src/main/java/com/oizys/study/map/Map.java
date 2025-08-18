package com.oizys.study.map;

/**
 * @author wyn
 * Created on 2025/8/7
 */
public interface Map<K, V>  {
    V put(K key, V value);

    V get(K key);

    V remove(K key);

    int size();

    interface Entry<K, V> {
        K getKey();


        V getValue();


        V setValue(V value);
    }
}
