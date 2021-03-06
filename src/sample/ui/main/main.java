package sample.ui.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.database.DatabaseHandler;
import sample.settings.Preferences;
import sample.util.LibraryAssistantUtil;

public class main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Preferences.initConfig();

        Parent root = FXMLLoader.load(getClass().getResource("/sample/ui/signup/signup.fxml"));
        primaryStage.setTitle("Sign Up");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();

        LibraryAssistantUtil.setStageIcon(primaryStage);

        DatabaseHandler.getInstance();

    }
}
