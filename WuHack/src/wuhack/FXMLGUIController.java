/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wuhack;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebView;

/**
 *
 * @author Paul Petritsch
 */
public class FXMLGUIController implements Initializable
{
  
  @FXML
  private ComboBox cbLehrer;
  @FXML
  private TextArea taConsole;
  @FXML
  private WebView wv;
  
  private Kürzel kürzel;
  
  
  
  @Override
  public void initialize(URL url, ResourceBundle rb)
  {
    cbLehrer.getItems().addAll(kürzel.values());
    cbLehrer.setPromptText("Lehrer Kürzel");   
    
  }
  
}
