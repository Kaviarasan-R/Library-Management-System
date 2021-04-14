package sample.ui.main;

import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.alert.AlertMaker;
import sample.database.DatabaseHandler;
import sample.util.LibraryAssistantUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class mainController implements Initializable {

    @FXML
    private TextField bookID;

    @FXML
    private Text bookName;

    @FXML
    private Text bookAuthor;

    @FXML
    private Text bookStatus;

    @FXML
    private TextField memberID;

    @FXML
    private Text memberName;

    @FXML
    private Text memberMobile;

    @FXML
    private TextField bookID2;

    @FXML
    private StackPane rootPane;

    @FXML
    private BorderPane rootBorderPane;

    Boolean isReadyForSubmission = false;

    DatabaseHandler handler;

    @FXML
    private JFXHamburger hamburger;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private Text memberNameHolder;

    @FXML
    private Text memberEmailHolder;

    @FXML
    private Text memberMobileHolder;

    @FXML
    private Text bookTitleHolder;

    @FXML
    private Text bookAuthorHolder;

    @FXML
    private Text bookPublisherHolder;

    @FXML
    private Text issuedDateHolder;

    @FXML
    private Text noDaysHolder;

    @FXML
    private JFXButton renewButton;

    @FXML
    private JFXButton submissionButton;

    @FXML
    private JFXButton issueButton;

    @FXML
    private HBox hBox;

    @FXML
    private StackPane pieStack;

    @FXML
    private StackPane pieStack2;

    @FXML
    private Tab bookIssueTab;

    PieChart bookChart;
    PieChart memberChart;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handler = DatabaseHandler.getInstance();

        initDrawer();
        initGraph();
    }

    private void initGraph() {
        bookChart = new PieChart(handler.getBookGraphStatistics());
        memberChart = new PieChart(handler.getMemberGraphStatistics());
        pieStack.getChildren().add(bookChart);
        pieStack2.getChildren().add(memberChart);

        bookIssueTab.setOnSelectionChanged((Event event) -> {
            clearIssueEntries();
            if (bookIssueTab.isSelected()) {
                refreshGraphs();
            }
        });
    }

    private void refreshGraphs() {
        bookChart.setData(handler.getBookGraphStatistics());
        memberChart.setData(handler.getMemberGraphStatistics());
    }

    private void enableDisableGraph(Boolean status) {
        if (status) {
            bookChart.setOpacity(1);
            memberChart.setOpacity(1);
        } else {
            bookChart.setOpacity(0);
            memberChart.setOpacity(0);
        }
    }

    private void initDrawer() {
        try{
            VBox toolbar = FXMLLoader.load(getClass().getResource("/sample/ui/main/toolbar/toolbar.fxml"));
            drawer.setSidePane(toolbar);
            drawer.setMinWidth(0);
            drawer.setDefaultDrawerSize(150);
        }catch (IOException e){
            Logger.getLogger(mainController.class.getName()).log(Level.SEVERE,null,e);
        }

        HamburgerSlideCloseTransition tasks = new HamburgerSlideCloseTransition(hamburger);
        tasks.setRate(-1);
        hamburger.addEventHandler(MouseEvent.MOUSE_CLICKED, (Event event) -> {
            tasks.setRate(tasks.getRate() * -1);
            tasks.play();
            if (drawer.isClosed()) {
                drawer.open();
                drawer.setMinWidth(150);
            } else {
                drawer.close();
                drawer.setMinWidth(0);
            }
        });
    }



    @FXML
    private void loadBookAction(ActionEvent event){
        enableDisableGraph(false);

        String id = bookID.getText();
        String query = "SELECT * FROM BOOK WHERE id = '"+id+"'";
        ResultSet rs = handler.executeQuery(query);
        Boolean flag = false;
        try{
            while(rs.next()){
                String name = rs.getString("title");
                String author = rs.getString("author");
                Boolean bstatus = rs.getBoolean("isavail");

                bookName.setText(name);
                bookAuthor.setText(author);
                String status = (bstatus)?"Available" : "Not Available";
                bookStatus.setText(status);
                flag = true;

                if(bookID.getText().isEmpty() && memberID.getText().isEmpty()){

                }else if(bookID.getText().isEmpty() || memberID.getText().isEmpty()){

                }else {
                    disableButton2(true);
                }
            }
            if(!flag){
                bookAuthor.setText("No such book available");
                bookName.setText("");
                bookStatus.setText("");
            }

        }catch(Exception e){
            Logger.getLogger(mainController.class.getName()).log(Level.SEVERE,null,e);
        }
    }

    @FXML
    private void loadMemberAction(ActionEvent event){
        enableDisableGraph(false);

        String id = memberID.getText();
        String query = "SELECT * FROM MEMBER WHERE id = '"+id+"'";
        ResultSet rs = handler.executeQuery(query);
        Boolean flag = false;
        try{
            while(rs.next()){
                String name = rs.getString("name");
                String mobile = rs.getString("mobile");

                memberName.setText(name);
                memberMobile.setText(mobile);
                flag = true;

                if(bookID.getText().isEmpty() && memberID.getText().isEmpty()){

                }else if(bookID.getText().isEmpty() || memberID.getText().isEmpty()){

                }else {
                    disableButton2(true);
                }
            }
            if(!flag){
                memberName.setText("Member not found");
                memberMobile.setText("");
            }

        }catch(Exception e){
            Logger.getLogger(mainController.class.getName()).log(Level.SEVERE,null,e);
        }
    }

    @FXML
    private void loadIssue(ActionEvent event){
        String mID = memberID.getText();
        String bID = bookID.getText();

        String query1 = "SELECT * FROM BOOK WHERE id = '"+bID+"'";
        String query2 = "SELECT * FROM MEMBER WHERE id = '"+mID+"'";

        ResultSet rs1 = handler.executeQuery(query1);
        ResultSet rs2 = handler.executeQuery(query2);

        Boolean flag = false;
        try{
            while(rs1.next() && rs2.next()){

                String query3 = "SELECT * FROM ISSUE WHERE bookID = '"+bID+"'";
                ResultSet rs3 = handler.executeQuery(query3);

                if (rs3.next()) {
                    JFXButton button = new JFXButton("Okay");
                    AlertMaker.showMaterialDialog(rootPane,rootBorderPane, Arrays.asList(button),"Oops!!","Sorry, this book is already issued");
                    return;
                }else {
                    JFXButton yesButton = new JFXButton("YES");
                    yesButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event1) -> {
                        String str = "INSERT INTO ISSUE(bookID,memberID) VALUES('"+bID+"','"+mID+"')";
                        String str2 = " UPDATE BOOK SET isavail = FALSE WHERE ID = '"+bID+"' ";

                        if(handler.executeAction(str) && handler.executeAction(str2) ){
                            JFXButton button = new JFXButton("OK");
                            AlertMaker.showMaterialDialog(rootPane,rootBorderPane, Arrays.asList(button),"Hurray!!","Book Issued Successfully");
                            refreshGraphs();
                        }else{
                            JFXButton button = new JFXButton("Okay, I'll Check");
                            AlertMaker.showMaterialDialog(rootPane,rootBorderPane, Arrays.asList(button),"Oops!!","Sorry, couldn't Issue the book");
                        }
                        clearIssueEntries();
                    });

                    JFXButton noButton = new JFXButton("NO");
                    noButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event1) -> {
                        clearIssueEntries();
                        return;
                    });

                    AlertMaker.showMaterialDialog(rootPane,rootBorderPane, Arrays.asList(yesButton,noButton),"Confirm Issue","Are you sure want to issue the book "+ rs1.getString("title") + " to " + rs2.getString("name") + " ?");

                }

                flag = true;
            }
            if(!flag){

                JFXButton button = new JFXButton("OKAY");
                AlertMaker.showMaterialDialog(rootPane,rootBorderPane, Arrays.asList(button),":(","Sorry, Please enter correct details");
            }

        }catch(Exception e){
            Logger.getLogger(mainController.class.getName()).log(Level.SEVERE,null,e);
        }

    }

    private void clearIssueEntries() {
        bookID.clear();
        memberID.clear();
        bookName.setText("");
        bookAuthor.setText("");
        bookStatus.setText("");
        memberName.setText("");
        memberMobile.setText("");

        disableButton2(false);
        enableDisableGraph(true);
    }

    private void disableButton2(boolean b) {
        if(b){
            issueButton.setDisable(false);
        }
        else {
            issueButton.setDisable(true);
        }
    }

    @FXML
    private void loadBookinfo(ActionEvent event){
        clearEntries();
        isReadyForSubmission = false;

        try{
            String id = bookID2.getText();
            String myQuery = "SELECT ISSUE.bookID, ISSUE.memberID, ISSUE.issueDate, ISSUE.renew_count," +
                    "MEMBER.name, MEMBER.mobile, MEMBER.email," +
                    "BOOK.title,BOOK.author,BOOK.publisher,BOOK.isavail\n" +
                    "FROM ISSUE\n" +
                    "LEFT JOIN MEMBER\n" +
                    "ON ISSUE.memberID = MEMBER.ID\n" +
                    "LEFT JOIN BOOK\n" +
                    "ON ISSUE.bookID = BOOK.ID\n" +
                    "WHERE ISSUE.bookID = '"+id+"' ";

            ResultSet rs = handler.executeQuery(myQuery);

            if (rs.next()){
                memberNameHolder.setText(rs.getString("name"));
                memberEmailHolder.setText(rs.getString("email"));
                memberMobileHolder.setText(rs.getString("mobile"));

                bookTitleHolder.setText(rs.getString("title"));
                bookAuthorHolder.setText(rs.getString("author"));
                bookPublisherHolder.setText(rs.getString("publisher"));


                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date date = df.parse(rs.getString("issueDate"));
                Date time = new Date(date.getTime());
                issuedDateHolder.setText(time.toString());

                Long timeElapsed = System.currentTimeMillis() - date.getTime();
                Long days = TimeUnit.DAYS.convert(timeElapsed,TimeUnit.MILLISECONDS);
                noDaysHolder.setText(days.toString());

                isReadyForSubmission = true;
                disableButton(true);
                hBox.setOpacity(1);

            } else{
                JFXButton button = new JFXButton("Okay, I'll Check");
                AlertMaker.showMaterialDialog(rootPane,rootBorderPane, Arrays.asList(button),"oh no!!","No such book exists in Issue Records");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void clearEntries() {
        memberNameHolder.setText("");
        memberEmailHolder.setText("");
        memberMobileHolder.setText("");

        bookTitleHolder.setText("");
        bookAuthorHolder.setText("");
        bookPublisherHolder.setText("");

        issuedDateHolder.setText("");
        noDaysHolder.setText("");

        disableButton(false);
        hBox.setOpacity(0);
    }

    private void disableButton(boolean b) {
        if(b){
            renewButton.setDisable(false);
            submissionButton.setDisable(false);
        }
        else {
            renewButton.setDisable(true);
            submissionButton.setDisable(true);
        }
    }

    @FXML
    private void loadSubmission(ActionEvent event){
        if(!isReadyForSubmission){

            JFXButton button = new JFXButton("Okay, I'll Check");
            AlertMaker.showMaterialDialog(rootPane,rootBorderPane, Arrays.asList(button),"Oops!!","Please enter a book id to submit");

        }else{

            JFXButton yesButton = new JFXButton("YES");
            yesButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event1) -> {
                String id = bookID2.getText();
                String query = "DELETE FROM ISSUE WHERE bookID = '"+id+"'";
                String query2 = "UPDATE BOOK SET isavail = TRUE WHERE ID = '"+id+"'";

                if(handler.executeAction(query) && handler.executeAction(query2)){
                    JFXButton button = new JFXButton("OK");
                    AlertMaker.showMaterialDialog(rootPane,rootBorderPane, Arrays.asList(button),"Aww!!","Book Submitted Successfully");
                    clearEntries();
                }else{
                    JFXButton button = new JFXButton("OK");
                    AlertMaker.showMaterialDialog(rootPane,rootBorderPane, Arrays.asList(button),"Sorry","Submission Failed");
                }
            });

            JFXButton noButton = new JFXButton("NO");
            noButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event1) -> {
                return;
            });

            AlertMaker.showMaterialDialog(rootPane,rootBorderPane, Arrays.asList(yesButton,noButton),"Confirm Submission","Are you sure want to return the book ?");

        }
    }

    @FXML
    private void loadRenew(ActionEvent event){

        if(!isReadyForSubmission){

            JFXButton button = new JFXButton("Okay, I'll Check");
            AlertMaker.showMaterialDialog(rootPane,rootBorderPane, Arrays.asList(button),"oh oh!!","Please enter a book id to submit");

        }else{

            JFXButton yesButton = new JFXButton("YES");
            yesButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event1) -> {
                String id = bookID2.getText();
                String query = "UPDATE ISSUE SET issueDate = CURRENT_DATE, renew_count = renew_count + 1 WHERE bookID = '" + id + "'";

                if(handler.executeAction(query)){
                    JFXButton button = new JFXButton("OK");
                    AlertMaker.showMaterialDialog(rootPane,rootBorderPane, Arrays.asList(button),"Awesome!","Book Renewed Successfully");
                    clearEntries();
                }else{
                    JFXButton button = new JFXButton("OK");
                    AlertMaker.showMaterialDialog(rootPane,rootBorderPane, Arrays.asList(button),":(","Sorry, Renew Failed");
                }
            });

            JFXButton noButton = new JFXButton("NO");
            noButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event1) -> {
                return;
            });

            AlertMaker.showMaterialDialog(rootPane,rootBorderPane, Arrays.asList(yesButton,noButton),"Confirm Renewal","Are you sure want to renew the book ?");

        }
    }

    @FXML
    private void menuClose(ActionEvent event){
        ((Stage)rootPane.getScene().getWindow()).close();
    }

    @FXML
    private void menuBook(ActionEvent event){
        LibraryAssistantUtil.loadWindow(getClass().getResource("/sample/ui/addBook/add_book.fxml"),"Add Book",null);
    }

    @FXML
    private void menuMember(ActionEvent event){
        LibraryAssistantUtil.loadWindow(getClass().getResource("/sample/ui/addMember/add_member.fxml"),"Add Member",null);
    }

    @FXML
    private void menuViewBook(ActionEvent event){
        LibraryAssistantUtil.loadWindowTable(getClass().getResource("/sample/ui/bookList/book_list.fxml"),"View Book",null);
    }

    @FXML
    private void menuViewMember(ActionEvent event){
        LibraryAssistantUtil.loadWindowTable(getClass().getResource("/sample/ui/memberList/member_list.fxml"),"View Member",null);
    }

    @FXML
    private void menuAbout(ActionEvent event){
        LibraryAssistantUtil.loadWindow(getClass().getResource("/sample/ui/about/about.fxml"),"About",null);
    }
}
