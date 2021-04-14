package sample.ui.editMember;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.database.DatabaseHandler;
import sample.ui.memberList.memberListController;

import java.net.URL;
import java.util.ResourceBundle;

public class editMemberController implements Initializable {

    @FXML
    private JFXTextField name;

    @FXML
    private JFXTextField memberID;

    @FXML
    private JFXTextField mobile;

    @FXML
    private JFXTextField email;

    DatabaseHandler handler;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handler = DatabaseHandler.getInstance();
    }


    @FXML
    private void editMember(ActionEvent event){

        String nameText = name.getText();
        String memberIDText = memberID.getText();
        String mobileText = mobile.getText();
        String emailText = email.getText();

        if (nameText.isEmpty() || memberIDText.isEmpty() || mobileText.isEmpty() || emailText.isEmpty()){
            memberID.getStyleClass().add("wrong-credentials");
            name.getStyleClass().add("wrong-credentials");
            mobile.getStyleClass().add("wrong-credentials");
            email.getStyleClass().add("wrong-credentials");
            return;
        }


        String query = "update member set name = '"+nameText+"', mobile = '"+mobileText+"', email = '"+emailText+"' where id = '"+memberIDText+"'";
        if(handler.executeAction(query)){
            ((Stage)memberID.getScene().getWindow()).close();

        }
    }

    public void inflateUI(memberListController.Member member){
        name.setText(member.getName());
        memberID.setText(member.getId());
        mobile.setText(member.getMobile());
        email.setText(member.getEmail());
        memberID.setEditable(false);

    }


    @FXML
    private void cancel(ActionEvent event){
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }
}
