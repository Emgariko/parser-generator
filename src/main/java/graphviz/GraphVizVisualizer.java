package graphviz;

import testparser.TestParser;

import java.io.File;

public class GraphVizVisualizer {
    private static int ind;

    public static void createDotGraph(String dotFormat,String fileName) {
        GraphViz gv = new GraphViz();
        gv.addln(gv.start_graph());
        gv.add(dotFormat);
        gv.addln(gv.end_graph());
        String type = "pdf";
        gv.decreaseDpi();
        gv.decreaseDpi();
        File out = new File(fileName+"."+ type);
        gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), type ), out );
    }

    private static String getFillColor(TestParser.Node node) {
        String res = "white";
        if (node.getChildren().size() == 0) {
            res = "darkgrey";
        }
        return res;
    }

    private static void buildGraph(TestParser.Node u, StringBuilder res) {
        String fillColor = getFillColor(u);

        res.append(ind).append(" [label=\"").append(u.getName()).append("\"").append(", style=filled")
                .append(", fillcolor=").append(fillColor).append("]");
        int curInd = ind++;
        for (TestParser.Node ch : u.getChildren()) {
            res.append("\n");
            res.append(curInd).append(" -> ").append(ind).append(';');
            res.append("\n");
            buildGraph(ch, res);
        }
    }

    private static String graphToStr(TestParser.Node root, String srcExpr) {
        StringBuilder res = new StringBuilder("label = \"Parse tree\n" + srcExpr + "\"\n");
        buildGraph(root, res);
        return res.toString();
    }

    public static void visualize(TestParser.Node root, String srcExpr) {
        ind = 0;
        String dotFormat = graphToStr(root, srcExpr);
        createDotGraph(dotFormat, "DotGraph");
    }
}
