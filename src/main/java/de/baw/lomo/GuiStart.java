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
package de.baw.lomo;

import java.io.File;

import de.baw.lomo.core.OneDimensionalModel;
import de.baw.lomo.core.data.Case;
import de.baw.lomo.gui.Controller;
import de.baw.lomo.io.IOUtils;
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
    
    try {
      // Workaround for ojdkbuild 1.8 (https://github.com/ojdkbuild/ojdkbuild/issues/68)
      Class.forName("com.sun.deploy.uitoolkit.impl.fx.HostServicesFactory");
      c.setHostServices(getHostServices());
    } catch (ClassNotFoundException e) {
      c.setHostServices(null);
    }
    
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
    primaryStage.setTitle(Messages.getString("lomo.name"));
    primaryStage.show();
//    c.initConsole();
  }
  
  public static void main(String[] args) {
    launch(args);
  }  
}
