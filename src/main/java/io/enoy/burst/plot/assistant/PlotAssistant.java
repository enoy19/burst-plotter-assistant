package io.enoy.burst.plot.assistant;

import io.enoy.burst.plot.assistant.model.PlotAssistantExecution;
import io.enoy.burst.plot.assistant.model.PlotAssistantModel;
import io.enoy.burst.plot.assistant.model.PlotAssistantModelFactory;
import io.enoy.burst.plot.assistant.util.PlotAssistantGridUtil;
import io.enoy.burst.plot.assistant.util.PlotAssistantPersistenceUtil;
import io.enoy.burst.plot.assistant.util.PlotAssistantPlotFileUtil;
import io.enoy.burst.plot.assistant.util.PlotAssistantPlotFileUtil.PlotFile;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class PlotAssistant implements Initializable {

	@FXML
	private HBox hBoxTop;

	@FXML
	private TextField numericalAccountId;

	@FXML
	private TextField startingNonce;

	@FXML
	private TextField nonces;

	@FXML
	private TextField staggerSize;

	@FXML
	private CheckBox optimized;

	@FXML
	private RadioButton notOptimized;

	@FXML
	private GridPane gridPane;

	@FXML
	private ProgressBar progressBar;

	@FXML
	private Label progressTextField;

	@FXML
	private Spinner<Integer> cyclesSpinner;

	@FXML
	private ToggleButton startStopButton;

	private FileChooser fileChooser;
	private DirectoryChooser plotFileDirectoryChooser;
	private Thread workerThread;

	public void initialize(URL location, ResourceBundle resources) {
		cyclesSpinner.setValueFactory(new IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 2, 1));

		fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Plot Assistant Files", "*.paf"));

		plotFileDirectoryChooser = new DirectoryChooser();
	}

	@FXML
	void open(ActionEvent event) {
		File file = fileChooser.showOpenDialog(null);

		if (Objects.nonNull(file))
			try {
				PlotAssistantModel model = PlotAssistantPersistenceUtil.open(file);
				loadModel(model);
			} catch (IOException e) {
				e.printStackTrace();
				// TODO: error message
			}
	}

	private void loadModel(PlotAssistantModel model) {
		numericalAccountId.setText(model.getNumericalAccountId());
		startingNonce.setText(model.getStartingNonce());
		nonces.setText(model.getNonces());
		staggerSize.setText(model.getStaggerSize());
		optimized.setSelected(model.isOptimized());
		cyclesSpinner.getEditor().setText(Integer.toString(model.getCycles()));

		for (PlotAssistantExecution execution : model.getExecutions()) {
			final int gridRow = execution.getType().getGridRow();
			PlotAssistantGridUtil.setActive(gridPane, gridRow, execution.isActive());
			PlotAssistantGridUtil.setCommand(gridPane, gridRow, execution.getCommand());
			PlotAssistantGridUtil.setIgnoreExitCode(gridPane, gridRow, execution.isIgnoreExitCode());
			PlotAssistantGridUtil.setSucessExitCodes(gridPane, gridRow, execution.getSuccessExitCodes());
		}
	}

	@FXML
	void saveAs(ActionEvent event) {
		File file = fileChooser.showSaveDialog(null);

		if (Objects.nonNull(file))
			try {
				PlotAssistantPersistenceUtil.save(createModel(), file);
			} catch (IOException e) {
				e.printStackTrace();
				// TODO: error message
			}
	}

	@FXML
	void showLog(ActionEvent event) {
		// TODO
	}

	@FXML
	void startStop(ActionEvent event) {
		// this might by confusing. "start if isRunning".
		// The event is fired, after the changes to the selected Property

		if (!isNoncesValid()) {
			event.consume();
			startStopButton.setSelected(false);
			return;
		}

		if (isRunning()) {
			start();
		} else {
			stop();
		}
	}

	@FXML
	void findStartingNonce() {

		File directory = plotFileDirectoryChooser.showDialog(null);

		if (Objects.nonNull(directory)) {
			BigInteger nextStartingNonce = PlotAssistantPlotFileUtil.findNextStartingNonce(directory);

			if (Objects.nonNull(nextStartingNonce))
				startingNonce.setText(nextStartingNonce.toString());
		}

	}

	private boolean isNoncesValid() {
		BigInteger nonce = new BigInteger(getNonce());
		BigInteger staggerSize = new BigInteger(getStaggerSize());

		final BigInteger mod = nonce.mod(staggerSize);
		return mod.equals(BigInteger.ZERO);
	}

	private void stop() {
		if (workerThread.isAlive())
			workerThread.interrupt();
		disableControls(false);
	}

	private void start() {
		PlotAssistantWorker worker = new PlotAssistantWorker(createModel(), this::updateProgess, this::done);

		disableControls(true);

		workerThread = new Thread(worker, "Plot Assistant Worker Thread");
		workerThread.setDaemon(true);
		workerThread.start();
	}

	private void disableControls(boolean state) {
		gridPane.setDisable(state);
		hBoxTop.setDisable(state);
		cyclesSpinner.setDisable(state);
	}

	private void updateProgess(int cycleIndex) {
		int cycles = getCycles();
		final double progress = (double) (cycleIndex + 1) / (double) cycles;

		Platform.runLater(() -> progressBar.setProgress(progress));
	}

	private void done() {
		Platform.runLater(startStopButton::fire);
	}

	public int getCycles() {
		return cyclesSpinner.getValue();
	}

	private boolean isRunning() {
		return startStopButton.isSelected();
	}

	public boolean isOptimized() {
		return optimized.isSelected();
	}

	public String getNumericalAccountId() {
		return numericalAccountId.getText();
	}

	public String getStartingNonce() {
		return startingNonce.getText();
	}


	public String getNonce() {
		return nonces.getText();
	}

	public String getStaggerSize() {
		return staggerSize.getText();
	}

	public GridPane getGridPane() {
		return gridPane;
	}

	private PlotAssistantModel createModel() {
		return PlotAssistantModelFactory.create(this);
	}
}
