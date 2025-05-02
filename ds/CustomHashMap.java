package ds;

import java.util.ArrayList;

public class CustomHashMap<K, V> {
    private class Entry {
        K key;
        V value;
        Entry next;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    private ArrayList<Entry> buckets;
    private int size;
    private static final int INITIAL_CAPACITY = 16;

    public CustomHashMap() {
        buckets = new ArrayList<>(INITIAL_CAPACITY);
        for (int i = 0; i < INITIAL_CAPACITY; i++) {
            buckets.add(null);
        }
        size = 0;
    }

    public void put(K key, V value) {
        int index = getIndex(key);
        Entry entry = buckets.get(index);
        if (entry == null) {
            buckets.set(index, new Entry(key, value));
            size++;
        } else {
            while (entry.next != null && !entry.key.equals(key)) {
                entry = entry.next;
            }
            if (entry.key.equals(key)) {
                entry.value = value;
            } else {
                entry.next = new Entry(key, value);
                size++;
            }
        }
    }

    public V get(K key) {
        int index = getIndex(key);
        Entry entry = buckets.get(index);
        while (entry != null) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
            entry = entry.next;
        }
        return null;
    }

    public void remove(K key) {
        int index = getIndex(key);
        Entry entry = buckets.get(index);
        Entry prev = null;
        while (entry != null) {
            if (entry.key.equals(key)) {
                if (prev == null) {
                    buckets.set(index, entry.next);
                } else {
                    prev.next = entry.next;
                }
                size--;
                return;
            }
            prev = entry;
            entry = entry.next;
        }
    }

    public int size() {
        return size;
    }

    private int getIndex(K key) {
        return Math.abs(key.hashCode() % INITIAL_CAPACITY);
    }
}