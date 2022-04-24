package org.whywolk.topl.util;

public abstract class AbstractHashTable <Key, Value> implements IHashTable <Key, Value> {
    protected final int DEFAULT_INITIAL_CAPACITY = 16;
    protected final int MAX_CAPACITY = 1 << 10;
    protected final float DEFAULT_LOAD_FACTOR = 0.75f;

    protected int size;
    protected int count;

    protected class Node <K,V> {
        int hash;
        K key;
        V value;

        protected Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }
    }

    public void insert(Key k, Value v) {}

    public abstract Value lookUp(Key k);

    public void remove(Key k) {}

    protected AbstractHashTable() {}

    protected void enlarge() {};

    protected int indexFor(int h, int length) {
        return h & (length - 1);
    }

    protected int shuffle(int h) {
        return h ^ (h >>> 16);
    }

    protected int hash(Key k) {
        int h = 0;
        if (k instanceof String){
            return shuffle(hashCode((String) k));
        }

        return shuffle(k.hashCode());
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
