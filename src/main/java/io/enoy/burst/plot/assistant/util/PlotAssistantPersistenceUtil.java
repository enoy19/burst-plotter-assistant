package io.enoy.burst.plot.assistant.util;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.enoy.burst.plot.assistant.model.PlotAssistantModel;

import java.io.File;
import java.io.IOException;

public class PlotAssistantPersistenceUtil {

	private static final DefaultPrettyPrinter PRETTY_PRINTER = new DefaultPrettyPrinter();

	public static void save(PlotAssistantModel model, File file) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(file, model);
		mapper.writer(PRETTY_PRINTER).writeValue(file, model);
	}

	public static PlotAssistantModel open(File file) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(file, PlotAssistantModel.class);
	}

}
