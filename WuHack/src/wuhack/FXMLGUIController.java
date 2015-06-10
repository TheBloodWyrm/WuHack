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
import java.util.Arrays;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Popup;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

/**
 *
 * @author Paul Petritsch
 */
public class FXMLGUIController implements Initializable
{

  @FXML
  private TableView tvDaten;
  @FXML
  private TextArea taConsole;
  @FXML
  private WebView wv;
  @FXML
  private Button btUpdate;

  @FXML
  private ToggleButton btLehrer, btKlassen;

  public URL url;
  private BufferedWriter writer;
  private WebEngine webEngine;
  private ScheduleModel model;
  private boolean lehrer;

  private int getCalendarWeek()
  {
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(System.currentTimeMillis());
    int week = cal.get(Calendar.WEEK_OF_YEAR);

    return week;
  }

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

        if (pop.isReady())
        {
          load();
        }
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

  private void load()
  {
    System.out.println("load");

    webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>()
    {

      @Override
      public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue)
      {
        if (newValue == Worker.State.SUCCEEDED)
        {

          System.out.println(model.analyzeDoc(webEngine.getDocument(), getCalendarWeek(), 1)[0][0]);
        }
      }
    });
    webEngine.load("https://supplierplan.htl-kaindorf.at/supp_neu/" + getCalendarWeek() + "/c/c" + String.format("%05d", 1) + ".htm");
    //model.loadAllLessons(webEngine, getCalendarWeek());
    System.out.println("finished");
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

    //tvDaten.getItems().clear();
    TableColumn<String, String> col = new TableColumn<>("Kürzel");
    col.setCellValueFactory((CellDataFeatures<String, String> s) -> (ObservableValue<String>) ScheduleModel.getKuerzel());
    col.setCellValueFactory((CellDataFeatures<String, String> s) -> (ObservableValue<String>) Arrays.asList("asdf", "adslkf", "lksjsdlk"));
    
    tvDaten.getColumns().clear();
    tvDaten.getColumns().add(col);
    
    ObservableList<String> data = tvDaten.getItems();
    data.clear();
    data.addAll(ScheduleModel.getKuerzel());
    data.addAll(Arrays.asList("asdf", "adslkf", "lksjsdlk"));
    
    col.setPrefWidth(200);
    
    
    
    //tvDaten.getItems().addAll(Arrays.asList("asdf", "adslkf", "lksjsdlk"));
  }

public void onDaten(MouseEvent event)
  {
    int index = tvDaten.getSelectionModel().getSelectedIndex();
    if(index >= 0)
    {
      System.out.println("clicked index: " + tvDaten.getItems().get(index));
    }
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
    tvDaten.setOnMouseClicked(this::onDaten);

    model = ScheduleModel.getInstance();//new ScheduleModel();
    webEngine = wv.getEngine();

    webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>()
    {

      @Override
        public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue)
      {
        System.out.println(newValue);
        if (newValue == Worker.State.SUCCEEDED)
        {

          //webEngine.loadContent(convertToString(HTMLModel.convertToHTML(schedule, "1AHIF", getCalendarWeek())));
        }
      }
    });
  }
}
