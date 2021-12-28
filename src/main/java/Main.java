import generator.LexerGenerator;
import generator.ParserGenerator;
import grammar.domain.Grammar;
import grammar.domain.Nonterminal;
import grammar.domain.Terminal;
import grammar.parser.GrammarLexer;
import grammar.parser.GrammarParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import parser.LexerTemplate;
import parser.exception.ParseException;

import java.io.IOException;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        GrammarLexer grammarLexer = new GrammarLexer(CharStreams.fromFileName("src/main/java/user-grammar.txt"));
        CommonTokenStream commonTokenStream = new CommonTokenStream(grammarLexer);
        GrammarParser grammarParser = new GrammarParser(commonTokenStream);
        Grammar g = new Grammar();
        GrammarParser.GramContext example = grammarParser.gram(g);
        var t = g.getClass();
        /*String test = "()()(())";
        LexerTemplate lexerTemplate = new LexerTemplate(test);
        try {
            LexerTemplate.Token token = lexerTemplate.nextToken();
            while (lexerTemplate.hasNext()) {
                System.out.println(token);
                token = lexerTemplate.nextToken();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        /*LexerGenerator lexerGenerator = new LexerGenerator(g);
        lexerGenerator.generate();*/
        ParserGenerator gen = new ParserGenerator(g);
        gen.calc();
        g.printFirst();
        System.out.println();
        g.printFollow();
        System.out.println("test");
        /*String vbar = "|";
        Pattern pat = Pattern.compile("\\|");
        System.out.println(pat.matcher(vbar).matches());*/
    }
}
