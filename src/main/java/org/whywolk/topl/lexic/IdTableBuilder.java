package org.whywolk.topl.lexic;

import org.whywolk.topl.util.*;

import java.util.ArrayList;

public class IdTableBuilder {
    // <id_name, [0, 2]> --- IdEntry
    private IHashTable<String, IdEntry> idTable;
    private ArrayList<Token> tokens;
    private Tree<Integer> scopes;
    private Tree<Integer> curScope;

    public IdTableBuilder(ArrayList<Token> tokens) {
        this.idTable = new HashTableChain<>();
        this.tokens = tokens;
        this.scopes = new Tree<>();
        this.curScope = this.scopes;
    }

    public void run() {
        for (Token token : tokens) {
            if (token.getType() == Parser.TokenType.ID) {
                Entry e = new Entry(token.getValue(), curScope.getPath());
                IdEntry id = new IdEntry(token);
                idTable.insert(e.toString(), id);
            }
            else if (token.getType() == Parser.TokenType.BRACKET_FIGURE_L) {
                initializeScope();
            } else if (token.getType() == Parser.TokenType.BRACKET_FIGURE_R) {
                finalizeScope();
            }
        }
        System.out.println("Owari");
    }

    private class Entry {
        String id;
        String scope;

        public Entry(String id, String scope) {
            this.id = id;
            this.scope = scope;
        }

        @Override
        public String toString() {
            return id + " | " + scope;
        }
    }

    public IHashTable<String, IdEntry> getIdTable() {
        return idTable;
    }

    private void initializeScope() {
        this.curScope = this.curScope.addChild();
    }

    private void finalizeScope() {
        this.curScope = this.curScope.getParent();
    }
}
