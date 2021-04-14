package sample.ui.signup;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.util.LibraryAssistantUtil;

public class signup extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("signup.fxml"));
        stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Signup");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();

        LibraryAssistantUtil.setStageIcon(stage);
    }
}
