package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int RESIZE_FACTOR = 1;
    private int tableLength;
    private int threshold;
    private int size = 0;
    private Node<K, V>[] hashMap;

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        if (hashMap[hashIndex(key)] == null) {
            hashMap[hashIndex(key)] = new Node<>(key, value, null);
            size++;
        } else {
            Node<K, V> currentNode = hashMap[hashIndex(key)];
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = new Node<>(key, value, null);
                    size++;
                    return;
                }
                currentNode = currentNode.next;
            }
        }

    }

    @Override
    public V getValue(K key) {
        if (size > 0) {
            Node<K, V> currentNode;
            if (key == null) {
                currentNode = hashMap[hashIndex(key)];
                while (currentNode != null) {
                    if (currentNode.key == null) {
                        return currentNode.value;
                    }
                    currentNode = currentNode.next;
                }
                return null;
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
        return size;
    }

    private void resize() {
        if (hashMap == null) {
            tableLength = DEFAULT_CAPACITY;
            threshold = (int)(tableLength * LOAD_FACTOR);
            hashMap = new Node[tableLength];
        } else {
            tableLength = tableLength << RESIZE_FACTOR;
            threshold = (int)(tableLength * LOAD_FACTOR);
            hashMap = transformTable(tableLength);
        }
    }

    private Node<K, V>[] transformTable(int newSize) {
        Node<K, V>[] newTable = new Node[newSize];

        for (int i = 0; i < hashMap.length; i++) {
            Node<K, V> node = hashMap[i];
            while (node != null) {
                Node<K, V> next = node.next; // запам'ятати наступний вузол
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
        return key == null ? 0 : Math.abs(key.hashCode() % tableLength);
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
