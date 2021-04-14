package sample.ui.editBook;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.database.DatabaseHandler;
import sample.ui.bookList.bookListController;

import java.net.URL;
import java.util.ResourceBundle;

public class editBookController implements Initializable {

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
    private void editbook(ActionEvent event){

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


        String query = "update book set title = '"+bookTitle+"', author = '"+bookAuthor+"', publisher = '"+bookPublisher+"' where id = '"+bookID+"'";
        if(handler.executeAction(query)){
            ((Stage)id.getScene().getWindow()).close();

        }
    }

    public void inflateUI(bookListController.Book book){
        title.setText(book.getTitle());
        id.setText(book.getId());
        author.setText(book.getAuthor());
        publisher.setText(book.getPublisher());
        id.setEditable(false);

    }


    @FXML
    private void cancel(ActionEvent event){
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }
}
