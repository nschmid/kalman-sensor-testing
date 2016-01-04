package ch.bfh.mobileroboter;

import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import pk.com.habsoft.robosim.filters.kalman.KalmanFilterSimulator;

public class KalmanTestsController {

	final static String TOTAL_TIME_TAG = "TOTAL_TIME";
	final static String VARIANCE_TAG = "VARIANCE";
	final static String CAR_SPEED_TAG = "CAR_SPEED";

	static int DEFAULT_TOTAL_TIME = 20;
	final static int MIN_TOTAL_TIME = 10;
	final static int MAX_TOTAL_TIME = 100;

	static double DEFAULT_VARIANCE = 3;
	final static int MIN_VARIANCE = 0;
	final static int MAX_VARIANCE = 10;

	static double DEFAULT_CAR_SPEED = 0.5;

	// LineChartPanel pnlPosition = null;

	// BarChartPanel pnlPositionError = null;

	@FXML
	private Button btnUpdate;
	@FXML
	private Spinner<Double> spinner1;
	@FXML
	private Spinner<Double> spinner2;
	@FXML
	private Spinner<Double> spinner3;
	@FXML
	private CheckBox checkBox1;
	@FXML
	private CheckBox checkBox2;
	@FXML
	private CheckBox checkBox3;
	@FXML
	private Slider numOfProbes;
	@FXML
	private Label lblNumOfProbes;
	// @FXML
	// public LineChart<Number, Number> chart;

	//
	// @FXML
	// private Spinner<Double> spnTotalTime;
	// @FXML
	// private Spinner<Double> spnVariance;
	// @FXML
	// private Spinner<Double> spnRobotSpeed;

	KalmanFilterSimulator sensor1simulator = null;
	KalmanFilterSimulator sensor2simulator = null;
	KalmanFilterSimulator sensor3simulator = null;
	Chart c;

	@FXML
	LineChart<String, Number> lineChart;

	@FXML
	ScatterChart<String, Number> scatterChart;

	public KalmanTestsController() {
		c = new Chart();
		lineChart = c.lineChart;

		scatterChart = c.scatterChart;
	}

	@FXML
	public void initialize() {
		numOfProbes.valueProperty().addListener((observable, oldValue, newValue) -> {
			int n = newValue.intValue();
			lblNumOfProbes.setText(n + "");
		});
	}

	// private void update() {
	// DEFAULT_TOTAL_TIME =
	// Integer.parseInt(spnTotalTime.getValue().toString());
	// DEFAULT_VARIANCE = Double.parseDouble(spnVariance.getValue().toString());
	// DEFAULT_CAR_SPEED =
	// Double.parseDouble(spnRobotSpeed.getValue().toString());
	// update(DEFAULT_TOTAL_TIME, DEFAULT_VARIANCE, DEFAULT_CAR_SPEED);
	// }

	public void update(int total_time, double variance, double carSpeed) {
		sensor1simulator = new KalmanFilterSimulator(total_time, variance, carSpeed);
		sensor1simulator.simulate();
		// pnlPosition.clearData();
		// pnlPosition.addData("Measurement",
		// simulator.getPositionMeasurements());
		// pnlPosition.addData("Car Position", simulator.getCarPositions());
		// pnlPosition.addData("Kalman Postion",
		// simulator.getPositionsKalman());

		// pnlPositionError.setData(simulator.getPositionMeasurementError(),
		// simulator.getPositionKalmanError());
	}

