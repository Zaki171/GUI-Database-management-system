import javax.swing.*;
import java.awt.*;

public class DrawGraph extends JPanel {
    private int[] data;
    public static final int xaxisY = 600;
    public static final int yAxisLen = 550;
    public static final int yaxisX = 50;

    public DrawGraph(int[] data){
        this.data = data;
        this.setLayout(new GridLayout());
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g){
        Graphics2D g2D = (Graphics2D)g;
        double interval = chooseInterval();
        doAxis(g2D,interval);
        int[] yValues = groupAges();
        double scale = yAxisLen/(interval*10);
        g2D.setColor(Color.BLUE);
        g2D.drawRect(50, (int) (xaxisY-(yValues[0]*scale)),150, (int) (yValues[0]*scale));
        g2D.fillRect(50, (int) (xaxisY-(yValues[0]*scale)),150, (int) (yValues[0]*scale));
        g2D.setColor(Color.RED);
        g2D.drawRect(200, (int) (xaxisY-(yValues[1]*scale)),150, (int) (yValues[1]*scale));
        g2D.fillRect(200, (int) (xaxisY-(yValues[1]*scale)),150, (int) (yValues[1]*scale));
        g2D.setColor(Color.YELLOW);
        g2D.drawRect(350, (int) (xaxisY-(yValues[2]*scale)),150, (int) (yValues[2]*scale));
        g2D.fillRect(350, (int) (xaxisY-(yValues[2]*scale)),150, (int) (yValues[2]*scale));
        g2D.setColor(Color.GREEN);
        g2D.drawRect(500, (int) (xaxisY-(yValues[3]*scale)),150, (int) (yValues[3]*scale));
        g2D.fillRect(500, (int) (xaxisY-(yValues[3]*scale)),150, (int) (yValues[3]*scale));
        xAxisLabels(g2D);
    }

    private void xAxisLabels(Graphics2D g2D){
        g2D.setColor(Color.WHITE);
        g2D.drawString("0-25",125,625);
        g2D.drawString("25-50",275,625);
        g2D.drawString("50-75",425,625);
        g2D.drawString("75+",575,625);
        g2D.drawString("Age",350,645);
    }

    private int chooseInterval(){
        int interval;
        if(data.length<1000){
            interval = 10;
        }
        else if(data.length>1000 && data.length<10000){
            interval = 100;
        }
        else if(data.length>10000 && data.length<100000){
            interval = 1000;
        }
        else{
            interval = 10000;
        }
        return interval;
    }

    private int[] groupAges(){
        int[] yValues = new int[4];
        for(int val : data){
            if(val>=0 && val<25){
                yValues[0]++;
            }
            if(val>=25 && val<50){
                yValues[1]++;
            }
            if(val>=50 && val<75){
                yValues[2]++;
            }
            if(val >= 75){
                yValues[3]++;
            }
        }
        return yValues;
    }

    private void doAxis(Graphics2D g2D, double interval){
        g2D.setColor(Color.WHITE);
        g2D.drawString("No. people",5,20);
        g2D.drawLine(yaxisX-1,50,yaxisX-1,xaxisY);
        g2D.drawLine(yaxisX-1,xaxisY,650,xaxisY);
        int counter = 0;
        int y =xaxisY;
        while(counter <= interval*10){
            g2D.drawString(String.format("%d",counter),5,y);
            y = y - (yAxisLen/10);
            counter = (int) (counter + interval);
        }

    }

}
