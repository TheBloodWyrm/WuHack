/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wuhack;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.net.Authenticator;
import java.net.HttpRetryException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;
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
public class FXMLGUIController implements Initializable {

    @FXML
    private TableView tvDaten;
    @FXML
    private TextArea taConsole;
    @FXML
    private WebView wv;
    @FXML
    private Button btUpdate;
    @FXML
    private ToggleButton tbChange;

//    public URL url;
//    private BufferedWriter writer;
    private WebEngine webEngine;
    private ScheduleModel model;
//    private boolean lehrer;

    private int getCalendarWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        int week = cal.get(Calendar.WEEK_OF_YEAR);

        return week;
    }

    public void onUpdate(ActionEvent event) {
        FXMLPopupController pop = new FXMLPopupController();
        FXMLLoader l = new FXMLLoader(WebBrowserTest.class.getResource("FXMLPopup.fxml"));
        l.setController(pop);
        Popup auth = new Popup();

        auth.setOnHidden(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                System.out.println("" + pop.isReady());
                Authenticator.setDefault(new AuthenticatorTest(pop.getUserName(), pop.getPassword()));

                if (pop.isReady()) {
                    DAL.download();
                    model.loadAllLessons(webEngine, getCalendarWeek());
                }
            }
        });

        try {
            auth.getContent().add(l.load());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        auth.show(btUpdate.getScene().getWindow());

        btUpdate.setText("Update");

//        if (lehrer) {
//            onLehrer(null);
//        } else {
//            onKlassen(null);
//        }
    }

    private void load() {
        System.out.println("load");

        webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {

            @Override
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                if (newValue == Worker.State.SUCCEEDED) {

                    System.out.println(model.analyzeDoc(webEngine.getDocument(), getCalendarWeek(), 1)[0][0]);
                    onChange(null);
                }
            }
        });
        webEngine.load("https://supplierplan.htl-kaindorf.at/supp_neu/" + getCalendarWeek() + "/c/c" + String.format("%05d", 1) + ".htm");
        //model.loadAllLessons(webEngine, getCalendarWeek());
        System.out.println("finished");
    }

    public void onError(Exception e) {
        taConsole.appendText(e.getMessage());
    }

    public void setTextOnTextArea(String t) {
        taConsole.setText(t);
    }

//    public void onLehrer(ActionEvent event) {
//        //btKlassen.setDisable(false);
//        //btLehrer.setDisable(true);
//        lehrer = true;
//
//        ((TableColumn<TableModel, String>) tvDaten.getColumns().get(0)).setText("Lehrer");
//        tvDaten.getItems().clear();
//        ObservableList<TableModel> data = FXCollections.observableArrayList();
//        for (String s : ScheduleModel.getInstance().getKuerzel()) {
//            data.add(new TableModel(s, ""));
//        }
//        tvDaten.setItems(data);
//    }
    public void onDaten(MouseEvent event) {
        int index = tvDaten.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            String str = tvDaten.getItems().get(index).toString();
            System.out.println("clicked index: " + index + str);
            
            String mode = ((TableColumn) tvDaten.getColumns().get(0)).getText();
            
            if(mode.equals("Lehrer")) { //!tbChange.getText().equals("Lehrer")
                System.out.println("Lehrer");
                webEngine.loadContent(convertToString(HTMLModel.convertToHTML(model.getTeacherLessons(str), str, getCalendarWeek())));
            } else {
                System.out.println("Klasse");
                webEngine.loadContent(convertToString(HTMLModel.convertToHTML(model.getClassroomsLessons(str), str, getCalendarWeek())));
            }
        }
    }

//    public void onKlassen(ActionEvent event) {
//        //btKlassen.setDisable(true);
//        //btLehrer.setDisable(false);
//        lehrer = false;
//
//        ((TableColumn<TableModel, String>) tvDaten.getColumns().get(0)).setText("Klassen");
//        tvDaten.getItems().clear();
//        ObservableList<TableModel> data = FXCollections.observableArrayList();
//        for (String s : ScheduleModel.getInstance().getClasses()) {
//            data.add(new TableModel(s, ""));
//        }
//        tvDaten.setItems(data);
//    }
    private void onChange(ActionEvent e) {

        if (tbChange.getText().equals("Lehrer")) {
            tbChange.setText("Klassen");

            ((TableColumn<TableModel, String>) tvDaten.getColumns().get(0)).setText("Lehrer");
            tvDaten.getItems().clear();

            for (String s : ScheduleModel.getInstance().getKuerzel()) {
                tvDaten.getItems().add(new TableModel(s, ""));
            }

        } else {
            tbChange.setText("Lehrer");

            ((TableColumn<TableModel, String>) tvDaten.getColumns().get(0)).setText("Klassen");
            tvDaten.getItems().clear();

            for (String s : ScheduleModel.getInstance().getClasses()) {
                tvDaten.getItems().add(new TableModel(s, ""));
            }

        }
    }
    
     public static String convertToString(Document doc) {
        try {
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            return sw.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Error converting to String", ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btUpdate.setOnAction(this::onUpdate);
        tbChange.setOnAction(this::onChange);
        tvDaten.setOnMouseClicked(this::onDaten);

        model = ScheduleModel.getInstance();
        webEngine = wv.getEngine();
    }
}
