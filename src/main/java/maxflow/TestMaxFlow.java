package maxflow;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.GraphParseException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

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

    public static void main(String[] args) throws IOException, GraphParseException
    {
        System.setProperty("org.graphstream.ui", "swing");

        testAndExempleCours();

        Graph reseau = new SingleGraph("Autoroute");
        reseau.setAttribute("ui.stylesheet", styleSheet);

        reseau.read("src/main/resources/reseau.dgs");

        MaxFlow mf = new MaxFlow();

        double flowAI = getFlowMaxBetweenAandI(reseau, mf);

        System.out.println(flowAI);

        reseau.edges().forEach( e ->
        {
            double flow = mf.getFlow(e);
            double cap = mf.getCapacity(e);

            e.setAttribute("ui.label", flow > 0 ? flow : 0.0);

            if (cap == flow) e.setAttribute("ui.style", "fill-color: red;");
        });

        brutForceX4(reseau, 4);
        //AfficheurGraph.defaultViewWithIdTitle(reseau);

        Graph reseauEcart = new SingleGraph("Graph d'écart autoroute");
        reseauEcart.setAttribute("ui.stylesheet", styleSheet);

        reseau.nodes().forEach(n -> reseauEcart.addNode(n.getId()).setAttribute("ui.label", n.getAttribute("ui.label")));

        reseau.edges().forEach( e ->
        {
            Node nSource = e.getSourceNode();
            Node nTarget = e.getTargetNode();

            Edge ed = reseauEcart.addEdge(e.getId(), nTarget.getId(), nSource.getId(), true);
            double label = (double) e.getAttribute("ui.label");

            ed.setAttribute("ui.label", label != 0 ? label : null);

            ed.setAttribute("ui.style", "fill-color: red; text-alignment: above; text-color: red;");
            ed.setAttribute("cap", e.getAttribute("cap"));
        });

        reseauEcart.edges().forEach(e ->
        {
            double diff = 0;

            if(e.getAttribute("ui.label") != null)
                diff = (double) e.getAttribute("cap") - (double) e.getAttribute("ui.label");

            if(diff > 0)
            {
                Edge ed = reseauEcart.addEdge(String.valueOf(reseauEcart.getEdgeCount()+1), e.getTargetNode(), e.getSourceNode(), true);

                ed.setAttribute("ui.label", diff);
                ed.setAttribute("ui.style", "fill-color: green; text-alignment: under; text-color: green;");
            }
        });

        JFrame frameAutoroute = new JFrame("autoroute & graph écart");
        frameAutoroute.add(BorderLayout.WEST, new AfficheurGraph(reseau, "Autoroute"));
        frameAutoroute.add(BorderLayout.EAST, new AfficheurGraph(reseauEcart, "graph écart"));

        frameAutoroute.setResizable(false);
        frameAutoroute.setSize(1213, 600);
        frameAutoroute.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameAutoroute.setLocationRelativeTo(null);
        frameAutoroute.setVisible(true);

        //AfficheurGraph.defaultView(reseauEcart, "Réseau écart (Autoroute)");
    }

    private static double getFlowMaxBetweenAandI(Graph g, MaxFlow mf)
    {
        mf.init(g);
        mf.setSource(g.getNode("A"));
        mf.setSink(g.getNode("I"));
        mf.compute();

        return mf.getFlow();
    }

    /**
     * Tests chacun des cas avec la solution optimal.
     * Le nombre d'arrete de passé est le nb max de liens du tableau qui seront gardée après chaque exécution.
     * <br/><br/>
     * A chaques nouvelle itération, le lien qui donne le flow max est gardée et supprimer des liens possible pour l'itération suivante
     * @param g le graph qui seras utilisé ET modifier lors de l'éxécution
     * @param nbArrete le nombre de nouveaux lien à gardée
     */
    private static void brutForceX4(Graph g, int nbArrete)
    {
        String[] nvRoutes = new String[]{ "A_C", "A_H", "A_D", "A_I", "C_D", "C_H", "C_I", "D_H", "D_I", "H_I" };

        nbArrete = Math.min(Math.max(nbArrete, 0), nvRoutes.length);

        String[][] liensOpti = new String[nbArrete][2];

        for (int i = 0; i < nbArrete; i++)
        {
            double[] max = brutForce(g, nvRoutes);

            liensOpti[i][0] = nvRoutes[(int) max[0]];
            liensOpti[i][1] = String.valueOf(max[1]);

            String[] route = liensOpti[i][0].split("_");

            Edge e = g.getEdge(route[0] + "-" + route[1]);

            boolean isUpdate = e != null;

            if(isUpdate)
            {
                double ancienne = (double) e.getAttribute("cap");

                e.setAttribute("cap", ancienne + 2500);
                e.setAttribute("ui.label", ancienne + 2500);
            }
            else
            {
                e = g.addEdge(nvRoutes[i], route[0], route[1], true);
                e.setAttribute("cap", 2500.0);
                e.setAttribute("ui.label", 2500.0);
            }

            int finalI = i;
            nvRoutes = Arrays.stream(nvRoutes).filter(s -> !s.equals(liensOpti[finalI][0])).toList().toArray(new String[0]);
        }

        System.out.println("--- résulats ---");

        for (String[] tab : liensOpti)
            System.out.println(tab[0] + " : " + tab[1]);
    }

    /**
     *
     * @param g le graph sur lequel les liens seront ajouter (temporairement)
     * @param nvRoutes un tableau des nouvelles routes (ou maj si existante) chaque string sous forme ID1_ID2 ou ID1 represent l'id su noeud source
     * @return un tableau à 2 case, la première donne l'indice de la route max dans le tableau et la seconde la valeur du flow max avec cette route
     */
    private static double[] brutForce(Graph g, String[] nvRoutes)
    {
        double[] nvValues = new double[nvRoutes.length];

        for (int i = 0; i < nvRoutes.length; i++)
        {
            String[] route = nvRoutes[i].split("_");

            Edge e = g.getEdge(route[0] + "-" + route[1]);

            boolean isUpdate = e != null;

            if(isUpdate)
            {
                double ancienne = (double) e.getAttribute("cap");

                e.setAttribute("cap", ancienne + 2500);
            }
            else
            {
                e = g.addEdge(nvRoutes[i], route[0], route[1], true);
                e.setAttribute("cap", 2500.0);
            }

            nvValues[i] = getFlowMaxBetweenAandI(g, new MaxFlow());

            if(isUpdate)
            {
                double nouvelle = (double) e.getAttribute("cap");

                e.setAttribute("cap", nouvelle - 2500);
            }
            else
            {
                g.removeEdge(e);
            }
        }

        double max = Arrays.stream(nvValues).max().orElse(0);
        int indice = 0;

        for (int i = 0; i < nvValues.length; i++)
            if(nvValues[i] == max)
                indice = i;

        return new double[]{ indice, max };
    }

    private static void testAndExempleCours() throws IOException, GraphParseException
    {
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

        graphCours.read("src/main/resources/exempleCours.dgs");

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
    }
}
