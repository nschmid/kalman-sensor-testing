package ch.bfh.mobileroboter;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

public class Chart {

	// defining the axes
	final CategoryAxis xAxis = new CategoryAxis();
	final NumberAxis yAxis = new NumberAxis();
	// creating the chart
	final LineChart<String, Number> lineChart = new LineChart<String, Number>(xAxis, yAxis);

	final ScatterChart<String, Number> scatterChart = new ScatterChart<String, Number>(xAxis, yAxis);

	public Chart() {
		lineChart.setTitle("Kalman Filter");
		xAxis.setLabel("");
		yAxis.setLabel("");

		scatterChart.setTitle("Kalman Filter");
	}

	public Series createSeries(String seriesName, double[] arr) {

		// defining a series
		XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
		series.setName(seriesName);

		// populating the series with data

		for (int i = 0; i < arr.length; i++) {
			int c = i + 1;
			series.getData().add(new XYChart.Data(c + "", arr[i]));
			System.out.println(arr[i]);
		}

		return series;
	}
}
