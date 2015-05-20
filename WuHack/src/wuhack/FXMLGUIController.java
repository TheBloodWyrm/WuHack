/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wuhack;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebEngine;
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
  public URL url;
  
  public void onCheckBox() throws Exception{
    WebEngine webEngine = wv.getEngine();
    for(int i = 1; i < 32; i++){
    url = new URL("https://supplierplan.htl-kaindorf.at/supp_neu/20/c/c0000" + i + ".htm");
    ScheduleModel m = new ScheduleModel();
    m.analyzeDoc(webEngine.documentProperty().get());
    System.out.println(url);
    }
    
    BufferedReader in = new BufferedReader(
    new InputStreamReader(url.openStream()));

    String inputLine;
    while ((inputLine = in.readLine()) != null)
      System.out.println(inputLine);
    in.close();
    
    
    webEngine.load(url);
  }
  
  
  @Override
  public void initialize(URL url, ResourceBundle rb)
  {
    cbLehrer.getItems().addAll(kürzel.values());
    cbLehrer.setPromptText("Lehrer Kürzel");   
    cbLehrer.setOnAction(new EventHandler()
    {

      @Override
      public void handle(Event event)
      {
        try
        {
          onCheckBox();
        } catch (Exception ex)
        {
          System.out.println(ex.getMessage());
        }
      }
    });
  }
  
}
