/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wuhack;

import java.io.IOException;
import java.net.Authenticator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author alle
 */
public class WebBrowserTest extends Application {

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();

        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        root.getChildren().add(browser);
        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("stuff");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Popup führt Anmeldung durch
        FXMLPopupController pop = new FXMLPopupController();
        FXMLLoader l = new FXMLLoader(WebBrowserTest.class.getResource("FXMLPopup.fxml"));
        l.setController(pop);
        Popup auth = new Popup();
        auth.setOnHidden(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                System.out.println(""+pop.isReady());
                Authenticator.setDefault(new AuthenticatorTest(pop.getUserName(), pop.getPasswword()));
                webEngine.load("https://supplierplan.htl-kaindorf.at/supp_neu/20/c/c00001.htm");
            }
        });
        try {
            auth.getContent().add(l.load());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        auth.show(primaryStage);
        
        ScheduleModel m = new ScheduleModel();
        m.analyzeDoc(webEngine.documentProperty().get());
        
    }

    public static void main(String[] args) {
        launch(args);
    }

}
