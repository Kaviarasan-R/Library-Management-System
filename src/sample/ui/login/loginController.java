package sample.ui.login;

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
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class loginController implements Initializable {

    @FXML
    private JFXTextField loginUsername;

    @FXML
    private JFXPasswordField loginPassword;


    DatabaseHandler handler;
    Preferences preferences;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        handler = DatabaseHandler.getInstance();
        preferences = Preferences.getPreferences();
    }

    @FXML
    private void loadLogin(ActionEvent event){

        String uname = loginUsername.getText();
        String pword = DigestUtils.shaHex(loginPassword.getText());

        String query = "select * from account where user = '"+uname+"' and password='"+pword+"' ";

        try{
            ResultSet rs = handler.executeQuery(query);
            if (rs.next()) {

                if (uname.isEmpty() || pword.isEmpty()){
                    loginUsername.getStyleClass().add("wrong-credentials");
                    loginPassword.getStyleClass().add("wrong-credentials");
                }else {
                    String pass = loginPassword.getText();
                    preferences.setPassword(pass);
                    preferences.writePreferenceToFile(preferences);

                    closeStage();
                    LibraryAssistantUtil.loadWindow(getClass().getResource("/sample/ui/main/main.fxml"),"Library Management System",null);
                    return;
                }
            }
        }catch (Exception e){
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE,null,e);
        }

        if (uname.isEmpty() || pword.isEmpty()){
            loginUsername.getStyleClass().add("wrong-credentials");
            loginPassword.getStyleClass().add("wrong-credentials");
        }

        loginUsername.getStyleClass().add("wrong-credentials");
        loginPassword.getStyleClass().add("wrong-credentials");
    }


    @FXML
    private void loadSign(ActionEvent event){
        closeStage();
        LibraryAssistantUtil.loadWindow(getClass().getResource("/sample/ui/signup/signup.fxml"),"Sign Up",null);
    }

    private void closeStage() {
        ((Stage) loginUsername.getScene().getWindow()).close();
    }


    @FXML
    private void loadCancel2(ActionEvent event){
        System.exit(0);
    }
}
