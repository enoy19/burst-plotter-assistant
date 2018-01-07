package io.enoy.burst.plot.assistant.model;

import io.enoy.burst.plot.assistant.util.PlotAssistantGridUtil;
import javafx.scene.layout.GridPane;

import java.util.List;

public class PlotAssistantExecutionFactory {

	public static PlotAssistantExecution create(GridPane gridPane, PlotAssistantExecutionType type) {
		PlotAssistantExecution execution = new PlotAssistantExecution();

		final int gridRow = type.getGridRow();

		final boolean active = PlotAssistantGridUtil.isActive(gridPane, gridRow);
		final String command = PlotAssistantGridUtil.getCommand(gridPane, gridRow);
		final List<Integer> successExitCodes = PlotAssistantGridUtil.getSucessExitCodes(gridPane, gridRow);
		final boolean ignoreExitCode = PlotAssistantGridUtil.isIgnoreExitCode(gridPane, gridRow);

		execution.setActive(active);
		execution.setCommand(command);
		execution.setSuccessExitCodes(successExitCodes);
		execution.setIgnoreExitCode(ignoreExitCode);
		execution.setType(type);

		return execution;
	}

}
