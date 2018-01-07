package io.enoy.burst.plot.assistant.util;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PlotAssistantGridUtil {

	public static void setActive(GridPane gridPane, int row, boolean value) {
		CheckBox checkBox = getNodeByRowColumnIndex(row, 0, gridPane);
		checkBox.setSelected(value);
	}

	public static boolean isActive(GridPane gridPane, int row) {
		CheckBox checkBox = getNodeByRowColumnIndex(row, 0, gridPane);
		return checkBox.isSelected();
	}

	public static void setCommand(GridPane gridPane, int row, String value) {
		TextField textField = getNodeByRowColumnIndex(row, 2, gridPane);
		textField.setText(value);
	}

	public static String getCommand(GridPane gridPane, int row) {
		TextField textField = getNodeByRowColumnIndex(row, 2, gridPane);
		return textField.getText();
	}

	public static void setSucessExitCodes(GridPane gridPane, int row, List<Integer> successExitCodes) {
		TextField textField = getNodeByRowColumnIndex(row, 3, gridPane);
		String successExitCodesString =
				successExitCodes
						.stream()
						.map(i -> Integer.toString(i))
						.collect(Collectors.joining(","));

		textField.setText(successExitCodesString);
	}

	public static List<Integer> getSucessExitCodes(GridPane gridPane, int row) {
		TextField textField = getNodeByRowColumnIndex(row, 3, gridPane);
		String successExitCodesString = textField.getText();
		String[] successExitCodesSplit = successExitCodesString.split(",");

		List<Integer> successExitCodes = new ArrayList<>(successExitCodesSplit.length);

		for (String successExitCodeString : successExitCodesSplit) {
			int successExitCode = Integer.parseInt(successExitCodeString);
			successExitCodes.add(successExitCode);
		}

		return successExitCodes;
	}

	public static void setIgnoreExitCode(GridPane gridPane, int row, boolean ignoreExitCode) {
		CheckBox checkBox = getNodeByRowColumnIndex(row, 4, gridPane);
		checkBox.setSelected(ignoreExitCode);
	}

	public static boolean isIgnoreExitCode(GridPane gridPane, int row) {
		CheckBox checkBox = getNodeByRowColumnIndex(row, 4, gridPane);
		return checkBox.isSelected();
	}

	@SuppressWarnings("unchecked")
	private static <T extends Node> T getNodeByRowColumnIndex(final int row, final int column, final GridPane gridPane) {
		final ObservableList<Node> children = gridPane.getChildren();
		for (Node node : children) {
			Integer rowIndex = GridPane.getRowIndex(node);
			Integer columnIndex = GridPane.getColumnIndex(node);

			rowIndex = Objects.isNull(rowIndex) ? 0 : rowIndex;
			columnIndex = Objects.isNull(columnIndex) ? 0 : columnIndex;

			if (rowIndex == row && columnIndex == column) {
				return (T) node;
			}
		}

		throw new IllegalStateException(String.format("Grid node not found. Row: %d Column: %d", row, column));
	}

}
