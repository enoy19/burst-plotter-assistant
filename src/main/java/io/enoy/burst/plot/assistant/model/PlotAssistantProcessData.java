package io.enoy.burst.plot.assistant.model;

import lombok.Data;

@Data
public class PlotAssistantProcessData {

	private final String numericalAccountId;
	private final String startNonce;
	private final String nonces;
	private final String staggerSize;

}
