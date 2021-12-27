import grammar.domain.Grammar;
import grammar.parser.GrammarLexer;
import grammar.parser.GrammarParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import parser.Lexer;
import parser.exception.ParseException;

import java.io.IOException;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws IOException {
        /*GrammarLexer grammarLexer = new GrammarLexer(CharStreams.fromFileName("src/main/java/user-grammar.txt"));
        CommonTokenStream commonTokenStream = new CommonTokenStream(grammarLexer);
        GrammarParser grammarParser = new GrammarParser(commonTokenStream);
        Grammar g = new Grammar();
        GrammarParser.GramContext example = grammarParser.gram(g);
        var t = g.getClass();*/
        String test = "()()(())";
        Lexer lexer = new Lexer(test);
        try {
            Lexer.Token token = lexer.nextToken();
            while (lexer.hasNext()) {
                System.out.println(token);
                token = lexer.nextToken();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
