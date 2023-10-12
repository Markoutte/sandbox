package me.markoutte.sandbox.algorithms.maps;

/**
 * This is a common definition of associative array (a.k.a. Map)
 * @param <K>
 * @param <V>
 */
public interface AssociativeArray<K, V> {

    V get(K key);

    V put(K key, V value);

    V remove(K key);

    int size();

}
