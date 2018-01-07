package io.enoy.burst.plot.assistant;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PlotAssistantApplication extends Application {

	public static void main(String[] args) {
		Application.launch(args);
	}

	public void start(Stage primaryStage) throws Exception {

		FXMLLoader loader = new FXMLLoader();
		loader.load(PlotAssistantApplication.class.getResourceAsStream("/PlotAssistant.fxml"));

		Scene scene = new Scene(loader.getRoot());
		primaryStage.setScene(scene);

		primaryStage.setTitle("Burst Plot Assistant");
		primaryStage.show();

	}

}
