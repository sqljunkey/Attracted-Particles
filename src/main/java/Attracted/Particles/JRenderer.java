package Attracted.Particles;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import net.miginfocom.swing.MigLayout;

public class JRenderer extends JFrame {

	JPanel jpanel = new JPanel();
	XYPlot plot;

	public JRenderer(String title) {

		// initVariables();

		windowComponents();

		this.add(jpanel);
		setSize(2000, 1800);

		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		pack();
		setVisible(true);

	}

	public void windowComponents() {
		jpanel.setLayout(new MigLayout("", "[]",
				"[]"));

		//// Add Scatter Plot

		jpanel.add(createScatter(), "cell 0 0 12 12 , grow");
		jpanel.validate();
	}

	public ChartPanel createScatter() {
		XYDataset dataset = createDataset(null);

		JFreeChart chart = ChartFactory.createScatterPlot("particle sim", "X", "Y", dataset, PlotOrientation.VERTICAL,
				true,

				true, false);

		plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(new Color(255, 228, 196));
		
		XYItemRenderer renderer = plot.getRenderer();
		renderer.setSeriesPaint(0, Color.blue);
		renderer.setSeriesPaint(1, Color.green);
		renderer.setSeriesPaint(2, Color.red);
		double size = 1.0;
		double delta = size / 2.0;
		Shape shape1 = new Rectangle2D.Double(-delta, -delta, size, size);
		Shape shape2 = new Ellipse2D.Double(-delta, -delta, size * 3, size * 3);
		renderer.setSeriesShape(0, shape1);
		renderer.setSeriesShape(1, shape1);
		renderer.setSeriesShape(2, shape1);
		ChartPanel panel = new ChartPanel(chart);
		return panel;

	}

	private XYDataset createDataset(List<Particle> particles) {
		XYSeriesCollection dataset = new XYSeriesCollection();
		
		List<XYSeries> series = new ArrayList<>();
		
		 series.add(new XYSeries("Blue"));
		 series.add(new XYSeries("Green"));
		 series.add(new XYSeries("Red"));

		if (particles == null) {
			series.get(0).add(-50.0, 0.0);
			
		} else {
			
			Random r = new Random();

			for (Particle p : particles) {
				
				if(p.getColor().contentEquals("BLUE")) {
				series.get(0).add(p.getX(), p.getY());
				}
				else if(p.getColor().contentEquals("GREEN")) {
					series.get(1).add(p.getX(), p.getY());
				}
				else {
					series.get(2).add(p.getX(), p.getY());
				}

			}
			
		}

		
		for(XYSeries s : series) {
			
			dataset.addSeries(s);
		}
		
		
		return dataset;
	}

	public void updatePlot(List<Particle> particles) {

		plot.setDataset(createDataset(particles));

	}

}
