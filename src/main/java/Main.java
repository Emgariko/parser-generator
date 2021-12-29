import generator.ParserGenerator;
import grammar.domain.Grammar;
import grammar.parser.GrammarLexer;
import grammar.parser.GrammarParser;
//import graphviz.GraphVizVisualizer;
import graphviz.GraphVizVisualizer;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import testparser.TestParser;
import testparser.exception.ParseException;
//import testparser.TestParser;
//import testparser.exception.ParseException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        GrammarLexer grammarLexer = new GrammarLexer(CharStreams.fromFileName("src/main/java/user-grammar.txt"));
        CommonTokenStream commonTokenStream = new CommonTokenStream(grammarLexer);
        GrammarParser grammarParser = new GrammarParser(commonTokenStream);
        Grammar g = new Grammar();
        GrammarParser.GramContext example = grammarParser.gram(g);

        /*ParserGenerator parserGenerator = new ParserGenerator(g);
        parserGenerator.generate();*/

        String test = "a|b(c)";

        TestParser parser = new TestParser();

        try {
            TestParser.Node parsed = parser.parse(test);
            GraphVizVisualizer.visualize(parsed, test);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        /*LexerGenerator lexerGenerator = new LexerGenerator(g);
        lexerGenerator.generate();*/

        /*String test = "() | ()";
        TestLexer lexer = new TestLexer(test);
        try {
            TestLexer.Token token = lexer.nextToken();
            while (lexer.hasNext()) {
                System.out.println(token);
                token = lexer.nextToken();
            }
        } catch (testparser.exception.ParseException e) {
            e.printStackTrace();
        }*/
        /*ParserGenerator gen = new ParserGenerator(g);
        gen.calc();
        g.printFirst();
        System.out.println();
        g.printFollow();
        System.out.println("test");*/
        /*String vbar = "|";
        Pattern pat = Pattern.compile("\\|");
        System.out.println(pat.matcher(vbar).matches());*/

    }
}
