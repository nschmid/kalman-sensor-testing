package ch.bfh.mobileroboter;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import pk.com.habsoft.robosim.filters.kalman.KalmanFilterSimulator;

public class KalmanTestsController {

	@FXML
	private Button btnUpdate;
	@FXML
	private Spinner<Double> spinner1;
	@FXML
	private Spinner<Double> spinner2;
	@FXML
	private Spinner<Double> spinner3;
	@FXML
	private CheckBox sensor1Active;
	@FXML
	private CheckBox sensor2Active;
	@FXML
	private CheckBox sensor3Active;
	@FXML
	private TextField numOfProbes;
	@FXML
	CheckBox sensor1Output;
	@FXML
	CheckBox sensor2Output;
	@FXML
	CheckBox sensor3Output;
	@FXML
	CheckBox kalman1Output;
	@FXML
	CheckBox kalman2Output;
	@FXML
	CheckBox kalman3Output;
	@FXML
	CheckBox robotPositionOutput;
	@FXML
	CheckBox computedKalmanOutput;

	@FXML
	TextField noise1;
	@FXML
	TextField noise2;
	@FXML
	TextField noise3;
	@FXML
	TextField robotVelocity;

	@FXML
	Button noise1Plus;
	@FXML
	Button noise1Minus;
	@FXML
	Button noise2Plus;
	@FXML
	Button noise2Minus;
	@FXML
	Button noise3Plus;
	@FXML
	Button noise3Minus;
	@FXML
	Button probesMinus;
	@FXML
	Button probesPlus;
	@FXML
	Button velocityMinus;
	@FXML
	Button velocityPlus;

	KalmanFilterSimulator sensor1simulator = null;
	KalmanFilterSimulator sensor2simulator = null;
	KalmanFilterSimulator sensor3simulator = null;

	// defining the axes
	@FXML
	NumberAxis xAxis;
	@FXML
	NumberAxis yAxis;
	@FXML
	LineChart<Number, Number> lineChart;
	@FXML
	ScatterChart<Number, Number> scatterChart;

	@FXML
	Pane canvas;

	public KalmanTestsController() {

	}

	@FXML
	public void initialize() {

		sensor1Output.selectedProperty().addListener((observable, oldValue, newValue) -> {
			redrawCharts();
		});
		kalman1Output.selectedProperty().addListener((observable, oldValue, newValue) -> {
			redrawCharts();
		});
		sensor2Output.selectedProperty().addListener((observable, oldValue, newValue) -> {
			redrawCharts();
		});
		kalman2Output.selectedProperty().addListener((observable, oldValue, newValue) -> {
			redrawCharts();
		});
		sensor3Output.selectedProperty().addListener((observable, oldValue, newValue) -> {
			redrawCharts();
		});
		kalman3Output.selectedProperty().addListener((observable, oldValue, newValue) -> {
			redrawCharts();
		});
		robotPositionOutput.selectedProperty().addListener((observable, oldValue, newValue) -> {
			redrawCharts();
		});
		computedKalmanOutput.selectedProperty().addListener((observable, oldValue, newValue) -> {
			redrawCharts();
		});
		sensor1Active.selectedProperty().addListener((observable, oldValue, newValue) -> {
			redrawCharts();
		});
		sensor2Active.selectedProperty().addListener((observable, oldValue, newValue) -> {
			redrawCharts();
		});
		sensor3Active.selectedProperty().addListener((observable, oldValue, newValue) -> {
			redrawCharts();
		});

		lineChart.setTitle("Kalman Filter");
		// xAxis.setLabel("");
		// yAxis.setLabel("");
		scatterChart.setTitle("Kalman Filter");

		xAxis.setLowerBound(0);
		xAxis.setUpperBound(500);

		yAxis.setLowerBound(0);
		yAxis.setUpperBound(5);
	}

	@FXML
	public void createDataset() {
		generateKalmanData();
		redrawCharts();
	}

