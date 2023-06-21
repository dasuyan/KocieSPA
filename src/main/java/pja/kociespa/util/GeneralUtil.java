package pja.kociespa.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import pja.kociespa.Main;

import java.io.IOException;

/**
 * A utility class to switch between scenes and show alerts. Might be expanded in the future.
 */
public class GeneralUtil {
    public static void switchToNextScene(Stage currentStage, String nextSceneResourcePath) throws IOException {
        // Load the FXML file for the next scene
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(nextSceneResourcePath));
        Parent root = loader.load();

        // Create a new scene with the loaded root node
        Scene scene = new Scene(root);

        // Set the new scene on the stage
        currentStage.setScene(scene);
    }

    public static void showErrorAlert(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.resizableProperty().setValue(true);
        alert.show();
    }
}
