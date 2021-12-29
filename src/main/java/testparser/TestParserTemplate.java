package testparser;

import grammar.domain.Grammar;
import testparser.exception.ParseException;

import java.util.ArrayList;
import java.util.List;

public class TestParser {
    private TestLexer lexer;
    private Grammar g;

    public static class Node {
        private final String name;
        private final List<Node> ch = new ArrayList<>();

        public Node(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void addChild(Node node) {
            ch.add(node);
        }
    }

    public TestParser(Grammar g) {
        this.g = g;
    }

    public static class Node_r extends Node {
        Node_r() {
            super("r");
        }
        // return value
    }

    public Node parse(String s) throws ParseException {
        lexer = new TestLexer(s);
        lexer.nextToken();
        Node res = r();
        return res;
    }

    public Node_r r() throws ParseException {
        Node_r res = new Node_r();
        switch (lexer.getCurToken()) {
            case
        }
    }

}
