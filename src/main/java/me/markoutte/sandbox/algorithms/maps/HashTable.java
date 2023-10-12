package me.markoutte.sandbox.algorithms.maps;

import java.util.Objects;

public class HashTable<K, V> implements AssociativeArray<K, V> {

    private final Entry[] table;
    private int size = 0;

    public HashTable(int capacity) {
        this.table = new HashTable.Entry[Math.max(100, capacity)];
    }

    private int place(K key) {
        return key.hashCode() % table.length;
    }

    private Entry find(K key, int place) {
        Entry entry = table[place];
        while (entry != null) {
            if (Objects.equals(entry.key, key)) {
                return entry;
            }
            entry = entry.next;
        }
        return null;
    }

    @Override
    public V get(K key) {
        var place = place(key);
        Entry entry = find(key, place);
        return entry == null ? null : entry.value;
    }

    @Override
    public V put(K key, V value) {
        var place = place(key);
        var entry = find(key, place);
        if (entry != null) {
            var oldValue = entry.value;
            entry.value = value;
            return oldValue;
        } else {
            Entry newEntry = new Entry(key, value);
            if (table[place] != null) {
                newEntry.next = table[place];
            }
            table[place] = newEntry;
            size++;
            return null;
        }
    }

    @Override
    public V remove(K key) {
        var place = place(key);
        Entry entry = find(key, place);
        if (entry == null) {
            return null;
        } else {
            var current = table[place];
            if (current == entry) {
                table[place] = entry.next;
            } else {
                while (current.next != entry) {
                    current = current.next;
                }
                current.next = entry.next;
            }
            size--;
            return entry.value;
        }
    }

    @Override
    public int size() {
        return size;
    }

    private class Entry {
        final K key;
        V value;

        Entry next;

        private Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
