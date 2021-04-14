package sample.ui.about;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class aboutController implements Initializable {

    @FXML
    private TextArea aboutText;

    @FXML
    private AnchorPane anchorPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        aboutText.setText("Hi hello everyone I am Kavi,\n" +
                          "this my Library Management\n" +
                          "System.In this application\n" +
                          "you can Add books, members,\n" +
                          "view books, view members.\n" +
                          "And you can Issue, renew,\n" +
                          "submit books.Hope you like\n" +
                          "this software.Thank you for\n" +
                          "downloading my software.");

    }

}
