package ch.bfh.mobileroboter;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
	private Spinner<Double> spnTotalTime = new Spinner<>();
	
	@FXML
	private Spinner<Double> spinner1 = new Spinner<>();
	
	@FXML
	private Spinner<Double> spinner2 = new Spinner<>();
	
	@FXML
	private Spinner<Double> spinner3 = new Spinner<>();
	
	@FXML
	private CheckBox checkBox1 = new CheckBox();
	
	@FXML
	private CheckBox checkBox2 = new CheckBox();
	
	@FXML
	private CheckBox checkBox3 = new CheckBox();
	
	@FXML
	public LineChart chart = new LineChart(null, null);
	
	@FXML
	private Spinner<Double> spnVariance = new Spinner<>();
	
	@FXML
	private Spinner<Double> spnRobotSpeed = new Spinner<>();

	KalmanFilterSimulator simulator = null;

//	private void update() {
//		DEFAULT_TOTAL_TIME = Integer.parseInt(spnTotalTime.getValue().toString());
//		DEFAULT_VARIANCE = Double.parseDouble(spnVariance.getValue().toString());
//		DEFAULT_CAR_SPEED = Double.parseDouble(spnRobotSpeed.getValue().toString());
//		update(DEFAULT_TOTAL_TIME, DEFAULT_VARIANCE, DEFAULT_CAR_SPEED);
//	}

	public void update(int total_time, double variance, double carSpeed) {
		simulator = new KalmanFilterSimulator(total_time, variance, carSpeed);
		simulator.simulate();
//		pnlPosition.clearData();
//		pnlPosition.addData("Measurement", simulator.getPositionMeasurements());
//		pnlPosition.addData("Car Position", simulator.getCarPositions());
//		pnlPosition.addData("Kalman Postion", simulator.getPositionsKalman());

//		pnlPositionError.setData(simulator.getPositionMeasurementError(), simulator.getPositionKalmanError());
	}
	
	
	@FXML
	public void update(){
		double variance = spinner1.getValue();
		checkBox1.isSelected();
		double carSpeed = 0;
		simulator = new KalmanFilterSimulator(1, variance, carSpeed );
		simulator.simulate();
		
	}
	
}
