package maxflow;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
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
            if (flow > 0) {
                e.setAttribute("ui.label", flow);
            } else {
                e.setAttribute("ui.label", 0.0);
            }
            if (cap == flow) e.setAttribute("ui.style", "fill-color: red;");
        });

        reseau.display();

        Graph reseauEcart = new SingleGraph("Graph d'écart autoroute");
        reseauEcart.setAttribute("ui.stylesheet", styleSheet);
        for(Node n: reseau.nodes().toList()) {
            reseauEcart.addNode(n.getId()).setAttribute("ui.label", n.getAttribute("ui.label"));
        }
        for(Edge e : reseau.edges().toList()) {
            Node nSource = e.getSourceNode();
            Node nTarget = e.getTargetNode();

            Edge ed = reseauEcart.addEdge(e.getId(), nTarget.getId(), nSource.getId(), true);
            double label = (double)e.getAttribute("ui.label");
            if(label != 0) {
                ed.setAttribute("ui.label", label);
            } else {
                ed.setAttribute("ui.label", "");
            }
            ed.setAttribute("ui.style", "fill-color: red; text-alignment: above; text-color: red;");
            ed.setAttribute("cap", e.getAttribute("cap"));
        }
        for(Edge e : reseauEcart.edges().toList()) {
            double diff = 0;
            if(e.getAttribute("ui.label") == "") {
                diff = 0;
            } else {
                diff = (double)e.getAttribute("cap") - (double)e.getAttribute("ui.label");
            }
            if(diff > 0) {
                Edge ed = reseauEcart.addEdge(""+reseauEcart.getEdgeCount()+1, e.getTargetNode(), e.getSourceNode(), true);
                ed.setAttribute("ui.label", diff);
                ed.setAttribute("ui.style", "fill-color: green; text-alignment: under; text-color: green;");
            }
        }
        reseauEcart.display();
    }

}
