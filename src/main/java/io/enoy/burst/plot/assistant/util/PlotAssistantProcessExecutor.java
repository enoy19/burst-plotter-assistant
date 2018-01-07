package io.enoy.burst.plot.assistant.util;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.buffer.CircularFifoBuffer;
import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class PlotAssistantProcessExecutor {

	public static ExecutorResult execute(String command) throws IOException, InterruptedException {
		ProcessBuilder processBuilder = new ProcessBuilder(getCmdArray(command));

		Process process = processBuilder.start();

		ExecutorReader stdErr = startReader(process.getErrorStream());
		ExecutorReader stdOut = startReader(process.getInputStream());

		final int exitCode = process.waitFor();

		System.out.println(stdOut.getData());
		return new ExecutorResult(exitCode, stdErr.getData());
	}

	private static String[] getCmdArray(String command) {
		// TODO determine UNIX/Windows
		return new String[]{"cmd.exe", "/c", command};
	}

	private static ExecutorReader startReader(final InputStream inputStream) {
		final ExecutorReader executorReader = new ExecutorReader(inputStream);

		Thread readerThread = new Thread(executorReader, "Plot Assistant Executor Reader");
		readerThread.setDaemon(true);
		readerThread.start();

		return executorReader;
	}

	@Data
	public static class ExecutorResult {

		private final int exitCode;
		private final String stdErr;

	}

	@RequiredArgsConstructor
	private static class ExecutorReader implements Runnable {

		private final InputStream inputStream;
		private CircularFifoQueue<String> data = new CircularFifoQueue<>(32);

		@Override
		public void run() {

			byte[] buffer = new byte[512];
			try {
				while (!Thread.interrupted() && inputStream.read(buffer) != -1) {
					data.add(new String(buffer, StandardCharsets.UTF_8));
				}
			} catch (IOException e) {
				// Ignore
				System.err.println("Ignore the following:");
				e.printStackTrace();
			}

		}

		public String getData() {
			return data.stream()
					.collect(Collectors.joining());
		}

	}

}
