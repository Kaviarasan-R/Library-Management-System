package sample.ui.memberList;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.alert.AlertMaker;
import sample.database.DatabaseHandler;
import sample.ui.addBook.addBookController;
import sample.ui.editMember.editMemberController;
import sample.ui.main.mainController;
import sample.util.LibraryAssistantUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class
memberListController implements Initializable {

    ObservableList<Member> list = FXCollections.observableArrayList();

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private StackPane rootStack;

    @FXML
    private TableView<Member> tableView;

    @FXML
    private TableColumn<Member, String> idCol;

    @FXML
    private TableColumn<Member, String> nameCol;

    @FXML
    private TableColumn<Member, String> mobileCol;

    @FXML
    private TableColumn<Member, String> emailCol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();
        loadData();
    }

    private void initCol() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        mobileCol.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    private void loadData(){
        list.clear();

        DatabaseHandler handler = DatabaseHandler.getInstance();
        String query = "SELECT * FROM MEMBER";
        ResultSet rs = handler.executeQuery(query);
        try{
            while(rs.next()){
                String id = rs.getString("id");
                String nameText = rs.getString("name");
                String mobileText = rs.getString("mobile");
                String emailText = rs.getString("email");

                list.add(new Member(id,nameText,mobileText,emailText));
            }
        }catch (SQLException e){
            Logger.getLogger(addBookController.class.getName()).log(Level.SEVERE,null,e);
        }

        tableView.getItems().setAll(list);
    }

    public static class Member{
        private final SimpleStringProperty id;
        private final SimpleStringProperty name;
        private final SimpleStringProperty mobile;
        private final SimpleStringProperty email;


        public Member(String id, String name, String mobile, String email) {
            this.id = new SimpleStringProperty(id);
            this.name = new SimpleStringProperty(name);
            this.mobile = new SimpleStringProperty(mobile);
            this.email = new SimpleStringProperty(email);

        }

        public String getId() {
            return id.get();
        }

        public String getName() {
            return name.get();
        }

        public String getMobile() {
            return mobile.get();
        }

        public String getEmail() {
            return email.get();
        }

    }

    @FXML
    private void handleEdit2(ActionEvent event){
        Member selectedMember = tableView.getSelectionModel().getSelectedItem();
        if(selectedMember == null){
            return;
        }

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/ui/editMember/editMember.fxml"));
            Parent root = loader.load();

            editMemberController controller = (editMemberController) loader.getController();
            controller.inflateUI(selectedMember);

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Edit Member");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();

            LibraryAssistantUtil.setStageIcon(stage);

            stage.setOnCloseRequest((e)-> handleRefresh2(new ActionEvent()));

        }catch(IOException e){
            Logger.getLogger(mainController.class.getName()).log(Level.SEVERE,null,e);
        }
    }

    @FXML
    private void handleRefresh2(ActionEvent event){
        loadData();
    }

    @FXML
    private void handleDelete2(ActionEvent event){
        Member selectedMember = tableView.getSelectionModel().getSelectedItem();
        if(selectedMember == null){
            return;
        }
        Boolean result2 = DatabaseHandler.getInstance().isAlreadyIssued2(selectedMember);

        if (result2){

            JFXButton button = new JFXButton("Okay, I'll Check");
            AlertMaker.showMaterialDialog(rootStack,anchorPane, Arrays.asList(button),"Oops!!",selectedMember.getName() + " is issued with a book");
            return;
        }


        JFXButton yesButton = new JFXButton("YES");
        yesButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event1) -> {
            Boolean result1 = DatabaseHandler.getInstance().deleteMember(selectedMember);
            if (result1){
                JFXButton button = new JFXButton("OK");
                AlertMaker.showMaterialDialog(rootStack,anchorPane, Arrays.asList(button),"happie :)",selectedMember.getName() + "was deleted successfully");

                list.remove(selectedMember);
            }else {
                JFXButton button = new JFXButton("OK");
                AlertMaker.showMaterialDialog(rootStack,anchorPane, Arrays.asList(button),"Shit happened :(","Sorry, could not delete " + selectedMember.getName());
            }

        });

        JFXButton noButton = new JFXButton("NO");
        noButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event1) -> {
            return;
        });

        AlertMaker.showMaterialDialog(rootStack,anchorPane, Arrays.asList(yesButton,noButton),"Confirm Delete","Are you sure want to delete " + selectedMember.getName() + " ?");
    }
}
