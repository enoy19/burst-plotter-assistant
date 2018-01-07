package io.enoy.burst.plot.assistant.model;

import lombok.Data;

import java.util.List;

@Data
public class PlotAssistantExecution {

	private PlotAssistantExecutionType type;
	private boolean active;
	private String command;
	private List<Integer> successExitCodes;
	private boolean ignoreExitCode;

}
