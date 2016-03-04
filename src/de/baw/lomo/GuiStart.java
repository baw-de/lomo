package de.baw.lomo;

import de.baw.lomo.core.Schleuse;
import de.baw.lomo.gui.Controller;
import de.baw.lomo.gui.Messages;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GuiStart extends Application {   
  
  @Override
  public void start(Stage primaryStage) throws Exception {
    
//    Locale.setDefault(Locale.ENGLISH);
      
    FXMLLoader loader = new FXMLLoader(Controller.class.getResource("ui.fxml"), 
        Messages.RESOURCE_BUNDLE);
    loader.load();  
      
    Controller c = loader.getController();    
    c.initModel(new Schleuse());
    
    Parent root = loader.getRoot();    
    primaryStage.setScene(new Scene(root));
    primaryStage.setTitle(Messages.getString("appTitle"));
    primaryStage.show();
    c.initConsole();
  }
  
  public static void main(String[] args) {
    launch(args);
  }  
}
