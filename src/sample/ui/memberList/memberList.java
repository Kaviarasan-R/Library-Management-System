package sample.ui.memberList;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class memberList extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("member_list.fxml"));
        primaryStage.setTitle("Member List");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
