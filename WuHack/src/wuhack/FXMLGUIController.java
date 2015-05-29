/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wuhack;

import java.io.BufferedWriter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
  @FXML
  private Button btUpdate;

  private Kürzel kürzel;
  public URL url;
  private BufferedWriter writer;

  public void onUpdate(ActionEvent event)
  {
      btUpdate.setText("Update");
      DAL.download();
  }
  public void onError(Exception e){
    taConsole.setText(e.getMessage());
  }
  public void setTextOnTextArea(String t){
    taConsole.setText(t);
  }

  @Override
  public void initialize(URL url, ResourceBundle rb)
  {
    btUpdate.setOnAction(this::onUpdate);
  }



}
