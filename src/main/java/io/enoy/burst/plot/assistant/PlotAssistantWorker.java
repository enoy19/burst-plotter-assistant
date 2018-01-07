package io.enoy.burst.plot.assistant;

import io.enoy.burst.plot.assistant.model.PlotAssistantCycleData;
import io.enoy.burst.plot.assistant.model.PlotAssistantExecution;
import io.enoy.burst.plot.assistant.model.PlotAssistantModel;
import io.enoy.burst.plot.assistant.model.PlotAssistantProcessData;
import io.enoy.burst.plot.assistant.util.PlotAssistantProcessExecutor;
import io.enoy.burst.plot.assistant.util.PlotAssistantProcessExecutor.ExecutorResult;
import io.enoy.burst.plot.assistant.util.PlotAssistantTemplateUtil;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Objects;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class PlotAssistantWorker implements Runnable {

	private final PlotAssistantModel model;
	private final Consumer<Integer> processListener;
	private final Runnable doneListener;

	@Override
	public void run() {
		try{
			start();
		}finally {
			doneListener.run();
		}

	}

	private void start() {
		final PlotAssistantProcessData processData = new PlotAssistantProcessData(
				model.getNumericalAccountId(),
				model.getStartingNonce(),
				model.getNonces(),
				model.getStaggerSize()
		);

		execute(model.getExecutionProcessStart(), model, processData, null);

		for (int cycleIndex = 0; cycleIndex < model.getCycles(); cycleIndex++) {
			String currentStartingNonce = getCurrentStartingNonce(cycleIndex, model.getStartingNonce(), model.getNonces());
			PlotAssistantCycleData cycleData = new PlotAssistantCycleData(currentStartingNonce, cycleIndex);

			execute(model.getExecutionCycleStart(), model, processData, cycleData);
			{
				execute(model.getExecutionPlotterPre(), model, processData, cycleData);
				execute(model.getExecutionPlotter(), model, processData, cycleData);
				execute(model.getExecutionPlotterPost(), model, processData, cycleData);
				// -----------------------------------------------------------
				if (!model.isOptimized()) {
					execute(model.getExecutionOptimizerPre(), model, processData, cycleData);
					execute(model.getExecutionOptimizer(), model, processData, cycleData);
					execute(model.getExecutionOptimizerPost(), model, processData, cycleData);
				}
			}
			execute(model.getExecutionCycleEnd(), model, processData, cycleData);

			processListener.accept(cycleIndex);
		}

		execute(model.getExecutionProcessEnd(), model, processData, null);
	}

	private String getCurrentStartingNonce(int cycleIndex, String startingNonceString, String noncesString) {

		BigInteger nonces = new BigInteger(noncesString);
		BigInteger cycleIndexMultiplier = new BigInteger(Integer.toString(cycleIndex));

		BigInteger currentStartingNonce = new BigInteger(startingNonceString);
		return currentStartingNonce.add(nonces.multiply(cycleIndexMultiplier)).toString();
	}

	private void execute(PlotAssistantExecution execution,
	                     PlotAssistantModel model,
	                     PlotAssistantProcessData processData,
	                     PlotAssistantCycleData cycleData) {
		if (execution.isActive()) {
			String command = execution.getCommand();

			if (Objects.nonNull(command) && !command.trim().isEmpty()) {
				command = renderTemplate(command, processData, cycleData);
				executeCommand(execution, command);
			}

		}

	}

	private String renderTemplate(String command, PlotAssistantProcessData processData, PlotAssistantCycleData cycleData) {
		command = PlotAssistantTemplateUtil.render(command, cycleData, processData);
		command = command.trim();
		return command;
	}

	private void executeCommand(PlotAssistantExecution execution, String command) {
		ExecutorResult result = null;

		try {
			result =
					PlotAssistantProcessExecutor.execute(command);

			final boolean successful = execution.isIgnoreExitCode()
					|| execution.getSuccessExitCodes().contains(result.getExitCode());

			if (!successful) {
				throw new IllegalStateException(String.format(
						"Execution failed. Command: %s%nExecution: %s%nStdErr: %s",
						command,
						execution.toString(),
						result.getStdErr())
				);
			}
		} catch (InterruptedException | IOException e) {
			StringBuilder errorMessage = new StringBuilder("Process fatal error");

			if (Objects.nonNull(result))
				errorMessage.append(String.format(": %s", result.getStdErr()));

			throw new IllegalStateException(errorMessage.toString(), e);
			// TODO: logger
		}
	}

}
