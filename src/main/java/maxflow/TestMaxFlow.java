package maxflow;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.GraphParseException;

import java.io.IOException;

public class TestMaxFlow
{
    protected static String styleSheet =
            "node {" +
                    "   text-size: 20px;" +
                    "   text-alignment: above;" +
                    "   fill-mode: gradient-radial;" +
                    "}" +
                    "edge {" +
                    "   text-alignment: above;" +
                    "   text-size: 20px;" +
                    "}";

    public static void main(String[] args)
    {
        System.setProperty("org.graphstream.ui", "swing");

        Graph g = new SingleGraph("test");
        g.addNode("s");
        g.addNode("1");
        g.addNode("2");
        g.addNode("t");
        g.addEdge("e1", "s", "1", true).setAttribute("cap", 10);
        g.addEdge("e2", "s", "2", true).setAttribute("cap", 10);
        g.addEdge("e3", "1", "2", true).setAttribute("cap", 5);
        g.addEdge("e4", "1", "t", true).setAttribute("cap", 8);
        g.addEdge("e5", "2", "t", true).setAttribute("cap", 7);
        //g.display();

        MaxFlow mf = new MaxFlow();
        mf.setCapacityAttribute("cap");
        mf.init(g);
        mf.setSource(g.getNode("s"));
        mf.setSink(g.getNode("t"));
        mf.compute();

        System.out.println(mf.getFlow());

        g.nodes().forEach(n ->
        {
            n.setAttribute("ui.label", n.getId());
            n.setAttribute("ui.style", "fill-color: blue; text-size: 15px; text-alignment: above;");
        });

        g.edges().forEach( e ->
        {
            double flow = mf.getFlow(e);
            double cap = mf.getCapacity(e);
            if (flow > 0) e.setAttribute("ui.label", "" + flow);
            if (cap == flow) e.setAttribute("ui.style", "fill-color: red;");
        });

        Graph graphCours = new SingleGraph("test cours");
        graphCours.setAttribute("ui.stylesheet", styleSheet);
        try {
            graphCours.read("src/main/resources/exempleCours.dgs");
        } catch (IOException | GraphParseException e) {
            e.printStackTrace();
        }
        mf.init(graphCours);
        mf.setSource(graphCours.getNode("s"));
        mf.setSink(graphCours.getNode("t"));
        mf.compute();

        System.out.println(mf.getFlow());

        graphCours.edges().forEach( e ->
        {
            double flow = mf.getFlow(e);
            double cap = mf.getCapacity(e);
            if (flow > 0) e.setAttribute("ui.label", "" + flow);
            if (cap == flow) e.setAttribute("ui.style", "fill-color: red;");
        });

        //graphCours.display();


        Graph reseau = new SingleGraph("autoroute");
        reseau.setAttribute("ui.stylesheet", styleSheet);
        try {
            reseau.read("src/main/resources/reseau.dgs");
        } catch (IOException | GraphParseException e) {
            e.printStackTrace();
        }

        mf.init(reseau);
        mf.setSource(reseau.getNode("A"));
        mf.setSink(reseau.getNode("I"));
        mf.compute();

        System.out.println(mf.getFlow());

        reseau.edges().forEach( e ->
        {
            double flow = mf.getFlow(e);
            double cap = mf.getCapacity(e);
            if (flow > 0) e.setAttribute("ui.label", "" + flow);
            if (cap == flow) e.setAttribute("ui.style", "fill-color: red;");
        });

        reseau.display();
    }

}