	public void generateKalmanData() {
		double variance1 = Double.valueOf(noise1.getText());
		double variance2 = Double.valueOf(noise2.getText());
		double variance3 = Double.valueOf(noise3.getText());

		int probes = (int) Integer.parseInt(numOfProbes.getText());

		double robotSpeed = Double.valueOf(robotVelocity.getText());

		// generate the kalman data
		sensor1simulator = new KalmanFilterSimulator(probes, variance1, robotSpeed);
		sensor1simulator.simulate();

		sensor2simulator = new KalmanFilterSimulator(probes, variance2, robotSpeed);
		sensor2simulator.simulate();

		sensor3simulator = new KalmanFilterSimulator(probes, variance3, robotSpeed);
		sensor3simulator.simulate();

		nextStepAnimation = 0;
	}

	@FXML
	public void generateDatasetForSensor1() {

		double variance1 = Double.valueOf(noise1.getText());
		int probes = (int) Integer.parseInt(numOfProbes.getText());
		double robotSpeed = Double.valueOf(robotVelocity.getText());

		// generate the kalman data
		sensor1simulator = new KalmanFilterSimulator(probes, variance1, robotSpeed);
		sensor1simulator.simulate();

		redrawCharts();
	}

	@FXML
	public void generateDatasetForSensor2() {

		double variance2 = Double.valueOf(noise2.getText());

		int probes = (int) Integer.parseInt(numOfProbes.getText());
		double robotSpeed = Double.valueOf(robotVelocity.getText());

		// generate the kalman data
		sensor2simulator = new KalmanFilterSimulator(probes, variance2, robotSpeed);
		sensor2simulator.simulate();

		redrawCharts();
	}

	@FXML
	public void generateDatasetForSensor3() {

		double variance3 = Double.valueOf(noise3.getText());
		int probes = (int) Integer.parseInt(numOfProbes.getText());
		double robotSpeed = Double.valueOf(robotVelocity.getText());

		// generate the kalman data
		sensor3simulator = new KalmanFilterSimulator(probes, variance3, robotSpeed);
		sensor3simulator.simulate();

		redrawCharts();
	}

	@SuppressWarnings("unchecked")
	private void redrawCharts() {
		// if speed is 0 we use only positive variance to display it nicely on
		// the charts.
		boolean positiveOnly = robotVelocity.getText().equals("0");

		lineChart.getData().clear();
		scatterChart.getData().clear();

		if (robotPositionOutput.isSelected()) {
			scatterChart.getData()
					.add(createSeries("Roboter Position", sensor1simulator.getCarPositions(), positiveOnly));
			lineChart.getData().add(createSeries("Roboter Position", sensor1simulator.getCarPositions(), positiveOnly));
		}
		if (sensor1Output.isSelected()) {
			scatterChart.getData()
					.add(createSeries("Messung Sensor 1", sensor1simulator.getPositionMeasurements(), positiveOnly));
			lineChart.getData()
					.add(createSeries("Messung Sensor 1", sensor1simulator.getPositionMeasurements(), positiveOnly));
		}
		if (sensor2Output.isSelected()) {
			scatterChart.getData()
					.add(createSeries("Messung Sensor 2", sensor2simulator.getPositionMeasurements(), positiveOnly));
			lineChart.getData()
					.add(createSeries("Messung Sensor 2", sensor2simulator.getPositionMeasurements(), positiveOnly));
		}
		if (sensor3Output.isSelected()) {
			scatterChart.getData()
					.add(createSeries("Messung Sensor 3", sensor3simulator.getPositionMeasurements(), positiveOnly));
			lineChart.getData()
					.add(createSeries("Messung Sensor 3", sensor3simulator.getPositionMeasurements(), positiveOnly));
		}

		if (kalman1Output.isSelected()) {
			scatterChart.getData()
					.add(createSeries("Kalman Position 1", sensor1simulator.getPositionsKalman(), positiveOnly));
			lineChart.getData()
					.add(createSeries("Kalman Position 1", sensor1simulator.getPositionsKalman(), positiveOnly));
		}
		if (kalman2Output.isSelected()) {
			scatterChart.getData()
					.add(createSeries("Kalman Position 2", sensor2simulator.getPositionsKalman(), positiveOnly));
			lineChart.getData()
					.add(createSeries("Kalman Position 2", sensor2simulator.getPositionsKalman(), positiveOnly));
		}
		if (kalman3Output.isSelected()) {
			scatterChart.getData()
					.add(createSeries("Kalman Position 3", sensor3simulator.getPositionsKalman(), positiveOnly));
			lineChart.getData()
					.add(createSeries("Kalman Position 3", sensor3simulator.getPositionsKalman(), positiveOnly));
		}

		// combined kalman
		double[] arr = kalmanMean();

		if (computedKalmanOutput.isSelected()) {
			scatterChart.getData().add(createSeries("Kalman Position", arr, positiveOnly));
			lineChart.getData().add(createSeries("Kalman Position", arr, positiveOnly));
		}
	}

