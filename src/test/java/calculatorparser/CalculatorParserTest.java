package calculatorparser;

import calculatorparser.exception.ParseException;
import graphviz.GraphVizVisualizer;
import org.junit.jupiter.api.Test;

public class CalculatorParserTest {
    @Test
    public void test() {
        String test = "2+2*2*2";

        CalculatorParser parser = new CalculatorParser();

        try {
            CalculatorParser.Node parsed = parser.parse(test);
            GraphVizVisualizer.visualize(CalculatorParser.graphToStr(parsed, test));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
