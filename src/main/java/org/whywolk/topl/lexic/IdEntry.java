/*
 * License: GNU GPL version 3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Copyright (c) 2022  Author: Alex Shirshov <https://github.com/Whywolk>
 *
 */

package org.whywolk.topl.lexic;

public class IdEntry {
    private Token token;

    public IdEntry(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }
}
