/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wuhack;

import java.net.Authenticator;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 *
 * @author alle
 */
public class WebBrowserTest extends Application
{

  @Override
  public void start(Stage primaryStage)
  {
    StackPane root = new StackPane();

    WebView browser = new WebView();
    WebEngine webEngine = browser.getEngine();
    webEngine.load("https://supplierplan.htl-kaindorf.at/supp_neu/20/c/c00001.htm");
    
    Authenticator.setDefault(new AuthenticatorTest());
        
    
    root.getChildren().add(browser);
    Scene scene = new Scene(root, 300, 250);

    primaryStage.setTitle("stuff");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args)
  {
    launch(args);
  }

}