	@FXML
	public void update() {

		// Double variance1 = spinner1.getValue();
		double variance1 = 1.;
		double variance2 = 0.5;
		double variance3 = 1.5;

		int probes = (int) numOfProbes.getValue();
		lblNumOfProbes.setText(probes + "");

		double robotSpeed = 0;
		sensor1simulator = new KalmanFilterSimulator(probes, variance1, robotSpeed);
		sensor1simulator.simulate();

		sensor2simulator = new KalmanFilterSimulator(probes, variance2, robotSpeed);
		sensor2simulator.simulate();

		sensor3simulator = new KalmanFilterSimulator(probes, variance3, robotSpeed);
		sensor3simulator.simulate();

		Series measurements1 = c.createSeries("Measurement Sensor 1", sensor1simulator.getPositionMeasurements());
		Series robotPositions = c.createSeries("Robot Position", sensor1simulator.getCarPositions());
		Series kalmanPositions1 = c.createSeries("Kalman Position 1", sensor1simulator.getPositionsKalman());

		Series measurements2 = c.createSeries("Measurement Sensor 2", sensor2simulator.getPositionMeasurements());
		Series kalmanPositions2 = c.createSeries("Kalman Position 2", sensor2simulator.getPositionsKalman());

		Series measurements3 = c.createSeries("Measurement Sensor 3", sensor3simulator.getPositionMeasurements());
		Series kalmanPositions3 = c.createSeries("Kalman Position 3", sensor3simulator.getPositionsKalman());

		// lineChart.getData().add(series);

		lineChart.getData().clear();
		scatterChart.getData().clear();

		// all data
		// lineChart.getData().addAll(measurements1, robotPositions,
		// kalmanPositions1, measurements2, kalmanPositions2,
		// measurements3, kalmanPositions3);

		// combined kalman
		double[] k1 = sensor1simulator.getPositionsKalman();
		double[] k2 = sensor2simulator.getPositionsKalman();
		double[] k3 = sensor3simulator.getPositionsKalman();

		double[] arr = new double[k1.length];

		for (int i = 0; i < k1.length; i++) {
			// create mean
			arr[i] = (k1[i] + k2[i] + k3[i]) / 3;
		}

		Series mergedKalman = c.createSeries("Kalman Position", arr);
//		lineChart.getData().addAll(robotPositions, mergedKalman);
		scatterChart.getData().addAll(measurements1, measurements2, measurements3, mergedKalman);
//		scatterChart.getData().addAll(robotPositions, mergedKalman);

		// updateChart();
	}

	public void updateChart(Series<Number, Number> measurements, Series<Number, Number> robotPositions,
			Series<Number, Number> kalmanPositions) {

		// stage.setTitle("Line Chart Sample");

		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		final LineChart<String, Number> charttt = new LineChart<String, Number>(xAxis, yAxis);
		xAxis.setLabel("Month");

		charttt.setTitle("Stock Monitoring, 2010");

		XYChart.Series series1 = new XYChart.Series();
		series1.setName("Portfolio 1");

		series1.getData().add(new XYChart.Data("Jan", 23));
		series1.getData().add(new XYChart.Data("Feb", 14));
		series1.getData().add(new XYChart.Data("Mar", 15));
		series1.getData().add(new XYChart.Data("Apr", 24));
		series1.getData().add(new XYChart.Data("May", 34));
		series1.getData().add(new XYChart.Data("Jun", 36));
		series1.getData().add(new XYChart.Data("Jul", 22));
		series1.getData().add(new XYChart.Data("Aug", 45));
		series1.getData().add(new XYChart.Data("Sep", 43));
		series1.getData().add(new XYChart.Data("Oct", 17));
		series1.getData().add(new XYChart.Data("Nov", 29));
		series1.getData().add(new XYChart.Data("Dec", 25));

		XYChart.Series series2 = new XYChart.Series();
		series2.setName("Portfolio 2");
		series2.getData().add(new XYChart.Data("Jan", 33));
		series2.getData().add(new XYChart.Data("Feb", 34));
		series2.getData().add(new XYChart.Data("Mar", 25));
		series2.getData().add(new XYChart.Data("Apr", 44));
		series2.getData().add(new XYChart.Data("May", 39));
		series2.getData().add(new XYChart.Data("Jun", 16));
		series2.getData().add(new XYChart.Data("Jul", 55));
		series2.getData().add(new XYChart.Data("Aug", 54));
		series2.getData().add(new XYChart.Data("Sep", 48));
		series2.getData().add(new XYChart.Data("Oct", 27));
		series2.getData().add(new XYChart.Data("Nov", 37));
		series2.getData().add(new XYChart.Data("Dec", 29));

		XYChart.Series series3 = new XYChart.Series();
		series3.setName("Portfolio 3");
		series3.getData().add(new XYChart.Data("Jan", 44));
		series3.getData().add(new XYChart.Data("Feb", 35));
		series3.getData().add(new XYChart.Data("Mar", 36));
		series3.getData().add(new XYChart.Data("Apr", 33));
		series3.getData().add(new XYChart.Data("May", 31));
		series3.getData().add(new XYChart.Data("Jun", 26));
		series3.getData().add(new XYChart.Data("Jul", 22));
		series3.getData().add(new XYChart.Data("Aug", 25));
		series3.getData().add(new XYChart.Data("Sep", 43));
		series3.getData().add(new XYChart.Data("Oct", 44));
		series3.getData().add(new XYChart.Data("Nov", 45));
		series3.getData().add(new XYChart.Data("Dec", 44));

		// Scene scene = new Scene(lineChart, 800, 600);
		charttt.getData().addAll(series1, series2, series3);
		lineChart.getData().addAll(series1, series2, series3);

		// stage.setScene(scene);
		// stage.show();
	}
}
