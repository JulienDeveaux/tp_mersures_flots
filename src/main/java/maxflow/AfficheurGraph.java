package maxflow;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.swing_viewer.ViewPanel;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

import javax.swing.*;
import java.awt.*;

public class AfficheurGraph extends JFrame
{
    private Graph graph;

    public AfficheurGraph( Graph graph, String title )
    {
        this.graph = graph;

        this.setTitle(title);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        Viewer viewer = new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        viewer.enableAutoLayout();

        ViewPanel viewPanel = (ViewPanel) viewer.addDefaultView(false);
        this.add(viewPanel);
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(640, 480);
    }

    public void display()
    {
        this.setVisible(true);
    }

    public static AfficheurGraph defaultView(Graph g, String title)
    {
        AfficheurGraph ag = new AfficheurGraph(g, title);

        ag.setSize(800, 600);
        ag.setLocationRelativeTo(null);
        ag.display();

        return ag;
    }

    public static AfficheurGraph defaultViewWithIdTitle(Graph g)
    {
        return defaultView(g, g.getId());
    }
}
