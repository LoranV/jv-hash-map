package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private int size;
    private int threshold;
    private int capacity = 0;
    private K key;
    private V value;
    private MyHashMap<K, V> next;
    private MyHashMap<K, V>[] hashMap;

    public MyHashMap() {
    }

    public MyHashMap(K key, V value, MyHashMap<K, V> next) {
        this.key = key;
        this.value = value;
        this.next = next;
    }

    private void resize() {
        if (hashMap == null) {
            size = 16;
            threshold = (int)(size * 0.75);
            hashMap = new MyHashMap[size];
        } else {
            size = size << 1;
            threshold = (int)(size * 0.75);
            hashMap = transformTable(size);
        }
    }

    private MyHashMap<K, V>[] transformTable(int newSize) {
        MyHashMap<K, V>[] newTable = new MyHashMap[newSize];

        for (int i = 0; i < hashMap.length; i++) {
            MyHashMap<K, V> node = hashMap[i];
            while (node != null) {
                MyHashMap<K, V> next = node.next; // запам'ятати наступний вузол
                int newIndex = hashIndex(node.key); // обчислити новий індекс

                // вставити вузол на початок нового ланцюжка
                node.next = newTable[newIndex];
                newTable[newIndex] = node;

                node = next; // перейти до наступного у старому списку
            }
        }

        return newTable;
    }

    private int hashIndex(K key) {
        return Math.abs(key.hashCode() % size);
    }

    @Override
    public void put(K key, V value) {
        if (capacity == threshold) {
            resize();
        }
        if (key == null) {
            if (hashMap[0] == null) {
                hashMap[0] = new MyHashMap<>(key, value, null);
                capacity++;
            } else {
                MyHashMap currentNode = hashMap[0];
                while (currentNode.next != null || Objects.equals(currentNode.key, key)) {
                    if (Objects.equals(currentNode.key, key)) {
                        currentNode.value = value;
                        return;
                    }
                    currentNode = currentNode.next;
                }
                currentNode.next = new MyHashMap<>(key, value, null);
                capacity++;
            }
        } else if (hashMap[hashIndex(key)] == null) {
            hashMap[hashIndex(key)] = new MyHashMap<>(key, value, null);
            capacity++;
        } else {
            MyHashMap currentNode = hashMap[hashIndex(key)];
            while (currentNode.next != null || Objects.equals(currentNode.key, key)) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                currentNode = currentNode.next;
            }
            currentNode.next = new MyHashMap<>(key, value, null);
            capacity++;
        }

    }

    @Override
    public V getValue(K key) {
        if (capacity > 0) {
            MyHashMap<K, V> currentNode;
            if (key == null) {
                currentNode = hashMap[0];
                while (currentNode.next != null) {
                    if (currentNode.key == null) {
                        return currentNode.value;
                    }
                    currentNode = currentNode.next;
                }
                return currentNode.value;
            } else {
                currentNode = hashMap[hashIndex(key)];
                while (currentNode != null) {
                    if (currentNode.key != null) {
                        if (currentNode.key.equals(key)) {
                            return currentNode.value;
                        }
                    }
                    currentNode = currentNode.next;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return capacity;
    }
}
