package me.markoutte.sandbox.algorithms.maps;

import java.util.Objects;

public class OpenTable<K, V> implements AssociativeArray<K, V> {

    final Node<K, V>[] table;
    private int size = 0;

    public OpenTable(int capacity) {
        //noinspection unchecked
        table = new Node[capacity];
    }

    private int place(K key, int i) {
        return (key.hashCode() + i) % table.length;
    }

    private Node<K, V> find(K key) {
        for (int i = 0; i < table.length; i++) {
            int newPlace = place(key, i) % table.length;
            Node<K, V> node = table[newPlace];
            if (node == null) {
                return null;
            }
            if (!node.deleted && Objects.equals(node.key, key)) {
                return node;
            }
        }
        return null;
    }

    @Override
    public V get(K key) {
        Node<K, V> node = find(key);
        return node == null ? null : node.value;
    }

    @Override
    public V put(K key, V value) {
        Node<K, V> node = find(key);
        if (node != null) {
            V oldValue = node.value;
            node.value = value;
            return oldValue;
        } else {
            for (int i = 0; i < table.length; i++) {
                int place = place(key, i);
                if (table[place] == null || table[place].deleted) {
                    table[place] = new Node<>(key, value);
                    size++;
                    return null;
                }
            }
        }
        throw new IllegalStateException("No space left");
    }

    @Override
    public V remove(K key) {
        Node<K, V> node = find(key);
        if (node != null) {
            node.deleted = true;
            size--;
            return node.value;
        } else {
            return null;
        }
    }

    @Override
    public int size() {
        return size;
    }

    static class Node<K, V> {

        final K key;
        V value;
        boolean deleted = false;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
