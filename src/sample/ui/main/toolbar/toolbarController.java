package sample.ui.main.toolbar;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import sample.util.LibraryAssistantUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class toolbarController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void loadMember(ActionEvent event){
        LibraryAssistantUtil.loadWindow(getClass().getResource("/sample/ui/addMember/add_member.fxml"),"Add Member",null);
    }

    @FXML
    public void loadBook(ActionEvent event){
        LibraryAssistantUtil.loadWindow(getClass().getResource("/sample/ui/addBook/add_book.fxml"),"Add Book",null);
    }

    @FXML
    public void loadMemberList(ActionEvent event){
        LibraryAssistantUtil.loadWindowTable(getClass().getResource("/sample/ui/memberList/member_list.fxml"),"View Member",null);
    }

    @FXML
    public void loadBookList(ActionEvent event){
        LibraryAssistantUtil.loadWindowTable(getClass().getResource("/sample/ui/bookList/book_list.fxml"),"View Book",null);
    }

    @FXML
    public void loadSettings(ActionEvent event){
        LibraryAssistantUtil.loadWindow(getClass().getResource("/sample/settings/settings.fxml"),"Settings",null);
    }
}
