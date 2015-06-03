/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wuhack;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Authenticator;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.web.WebView;
import javafx.stage.Popup;
import javafx.stage.WindowEvent;

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

  @FXML
  private ToggleButton btLehrer, btKlassen;

  private Kürzel kürzel;
  public URL url;
  private BufferedWriter writer;
  private boolean lehrer = true;

  public void onUpdate(ActionEvent event)
  {
    FXMLPopupController pop = new FXMLPopupController();
    FXMLLoader l = new FXMLLoader(WebBrowserTest.class.getResource("FXMLPopup.fxml"));
    l.setController(pop);
    Popup auth = new Popup();
    auth.setOnHidden(new EventHandler<WindowEvent>()
    {

      @Override
      public void handle(WindowEvent event)
      {
        System.out.println("" + pop.isReady());
        Authenticator.setDefault(new AuthenticatorTest(pop.getUserName(), pop.getPassword()));
      }
    });
    try
    {
      auth.getContent().add(l.load());
    }
    catch (IOException ex)
    {
      System.out.println(ex.getMessage());
    }
    auth.show(btUpdate.getScene().getWindow());

    btUpdate.setText("Update");
    DAL.download();
  }

  public void onError(Exception e)
  {
    taConsole.setText(e.getMessage());
  }

  public void setTextOnTextArea(String t)
  {
    taConsole.setText(t);
  }

  public void onLehrer(ActionEvent event)
  {
    btKlassen.setDisable(false);
    btLehrer.setDisable(true);
    lehrer = true;
  }

  public void onKlassen(ActionEvent event)
  {
    btKlassen.setDisable(true);
    btLehrer.setDisable(false);
    lehrer = false;
  }

  @Override
  public void initialize(URL url, ResourceBundle rb)
  {
    btUpdate.setOnAction(this::onUpdate);
    btLehrer.setDisable(true);
    btLehrer.setOnAction(this::onLehrer);
    btKlassen.setOnAction(this::onKlassen);
  }

}
