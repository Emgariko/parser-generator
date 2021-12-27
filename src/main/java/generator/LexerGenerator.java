package generator;

import grammar.domain.Grammar;
import grammar.domain.Terminal;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class LexerGenerator {
    private final Grammar g;
    private final String pathPrefix = "src/main/java";

    public LexerGenerator(Grammar g) {
        this.g = g;
    }

    public void generate() throws IOException {
        String name = g.getName();
        Path lexerDir = Paths.get(pathPrefix, name.toLowerCase() + "parser");
        Files.createDirectories(lexerDir);
        String lexerFileName = capitalize(name) + "Lexer.java";

        Path excDir = Paths.get(pathPrefix, name.toLowerCase() + "parser", "exception");
        Files.createDirectories(excDir);
        String excFileName = "ParseException.java";

        try (BufferedWriter writer = Files.newBufferedWriter(excDir.resolve(excFileName))) {
            writer.write(excCode().toString());
        }

        try (BufferedWriter writer = Files.newBufferedWriter(lexerDir.resolve(lexerFileName))) {
            writer.write(lexerCode().toString());
        }
    }

    private StringBuilder excCode() {
        StringBuilder res = new StringBuilder();
        final String name = g.getName();
        final String package_ = name + "parser.exception";

        res.append(String.format(
                    "package %s;\n" +
                    "\n" +
                    "public class ParseException extends Exception {\n" +
                    "    public ParseException(String message) {\n" +
                    "        super(message);\n" +
                    "    }\n" +
                    "}\n",
                    package_
                )
        );

        return res;
    }

    private String capitalize(String s) {
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    private StringBuilder lexerCode() {
        StringBuilder res = new StringBuilder();
        final String name = g.getName().toLowerCase();
        final String capitalizedName = capitalize(name);

        final String package_ = name + "parser";
        res.append(String.format(
                "package %1$s;\n" +
                "\n" +
                "import %1$s.exception.ParseException;\n" +
                "import java.util.ArrayList;\n" +
                "import java.util.List;\n" +
                "import java.util.regex.Matcher;\n" +
                "import java.util.regex.Pattern;\n" +
                "\n" +
                "public class %2$sLexer {\n" +
                "    private final StringBuilder input;\n" +
                "    private Token curToken = null;\n" +
                "    private final List<TokenPattern> order = new ArrayList<>();\n" +
                "    private final Matcher matcher;\n" +
                "\n" +
                "%3$s" + // enum Token
                "\n" +
                "    public static class TokenPattern {\n" +
                "        Token token;\n" +
                "        Pattern pattern;\n" +
                "\n" +
                "        public TokenPattern(Token token, Pattern pattern) {\n" +
                "            this.token = token;\n" +
                "            this.pattern = pattern;\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    private void addToken(Token token, Pattern pattern) {\n" +
                "        order.add(new TokenPattern(token, pattern));\n" +
                "    }\n" +
                "\n" +
                "%4$s" + // Lexer constructor
                "\n" +
                "    public Token getCurToken() {return curToken;}\n" +
                "\n" +
                "    public boolean hasNext() {\n" +
                "        return curToken != Token.END;\n" +
                "    }\n" +
                "\n" +
                "    public Token nextToken() throws ParseException {\n" +
                "        if (input.length() == 0) {\n" +
                "            curToken = Token.END;\n" +
                "            return curToken;\n" +
                "        }\n" +
                "        for (TokenPattern tokenPattern : order) {\n" +
                "            matcher.usePattern(tokenPattern.pattern);\n" +
                "            if (matcher.lookingAt()) {\n" +
                "                String parsed = matcher.group();\n" +
                "                curToken = tokenPattern.token.setValue(parsed);\n" +
                "                input.delete(0, matcher.end());\n" +
                "                matcher.reset(input);\n" +
                "                return curToken;\n" +
                "            }\n" +
                "        }\n" +
                "        int pos = matcher.regionStart();\n" +
                "        char c = input.charAt(pos);\n" +
                "        throw new ParseException(String.format(\"Illegal character %%c at position %%d\", c, pos));\n" +
                "    }\n" +
                "}\n"
                ,

                package_, capitalizedName, tokenEnumCode(), lexerConstructorCode(capitalizedName).toString())
        );
        return res;
    }

    private StringBuilder tokenEnumCode() {
        StringBuilder res = new StringBuilder();
        StringBuilder values = new StringBuilder();
        ArrayList<Terminal> terms = g.getTerminals();
        for (int i = 0, n = terms.size(); i < n; i++) {
            Terminal term = terms.get(i);
            if (i == n - 1) {
                values.append(term.getName());
            } else {
                values.append(term.getName()).append(", ");
            }
        }
        if (terms.size() != 0) {
            values.append(", END");
        } else {
            values.append("END");
        }

        res.append(
                String.format(
                        "    public enum Token {\n" +
                        "        %1$s;\n" + // Terminals - enum values
                        "        private String value;\n" +
                        "\n" +
                        "        private Token setValue(String value) {\n" +
                        "            this.value = value;\n" +
                        "            return this;\n" +
                        "        }\n" +
                        "\n" +
                        "        public String getValue() {\n" +
                        "            return value;\n" +
                        "        }\n" +
                        "    }\n"
                        , values
                        )
        );

        return res;
    }

    private StringBuilder lexerConstructorCode(String capitalizedName) {
        StringBuilder res = new StringBuilder();
        StringBuilder addTokens = new StringBuilder();
        ArrayList<Terminal> terms = g.getTerminals();
        for (Terminal term : terms) {
            addTokens.append(String.format(
                    "        addToken(Token.%1$s, Pattern.compile(%2$s));"
                    , term.getName(), term.getRegex()));
            addTokens.append("\n");
        }


        res.append(
                String.format(
                        "    public %1$sLexer(String input) {\n" +
                        "        this.input = new StringBuilder(input);\n" +
                        "%2$s" +
                        "        matcher = order.get(0).pattern.matcher(input);\n" +
                        "    }\n"
                        , capitalizedName, addTokens
                        )
        );

        return res;
    }
}
