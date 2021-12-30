package graphviz;

import java.io.File;

public class GraphVizVisualizer {

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

    public static void visualize(String graph) {
        createDotGraph(graph, "DotGraph");
    }
}
