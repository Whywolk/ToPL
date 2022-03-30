package org.whywolk.topl.lexic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Solver {
    private String doc;
    private ArrayList<String> lines;
    private ArrayList<Token> tokens = new ArrayList<>();

    public Solver(String doc) {
        this.doc = doc;
    }

    public void start() {
        // split doc into lines
        this.lines = new ArrayList<>(Arrays.asList(this.doc.split("\\n+")));

        Pattern space = Pattern.compile("\\s+");
        for(int i = 0; i < this.lines.size(); i++) {
            int curPos = 0;
            String cur_line = this.lines.get(i);
            ArrayList<Token> curTokens = new ArrayList<>();

            Matcher space_m = space.matcher(cur_line);
            while (curPos < cur_line.length()) {

                // If found space, then change curPos
                if(space_m.find(curPos)) {
                    if(curPos == space_m.start()) {
                        curPos = space_m.end();
                        continue;
                    }
                }

                // determine comments
                {
                    TokenType[] types = {TokenType.COMMENT};
                    Token token = getToken(types, curPos, cur_line, i);
                    if (token.getType() != null) {
                        this.tokens.add(token);
                        break;
                    }
                }

                // First should be ID
                if((curTokens.size() == 0)
                        || (curTokens.get(curTokens.size() - 1).getType() == TokenType.END)) {
                    TokenType[] types = {TokenType.ID, TokenType.END};
                    Token token = getToken(types, curPos, cur_line, i);
                    curTokens.add(token);
                    curPos = token.getEnd();
                    continue;
                }

                // Then Assignation
                Token lastToken = curTokens.get(curTokens.size() - 1);
                if((curTokens.size() == 1 || curTokens.get(curTokens.size() - 2).getType() == TokenType.END)
                        && (lastToken.getType() == TokenType.ID )) {
                    TokenType[] types = {TokenType.ASSIGN};
                    Token token = getToken(types, curPos, cur_line, i);
                    curTokens.add(token);
                    curPos = token.getEnd();
                    continue;
                }

                // Then ID, (, NEG or NUM
                if((curTokens.size() == 2 || curTokens.get(curTokens.size() - 3).getType() == TokenType.END)
                        && lastToken.getType() == TokenType.ASSIGN) {
                    TokenType[] types = {TokenType.BRACKET_L, TokenType.NEGATIVE, TokenType.ID, TokenType.NUMBER};
                    Token token = getToken(types, curPos, cur_line, i);
                    curTokens.add(token);
                    curPos = token.getEnd();
                    continue;
                }

                // After ( should be (, ID, NUM or NEG
                if(lastToken.getType() == TokenType.BRACKET_L) {
                    TokenType[] types = {TokenType.BRACKET_L, TokenType.ID, TokenType.NUMBER, TokenType.NEGATIVE};
                    Token token = getToken(types, curPos, cur_line, i);
                    curTokens.add(token);
                    curPos = token.getEnd();
                    continue;
                }

                // After ID or NUM should be OP, ), ;
                if(lastToken.getType() == TokenType.ID
                        || lastToken.getType() == TokenType.NUMBER) {
                    TokenType[] types = {TokenType.OPERATION, TokenType.BRACKET_R, TokenType.END};
                    Token token = getToken(types, curPos, cur_line, i);
                    curTokens.add(token);
                    curPos = token.getEnd();
                    continue;
                }

                // After OP or NEG should be ID, NUM or (
                if(lastToken.getType() == TokenType.OPERATION
                        || lastToken.getType() == TokenType.NEGATIVE) {
                    TokenType[] types = {TokenType.ID, TokenType.NUMBER, TokenType.BRACKET_L};
                    Token token = getToken(types, curPos, cur_line, i);
                    curTokens.add(token);
                    curPos = token.getEnd();
                    continue;
                }

                // After ID, NUM or ) can be ;
                if(lastToken.getType() == TokenType.ID
                        || lastToken.getType() == TokenType.NUMBER
                        || lastToken.getType() == TokenType.BRACKET_R) {
                    TokenType[] types = {TokenType.END};
                    Token token = getToken(types, curPos, cur_line, i);
                    curTokens.add(token);
                    curPos = token.getEnd();
                }
            }
            this.tokens.addAll(curTokens);
        }
    }



    public ArrayList<Token> getTokens () {
        return this.tokens;
    }

    public enum TokenType {
        ID("[A-Za-z_][A-Za-z0-9_]*"),
        COMMENT("\\/\\/.+|\\/\\/"),
        ASSIGN("="),
        OPERATION("and|xor|or"),
        NEGATIVE("not"),
        END(";"),
        NUMBER("0x[0-9a-fA-F]+"),
        BRACKET_L("\\("),
        BRACKET_R("\\)");

        private final String regex;

        TokenType(String regex) {
            this.regex = regex;
        }

        public String getRegex() {
            return regex;
        }
    }

    private void next(boolean condition, TokenType[] types) {

    }

    private Token getToken(TokenType[] types, int cur_pos, String cur_line, int line_pos) {
        for(TokenType type : types) {
            Pattern p = Pattern.compile(type.getRegex());
            Matcher m = p.matcher(cur_line);
            if (m.find(cur_pos) && m.start() == cur_pos) {
                Token token = new Token(type, line_pos, m.start(), m.end(), m.group());
                return token;
            }
        }
        return new Token();
    }
}
