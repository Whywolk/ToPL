package org.whywolk.topl.lexic;

public class Token {
    private Parser.TokenType type;
    private int line;
    private int start;
    private int end;
    private String value;

    public Token() {}

    public Token(Parser.TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public Token(Parser.TokenType type, int line, int start, int end, String value) {
        this.type = type;
        this.line = line;
        this.start = start;
        this.end = end;
        this.value = value;
    }

    public void print() {
        System.out.format("%4d | %10s | %5d | %5d | %s %n", this.line, this.type, this.start, this.end, this.value);
    }

    public Parser.TokenType getType() {
        return type;
    }

    public void setType(Parser.TokenType type) {
        this.type = type;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", line=" + line +
                ", start=" + start +
                ", end=" + end +
                ", value='" + value + '\'' +
                '}';
    }
}
