package org.whywolk.topl.util;

import java.util.HashMap;

public abstract class AbstractHashTable <Key, Value> implements IHashTable <Key, Value> {
    protected final int DEFAULT_INITIAL_CAPACITY = 8;
    protected final int MAX_CAPACITY = 1 << 10;
    protected final float DEFAULT_LOAD_FACTOR = 0.75f;

    protected int size;
    protected int count;

    protected class Node {
        int hash;
        Key key;
        Value value;

        protected Node(int hash, Key key, Value value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }
    }

    public void insert(Key k, Value v) {
        throw new UnsupportedOperationException();
    }

    public abstract Value lookUp(Key k);

    protected AbstractHashTable() {}

    protected int indexFor(int h, int length) {
        return h & (length - 1);
    }

    protected int hash(Key k) {
        int h = 0;
        if (k instanceof String){
            int first = hashCode((String) k);
            int second = first >>> 16;
            int third = first ^ second;
            return (h = hashCode((String) k)) ^ (h >>> 16);
        }

        return (h = k.hashCode()) ^ (h >>> 16);
    }

    protected int hashCode(String s) {
        int h = 0;
        int a = 31;
        for (char ch : s.toCharArray()) {
            h = ((h * a) + ch);
        }
        return h;
    }

    protected boolean isLoaded() {
        return ((float)count/size > DEFAULT_LOAD_FACTOR);
    }
}
