package sample.ui.login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.util.LibraryAssistantUtil;

public class login extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Library Assistant");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();

        LibraryAssistantUtil.setStageIcon(stage);
    }
}
