package sample.ui.addBook;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import sample.database.DatabaseHandler;

import java.net.URL;
import java.util.ResourceBundle;

public class addBookController implements Initializable {

    @FXML
    private JFXTextField title;

    @FXML
    private JFXTextField id;

    @FXML
    private JFXTextField author;

    @FXML
    private JFXTextField publisher;


    DatabaseHandler handler;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        handler = DatabaseHandler.getInstance();
    }

    @FXML
    private void addbook(ActionEvent event){
        String bookID = id.getText();
        String bookTitle = title.getText();
        String bookAuthor = author.getText();
        String bookPublisher = publisher.getText();

        if (bookID.isEmpty() || bookAuthor.isEmpty() || bookTitle.isEmpty() || bookPublisher.isEmpty()){
            id.getStyleClass().add("wrong-credentials");
            title.getStyleClass().add("wrong-credentials");
            author.getStyleClass().add("wrong-credentials");
            publisher.getStyleClass().add("wrong-credentials");
            return;
        }

        String query = "INSERT INTO BOOK VALUES(" +
                "'"+bookID+"','"+bookTitle+"','"+bookAuthor+"','"+bookPublisher+"','"+1+"')";
        if(handler.executeAction(query)){
            ((Stage)id.getScene().getWindow()).close();
        }
    }


    @FXML
    private void cancel(ActionEvent event){
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

}
