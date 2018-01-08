package io.enoy.burst.plot.assistant.util;

import lombok.Data;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlotAssistantPlotFileUtil {

	private static final Pattern PLOT_FILE_PATTERN = Pattern.compile("(\\d+)_(\\d+)_(\\d+)_(\\d+)");

	public static BigInteger findNextStartingNonce(File directory) {
		PlotFile highestPlotfile = findHighestPlotFile(directory);

		if (Objects.nonNull(highestPlotfile)) {
			BigInteger startingNonce = new BigInteger(highestPlotfile.getStartingNonce());
			BigInteger nonces = new BigInteger(highestPlotfile.getNonces());
			return startingNonce.add(nonces);
		}

		return null;
	}

	public static PlotFile findHighestPlotFile(File directory) {
		List<PlotFile> plotFiles = getPlotFiles(directory);

		PlotFile highest = null;
		BigInteger highestStartingNonce = null;
		for (PlotFile plotFile : plotFiles) {
			BigInteger startingNonce = new BigInteger(plotFile.getStartingNonce());

			if (Objects.isNull(highestStartingNonce)) {
				highest = plotFile;
				highestStartingNonce = startingNonce;
			} else {
				if (highestStartingNonce.compareTo(startingNonce) < 0) {
					highestStartingNonce = startingNonce;
					highest = plotFile;
				}
			}
		}

		return highest;
	}

	public static List<PlotFile> getPlotFiles(File directory) {
		if (Objects.isNull(directory) || Objects.isNull(directory.listFiles()))
			return Collections.emptyList();

		List<PlotFile> plotFiles = new ArrayList<>(directory.listFiles().length);
		findPlotFiles(directory, plotFiles);
		return plotFiles;
	}

	private static void findPlotFiles(File directory, List<PlotFile> plotFiles) {
		final File[] files = directory.listFiles();

		if (Objects.nonNull(files))
			for (File file : files) {
				if (file.isDirectory()) {
					findPlotFiles(file, plotFiles);
				} else {
					PlotFile plotFile = getPlotFile(file);
					if (Objects.nonNull(plotFile)) {
						plotFiles.add(plotFile);
					}
				}

			}
	}

	public static PlotFile getPlotFile(File file) {

		Matcher matcher = PLOT_FILE_PATTERN.matcher(file.getName());

		if (matcher.find()) {
			return new PlotFile(
					matcher.group(1),
					matcher.group(2),
					matcher.group(3),
					matcher.group(4)
			);
		}

		return null;
	}

	@Data
	public static class PlotFile {

		private final String numericalAccountId;
		private final String startingNonce;
		private final String nonces;
		private final String staggerSize;

	}

}
