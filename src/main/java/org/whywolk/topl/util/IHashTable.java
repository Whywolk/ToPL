package org.whywolk.topl.util;

public interface IHashTable <Key, Value> {

    void insert(Key k, Value v);

    Value lookUp(Key k);
}
