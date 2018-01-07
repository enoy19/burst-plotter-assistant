package io.enoy.burst.plot.assistant.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class PlotAssistantModel {

	private String numericalAccountId;
	private String startingNonce;
	private String nonces;
	private String staggerSize;

	private boolean optimized;

	private int cycles;

	private PlotAssistantExecution executionProcessStart;
	private PlotAssistantExecution executionProcessEnd;
	private PlotAssistantExecution executionCycleStart;
	private PlotAssistantExecution executionCycleEnd;
	private PlotAssistantExecution executionPlotterPre;
	private PlotAssistantExecution executionPlotter;
	private PlotAssistantExecution executionPlotterPost;
	private PlotAssistantExecution executionOptimizerPre;
	private PlotAssistantExecution executionOptimizer;
	private PlotAssistantExecution executionOptimizerPost;

	@JsonIgnore
	public PlotAssistantExecution[] getExecutions() {
		return new PlotAssistantExecution[]{
				executionProcessStart,
				executionProcessEnd,
				executionCycleStart,
				executionCycleEnd,
				executionPlotterPre,
				executionPlotter,
				executionPlotterPost,
				executionOptimizerPre,
				executionOptimizer,
				executionOptimizerPost
		};
	}

}
