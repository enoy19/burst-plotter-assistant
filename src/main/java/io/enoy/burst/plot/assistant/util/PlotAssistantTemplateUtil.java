package io.enoy.burst.plot.assistant.util;

import io.enoy.burst.plot.assistant.model.PlotAssistantCycleData;
import io.enoy.burst.plot.assistant.model.PlotAssistantProcessData;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.util.regex.Pattern;

public class PlotAssistantTemplateUtil {

	private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{(\\w+)\\}");

	public static String render(String template, PlotAssistantCycleData cycleData, PlotAssistantProcessData processData) {
		VelocityContext context = new VelocityContext();
		context.put("cycle", cycleData);
		context.put("process", processData);

		StringWriter writer = new StringWriter();
		Velocity.evaluate(context, writer, "PA-Template", template);

		return writer.toString();
	}

}