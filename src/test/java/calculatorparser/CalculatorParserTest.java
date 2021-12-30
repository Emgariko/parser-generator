/*
package calculatorparser;

import calculatorparser.exception.ParseException;
import graphviz.GraphVizVisualizer;
import org.junit.jupiter.api.Test;

public class CalculatorParserTest {
    @Test
    public void test() {
        String test = "4/2/1+(1+2+3)";

        CalculatorParser parser = new CalculatorParser();

        try {
            CalculatorParser.Node parsed = parser.parse(test);
            GraphVizVisualizer.visualize(CalculatorParser.graphToStr(parsed, test));
            var res = (CalculatorParser.Node_e) parsed;
            System.out.println(res.x);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
*/
