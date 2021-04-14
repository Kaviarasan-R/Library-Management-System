package sample.ui.signup;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;
import sample.database.DatabaseHandler;
import sample.settings.Preferences;

import sample.util.LibraryAssistantUtil;


import java.net.URL;
import java.util.ResourceBundle;


public class signupController implements Initializable {

    @FXML
    private JFXTextField loadUser;

    @FXML
    private JFXTextField loadEmail;

    @FXML
    private JFXPasswordField loadPass;

    DatabaseHandler handler;

    Preferences preferences;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handler = DatabaseHandler.getInstance();
        preferences = Preferences.getPreferences();
    }

    @FXML
    private void loadCreate(ActionEvent event){
        String uname = loadUser.getText();
        String pword = DigestUtils.shaHex(loadPass.getText());
        String e_mail = loadEmail.getText();

        if (uname.isEmpty() || pword.isEmpty() || e_mail.isEmpty()){
            loadUser.getStyleClass().add("wrong-credentials");
            loadPass.getStyleClass().add("wrong-credentials");
            loadEmail.getStyleClass().add("wrong-credentials");

        }else {
            handler.executeAction("INSERT INTO ACCOUNT VALUES('"+uname+"','"+e_mail+"','"+pword+"')");
            closeStage();
            LibraryAssistantUtil.loadWindow(getClass().getResource("/sample/ui/login/login.fxml"),"Login",null);
        }
    }
    @FXML
    private void loadLogin2(ActionEvent event){
        closeStage();
        LibraryAssistantUtil.loadWindow(getClass().getResource("/sample/ui/login/login.fxml"),"Login",null);
    }

    @FXML
    private void loadCancel3(ActionEvent event){
        System.exit(0);
    }


    private void closeStage() {
        ((Stage) loadUser.getScene().getWindow()).close();
    }
}
