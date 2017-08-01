

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.util.Random;



class Plot extends ApplicationFrame {

    public double N(double x){
        if(Math.abs(x)<0.0000000001){
            return 0.5;
        }
        double val=0;
        double coef=Math.sqrt(1/(2*3.141592));
        boolean negative=false;
        if (x<0){
            x=-x;
            negative=true;
        }
        double xx=x,L=x-3;
        if(x>3){
            x=3;
        }
        double steps=100000;
        double step=x/steps;
        for(int i=0;i<steps;i++){
            double y=step*i;
            val+=Math.exp(-0.5*y*y);
        }
        val*=coef*step;
        if(xx>3) {
            double val2=0;
            step = L / steps;
            for (int i = 0; i < steps; i++) {
                double y = 3+step * i;
                val2 += Math.exp(-0.5 * y * y);
            }
            val2*=coef*step;
            if(L<10) {
                val += val2;
            }
            else{
                val=0.500;
            }
        }

        if(negative){
            return 0.5-val;
        }
        else {
            return 0.5+val;
        }
    }
    public double Call(double S,double K,double r,double sigma,double t){
        double val=0;
        double d1=(Math.log(S/K)+(r+sigma*sigma*0.5)*t)/(sigma*Math.sqrt(t));
        double d2=d1-sigma*Math.sqrt(t);
        double Nd1=N(d1);
        double Nd2=N(d2);
        double Fwd=K*Math.exp(-r*t);
        val=S*N(d1)-Fwd*N(d2);
        return val;
    }
    public double Put(double S,double K,double r,double sigma,double t){
        double Fwd=K*Math.exp(-r*t);
        double val=Fwd+Call( S, K, r, sigma, t)-S;
        return val;
    }
    public Plot(final String title)
    {
        super(title);
        final XYSeries series = new XYSeries("Random Data");
        int cnt = 0;
        Random r=new Random();
        float w=0;
        double t=100;
        int steps=100;
        double step=t/(double)steps;
        while (cnt < steps) {
            /*double t=r.nextGaussian();
            w+=t;
            double z=Math.exp(w);*/
            double S=cnt*step;
            if(S<0.001){
                S=0.00001;
            } 
            double y=Call(50,50,S/t,0.3,1);
            series.add(S,y);//Random.nextGaussian
            cnt++;
        }
        final XYSeriesCollection data = new XYSeriesCollection(series);
        final JFreeChart chart = ChartFactory.createXYLineChart(
                "XY Series Demo",
                "X",
                "Y",
                data,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);

    }
}

public class Solution {

    public static void main(final String[] args) {

        Plot demo = new Plot("XY Series Demo");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

}
