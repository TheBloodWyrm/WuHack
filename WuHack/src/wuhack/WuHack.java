/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wuhack;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetAddress;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Paul Petritsch
 */
public class WuHack extends Application
{
  
  @Override
  public void start(Stage stage) throws Exception
  {
    Parent root = FXMLLoader.load(getClass().getResource("FXMLGUI.fxml"));
    
    Scene scene = new Scene(root);
    stage.setTitle("WuHack");
    stage.setScene(scene);
    stage.show();    
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args)
  {
    launch(args);
  }
  
}
