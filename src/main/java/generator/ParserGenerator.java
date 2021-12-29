package generator;

import grammar.domain.Grammar;
import grammar.domain.Nonterminal;
import grammar.domain.Rule;
import grammar.domain.Terminal;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;

public class ParserGenerator {
    private final Grammar g;
    public static final String EPS_TOKEN_REGEX = "\"EPS\"";
    private LexerGenerator lexerGenerator;
    private final String pathPrefix = "src/main/java";

    // :TODO: if there is no EPS term in user grammar then it should be mocked.
    public ParserGenerator(Grammar g) {
        this.g = g;
        lexerGenerator = new LexerGenerator(g);
        g.calc();
    }

    private String capitalize(String s) {
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    public void generate() throws IOException {
        lexerGenerator.generate();

        String name = g.getName();
        Path parserDir = Paths.get(pathPrefix, name.toLowerCase() + "parser");
        Files.createDirectories(parserDir);
        String parserFileName = capitalize(name) + "Parser.java";

        try (BufferedWriter writer = Files.newBufferedWriter(parserDir.resolve(parserFileName))) {
            writer.write(parserCode().toString());
        }
    }

    private StringBuilder parserCode() {
        final String name = g.getName();
        final String capitalizedName = capitalize(g.getName());
        final String lexerName = capitalizedName + "Lexer";

        StringBuilder res = new StringBuilder();
        res.append(String.format(
                "package testparser;\n" +  // :TODO: fix it
                        "\n" +
                        "import grammar.domain.Grammar;\n" +
                        "import testparser.exception.ParseException;\n" +   // :TODO: import corresponding package
                        "\n" +
                        "import java.util.ArrayList;\n" +
                        "import java.util.List;\n" +
                        "\n" +
                        "public class %1$sParser {\n" +
                        "    private %1$sLexer lexer;\n" +
                        "\n" +
                        "    public static class Node {\n" +
                        "        private final String name;\n" +
                        "        private final ArrayList<Node> ch = new ArrayList<>();\n" +
                        "\n" +
                        "        public Node(String name) {\n" +
                        "            this.name = name;\n" +
                        "        }\n" +
                        "\n" +
                        "        public String getName() {\n" +
                        "            return name;\n" +
                        "        }\n" +
                        "\n" +
                        "        public void addChild(Node node) {\n" +
                        "            ch.add(node);\n" +
                        "        }\n" +
                        "\n" +
                        "        public ArrayList<Node> getChildren() {\n" +
                        "            return ch;\n" +
                        "        }\n" +
                        "    }\n" +
                        "\n" +
                        "    public TestParser() {\n" +
                        "    }\n\n",
                capitalizedName
        ));

        // node for each nonterm
        for (Nonterminal nonterm : g.getNonterms()) {
            res.append(String.format(
                    "    public static class Node_%1$s extends Node {\n" +
                    "        Node_%1$s() {\n" +
                    "            super(\"%1$s\");\n" +
                    "        }\n" +
                    "        \n",
                    nonterm.getName()
                    ));

            for (Nonterminal.Param ret : nonterm.getRets()) {
                res.append(String.format("        public %s %s;\n", ret.type, ret.name));
            }
            res.append("    }\n\n");
        }

        res.append(String.format("\tpublic Node parse(String s) throws ParseException {\n" +
                   "\t\tlexer = new %2$s(s);\n" +
                   "\t\tlexer.nextToken();\n" +
                   "\t\tNode res = %1$s();\n" +
                   "\t\tif(lexer.getCurToken() != %2$s.Token.END) {\n" +
                   "\t\t\tthrow new ParseException(\"Unexpected token\");\n" +
                   "\t\t}\n" +
                   "\t\treturn res;\n" +
                   "\t}\n\n", g.getStartName(), lexerName));

        // method for each nonterm
        for (Nonterminal nonterm : g.getNonterms()) {
            res.append(String.format(
                    "\tpublic Node_%1$s %1$s() throws ParseException {\n" +
                    "\t\tNode_%1$s res = new Node_%1$s();\n" +
                    "\t\tswitch(lexer.getCurToken()) {\n"
                    , nonterm.getName()));

            for (Rule rule : nonterm.getRules()) {
                HashSet<Terminal> first1 = g.first1(nonterm, rule, 0);
                for (Terminal term: first1) {
                    res.append(String.format(
                            "\t\t\tcase %1$s:\n",
                            term.getName()
                    ));
                }
                res.append("\t\t\t{\n");
                for (Rule.Element el : rule.getEls()) {
                    if (g.terminalsMap.containsKey(el.name)) {
                        Terminal ruleTerm = g.terminalsMap.get(el.name);

                        // :TODO: EPS case
                        // :TODO: case when there are no EPS in grammar ?

                        if (!checkEps(ruleTerm)) {
                            res.append(String.format(
//                                "\t\t\t{\n" +
                                    "\t\t\t\tif (lexer.getCurToken() != %1$s.Token.%2$s) {\n" +
                                            "\t\t\t\t\tthrow new ParseException(\"Expected another token.\");\n" +
                                            "\t\t\t\t}\n"
//                                "\t\t\t}\n"
                                    , lexerName, ruleTerm.getName()
                            ));
                            res.append(String.format(
//                                "\t\t\t\tres.addChild(new Node(\"%1$s\"));\n",
                                    "\t\t\t\tres.addChild(new Node(lexer.getCurToken().getValue()));\n" +
                                    "\t\t\t\tlexer.nextToken();\n",
                                    ruleTerm.getName()
                            ));
                        } else {
                            res.append(String.format(
//                                "\t\t\t\tres.addChild(new Node(\"%1$s\"));\n",
                                    "\t\t\t\tres.addChild(new Node(\"EPS\"));\n",
                                    ruleTerm.getName()
                            ));
                        }
                    } else if (g.nontermsMap.containsKey(el.name)) {
                        Nonterminal ruleNonterm = g.nontermsMap.get(el.name);
                        res.append(String.format(
                                "\t\t\t\tres.addChild(%1$s());\n",
                                el.name
                                ));
                    } else {
                        throw new RuntimeException("Not terminal and nonterminal in rule description");
                    }
                }
                res.append("\t\t\t\treturn res;\n");
                res.append("\t\t\t}\n");
            }
            res.append("\t\t\tdefault:\n" +
                    "\t\t\t\tthrow new ParseException(\"Unexpected token\");\n");
            res.append(String.format(
                    "\t\t}\n" +
                    "\t}\n\n"
                    ));
        }


        res.append("}\n");
        return res;
    }

    private boolean checkEps(String s) {
        return checkEps(g.terminalsMap.get(s));
    }

    private boolean checkEps(Terminal term) {
        return term.getRegex().equals(EPS_TOKEN_REGEX);
    }



}
