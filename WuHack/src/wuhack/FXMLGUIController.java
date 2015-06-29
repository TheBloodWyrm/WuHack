/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wuhack;

import java.io.IOException;
import java.net.Authenticator;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Popup;
import javafx.stage.WindowEvent;

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
    private ToggleButton btKlassen, btLehrer, btRaeume;
    @FXML
    private ComboBox<Integer> cbWeeks;

    private WebEngine webEngine = new WebEngine();
    private ScheduleModel model;

    private int getCalendarWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        int week = cal.get(Calendar.WEEK_OF_YEAR);

        return week;
    }

    public void onUpdate(ActionEvent event) {
        Log.log("Update schedule data...");
        Log.log("Authentication for server...");
        FXMLPopupController pop = new FXMLPopupController();
        FXMLLoader l = new FXMLLoader(WebBrowserTest.class.getResource("FXMLPopup.fxml"));
        l.setController(pop);
        Popup auth = new Popup();

        auth.setOnHidden(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                Log.log("Authenticate with \"" + pop.getUserName() + "\" at server");
                Authenticator.setDefault(new AuthenticatorTest(pop.getUserName(), pop.getPassword()));

                if (pop.isReady()) {

                    webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {

                        @Override
                        public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {

                            if (newValue == Worker.State.SUCCEEDED) {
                                cbWeeks.getItems().setAll(model.readWeeks(webEngine.getDocument()));
                                cbWeeks.getSelectionModel().select(0);
                                webEngine.getLoadWorker().stateProperty().removeListener(this);
                                //loadSchedule();
                            }
                        }
                    });
                    webEngine.load("https://supplierplan.htl-kaindorf.at/supp_neu/");

                }
            }
        });

        try {
            auth.getContent().add(l.load());
        } catch (IOException ex) {
            Log.log("Error while displaying popup: " + ex.getMessage());
        }

        auth.show(btUpdate.getScene().getWindow());

        btUpdate.setText("Update");
    }

    private void loadSchedule() {
        Log.log("Loading schedules from https://supplierplan.htl-kaindorf.at/supp_neu/...");
        //DAL.download();
        webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {

            @Override
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                if (newValue == Worker.State.SUCCEEDED) {
                    String mode = ((TableColumn) tvDaten.getColumns().get(0)).getText();
                    switch (mode) {
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

        //model.loadAllLessons(webEngine, getCalendarWeek());
        model.loadAllLessons(webEngine, cbWeeks.getValue());
    }

    private void onWeeks(ActionEvent e) {
        loadSchedule();
    }
    
    public void onDaten(Event event) {

        int index = tvDaten.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            String str = tvDaten.getItems().get(index).toString();

            String mode = ((TableColumn) tvDaten.getColumns().get(0)).getText();

            switch (mode) {
                case "Klassen":
                    Log.log("Load class schedule for " + str);
                    HTMLModel.convertToHTMLv3(wv.getEngine(), model.getClassLessons(str), "Klasse - " + str, getCalendarWeek());
                    break;
                case "Lehrer":
                    Log.log("Load teacher schedule for " + str);
                    HTMLModel.convertToHTMLv3(wv.getEngine(), model.getTeacherLessons(str), "Lehrer - " + str, getCalendarWeek());
                    break;
                case "Räume":
                    Log.log("Load room schedule for " + str);
                    HTMLModel.convertToHTMLv3(wv.getEngine(), model.getClassroomLessons(str), "Raum - " + str, getCalendarWeek());
                    break;
            }

            Log.log("Schedule complete");
        }
    }

    private void onKlassen(ActionEvent event) {
        Log.log("Display classes");
        enableBut(0);

        ((TableColumn<TableModel, String>) tvDaten.getColumns().get(0)).setText("Klassen");
        tvDaten.getItems().clear();

        List<String> data = model.getClasses();
        //data.sort((s1, s2) -> s1.compareToIgnoreCase(s2));
        for (String s : data) {
            tvDaten.getItems().add(new TableModel(s, ""));
        }
    }

    private void onLehrer(ActionEvent event) {
        Log.log("Display teachers");
        enableBut(1);

        ((TableColumn<TableModel, String>) tvDaten.getColumns().get(0)).setText("Lehrer");
        tvDaten.getItems().clear();

        List<String> data = model.getKuerzel();
        data.sort((s1, s2) -> s1.compareToIgnoreCase(s2));

        for (String s : data) {
            tvDaten.getItems().add(new TableModel(s, ""));
        }
    }

    private void onRaeume(ActionEvent event) {
        Log.log("Display rooms");
        enableBut(2);

        ((TableColumn<TableModel, String>) tvDaten.getColumns().get(0)).setText("Räume");
        tvDaten.getItems().clear();

        List<String> data = FXCollections.observableArrayList(model.getClassrooms());
        data.sort((s1, s2) -> s1.compareToIgnoreCase(s2));

        for (String s : data) {
            tvDaten.getItems().add(new TableModel(s, ""));
        }
    }

    private void enableBut(int index) {
        btKlassen.setDisable(false);
        btLehrer.setDisable(false);
        btRaeume.setDisable(false);

        switch (index) {
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Log.log("~~~~~ WuHack ~~~~~");
        Log.log(Calendar.getInstance().getTime().toString());
        Log.log("Initialize...");

        Log.get().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                taConsole.setScrollTop(Double.MAX_VALUE);
            }
        });

        taConsole.textProperty().bind(Log.get());

        btUpdate.setOnAction(this::onUpdate);
        tvDaten.setOnMouseClicked(this::onDaten);
        tvDaten.setOnKeyReleased(this::onDaten);

        btKlassen.setOnAction(this::onKlassen);
        btLehrer.setOnAction(this::onLehrer);
        btRaeume.setOnAction(this::onRaeume);

        cbWeeks.setOnAction(this::onWeeks);
        
        btKlassen.setDisable(true);

        model = ScheduleModel.getInstance();
        //webEngine = wv.getEngine();

        Log.log("Ready to operate");
    }
}
