package org.whywolk.topl.lexic;

import java.util.ArrayList;
import java.util.Arrays;
import java.lang.Character;

public class Parser {
    private String doc;
    private ArrayList<String> lines;
    private int curPos;
    private int curLinePos;
    private ArrayList<Token> tokens = new ArrayList<>();

    public enum TokenType {
        ID,
        NUMBER,
        COMMENT,
        ASSIGN,
        OPERATOR,
        NEGATIVE,
        END,
        BRACKET_L,
        BRACKET_R,
        BRACKET_FIGURE_L,
        BRACKET_FIGURE_R,
        ERROR;
    }

    public Parser() {
        this.doc = "";
        this.curLinePos = 0;
        this.curPos = 0;
    }

    public Parser(String doc) {
        this.doc = doc;
        this.lines = new ArrayList<>(Arrays.asList(this.doc.split("\\n+")));
        this.curLinePos = 0;
        this.curPos = 0;
    }

    public ArrayList<Token> getAllTokens() {
        return this.tokens;
    }

    public ArrayList<Token> getErrors() {
        ArrayList<Token> errors = new ArrayList<>();
        for (Token token : this.tokens) {
            if (token.getType() == TokenType.ERROR) {
                errors.add(token);
            }
        }
        return errors;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public void run() {

        for(String line : lines) {
            this.curPos = 0;

            while(curPos < line.length()) {

                char ch = line.charAt(curPos);
                // NEGATIVE or OPERATOR
                if (ch == 'n' || ch == 'o' || ch == 'x' || ch == 'a') {
                    Token token = getOperator(curPos, line);

                    // If it's NEGATIVE or OPERATOR then go next
                    if (token.getType() != null) {;
                        this.tokens.add(token);
                        continue;
                    }
                }
                // Else check for ID
                if (Character.isLetter(ch) || ch == '_') {
                    Token token = getId(curPos, line);
                    this.tokens.add(token);
                    this.curPos--;
                }
                // Else check for NUMBER
                else if (ch == '0') {
                    Token token = getNumber(curPos, line);
                    this.tokens.add(token);
                    this.curPos--;
                }
                else if (ch == '(') {
                    Token token = new Token(TokenType.BRACKET_L, this.curLinePos, curPos, curPos + 1,
                            line.substring(curPos, curPos + 1));
                    this.tokens.add(token);
                }
                else if (ch == ')') {
                    Token token = new Token(TokenType.BRACKET_R, this.curLinePos, curPos, curPos + 1,
                            line.substring(curPos, curPos + 1));
                    this.tokens.add(token);
                }
                else if (ch == '{') {
                    Token token = new Token(TokenType.BRACKET_FIGURE_L, this.curLinePos, curPos, curPos + 1,
                            line.substring(curPos, curPos + 1));
                    this.tokens.add(token);
                }
                else if (ch == '}') {
                    Token token = new Token(TokenType.BRACKET_FIGURE_R, this.curLinePos, curPos, curPos + 1,
                            line.substring(curPos, curPos + 1));
                    this.tokens.add(token);
                }
                else if (ch == '=') {
                    Token token = new Token(TokenType.ASSIGN, this.curLinePos, curPos, curPos + 1,
                            line.substring(curPos, curPos + 1));
                    this.tokens.add(token);
                }
                else if (ch == ';') {
                    Token token = new Token(TokenType.END, this.curLinePos, curPos, curPos + 1,
                            line.substring(curPos, curPos + 1));
                    this.tokens.add(token);
                }
                else if (ch == '/') {
                    Token token = getComment(curPos, line);
                    this.tokens.add(token);
                }
                // If this not spaces, then ERROR
                else if (ch != ' ' && ch != '\t') {
                    Token token = new Token(TokenType.ERROR, this.curLinePos, curPos, curPos + 1,
                            "Unexpected symbol : '" + line.charAt(curPos) + "'");
                    this.tokens.add(token);
                }

                this.curPos++;
            }
            this.curLinePos++;
        }
    }

    private Token getOperator(int start, String line) {
        this.curPos = start + 1;

        // get position of delimiter
        while (this.curPos < line.length()) {
            char ch = line.charAt(this.curPos);
            if (ch == ' ' || ch == '\t' || ch == ')' || ch == '(' || ch == ';' || ch == '=') {
                break;
            }
            this.curPos++;
        }

        String op = line.substring(start, this.curPos);
        if (op.equals("or") || op.equals("xor") || op.equals("and")) {
            return new Token(TokenType.OPERATOR, this.curLinePos, start, this.curPos,
                    line.substring(start, this.curPos));
        }
        else if (op.equals("not")){
            return new Token(TokenType.NEGATIVE, this.curLinePos, start, this.curPos,
                    line.substring(start, this.curPos));
        }
        this.curPos = start;
        return new Token();
    }

    private Token getId(int start, String line) {
        this.curPos = start + 1;

        // get position of delimiter
        while (this.curPos < line.length()) {
            char ch = line.charAt(this.curPos);
            if (ch == ' ' || ch == '\t' || ch == ')' || ch == '(' || ch == ';' || ch == '=') {
                break;
            }
            this.curPos++;
        }

        for(char ch : line.substring(start+1, this.curPos).toCharArray()) {
//            if ('0' <= ch && ch <= '9' || 'A' <= ch && ch <= 'Z' || 'a' <= ch && ch <= 'z' || ch == '_') {
            if (Character.isDigit(ch) || Character.isLetter(ch) || ch == '_') {
                continue;
            }
            else {
                return new Token(TokenType.ERROR, this.curLinePos, start, this.curPos,
                        "/ID/ Wrong symbol : '" + ch + "' in '" + line.substring(start, this.curPos) +
                                "', expected '0'-'9',  'A'-'Z', 'a'-'z' or '_'");
            }
        }
        return new Token(TokenType.ID, this.curLinePos, start, this.curPos,
                line.substring(start, this.curPos));
    }

    private Token getNumber(int start, String line) {
        this.curPos = start + 1;

        // get position of delimiter
        while (this.curPos < line.length()) {
            char ch = line.charAt(this.curPos);
            if (ch == ' ' || ch == '\t' || ch == ')' || ch == '(' || ch == ';' || ch == '=') {
                break;
            }
            this.curPos++;
        }

        if (line.charAt(start+1) != 'x'){
            return new Token(TokenType.ERROR, this.curLinePos, start, this.curPos,
                    "/Number/ Wrong symbol : '" + line.charAt(start+1) + "', expected 'x'");
        }
        else{
            if(start+2 == this.curPos) {
                return new Token(TokenType.ERROR, this.curLinePos, start, this.curPos,
                        "/Number/ Expected values after '" + line.substring(start, this.curPos) + "'");
            }
            for(char ch : line.substring(start+2, this.curPos).toCharArray()) {
                if ('0' <= ch && ch <= '9' || 'A' <= ch && ch <= 'F') {
                    continue;
                }
                else {
                    return new Token(TokenType.ERROR, this.curLinePos, start, this.curPos,
                            "/Number/ Wrong symbol : '" + ch + "' in '" + line.substring(start, this.curPos) + "', expected '0'-'9' or 'A'-'F'");
                }
            }
            return new Token(TokenType.NUMBER, this.curLinePos, start, this.curPos,
                    line.substring(start, this.curPos));
        }
    }

    private Token getComment(int start, String line) {
        this.curPos = start + 1;
        if (this.curPos < line.length() && line.charAt(this.curPos) == '/') {
            this.curPos = line.length();
            return new Token(TokenType.COMMENT, this.curLinePos, start, this.curPos,
                    line.substring(start, this.curPos));
        }
        return new Token(TokenType.ERROR, this.curLinePos, start, this.curPos,
                "Unexpected symbol : '" + line.charAt(start) + "'");
    }
}
