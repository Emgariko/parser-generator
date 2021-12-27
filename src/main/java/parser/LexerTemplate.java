package parser;

import parser.exception.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LexerTemplate {
    private final StringBuilder input;
    private Token curToken = null;
    private final List<TokenPattern> order = new ArrayList<>();
    private final Matcher matcher;

    public enum Token {
        EPS, LB, RB, END;
        private String value;

        private Token setValue(String value) {
            this.value = value;
            return this;
        }

        public String getValue() {
            return value;
        }
    }

    public static class TokenPattern {
        Token token;
        Pattern pattern;

        public TokenPattern(Token token, Pattern pattern) {
            this.token = token;
            this.pattern = pattern;
        }
    }

    private void addToken(Token token, Pattern pattern) {
        order.add(new TokenPattern(token, pattern));
    }

    public LexerTemplate(String input) {
        this.input = new StringBuilder(input);

        addToken(Token.EPS, Pattern.compile("EPS"));
        addToken(Token.LB, Pattern.compile("\\("));
        addToken(Token.RB, Pattern.compile("\\)"));
        matcher = order.get(0).pattern.matcher(input);
    }

    public Token getCurToken() {return curToken;}

    public boolean hasNext() {
        return curToken != Token.END;
    }

    public Token nextToken() throws ParseException {
        if (input.length() == 0) {
            curToken = Token.END;
            return curToken;
        }
        for (TokenPattern tokenPattern : order) {
            matcher.usePattern(tokenPattern.pattern);
            if (matcher.lookingAt()) {
                String parsed = matcher.group();
                curToken = tokenPattern.token.setValue(parsed);
                input.delete(0, matcher.end());
                matcher.reset(input);
                return curToken;
            }
        }
        int pos = matcher.regionStart();
        char c = input.charAt(pos);
        throw new ParseException(String.format("Illegal character %c at position %d", c, pos));
    }
}
