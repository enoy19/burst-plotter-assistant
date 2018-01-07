package io.enoy.burst.plot.assistant.model;

import io.enoy.burst.plot.assistant.PlotAssistant;
import javafx.scene.layout.GridPane;

public class PlotAssistantModelFactory {

	public static PlotAssistantModel create(PlotAssistant plotAssistant) {
		PlotAssistantModel model = new PlotAssistantModel();

		model.setNumericalAccountId(plotAssistant.getNumericalAccountId());
		model.setStartingNonce(plotAssistant.getStartingNonce());
		model.setNonces(plotAssistant.getNonce());
		model.setStaggerSize(plotAssistant.getStaggerSize());

		model.setOptimized(plotAssistant.isOptimized());
		model.setCycles(plotAssistant.getCycles());

		GridPane gridPane = plotAssistant.getGridPane();

		setExecutions(gridPane, model);

		return model;
	}

	private static void setExecutions(GridPane gridPane, PlotAssistantModel model) {
		for (PlotAssistantExecutionType plotAssistantExecutionType : PlotAssistantExecutionType.values()) {
			PlotAssistantExecution execution = getExecution(gridPane, plotAssistantExecutionType);
			plotAssistantExecutionType.setModelExecution(model, execution);
		}
	}

	private static PlotAssistantExecution getExecution(GridPane gridPane, PlotAssistantExecutionType type) {
		return PlotAssistantExecutionFactory.create(gridPane, type);
	}

}
