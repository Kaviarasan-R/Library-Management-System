package sample.ui.bookList;


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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
import sample.ui.editBook.editBookController;
import sample.ui.main.mainController;
import sample.ui.memberList.memberListController;
import sample.util.LibraryAssistantUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class bookListController implements Initializable {

    ObservableList<Book> list = FXCollections.observableArrayList();

    @FXML
    private StackPane rootStack;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TableView<Book> tableView;

    @FXML
    private TableColumn<Book, String> idCol;

    @FXML
    private TableColumn<Book, String> titleCol;

    @FXML
    private TableColumn<Book, String> authCol;

    @FXML
    private TableColumn<Book, String> pubCol;

    @FXML
    private TableColumn<Book, String> availCol;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();
        loadData();
    }

    private void initCol() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        pubCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        availCol.setCellValueFactory(new PropertyValueFactory<>("availability"));
    }


    private void loadData(){
        list.clear();

        DatabaseHandler handler = DatabaseHandler.getInstance();
        String query = "SELECT * FROM BOOK";
        ResultSet rs = handler.executeQuery(query);
        try{
            while(rs.next()){
                String id = rs.getString("id");
                String titles = rs.getString("title");
                String authors = rs.getString("author");
                String publishers = rs.getString("publisher");
                boolean avail = rs.getBoolean("isavail");

                String available = (avail) ? "Yes" : "No";
                list.add(new Book(id,titles,authors,publishers,available));
            }
        }catch (SQLException e){
            Logger.getLogger(addBookController.class.getName()).log(Level.SEVERE,null,e);
        }

        tableView.setItems(list);
    }

    public static class Book{
        private final SimpleStringProperty id;
        private final SimpleStringProperty title;
        private final SimpleStringProperty author;
        private final SimpleStringProperty publisher;
        private final SimpleStringProperty availability;

        public Book(String id, String title, String author, String publisher,String avail) {
            this.id = new SimpleStringProperty(id);
            this.title = new SimpleStringProperty(title);
            this.author = new SimpleStringProperty(author);
            this.publisher = new SimpleStringProperty(publisher);
            this.availability = new SimpleStringProperty(avail);
        }

        public String getId() {
            return id.get();
        }

        public String getTitle() {
            return title.get();
        }

        public String getAuthor() {
            return author.get();
        }

        public String getPublisher() {
            return publisher.get();
        }

        public String getAvailability() {
            return availability.get();
        }

    }

    @FXML
    private void handleBookDelete(ActionEvent event){

        Book selectedBook = tableView.getSelectionModel().getSelectedItem();
        if(selectedBook == null){
            return;
        }

        Boolean result2 = DatabaseHandler.getInstance().isAlreadyIssued(selectedBook);

        if (result2){

            JFXButton button = new JFXButton("Okay, I'll Check");
            AlertMaker.showMaterialDialog(rootStack,anchorPane, Arrays.asList(button),"Sorry :(",selectedBook.getTitle() + " book is already issued");
            return;
        }


        JFXButton yesButton = new JFXButton("YES");
        yesButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event1) -> {
            Boolean result1 = DatabaseHandler.getInstance().deleteBook(selectedBook);
            if (result1){
                JFXButton button = new JFXButton("OK");
                AlertMaker.showMaterialDialog(rootStack,anchorPane, Arrays.asList(button),":)",selectedBook.getTitle() + "was deleted successfully");

                list.remove(selectedBook);
            }else {
                JFXButton button = new JFXButton("OK");
                AlertMaker.showMaterialDialog(rootStack,anchorPane, Arrays.asList(button),":(","Sorry, couldn't delete " + selectedBook.getTitle());
            }

        });

        JFXButton noButton = new JFXButton("NO");
        noButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event1) -> {
            return;
        });

        AlertMaker.showMaterialDialog(rootStack,anchorPane, Arrays.asList(yesButton,noButton),"Confirm Delete","Are you sure want to delete " + selectedBook.getTitle() + " ?");
    }


    @FXML
    private void handleBookEdit(ActionEvent event){
        Book selectedBook = tableView.getSelectionModel().getSelectedItem();
        if(selectedBook == null){
            return;
        }

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/ui/editBook/editBook.fxml"));
            Parent root = loader.load();

            editBookController controller = (editBookController) loader.getController();
            controller.inflateUI(selectedBook);

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Edit Book");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();

            LibraryAssistantUtil.setStageIcon(stage);

            stage.setOnCloseRequest((e) -> handleRefresh(new ActionEvent()));

        }catch(IOException e){
            Logger.getLogger(mainController.class.getName()).log(Level.SEVERE,null,e);
        }

    }

    @FXML
    private void handleRefresh(ActionEvent event){
        loadData();
    }
}
