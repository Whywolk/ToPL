package org.whywolk.topl.util;

import java.util.Iterator;
import java.util.LinkedList;

public class HashTableChain <Key, Value> extends AbstractHashTable <Key, Value> {

    protected LinkedList<Node>[] table;

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
        // need rehash
        if (isLoaded()) {
            HashTableChain<Key, Value> newTable = new HashTableChain<Key, Value>(size * 2);
            for (int i = 0; i < size; i++) {
                if (table[i] != null) {
                    Iterator<Node> iter = table[i].iterator();
                    while (iter.hasNext()) {
                        Node el = iter.next();
                        newTable.insert(el.key, el.value);
                    }
                }
            }
            this.table = newTable.table;
            this.size = newTable.size;
            this.count = newTable.count;
        } else {
            int hash = hash(k);
            int i = indexFor(hash, size);
            if (table[i] == null) {
                table[i] = new LinkedList<Node>();
            } else {
                for (Node el : table[i]) {
                    if (el.key == k && el.value == v) {
                        return;
                    }
                }
            }
            table[i].add(new Node(hash, k, v));
            count++;
        }
    }

    public Value lookUp(Key k) {
        int hash = hash(k);
        int i = indexFor(hash, size);
        if (table[i] != null) {
            Iterator<Node> iter = table[i].iterator();
            while (iter.hasNext()) {
                Node el = iter.next();
                if (el.key.equals(k)) {
                    return el.value;
                }
            }
        }
        return null;
    }
}
