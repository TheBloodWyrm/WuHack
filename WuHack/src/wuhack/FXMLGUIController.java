/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wuhack;

import java.io.IOException;
import java.io.StringWriter;
import java.net.Authenticator;
import java.net.URL;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Popup;
import javafx.stage.WindowEvent;
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
  private TableView tvDaten;
  @FXML
  private TextArea taConsole;
  @FXML
  private WebView wv;
  @FXML
  private Button btUpdate;
  @FXML
  private ToggleButton btKlassen, btLehrer, btRaeume;

//    public URL url;
//    private BufferedWriter writer;
  private WebEngine webEngine;
  private ScheduleModel model;
//    private boolean lehrer;

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
          //DAL.download();
          model.loadAllLessons(webEngine, getCalendarWeek());
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
          
          
          String mode = ((TableColumn) tvDaten.getColumns().get(0)).getText();
          
          switch(mode)
          {
            case "Klassen":
              onKlassen(null);
              break;
            case "Lehrer":
              onLehrer(null);
              break;
            case "Räume":
              onRaeume(null);
              break;
          }
          
        }
      }
    });
    webEngine.load("https://supplierplan.htl-kaindorf.at/supp_neu/" + getCalendarWeek() + "/c/c" + String.format("%05d", 1) + ".htm");
    //model.loadAllLessons(webEngine, getCalendarWeek());
    System.out.println("finished");
  }

  public void onError(Exception e)
  {
    taConsole.appendText(e.getMessage());
  }

  public void setTextOnTextArea(String t)
  {
    taConsole.setText(t);
  }

  public void onDaten(MouseEvent event)
  {
    int index = tvDaten.getSelectionModel().getSelectedIndex();
    if (index >= 0)
    {
      String str = tvDaten.getItems().get(index).toString();
      System.out.println("clicked index: " + index + " " + str);

      String mode = ((TableColumn) tvDaten.getColumns().get(0)).getText();
      
      switch(mode)
          {
            case "Klassen":
              HTMLModel.convertToHTMLv3(webEngine, model.getClassLessons(str), str, getCalendarWeek());
              break;
            case "Lehrer":
              HTMLModel.convertToHTMLv3(webEngine, model.getTeacherLessons(str), str, getCalendarWeek());
              break;
            case "Räume":
              HTMLModel.convertToHTMLv3(webEngine, model.getClassroomLessons(str), str, getCalendarWeek());
              break;
          }
    }
  }

//  private void onChange(ActionEvent e)
//  {
//
//    if (tbChange.getText().equals("Lehrer"))
//    {
//      tbChange.setText("Klassen");
//
//      ((TableColumn<TableModel, String>) tvDaten.getColumns().get(0)).setText("Lehrer");
//      tvDaten.getItems().clear();
//
//      for (String s : ScheduleModel.getInstance().getKuerzel())
//      {
//        tvDaten.getItems().add(new TableModel(s, ""));
//      }
//
//    }
//    else
//    {
//      tbChange.setText("Lehrer");
//
//      ((TableColumn<TableModel, String>) tvDaten.getColumns().get(0)).setText("Klassen");
//      tvDaten.getItems().clear();
//
//      for (String s : ScheduleModel.getInstance().getClasses())
//      {
//        tvDaten.getItems().add(new TableModel(s, ""));
//      }
//
//    }
//  }

  private void onKlassen(ActionEvent event)
  {
    enableBut(0);
    
    ((TableColumn<TableModel, String>) tvDaten.getColumns().get(0)).setText("Klassen");
      tvDaten.getItems().clear();

      List<String> data = model.getClasses();
      // data.sort((s1, s2) -> s1.compareToIgnoreCase(s2));
      
      for (String s : data)
      {
        tvDaten.getItems().add(new TableModel(s, ""));
      }
  }

  private void onLehrer(ActionEvent event)
  {
    enableBut(1);
    
    ((TableColumn<TableModel, String>) tvDaten.getColumns().get(0)).setText("Lehrer");
      tvDaten.getItems().clear();

      List<String> data = model.getKuerzel();
      data.sort((s1, s2) -> s1.compareToIgnoreCase(s2));
      
      for (String s : data)
      {
        tvDaten.getItems().add(new TableModel(s, ""));
      }
  }

  private void onRaeume(ActionEvent event)
  {
    enableBut(2);
    
    ((TableColumn<TableModel, String>) tvDaten.getColumns().get(0)).setText("Räume");
      tvDaten.getItems().clear();

      List<String> data = model.getClassrooms();
      data.sort((s1, s2) -> s1.compareToIgnoreCase(s2));
      
      for (String s : data)
      {
        tvDaten.getItems().add(new TableModel(s, ""));
      }
  }

  private void enableBut(int index)
  {
    btKlassen.setDisable(false);
    btLehrer.setDisable(false);
    btRaeume.setDisable(false);

    switch (index)
    {
      case 0:
        btKlassen.setDisable(true);
        break;
      case 1:
        btLehrer.setDisable(true);
        break;
      case 2:
        btRaeume.setDisable(true);
        break;
    }
  }

  public static String convertToString(Document doc)
  {
    try
    {
      StringWriter sw = new StringWriter();
      TransformerFactory tf = TransformerFactory.newInstance();
      Transformer transformer = tf.newTransformer();
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
      transformer.setOutputProperty(OutputKeys.METHOD, "xml");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

      transformer.transform(new DOMSource(doc), new StreamResult(sw));
      return sw.toString();
    }
    catch (Exception ex)
    {
      throw new RuntimeException("Error converting to String", ex);
    }
  }

  @Override
  public void initialize(URL url, ResourceBundle rb)
  {
    btUpdate.setOnAction(this::onUpdate);
    tvDaten.setOnMouseClicked(this::onDaten);

    btKlassen.setOnAction(this::onKlassen);
    btLehrer.setOnAction(this::onLehrer);
    btRaeume.setOnAction(this::onRaeume);

    model = ScheduleModel.getInstance();
    webEngine = wv.getEngine();
  }
}
