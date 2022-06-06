/*
 * License: GNU GPL version 3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Copyright (c) 2022  Author: Alex Shirshov <https://github.com/Whywolk>
 *
 */

package org.whywolk.topl.util;

public interface IHashTable <Key, Value> {

    void insert(Key k, Value v);

    Value lookUp(Key k);

    void remove(Key k);
}
