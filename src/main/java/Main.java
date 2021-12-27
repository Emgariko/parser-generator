import parser.LexerTemplate;
import parser.exception.ParseException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        /*GrammarLexer grammarLexer = new GrammarLexer(CharStreams.fromFileName("src/main/java/user-grammar.txt"));
        CommonTokenStream commonTokenStream = new CommonTokenStream(grammarLexer);
        GrammarParser grammarParser = new GrammarParser(commonTokenStream);
        Grammar g = new Grammar();
        GrammarParser.GramContext example = grammarParser.gram(g);
        var t = g.getClass();*/
        String test = "()()(())";
        LexerTemplate lexerTemplate = new LexerTemplate(test);
        try {
            LexerTemplate.Token token = lexerTemplate.nextToken();
            while (lexerTemplate.hasNext()) {
                System.out.println(token);
                token = lexerTemplate.nextToken();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
