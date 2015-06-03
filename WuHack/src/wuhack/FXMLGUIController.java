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
import java.util.Calendar;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
<<<<<<< HEAD
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import static wuhack.WebBrowserTest.convertToString;
=======
import javafx.scene.control.ToggleButton;
import javafx.scene.web.WebView;
import javafx.stage.Popup;
import javafx.stage.WindowEvent;
>>>>>>> origin/master

/**
 *
 * @author Paul Petritsch
 */
public class FXMLGUIController implements Initializable {
    
    @FXML
    private ComboBox cbLehrer;
    @FXML
    private TextArea taConsole;
    @FXML
    private WebView wv;
    @FXML
    private Button btUpdate;
    
    public URL url;
    private BufferedWriter writer;
    private WebEngine webEngine;
    private ScheduleModel model;
    
    public void onUpdate(ActionEvent event) {
        btUpdate.setText("Update");
        System.out.println("load");
        webEngine.load("https://supplierplan.htl-kaindorf.at/supp_neu/" + getCalendarWeek() + "/c/c" + String.format("%05d", 1) + ".htm");
        System.out.println(model.analyzeDoc(webEngine.getDocument(), getCalendarWeek(), 1)[0].toString());
        //model.loadAllLessons(webEngine, getCalendarWeek());
        System.out.println("finished");
        DAL.download();
        
    }

    public void onError(Exception e) {
        taConsole.setText(e.getMessage());
    }

<<<<<<< HEAD
    public void setTextOnTextArea(String t) {
        taConsole.setText(t);
    }
    
    private int getCalendarWeek()
  {
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(System.currentTimeMillis());
    int week = cal.get(Calendar.WEEK_OF_YEAR);

    return week;
  }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = new ScheduleModel();
        webEngine = wv.getEngine();
        
        webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            
            @Override
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                System.out.println(newValue);
                if (newValue == Worker.State.SUCCEEDED) {
                    
                    //webEngine.loadContent(convertToString(HTMLModel.convertToHTML(schedule, "1AHIF", getCalendarWeek())));
                }
            }
        });
        
        btUpdate.setOnAction(this::onUpdate);
    }
    
=======
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

>>>>>>> origin/master
}
