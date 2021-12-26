import grammar.domain.Grammar;
import grammar.parser.GrammarLexer;
import grammar.parser.GrammarParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        GrammarLexer grammarLexer = new GrammarLexer(CharStreams.fromFileName("src/main/java/parser/sample.txt"));
        CommonTokenStream commonTokenStream = new CommonTokenStream(grammarLexer);
        GrammarParser grammarParser = new GrammarParser(commonTokenStream);
        Grammar g = new Grammar();
        GrammarParser.StartContext example = grammarParser.start(g);
    }
}
