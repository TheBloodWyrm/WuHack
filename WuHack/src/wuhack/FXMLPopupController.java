/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wuhack;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Popup;

/**
 * FXML Controller class
 *
 * @author Julian
 */
public class FXMLPopupController implements Initializable {

    @FXML
    private Button btAuth, btCancel;
    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField pfPassword;
    
    private String user;
    private char[] passwd;
    private boolean isReady;

    public String getUserName() {
        return user;
    }

    public char[] getPassword() {
        return passwd;
    }
    
    public boolean isReady() {
        return isReady;
    }
    
    private void onAuthenticate(ActionEvent e) {
        user = tfUsername.getText();
        passwd = pfPassword.getText().toCharArray();
        isReady = true;
        
        ((Popup) btAuth.getScene().getWindow()).hide();
    }
    
    private void onCancel(ActionEvent e) {
        isReady = false;
        ((Popup) btAuth.getScene().getWindow()).hide();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pfPassword.setOnAction(this::onAuthenticate);
        btAuth.setOnAction(this::onAuthenticate);
        btCancel.setOnAction(this::onCancel);
    }    
    
}
