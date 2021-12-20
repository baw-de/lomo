/*
 * Copyright (c) 2019-2021 Bundesanstalt für Wasserbau
 *
 * This file is part of LoMo.
 *
 * LoMo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * LoMo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LoMo.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.baw.lomo.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import de.baw.lomo.core.OneDimensionalModel;
import de.baw.lomo.core.data.Case;
import de.baw.lomo.io.IOUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
  
  @Override
  public void start(Stage primaryStage) throws Exception {
    
    Locale.setDefault(determineLocale());
      
    FXMLLoader loader = new FXMLLoader(Controller.class.getResource("ui.fxml"), 
        de.baw.lomo.gui.Messages.RESOURCE_BUNDLE);
    loader.load();  
      
    Controller c = loader.getController(); 
    c.setHostServices(getHostServices());
    
    Case data;
    
    try {
      data = IOUtils.readCaseFromXml(new File(getParameters().getRaw().get(0)));
    } catch (Exception e) {
      data = new Case();
    }    
    
    c.initModel(new OneDimensionalModel(),data);
    
    Parent root = loader.getRoot();    
    Scene scene = new Scene(root);
    scene.getStylesheets().add(
        Controller.class.getResource("ui.css").toExternalForm());
    primaryStage.setScene(scene);
    primaryStage.setTitle(ResourceBundle.getBundle("de.baw.lomo.version").getString("lomo.name"));
    primaryStage.show();
    c.initGUI();
    c.initConsole();
  }

  private Locale determineLocale() {

    try {
      Properties settings = new Properties();
      settings.load(new FileInputStream(App.getUserSettings()));
      String lang = settings.getProperty("lang");
      return Locale.forLanguageTag(lang);
    } catch (IOException e) {
      return Locale.getDefault();
    }

  }

  protected static String getVersionString() {
    return ResourceBundle.getBundle("de.baw.lomo.version").getString("lomo.version");
  }

  protected static String getUserSettings() {
    return System.getProperty("user.home") + File.separator + ".lomo" + File.separator + App.getVersionString() + File.separator + "settings.properties";
  }
  
  public static void main(String[] args) {
    launch(args);
  }  
}
