package sample.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import sample.ui.bookList.bookListController.Book;
import sample.ui.memberList.memberListController.Member;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseHandler {

    private static DatabaseHandler handler = null;
    private static final String DB_URL = "jdbc:sqlite:E:\\Java Projects\\LibraryAssistant\\lib.db";
    private static Connection conn = null;
    private static Statement statement = null;

    private DatabaseHandler() {
        createConnection();
        createAccount();
        createBookTable();
        createMemberTable();
        createIssueTable();
    }

    public static DatabaseHandler getInstance() {
        if(handler == null){
            handler = new DatabaseHandler();
        }
        return handler;
    }

    void createConnection() {
        try{
            conn = DriverManager.getConnection(DB_URL);
        }catch (Exception sql){
            sql.printStackTrace();
        }
    }

    void createBookTable() {
        String TABLE_NAME = "BOOK";
        try{
            statement = conn.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS '"+TABLE_NAME+"'(" +
                    " id varchar(200) primary key," +
                    " title varchar(200)," +
                    " author varchar(200)," +
                    " publisher varchar(100)," +
                    " isavail INTEGER DEFAULT 1) ");
            statement.close();
        }catch(Exception e) {
            System.err.println(e.getMessage());
        }
    }

    void createMemberTable() {
        String TABLE_NAME = "MEMBER";
        try{
            statement = conn.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS '"+TABLE_NAME+"'(" +
                    " id varchar(200) primary key," +
                    " name varchar(200)," +
                    " mobile varchar(20)," +
                    " email varchar(100) )");

            statement.close();
        }catch(Exception e) {
            System.err.println(e.getMessage());
        }
    }

    void createAccount() {
        String TABLE_NAME = "ACCOUNT";
        try{
            statement = conn.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS '"+TABLE_NAME+"'(" +
                    " user varchar(200)," +
                    " email varchar(200)," +
                    " password varchar(100) )");

            statement.close();
        }catch(Exception e) {
            System.err.println(e.getMessage());
        }
    }

    void createIssueTable() {
        String TABLE_NAME = "ISSUE";
        try{
            statement = conn.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS '"+TABLE_NAME+"'(" +
                    " bookID varchar(200) primary key," +
                    " memberID varchar(200)," +
                    " issueDate date default CURRENT_DATE," +
                    " renew_count integer default 0," +
                    " FOREIGN KEY (bookID) REFERENCES BOOK(id)," +
                    " FOREIGN KEY (memberID) REFERENCES MEMBER(id) )");

            statement.close();
        }catch(Exception e) {
            System.err.println(e.getMessage());
        }

    }

    public ResultSet executeQuery(String query){
        ResultSet result;
        try{
            statement = conn.createStatement();
            result = statement.executeQuery(query);
        }catch (Exception e){
            System.out.println("Error while doing executeQuery " + e.getLocalizedMessage());
            return null;
        }
        finally {}
        return result;
    }

    public boolean executeAction(String query){
        try{
            statement = conn.createStatement();
            statement.execute(query);
            return true;
        }catch (Exception e){
            System.out.println("Error while doing executeAction " + e.getMessage());
            return false;
        }finally {}
    }

    public boolean deleteBook(Book book){

        try{
            String deleteStatement = "DELETE FROM BOOK WHERE ID = ?";
            PreparedStatement prep = conn.prepareStatement(deleteStatement);
            prep.setString(1,book.getId());
            int res = prep.executeUpdate();
            if (res == 1){
                return true;
            }
        }catch (SQLException e){
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE,null,e);
        }

        return false;
    }

    public boolean deleteMember(Member member){

        try{
            String deleteStatement = "DELETE FROM MEMBER WHERE ID = ?";
            PreparedStatement prep = conn.prepareStatement(deleteStatement);
            prep.setString(1,member.getId());
            int res = prep.executeUpdate();
            if (res == 1){
                return true;
            }
        }catch (SQLException e){
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE,null,e);
        }

        return false;
    }

    public boolean isAlreadyIssued(Book book){

        try{
            String checkStatement = "SELECT COUNT(*) FROM ISSUE WHERE bookid=?";
            PreparedStatement prep = conn.prepareStatement(checkStatement);
            prep.setString(1,book.getId());
            ResultSet rs = prep.executeQuery();
            if (rs.next()){
                int count = rs.getInt(1);
                return (count>0);
            }
        }catch (SQLException e){
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE,null,e);
        }

        return false;
    }

    public boolean isAlreadyIssued2(Member member){

        try{
            String checkStatement = "SELECT COUNT(*) FROM ISSUE WHERE memberid=?";
            PreparedStatement prep = conn.prepareStatement(checkStatement);
            prep.setString(1,member.getId());
            ResultSet rs = prep.executeQuery();
            if (rs.next()){
                int count = rs.getInt(1);
                return (count>0);
            }
        }catch (SQLException e){
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE,null,e);
        }

        return false;
    }


    public ObservableList<PieChart.Data> getBookGraphStatistics() {
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        try {
            String qu1 = "SELECT COUNT(*) FROM BOOK";
            String qu2 = "SELECT COUNT(*) FROM ISSUE";
            ResultSet rs = executeQuery(qu1);
            if (rs.next()) {
                int count = rs.getInt(1);
                data.add(new PieChart.Data("Total Books (" + count + ")", count));
            }
            rs = executeQuery(qu2);
            if (rs.next()) {
                int count = rs.getInt(1);
                data.add(new PieChart.Data("Issued Books (" + count + ")", count));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public ObservableList<PieChart.Data> getMemberGraphStatistics() {
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        try {
            String qu1 = "SELECT COUNT(*) FROM MEMBER";
            String qu2 = "SELECT COUNT(DISTINCT memberID) FROM ISSUE";
            ResultSet rs = executeQuery(qu1);
            if (rs.next()) {
                int count = rs.getInt(1);
                data.add(new PieChart.Data("Total Members (" + count + ")", count));
            }
            rs = executeQuery(qu2);
            if (rs.next()) {
                int count = rs.getInt(1);
                data.add(new PieChart.Data("Active (" + count + ")", count));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }


}
