import javax.swing.*;
import java.awt.*;

public class ChartPanel extends JFrame {

    public ChartPanel(int[] data){
        DrawGraph graph = new DrawGraph(data);
        graph.setSize(650,700);
        graph.setBackground(Color.BLACK);
        this.add(graph);
        setResizable(false);
        getContentPane().setBackground(Color.BLACK);
        setPreferredSize(new Dimension(650,700));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }
}
