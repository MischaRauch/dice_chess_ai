package logic.mcts;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.util.List;

import static java.awt.Color.*;

public class SimulationPlotter extends ApplicationFrame {

    /**
     * A demonstration application showing an XY series containing a null value.
     *
     * @param title the frame title.
     */
    public SimulationPlotter(String title, XYSeries results, String yAxis) {
        super(title);
        final XYSeriesCollection data = new XYSeriesCollection(results);
        final JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                "n",
                yAxis,
                data,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(640, 640));
        setContentPane(chartPanel);

    }

    public SimulationPlotter(String title, List<XYSeries> series, String yAxis) {
        this(title, series, yAxis, false);
    }

    public SimulationPlotter(String title, List<XYSeries> series, String yAxis, boolean includesResult) {
        super(title);

        XYSeriesCollection data = new XYSeriesCollection();
        for (XYSeries s : series)
            data.addSeries(s);

        JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                "n",
                yAxis,
                data,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        customizeChart(chart, includesResult);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(640, 640));
        setContentPane(chartPanel);

    }

    private void customizeChart(JFreeChart chart) {   // here we make some customization
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        //chart.getXYPlot().getDataset()

        // sets paint color for each series
        renderer.setSeriesPaint(0, RED);
        renderer.setSeriesPaint(1, GREEN);
        renderer.setSeriesPaint(2, Color.YELLOW);
        renderer.setSeriesPaint(3, Color.CYAN);
        renderer.setSeriesLinesVisible(2, false);

        // sets thickness for series (using strokes)
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        renderer.setSeriesStroke(1, new BasicStroke(2.0f));
        renderer.setSeriesStroke(2, new BasicStroke(3.0f));

        // sets paint color for plot outlines
        plot.setOutlinePaint(Color.BLUE);
        plot.setOutlineStroke(new BasicStroke(2.0f));

        // sets renderer for lines
        plot.setRenderer(renderer);

        // sets plot background
        plot.setBackgroundPaint(Color.DARK_GRAY);

        // sets paint color for the grid lines
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);

    }

    private void customizeChart(JFreeChart chart, boolean includeResult) {   // here we make some customization
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        Color[] colors = new Color[]{YELLOW, RED, GREEN, CYAN, MAGENTA, PINK, ORANGE};

        for (int i = 0; i < plot.getDataset().getSeriesCount(); i++) {
            renderer.setSeriesPaint(i, colors[i]);
            renderer.setSeriesStroke(i, new BasicStroke(2.0f));
        }
        if (includeResult) renderer.setSeriesLinesVisible(0, false);

        // sets paint color for plot outlines
        plot.setOutlinePaint(WHITE);
        plot.setOutlineStroke(new BasicStroke(2.0f));

        // sets renderer for lines
        plot.setRenderer(renderer);

        // sets plot background
        plot.setBackgroundPaint(Color.DARK_GRAY);

        // sets paint color for the grid lines
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);

    }

}