	private double[] kalmanMean() {
		double[] k1 = sensor1simulator.getPositionsKalman();
		double[] k2 = sensor2simulator.getPositionsKalman();
		double[] k3 = sensor3simulator.getPositionsKalman();

		double[] arr = new double[k1.length];

		int kCount = 0;
		if (sensor1Active.isSelected())
			kCount++;
		if (sensor2Active.isSelected())
			kCount++;
		if (sensor3Active.isSelected())
			kCount++;

		for (int i = 0; i < k1.length; i++) {
			double v = 0;
			// create mean
			if (sensor1Active.isSelected()) {
				v += k1[i];
			}
			if (sensor2Active.isSelected()) {
				v += k2[i];
			}
			if (sensor3Active.isSelected()) {
				v += k3[i];
			}

			// arr[i] = (k1[i] + k2[i] + k3[i]) / 3;
			arr[i] = v / kCount;
		}
		return arr;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Series createSeries(String seriesName, double[] arr, boolean positiveOnly) {
		// defining a series
		XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
		series.setName(seriesName);
		for (int i = 0; i < arr.length; i++) {
			int c = i + 1;
			if (positiveOnly)
				series.getData().add(new XYChart.Data(c, Math.abs(arr[i])));
			else
				series.getData().add(new XYChart.Data(c, arr[i]));
		}
		return series;
	}

	@FXML
	public void noise1PlusClicked() {
		double d = Double.valueOf(noise1.getText());
		d += 0.5;
		noise1.setText(d + "");
	}

	@FXML
	public void noise2PlusClicked() {
		double d = Double.valueOf(noise2.getText());
		d += 0.5;
		noise2.setText(d + "");
	}

	@FXML
	public void noise3PlusClicked() {
		double d = Double.valueOf(noise3.getText());
		d += 0.5;
		noise3.setText(d + "");
	}

	@FXML
	public void noise1MinusClicked() {
		double d = Double.valueOf(noise1.getText());
		d -= 0.5;
		noise1.setText(d + "");
	}

	@FXML
	public void noise2MinusClicked() {
		double d = Double.valueOf(noise2.getText());
		d -= 0.5;
		noise2.setText(d + "");
	}

	@FXML
	public void noise3MinusClicked() {
		double d = Double.valueOf(noise3.getText());
		d -= 0.5;
		noise3.setText(d + "");
	}

	@FXML
	public void probesPlusClicked() {
		int i = Integer.valueOf(numOfProbes.getText());
		i += 1;
		numOfProbes.setText(i + "");
	}

	@FXML
	public void probesMinusClicked() {
		int i = Integer.valueOf(numOfProbes.getText());
		i -= 1;
		numOfProbes.setText(i + "");
	}

	@FXML
	public void velocityPlusClicked() {
		double d = Double.valueOf(robotVelocity.getText());
		d += 0.5;
		robotVelocity.setText(d + "");
	}

	@FXML
	public void velocityMinusClicked() {
		double d = Double.valueOf(robotVelocity.getText());
		d -= 0.5;
		robotVelocity.setText(d + "");
	}

	int nextStepAnimation = 0;
	private Timer timer;

	@FXML
	public void nextStepAnimation() {
		int probes = (int) Integer.parseInt(numOfProbes.getText());
		if (nextStepAnimation >= probes)
			nextStepAnimation = 0;

		step();
	}

	@FXML
	public void runAnimation() {
		nextStepAnimation = 0;
		timer = new Timer();

		int waitmillis = 250;
		int probes = (int) Integer.parseInt(numOfProbes.getText());
		if (probes < 20)
			waitmillis = 1000;
		else if (probes < 101)
			waitmillis = 600;

		timer.schedule(new TimerTask() {
			@Override
			public void run() {

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						step();
					}
				});
			}
		}, 0, waitmillis);
	}

	@FXML
	public void stopAnimation() {
		if (timer != null)
			timer.cancel();
	}

	private void step() {

		int probes = (int) Integer.parseInt(numOfProbes.getText());
		if (nextStepAnimation < probes - 1) {
			nextStepAnimation++;

			Rectangle sensor1 = null;
			Rectangle sensor2 = null;
			Rectangle sensor3 = null;

			if (sensor1 == null)
				sensor1 = createRectangle(Color.RED);
			if (sensor2 == null)
				sensor2 = createRectangle(Color.RED);
			if (sensor3 == null)
				sensor3 = createRectangle(Color.RED);

			Circle center1 = null;
			if (center1 == null)
				center1 = createCircle(Color.BLACK);
			Circle center2 = null;
			if (center2 == null)
				center2 = createCircle(Color.BLACK);
			Circle center3 = null;
			if (center3 == null)
				center3 = createCircle(Color.BLACK);
			Circle centerCombined = null;
			if (centerCombined == null)
				centerCombined = createCircle(Color.BLACK);

			Circle circle1 = null;
			if (circle1 == null)
				circle1 = createCircle(Color.BLUE);
			Circle circle2 = null;
			if (circle2 == null)
				circle2 = createCircle(Color.BLUE);
			Circle circle3 = null;
			if (circle3 == null)
				circle3 = createCircle(Color.BLUE);
			Circle circleCombined = null;
			if (circleCombined == null)
				circleCombined = createCircle(Color.GREEN);

			int scale = 100;

			double sens1val = sensor1simulator.getPositionMeasurements()[nextStepAnimation] * scale;
			double kal1val = sensor1simulator.getPositionsKalman()[nextStepAnimation] * scale;
			double sens2val = sensor2simulator.getPositionMeasurements()[nextStepAnimation] * scale;
			double kal2val = sensor2simulator.getPositionsKalman()[nextStepAnimation] * scale;
			double sens3val = sensor3simulator.getPositionMeasurements()[nextStepAnimation] * scale;
			double kal3val = sensor3simulator.getPositionsKalman()[nextStepAnimation] * scale;

			double kalmanCombined = kalmanMean()[nextStepAnimation] * scale;

			// default positioning
			sensor1.setX(150 - 2);
			sensor2.setX(450 - 2);
			sensor3.setX(750 - 2);
			center1.setCenterX(150);
			center2.setCenterX(450);
			center3.setCenterX(750);
			centerCombined.setCenterX(1050);

			circle1.setCenterX(150);
			circle2.setCenterX(450);
			circle3.setCenterX(750);
			circleCombined.setCenterX(1050);

			int centerOffset = 300;

			center1.setCenterY(centerOffset);
			center2.setCenterY(centerOffset);
			center3.setCenterY(centerOffset);
			centerCombined.setCenterY(centerOffset);
			circle1.setCenterY(centerOffset);
			circle2.setCenterY(centerOffset);
			circle3.setCenterY(centerOffset);
			circleCombined.setCenterY(centerOffset);

			// dynamic values
			circle1.setRadius(Math.abs(kal1val));
			circle2.setRadius(Math.abs(kal2val));
			circle3.setRadius(Math.abs(kal3val));
			circleCombined.setRadius(Math.abs(kalmanCombined));

			sensor1.setY(sens1val + centerOffset);
			sensor2.setY(sens2val + centerOffset);
			sensor3.setY(sens3val + centerOffset);

			canvas.getChildren().clear();

			if (sensor1Active.isSelected())
				canvas.getChildren().addAll(sensor1, circle1, center1);
			if (sensor2Active.isSelected())
				canvas.getChildren().addAll(sensor2, circle2, center2);
			if (sensor3Active.isSelected())
				canvas.getChildren().addAll(sensor3, circle3, center3);

			canvas.getChildren().addAll(centerCombined, circleCombined);

		} else {
			if (timer != null)
				timer.cancel();
		}

	}

	private Circle createCircle(Color color) {
		Circle circle = new Circle();
		circle.setStroke(color);
		circle.setFill(null);
		circle.setStrokeWidth(3);
		return circle;
	}

	private Rectangle createRectangle(Color color) {
		Rectangle rectangle = new Rectangle(20, 20, 4, 4);
		rectangle.setStroke(color);
		rectangle.setFill(color);
		rectangle.setStrokeWidth(3);
		return rectangle;
	}
}
