package maxflow;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.swing_viewer.ViewPanel;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

import javax.swing.*;
import java.awt.*;

public class AfficheurGraph extends JPanel
{
    private Graph graph;

    public AfficheurGraph( Graph graph, String title )
    {
        this.graph = graph;

        this.setLayout(new BorderLayout());

        Viewer viewer = new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        viewer.enableAutoLayout();

        ViewPanel viewPanel = (ViewPanel) viewer.addDefaultView(false);
        this.add(viewPanel);
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(600, 600);
    }

    public static JFrame defaultView(Graph g, String title)
    {
        JFrame frame = new JFrame(title);
        AfficheurGraph ag = new AfficheurGraph(g, title);

        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(ag);

        frame.setVisible(true);

        return frame;
    }

    public static JFrame defaultViewWithIdTitle(Graph g)
    {
        return defaultView(g, g.getId());
    }
}
