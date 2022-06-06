/*
 * License: GNU GPL version 3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Copyright (c) 2022  Author: Alex Shirshov <https://github.com/Whywolk>
 *
 */

package org.whywolk.topl.util;

import java.lang.reflect.Array;

public class HashTableOpenAddr <Key, Value> extends AbstractHashTable <Key, Value> {

    protected class Cell {
        boolean isDeleted;
        Node<Key, Value> node;

        protected Cell(int hash, Key key, Value value) {
            this.isDeleted = false;
            this.node = new Node<Key, Value>(hash, key, value);
        }
    }

    protected Cell[] table;

    public HashTableOpenAddr() {
        size = this.DEFAULT_INITIAL_CAPACITY;
        count = 0;
        table = (Cell[]) Array.newInstance(Cell.class, size);
    }

    public HashTableOpenAddr(int size) {
        this.size = size;
        count = 0;
        table = (Cell[]) Array.newInstance(Cell.class, size);
    }

    public void insert(Key k, Value v) {
        if (isLoaded()) {
            enlarge();
            insert(k, v);
        } else {
            int hash = hash(k);
            int i = indexFor(hash, size);

            if (table[i] == null) {
                table[i] = new Cell(hash, k, v);
            } else {
                if (table[i].node.key.equals(k)) {
                    return;
                } else {
                    int gHash = gHash(k);
                    int step = 1;
                    int newI = 0;
                    do {
                        newI = gIndexFor(hash, gHash, size, step);
                        if (newI == i) {
                            enlarge();
                            insert(k, v);
                        }
                        step++;
                    }
                    while (table[newI] != null);

                    table[newI] = new Cell(hash, k, v);
                }
            }
            count++;
        }
    }

    public Value lookUp(Key k) {
        int hash = hash(k);
        int i = indexFor(hash, size);
        if (table[i] != null) {
            if (table[i].node.key.equals(k)) {
                return table[i].node.value;
            } else {
                int gHash = gHash(k);
                int step = 1;
                int newI = 0;
                do {
                    newI = gIndexFor(hash, gHash, size, step);
                    if (newI == i || table[newI].node == null) {
                        return null;
                    }
                    step++;
                }
                while (!table[newI].node.key.equals(k));

                return table[newI].node.value;
            }
        }
        return null;
    }

    public void remove(Key k) {
        int hash = hash(k);
        int i = indexFor(hash, size);
        if (table[i] != null) {
            if (table[i].node.key.equals(k)) {
                table[i].isDeleted = true;
                table[i].node = null;
            } else {
                int gHash = gHash(k);
                int step = 1;
                int newI = 0;
                do {
                    newI = gIndexFor(hash, gHash, size, step);
                    if (newI == i) {
                        return;
                    }
                    step++;
                }
                while (!table[newI].node.key.equals(k));

                table[newI].isDeleted = true;
                table[newI].node = null;
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
        HashTableOpenAddr<Key, Value> newTable = new HashTableOpenAddr<>(size * 2);
        for (int i = 0; i < size; i++) {
            if (table[i] != null && !table[i].isDeleted) {
                newTable.insert(table[i].node.key, table[i].node.value);
            }
        }
        this.table = newTable.table;
        this.size = newTable.size;
        this.count = newTable.count;
    }

    protected int gIndexFor(int h, int g, int length, int step) {
        return Math.abs(((h + g*step) % (length)));
    }

    protected int gHash(Key k) {
        int h = 0;
        if (k instanceof String){
            return shuffle(gHashCode((String) k));
        }

        return shuffle(k.hashCode());
    }

    protected int gHashCode(String s) {
        int h = 0;
        int a = 5;
        for (char ch : s.toCharArray()) {
            h = ((h * a) + ch);
        }
        return h;
    }
}
