package tsp;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;

/**
 * GraphPlotter helps visualize TSP algorithm performance (GA vs DP)
 */
public class GraphPlotter {

    private XYSeriesCollection dataset;
    private String chartTitle;

    public GraphPlotter(String chartTitle) {
        this.chartTitle = chartTitle;
        this.dataset = new XYSeriesCollection();
    }

    public void addSeries(String name, double[] xValues, double[] yValues) {
        if (xValues.length != yValues.length)
            throw new IllegalArgumentException("X and Y arrays must be same length");

        XYSeries series = new XYSeries(name);
        for (int i = 0; i < xValues.length; i++) {
            series.add(xValues[i], yValues[i]);
        }
        dataset.addSeries(series);
    }

    public void displayChart() {
        SwingUtilities.invokeLater(() -> {
            JFreeChart chart = ChartFactory.createXYLineChart(
                    chartTitle,
                    "NFC",
                    "Distance",
                    dataset,
                    PlotOrientation.VERTICAL,
                    true,  // legend
                    true,  // tooltips
                    false  // URLs
            );

            JFrame frame = new JFrame(chartTitle);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new ChartPanel(chart));
            frame.pack();
            frame.setLocationRelativeTo(null); // center the window
            frame.setVisible(true);
        });
    }
}
