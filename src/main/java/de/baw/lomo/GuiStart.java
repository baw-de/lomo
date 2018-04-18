package de.baw.lomo;

import de.baw.lomo.core.OneDimensionalModel;
import de.baw.lomo.gui.Controller;
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
        de.baw.lomo.gui.Messages.RESOURCE_BUNDLE);
    loader.load();  
      
    Controller c = loader.getController();    
    c.initModel(new OneDimensionalModel());
    
    Parent root = loader.getRoot();    
    Scene scene = new Scene(root);
    scene.getStylesheets().add(
        Controller.class.getResource("ui.css").toExternalForm());
    primaryStage.setScene(scene);
    primaryStage.setTitle(Messages.getString("lomo.name"));
    primaryStage.show();
    c.initConsole();
  }
  
  public static void main(String[] args) {
    launch(args);
  }  
}
