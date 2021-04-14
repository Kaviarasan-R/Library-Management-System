package sample.settings;

import com.google.gson.Gson;
import org.apache.commons.codec.digest.DigestUtils;
import sample.alert.AlertMaker;
import sample.ui.login.loginController;
import sample.ui.main.mainController;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Preferences {

    public static final String CONFIG_FILE = "config.txt";

    int nDays;
    int fine;
    String password = "";

    //String pword = DigestUtils.shaHex(loginPassword.getText());

    public Preferences() {
        nDays = 14;
        fine = 1;
        setPassword(password);
    }

    public static void initConfig(){
        //Writer writer=null;
        try{
            Preferences preference = new Preferences();
            Gson gson = new Gson();
            Writer writer = new FileWriter(CONFIG_FILE);
            gson.toJson(preference,writer);
            writer.close();
        }catch(IOException io){
            Logger.getLogger(mainController.class.getName()).log(Level.SEVERE,null,io);
        }finally {
//            try {
//                writer.close();
//            }catch (IOException e){
//                Logger.getLogger(mainController.class.getName()).log(Level.SEVERE,null,e);
//            }
        }
    }

    public static Preferences getPreferences(){
        Gson gson = new Gson();
        Preferences preferences = new Preferences();
        try{
            preferences = gson.fromJson(new FileReader(CONFIG_FILE), Preferences.class);

        }catch(FileNotFoundException e){
            initConfig();
            Logger.getLogger(mainController.class.getName()).log(Level.SEVERE,null,e);
        }
        return preferences;
    }

    public static void writePreferenceToFile(Preferences preference){
        Writer writer=null;
        try{
            Gson gson = new Gson();
            writer = new FileWriter(CONFIG_FILE);
            gson.toJson(preference,writer);

//            AlertMaker.showSimpleAlert("Success","Settings Updated");

        }catch(IOException io){
            Logger.getLogger(mainController.class.getName()).log(Level.SEVERE,null,io);
//            AlertMaker.showErrorMessage(io,"Failed","Can't save configuration file");
        }finally {
            try {
                writer.close();
            }catch (IOException e){
                Logger.getLogger(mainController.class.getName()).log(Level.SEVERE,null,e);
            }
        }
    }

    public int getnDays() {
        return nDays;
    }

    public void setnDays(int nDays) {
        this.nDays = nDays;
    }

    public int getFine() {
        return fine;
    }

    public void setFine(int fine) {
        this.fine = fine;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = DigestUtils.shaHex(password);
    }

    //    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = DigestUtils.shaHex(password);
//    }
}
