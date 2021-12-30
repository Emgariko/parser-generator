package regexparser;

import graphviz.GraphVizVisualizer;
import org.junit.jupiter.api.Test;
import regexparser.exception.ParseException;

public class RegexParserTest {

    @Test()
    public void test() {
        String test = "(a)|bc";

        RegexParser parser = new RegexParser();

        try {
            RegexParser.Node parsed = parser.parse(test);
            GraphVizVisualizer.visualize(RegexParser.graphToStr(parsed, test));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
