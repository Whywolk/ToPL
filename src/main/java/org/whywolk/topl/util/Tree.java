/*
 * License: GNU GPL version 3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Copyright (c) 2022  Author: Alex Shirshov <https://github.com/Whywolk>
 *
 */

package org.whywolk.topl.util;

import java.util.ArrayList;
import java.util.List;

public class Tree <T> {

    private T data;
    private Tree<T> parent;
    private List<Tree<T>> children;

    public boolean isRoot() {
        return parent == null;
    }

    public boolean isLeaf() {
        return children.size() == 0;
    }

    public Tree() {
        this.children = new ArrayList<>();
    }

    public Tree(T data) {
        this.data = data;
        this.children = new ArrayList<>();
    }

    public Tree<T> addChild() {
        Tree<T> childNode = new Tree<T>();
        childNode.parent = this;
        this.children.add(childNode);
        return childNode;
    }

    public Tree<T> addChild(T data) {
        Tree<T> childNode = new Tree<T>(data);
        childNode.parent = this;
        this.children.add(childNode);
        return childNode;
    }

    public int getLevel() {
        if (this.isRoot())
            return 0;
        else
            return parent.getLevel() + 1;
    }

    public T getData() {
        return data;
    }

    public Tree<T> getParent() {
        return parent;
    }

    public List<Tree<T>> getChildren() {
        return children;
    }

    public String getPath() {
        String str = "";
        Tree<T> node = this;
        while (!node.isRoot()) {
            str = ("/" + node.parent.children.indexOf(node)) + str;
            node = node.parent;
        }
        return "0" + str;
    }

    public void print() {
        Tree<T> node = this;
        printNode(node);
    }

    private void printNode(Tree<T> node) {
        for (int i = 0; i < node.getLevel(); i++) {
            System.out.print("    ");
        }
        System.out.println(node.getData());
        for (Tree<T> child : node.getChildren()) {
            printNode(child);
        }
    }
}
