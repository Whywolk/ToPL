/*
 * License: GNU GPL version 3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Copyright (c) 2022  Author: Alex Shirshov <https://github.com/Whywolk>
 *
 */

package org.whywolk.topl.syntax;

import org.whywolk.topl.lexic.IdEntry;
import org.whywolk.topl.lexic.Parser;
import org.whywolk.topl.lexic.Token;
import org.whywolk.topl.util.HashTableChain;
import org.whywolk.topl.util.IHashTable;
import org.whywolk.topl.util.Tree;

import java.util.ArrayList;

// S ::= id = F ;
// F ::= E T
// E ::= const
// E ::= id
// E ::= ( F )
// E ::= not E
// T ::= op F
// T ::= ''

public class LLParser {
    private ArrayList<Token> tokens;
    private int curIdx;
    private Tree<String> trace;
    private Tree<String> curNode;
    private boolean success;
    private IHashTable<String, IdEntry> idTable;
    private Token curId;

    public LLParser(ArrayList<Token> tokens) {
        this.tokens = tokens;
        this.curIdx = 0;
        this.success = true;
        this.idTable = new HashTableChain<>();
    }

    public void printTrace() {
        System.out.println("--------Trace--------");
        this.trace.print();
        System.out.println("---------------------");
    }

    public void analyze() {
        while (success && curIdx < tokens.size()) {
            S();
            printTrace();
        }
    }

    private void S() {
        if (!tokens.isEmpty()) {
            this.trace = new Tree<>("S");
            this.curNode = this.trace;

            if (tokens.get(curIdx).getType() == Parser.TokenType.ID) {
                curNode.addChild(tokens.get(curIdx).getValue());
                curId = tokens.get(curIdx);
                curIdx++;

                if (curIdx < tokens.size() && tokens.get(curIdx).getType() == Parser.TokenType.ASSIGN) {
                    curNode.addChild(tokens.get(curIdx).getValue());
                    curIdx++;

                    F();
                    if (!success) return;

                    if (curIdx < tokens.size() && tokens.get(curIdx).getType() == Parser.TokenType.END) {
                        curNode.addChild(tokens.get(curIdx).getValue());
                        curIdx++;

                        idTable.insert(curId.getValue(), new IdEntry(curId));
                    } else {
                        System.out.println("Expected ';' at line " + tokens.get(curIdx-1).getLine()
                                +  " Pos " + tokens.get(curIdx-1).getStart());
                        success = false;
                    }
                } else {
                    System.out.println("Expected Assignation at line " + tokens.get(curIdx-1).getLine()
                            +  " Pos " + tokens.get(curIdx-1).getStart());
                    success = false;
                }
            } else {
                System.out.println("Expected Identifier at line " + tokens.get(curIdx).getLine()
                        +  " Pos " + tokens.get(curIdx).getStart());
                success = false;
            }
        }
    }

    private void F() {
        curNode = curNode.addChild("F");

        if (success) {
            E();
        }
        if (!success) return;

        if (success) {
            T();
        }
        if (!success) return;

        curNode = curNode.getParent();
    }

    private void E() {
        curNode = curNode.addChild("E");
        if (curIdx >= tokens.size()) return;
        switch (tokens.get(curIdx).getType()) {
            case NUMBER:
                curNode.addChild(tokens.get(curIdx).getValue());
                curIdx++;
                break;
            case ID:
                IdEntry tmp = idTable.lookUp(tokens.get(curIdx).getValue());
                if (tmp != null && tokens.get(curIdx).getValue().equals(tmp.getToken().getValue())) {
                    curNode.addChild(tokens.get(curIdx).getValue());
                    curIdx++;
                } else {
                    System.out.println("Variable is not defined: " + tokens.get(curIdx).getValue());
                    success = false;
                }
                break;
            case NEGATIVE:
                curNode.addChild(tokens.get(curIdx).getValue());
                curIdx++;

                if (success) {
                    E();
                }
                if (!success) return;

                break;
            case BRACKET_L:
                curNode.addChild(tokens.get(curIdx).getValue());
                curIdx++;

                if (success) {
                    F();
                }
                if (!success) return;

                if (tokens.get(curIdx).getType() == Parser.TokenType.BRACKET_R) {
                    curNode.addChild(tokens.get(curIdx).getValue());
                    curIdx++;

                } else {
                    System.out.println("Expected Right Bracket at line " + tokens.get(curIdx).getLine()
                            +  " Pos " + tokens.get(curIdx).getStart());
                    success = false;
                }
                break;
            default:
                System.out.println("Expected Expression at line " + tokens.get(curIdx).getLine()
                        +  " Pos " + tokens.get(curIdx).getStart());
                success = false;
        }

        curNode = curNode.getParent();
    }

    private void T() {
        curNode = curNode.addChild("T");
        if (curIdx < tokens.size() && tokens.get(curIdx).getType() == Parser.TokenType.OPERATOR) {
            curNode.addChild(tokens.get(curIdx).getValue());
            curIdx++;
            if (success) {
                F();
            }
            if (!success) return;
        }
        curNode = curNode.getParent();
    }
}
