package sample.settings;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;
import sample.alert.AlertMaker;
import sample.database.DatabaseHandler;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class settingsController implements Initializable {

    @FXML
    private JFXTextField nDays;

    @FXML
    private JFXTextField fine;

    @FXML
    private JFXTextField username;

    @FXML
    private JFXPasswordField password;

    DatabaseHandler handler;
    Preferences preferences;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handler = DatabaseHandler.getInstance();
        preferences = Preferences.getPreferences();
        initDefaultValues();
    }



    @FXML
    private void handleSavebtn(ActionEvent event){
        int nDays1 = Integer.parseInt(nDays.getText());
        int fine1 = Integer.parseInt(fine.getText());
        String username1 = username.getText();
        String password1 = DigestUtils.shaHex(password.getText());
        String sql = "SELECT * FROM ACCOUNT WHERE user= '"+username1+"' ";

        try{
            ResultSet rs = handler.executeQuery(sql);
            if (rs.next()) {

                if (username1.isEmpty() || password1.isEmpty()){
                    username.getStyleClass().add("wrong-credentials");
                    password.getStyleClass().add("wrong-credentials");

                }

                if(nDays1 < 0 || fine1 < 0){
                    nDays.getStyleClass().add("wrong-credentials");
                    fine.getStyleClass().add("wrong-credentials");
                }

                if (password1.equals(preferences.getPassword())){
                    if (nDays1 >= 0 && fine1 >= 0){

                        preferences.setnDays(nDays1);
                        preferences.setFine(fine1);

                        preferences.writePreferenceToFile(preferences);
                        ((Stage) fine.getScene().getWindow()).close();
                        return;
                    }
                }else{
                    System.out.println("Password mismatch");
                }
            }else{
                System.out.println("Result set failed");
            }
        }catch (Exception e){
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE,null,e);
        }

        if (username1.isEmpty() || password1.isEmpty()){
            username.getStyleClass().add("wrong-credentials");
            password.getStyleClass().add("wrong-credentials");
        }

        if(nDays1 < 0 || fine1 < 0){
            nDays.getStyleClass().add("wrong-credentials");
            fine.getStyleClass().add("wrong-credentials");
        }


        username.getStyleClass().add("wrong-credentials");
        password.getStyleClass().add("wrong-credentials");
        nDays.getStyleClass().add("wrong-credentials");
        fine.getStyleClass().add("wrong-credentials");
        return;

    }

    @FXML
    private void handleCancelbtn(ActionEvent event){
        ((Stage) fine.getScene().getWindow()).close();
    }

    private void initDefaultValues() {
        Preferences preferences = Preferences.getPreferences();
        nDays.setText(String.valueOf(preferences.getnDays()));
        fine.setText(String.valueOf(preferences.getFine()));
    }

}
