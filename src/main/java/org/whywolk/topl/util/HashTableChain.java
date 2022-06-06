/*
 * License: GNU GPL version 3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Copyright (c) 2022  Author: Alex Shirshov <https://github.com/Whywolk>
 *
 */

package org.whywolk.topl.util;

import java.util.LinkedList;

public class HashTableChain <Key, Value> extends AbstractHashTable <Key, Value> {

    protected LinkedList<Node<Key, Value>>[] table;

    public HashTableChain() {
        size = this.DEFAULT_INITIAL_CAPACITY;
        count = 0;
        table = new LinkedList[size];
    }

    public HashTableChain(int size) {
        this.size = size;
        count = 0;
        table = new LinkedList[size];
    }

    public void insert(Key k, Value v){
        if (isLoaded()) {
            enlarge();
            insert(k, v);
        } else {
            int hash = hash(k);
            int i = indexFor(hash, size);
            if (table[i] == null) {
                table[i] = new LinkedList<Node<Key, Value>>();
            } else {
                for (Node<Key, Value> el : table[i]) {
                    if (el.key.equals(k)) {
                        return;
                    }
                }
            }
            table[i].add(new Node<Key, Value>(hash, k, v));
            count++;
        }
    }

    public Value lookUp(Key k) {
        int hash = hash(k);
        int i = indexFor(hash, size);
        if (table[i] != null) {
            for (Node<Key, Value> el : table[i]) {
                if (el.key.equals(k)) {
                    return el.value;
                }
            }
        }
        return null;
    }

    public void remove(Key k) {
        int hash = hash(k);
        int i = indexFor(hash, size);
        if (table[i] != null) {
            for (Node<Key, Value> el : table[i]) {
                if (el.key.equals(k)) {
                    table[i].remove(el);
                    count--;
                }
            }
        }
    }

    protected void enlarge() {
        if (size == MAX_CAPACITY) {
            try {
                throw new Exception("HashTable Overflow");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        HashTableChain<Key, Value> newTable = new HashTableChain<>(size * 2);
        for (int i = 0; i < size; i++) {
            if (table[i] != null) {
                for (Node<Key, Value> el : table[i]) {
                    newTable.insert(el.key, el.value);
                }
            }
        }
        this.table = newTable.table;
        this.size = newTable.size;
        this.count = newTable.count;
    }
}
