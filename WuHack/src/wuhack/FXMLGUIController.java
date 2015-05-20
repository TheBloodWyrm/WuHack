/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wuhack;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
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
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;

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
  private BufferedWriter writer;

  public void onCheckBox() throws Exception
  {
        for(int i = 1; ;i++){
        String formatted = String.format("%02d", i);
        URL findurl = new URL("https://supplierplan.htl-kaindorf.at/supp_neu/21/c/c000" + formatted + ".htm");
        BufferedReader in = new BufferedReader(
        new InputStreamReader(findurl.openStream()));
        BufferedWriter docwriter = new BufferedWriter(new FileWriter("outputfile" + formatted + ".txt"));

        String inputLine;
        while ((inputLine = in.readLine()) != null){
            try{
                docwriter.write(inputLine);
            }
            catch(IOException e){
                e.printStackTrace();
                return;
            }
        }
        in.close();
        docwriter.close();
        }
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
