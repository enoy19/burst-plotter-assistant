package io.enoy.burst.plot.assistant.model;

import java.util.function.BiConsumer;

public enum PlotAssistantExecutionType {
	PROCESS_START(1, PlotAssistantModel::setExecutionProcessStart),
	PROCESS_END(10, PlotAssistantModel::setExecutionProcessEnd),
	CYCLE_START(2, PlotAssistantModel::setExecutionCycleStart),
	CYCLE_END(9, PlotAssistantModel::setExecutionCycleEnd),
	PRE_PLOTTER(3, PlotAssistantModel::setExecutionPlotterPre),
	PLOTTER(4, PlotAssistantModel::setExecutionPlotter),
	POST_PLOTTER(5, PlotAssistantModel::setExecutionPlotterPost),
	PRE_OPTIMIZER(6, PlotAssistantModel::setExecutionOptimizerPre),
	OPTIMIZER(7, PlotAssistantModel::setExecutionOptimizer),
	POST_OPTIMIZER(8, PlotAssistantModel::setExecutionOptimizerPost);

	private final int gridRow;
	private final BiConsumer<PlotAssistantModel, PlotAssistantExecution> modelExecutionSetter;

	PlotAssistantExecutionType(int gridRow, BiConsumer<PlotAssistantModel, PlotAssistantExecution> modelExecutionSetter) {
		this.gridRow = gridRow;
		this.modelExecutionSetter = modelExecutionSetter;
	}

	public int getGridRow() {
		return gridRow;
	}

	public void setModelExecution(PlotAssistantModel model, PlotAssistantExecution execution) {
		modelExecutionSetter.accept(model, execution);
	}

}
