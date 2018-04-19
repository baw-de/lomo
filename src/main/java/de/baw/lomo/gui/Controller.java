/*******************************************************************************
 * Copyright (C) 2018 Bundesanstalt für Wasserbau
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
 ******************************************************************************/
package de.baw.lomo.gui;

import java.awt.image.BufferedImage;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import org.controlsfx.control.PropertySheet;
import org.controlsfx.control.PropertySheet.Item;
import org.controlsfx.property.BeanProperty;
import org.controlsfx.property.BeanPropertyUtils;

import com.sun.javafx.charts.Legend;

import de.baw.lomo.core.Model;
import de.baw.lomo.core.data.Case;
import de.baw.lomo.core.data.FillingType;
import de.baw.lomo.core.data.FillingTypes;
import de.baw.lomo.core.data.GateFillingType;
import de.baw.lomo.core.data.RectangularSluiceGateFillingType;
import de.baw.lomo.core.data.Results;
import de.baw.lomo.io.IOUtils;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;

public class Controller implements Initializable {
  @FXML
  private Pane rootPane;
  @FXML
  private Pane plotPane;
  @FXML
  private PropertySheet propList;
  @FXML
  private Button btnCalc;
  @FXML
  private LineChart<Number, Number> bgChart;
  @FXML
  private LineChart<Number, Number> fgChart;
  @FXML
  private NumberAxis bgXaxis;
  @FXML
  private NumberAxis bgYaxis;
  @FXML
  private NumberAxis fgXaxis;
  @FXML
  private NumberAxis fgYaxis;
  @FXML
  private TextArea console;
  @FXML
  private Menu menuFillingType;
  @FXML
  private ProgressIndicator progress;
  @FXML
  private CheckMenuItem menuShowPropCharts;
  @FXML
  private Text chartExportTag;
  
  private File lastUsedDir = null;
  
  public static BooleanProperty SHOW_PROP_CHARTS = new SimpleBooleanProperty(true);

  private Model model;
  private Case data;
  private Results lastResults;
  // private ResourceBundle resources;

  private XYChart.Series<Number, Number> seriesQ;
  private XYChart.Series<Number, Number> seriesF;
  private XYChart.Series<Number, Number> seriesH;
  private XYChart.Series<Number, Number> seriesO;

  private final double[] multiples = new double[] { 0.01, 0.1, 0.25, 1, 2, 5,
      10, 25, 50, 100, 500, 1000, 2000 };
  private double bgYmax = Double.NaN;
  private double bgYmin = Double.NaN;
  private boolean isComputing = false;

  @Override
  public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

    bgXaxis.setLabel(Messages.getString("lblXAxis")); //$NON-NLS-1$
    fgXaxis.upperBoundProperty().bind(bgXaxis.upperBoundProperty());
    fgXaxis.lowerBoundProperty().bind(bgXaxis.lowerBoundProperty());
    bgYaxis.setLabel(Messages.getString("lblYAxisLeft")); //$NON-NLS-1$
    fgYaxis.setLabel(Messages.getString("lblYAxisRight")); //$NON-NLS-1$

    seriesF = new XYChart.Series<Number, Number>();
    seriesF.setName(Messages.getString("lblSeriesF")); //$NON-NLS-1$
    fgChart.getData().add(seriesF);

    seriesQ = new XYChart.Series<Number, Number>();
    seriesQ.setName(Messages.getString("lblSeriesQ")); //$NON-NLS-1$
    fgChart.getData().add(seriesQ);


    seriesH = new XYChart.Series<Number, Number>();
    seriesH.setName(Messages.getString("lblSeriesH")); //$NON-NLS-1$
    bgChart.getData().add(seriesH);
    
    seriesO = new XYChart.Series<Number, Number>();
    seriesO.setName(Messages.getString("lblSeriesO")); //$NON-NLS-1$
    bgChart.getData().add(seriesO);

    fgYaxis.needsLayoutProperty()
        .addListener((observable, oldValue, newValue) -> {

          if (Double.isNaN(bgYmax)) {
            return;
          }

          final double fgYUpper = fgYaxis.getUpperBound();
          final double fgYLower = fgYaxis.getLowerBound();
          final double fgYDelta = fgYaxis.getTickUnit();

          if ((fgYLower == 0 && fgYUpper == 0) || fgYDelta == 0) {
            return;
          }

          final double fgYNegativeDeltaCount = fgYLower / fgYDelta;
          final double fgYPositiveDeltaCount = fgYUpper / fgYDelta;

          for (final double m : multiples) {

            final double bgYLower = fgYNegativeDeltaCount * m;
            final double bgYUpper = fgYPositiveDeltaCount * m;

            if (bgYLower <= bgYmin && bgYUpper >= bgYmax) {

              bgYaxis.setUpperBound(bgYUpper);
              bgYaxis.setLowerBound(bgYLower);
              bgYaxis.setTickUnit(m);

              bgChart.layout();

              return;
            }
          }
        });
    
    SHOW_PROP_CHARTS.bind(menuShowPropCharts.selectedProperty());

//    initConsole();    
    
  }
  
  public void initConsole() {

    try {

      PrintStream out = new PrintStream(new MyOutputStream(), true, "UTF-8"); //$NON-NLS-1$

      System.setOut(out);
      System.setErr(out);

    } catch (IOException e) {
      e.printStackTrace();
    } 
  }
  
  public class MyOutputStream extends OutputStream {

    private PipedOutputStream out = new PipedOutputStream();
    private Reader reader;

    public MyOutputStream() throws IOException {
      PipedInputStream in = new PipedInputStream(out);
      reader = new InputStreamReader(in, "UTF-8"); //$NON-NLS-1$
    }

    public void write(int i) throws IOException {
      out.write(i);
    }

    public void write(byte[] bytes, int i, int i1) throws IOException {
      out.write(bytes, i, i1);
    }

    public void flush() throws IOException {
      Platform.runLater(() -> {
        try {

          if (reader.ready()) {
            char[] chars = new char[1024];
            int n = reader.read(chars);

            console.appendText(new String(chars, 0, n));

            // limit text length
            if (console.getText().length() > 10000) {
              console.deleteText(0, 5000);
            }
            // set cursor to end for scroll down
            console.positionCaret(console.getText().length() - 1);
          }

        } catch (IOException e) {
          e.printStackTrace();
        }
      });

    }
  }

  public void initModel(Model model) {

    this.model = model;
    this.data = new Case();

    model.setCaseData(data);

    initPropertSheet();
    initFillingTypeMenu();
  }

  @FXML
  public void processButtonCalc(ActionEvent event) {

    if (!isComputing) {      
      
      final Task<Results> task = new Task<Results>() {

        @Override
        protected Results call() throws Exception {
          isComputing = true;
          
          return model.run();
        }

        @Override
        protected void succeeded() {
          super.succeeded();
          
          Results results = getValue();
          
          final double[] t = results.getTimeline();
          final double[] q = results.getDischargeOverTime();
          final double[] lf = results.getLongitudinalForceOverTime();
          final double[] h = results.getChamberWaterLevelOverTime();
          final double[] o = results.getValveOpeningOverTime();

          final List<XYChart.Data<Number, Number>> dataQ = new ArrayList<>(
              t.length);
          final List<XYChart.Data<Number, Number>> dataF = new ArrayList<>(
              t.length);
          final List<XYChart.Data<Number, Number>> dataH = new ArrayList<>(
              t.length);
          final List<XYChart.Data<Number, Number>> dataO = new ArrayList<>(
              t.length);

          bgYmax = Double.MIN_VALUE;
          bgYmin = Double.MAX_VALUE;

          for (int i = 0; i < t.length; i++) {

            if (i > 0 && t[i] == 0) {
              break;
            }

            dataQ.add(new XYChart.Data<>(t[i], q[i]));
            dataF.add(new XYChart.Data<>(t[i], lf[i] / 1000.));
            dataH.add(new XYChart.Data<>(t[i], h[i]));
            
            double scale = 1.;
            
            if (data.getFillingType() instanceof RectangularSluiceGateFillingType) {
              scale = 10.;
            }
            
            dataO.add(new XYChart.Data<>(t[i], o[i] * scale));

            bgYmax = Math.max(bgYmax, Math.max(h[i],o[i] * scale));
            bgYmin = Math.min(bgYmin, Math.min(h[i],o[i] * scale));
          }

          seriesQ.setData(FXCollections.observableList(dataQ));
          seriesF.setData(FXCollections.observableList(dataF));
          seriesH.setData(FXCollections.observableList(dataH));
          seriesO.setData(FXCollections.observableList(dataO));
          
          if (!(data.getFillingType() instanceof GateFillingType)) {
            seriesO.getData().clear();
          }

          lastResults = results;
          isComputing = false;
        }

        @Override
        protected void failed() {
          super.failed();          
          isComputing = false;
        }
        
        @Override
        protected void cancelled() {
          super.cancelled();          
          isComputing = false;
        }
      };
      
      task.runningProperty().addListener((observable, oldValue, newValue) ->  {
        if(newValue) {
          System.out.println(Messages.getString("promptStartCalc")); //$NON-NLS-1$      
          progress.visibleProperty().set(true);
        } else {
          progress.visibleProperty().set(false);
        }
      });
      
      task.exceptionProperty().addListener((observable, oldValue, newValue) ->  {
        if(newValue != null) {
          Exception ex = (Exception) newValue;
          ex.printStackTrace();
        }
      });
      
      new Thread(task).start();
    } 
  }

  @FXML
  public void processMenuNew(ActionEvent event) {
    data = new Case();
    clearFigure();
    initPropertSheet();
    initFillingTypeMenu();
    model.setCaseData(data);
  }

  @FXML
  public void processMenuFileOpen(ActionEvent event) {

    final FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle(Messages.getString("dlgTitleOpenXmlCaseFile")); //$NON-NLS-1$
    fileChooser.getExtensionFilters().addAll(
        new ExtensionFilter(Messages.getString("descrXmlCaseFileFilter"), "*.xml"), //$NON-NLS-1$ //$NON-NLS-2$
        new ExtensionFilter(Messages.getString("descrAllFileFilter"), "*.*")); //$NON-NLS-1$ //$NON-NLS-2$

    if (lastUsedDir != null) {
      fileChooser.setInitialDirectory(lastUsedDir);
    }    
    
    final File selectedFile = fileChooser
        .showOpenDialog(rootPane.getScene().getWindow());

    if (selectedFile == null) {
      return;
    }
    
    lastUsedDir = selectedFile.getParentFile();

    data = IOUtils.readCaseFromXml(selectedFile);
    clearFigure();
    initPropertSheet();
    initFillingTypeMenu();
    model.setCaseData(data);
  }

  @FXML
  public void processMenuFileSave(ActionEvent event) {

    final FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle(Messages.getString("dlgTitleSaveXmlCaseFile")); //$NON-NLS-1$
    fileChooser.getExtensionFilters().addAll(
        new ExtensionFilter(Messages.getString("descrXmlCaseFileFilter"), "*.xml"), //$NON-NLS-1$ //$NON-NLS-2$
        new ExtensionFilter(Messages.getString("descrAllFileFilter"), "*.*")); //$NON-NLS-1$ //$NON-NLS-2$

    if (lastUsedDir != null) {
      fileChooser.setInitialDirectory(lastUsedDir);
    }   
    
    final File selectedFile = fileChooser
        .showSaveDialog(rootPane.getScene().getWindow());

    if (selectedFile == null) {
      return;
    }
    
    lastUsedDir = selectedFile.getParentFile();

    IOUtils.writeCaseToXml(data, selectedFile);
  }

  @FXML
  public void processMenuExportImage(ActionEvent event) {

    final FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle(Messages.getString("dlgTitleExportImage")); //$NON-NLS-1$
    fileChooser.getExtensionFilters().addAll(
        new ExtensionFilter(Messages.getString("descrPngFileFilter"), "*.png"), //$NON-NLS-1$ //$NON-NLS-2$
        new ExtensionFilter(Messages.getString("descrAllFileFilter"), "*.*")); //$NON-NLS-1$ //$NON-NLS-2$

    if (lastUsedDir != null) {
      fileChooser.setInitialDirectory(lastUsedDir);
    }   
    
    final File selectedFile = fileChooser
        .showSaveDialog(rootPane.getScene().getWindow());

    if (selectedFile == null) {
      return;
    }
    
    lastUsedDir = selectedFile.getParentFile();
    
    chartExportTag.textProperty().set(getExportTag());
    chartExportTag.visibleProperty().set(true);

    final WritableImage snapshot = plotPane.snapshot(new SnapshotParameters(),
        null);
    final BufferedImage image = SwingFXUtils.fromFXImage(snapshot, null);
    try {
      ImageIO.write(image, "png", selectedFile); //$NON-NLS-1$
    } catch (final IOException e) {

      throw new RuntimeException(Messages.getString("errExportImageFailed"), e); //$NON-NLS-1$
    }
    
    chartExportTag.visibleProperty().set(false);
  }

  @FXML
  public void processMenuExportResults(ActionEvent event) {

    final FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle(Messages.getString("dlgTitleExportResults")); //$NON-NLS-1$
    fileChooser.getExtensionFilters().addAll(
        new ExtensionFilter(Messages.getString("descrDatFileFilter"), "*.dat"), //$NON-NLS-1$ //$NON-NLS-2$
        new ExtensionFilter(Messages.getString("descrAllFileFilter"), "*.*")); //$NON-NLS-1$ //$NON-NLS-2$

    if (lastUsedDir != null) {
      fileChooser.setInitialDirectory(lastUsedDir);
    }  
    
    final File selectedFile = fileChooser
        .showSaveDialog(rootPane.getScene().getWindow());

    if (selectedFile == null) {
      return;
    }
    
    lastUsedDir = selectedFile.getParentFile();

    IOUtils.writeResultsToText(lastResults, selectedFile, getExportTag());
  }

  @FXML
  public void processMenuExit(ActionEvent event) {

    Platform.exit();
  }

  @FXML
  public void processMenuAbout(ActionEvent event) {

    final Alert dlg = new Alert(AlertType.INFORMATION);
    dlg.initModality(Modality.APPLICATION_MODAL);
    dlg.getDialogPane().setPrefWidth(500);
    dlg.setResizable(true);
    dlg.initOwner(rootPane.getScene().getWindow());
    dlg.setTitle(Messages.getString("dlgAbout.title")); //$NON-NLS-1$
    dlg.setHeaderText(de.baw.lomo.Messages.getString("lomo.name")); //$NON-NLS-1$
    
    final StringBuffer aboutString = new StringBuffer();
    aboutString.append(de.baw.lomo.Messages.getString("lomo.copyright")); //$NON-NLS-1$
    aboutString.append("\n"); //$NON-NLS-1$
    aboutString.append(Messages.getString("dlgAbout.web_baw")); //$NON-NLS-1$
    aboutString.append("\n\n"); //$NON-NLS-1$
    aboutString.append(de.baw.lomo.Messages.getString("lomo.version")); //$NON-NLS-1$
    aboutString.append("\n"); //$NON-NLS-1$
    aboutString.append(Messages.getString("dlgAbout.web_lomo")); //$NON-NLS-1$
    aboutString.append("\n"); //$NON-NLS-1$
    dlg.getDialogPane().setContentText(aboutString.toString());
        
    final StringBuffer licenseString = new StringBuffer();
    licenseString.append(Messages.getString("dlgAbout.license_lomo")); //$NON-NLS-1$
    licenseString.append("\n\n"); //$NON-NLS-1$
    licenseString.append(Messages.getString("dlgAbout.license_3rdParty")); //$NON-NLS-1$
    
    TextArea license = new TextArea(licenseString.toString()); 
    
    license.setEditable(false);
    license.setWrapText(true);
    license.setPrefHeight(180);
    
    GridPane.setVgrow(license, Priority.ALWAYS);
    GridPane.setHgrow(license, Priority.ALWAYS);
    
    GridPane expContent = new GridPane();
    expContent.add(new Label(Messages.getString("dlgAbout.license")), 0, 0); //$NON-NLS-1$
    expContent.add(license, 0, 1);
    
    dlg.getDialogPane().setExpandableContent(expContent);
    dlg.showAndWait();
  }
  
  @FXML
  public void processMenuParameterHelp(ActionEvent event) {

    final Alert alert = new Alert(AlertType.INFORMATION);
    alert.initOwner(rootPane.getScene().getWindow());
    alert.initModality(Modality.NONE);
    alert.setTitle(Messages.getString("dlgParameterHelpTitle")); //$NON-NLS-1$
    alert.setHeaderText(Messages.getString("dlgParameterHelpIntro")); //$NON-NLS-1$

    final ScrollPane pane = new ScrollPane();
    pane.setFitToWidth(true);
    pane.setStyle("-fx-background-color: #FFFFFF;"); //$NON-NLS-1$
    pane.setPrefWidth(500);
    pane.setPrefHeight(400);

    final TextFlow textFlow = new TextFlow();
    textFlow.setStyle("-fx-background-color: #FFFFFF;"); //$NON-NLS-1$

    ObservableList<Item> liste = BeanPropertyUtils.getProperties(data);

    final Text headingGeneral = new Text(
        String.format("%s\n\r", Messages.getString("dlgParameterHelpGeneralParameters"))); //$NON-NLS-1$ //$NON-NLS-2$
    final Font defaultFont = headingGeneral.getFont();
    headingGeneral.setFont(Font.font(defaultFont.getFamily(), 18));
    textFlow.getChildren().add(headingGeneral);

    writePropertyHelpDescription(textFlow, liste);

    for (FillingType fillingType : FillingTypes.LIST) {

      final Text headingFillingType = new Text(
          String.format("\n%s\n\r", fillingType.toString())); //$NON-NLS-1$
      headingFillingType.setFont(Font.font(defaultFont.getFamily(), 18));
      textFlow.getChildren().add(headingFillingType);

      liste = BeanPropertyUtils.getProperties(fillingType);
      writePropertyHelpDescription(textFlow, liste);
    }

    pane.setContent(textFlow);
    alert.getDialogPane().setContent(pane);
    alert.showAndWait();
  }
  
  @FXML
  public void processMenuLoadComparison(ActionEvent event) {

    final FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle(Messages.getString("dlgTitleLoadComparison")); //$NON-NLS-1$
    fileChooser.getExtensionFilters().addAll(
        new ExtensionFilter(Messages.getString("descrDatFileFilter"), "*.dat"), //$NON-NLS-1$ //$NON-NLS-2$
        new ExtensionFilter(Messages.getString("descrAllFileFilter"), "*.*")); //$NON-NLS-1$ //$NON-NLS-2$

    if (lastUsedDir != null) {
      fileChooser.setInitialDirectory(lastUsedDir);
    }

    final File selectedFile = fileChooser
        .showOpenDialog(rootPane.getScene().getWindow());

    if (selectedFile == null) {
      return;
    }

    lastUsedDir = selectedFile.getParentFile();

    final Results results = IOUtils.readResultsFromFile(selectedFile);

    plotComparison(results);  
  }
  
  @FXML
  public void processMenuSnapshotComparison(ActionEvent event) {
    plotComparison(lastResults);
  }
  
  @FXML
  public void processMenuClearComparison(ActionEvent event) {    
    clearComparison();    
  }

  private void writePropertyHelpDescription(TextFlow textFlow,
      ObservableList<Item> liste) {
    Collections.sort(liste, propertyComparator);
    for (Item i : liste) {

      final PropertyDescriptor p = ((BeanProperty) i).getPropertyDescriptor();

      final Text displayName = new Text(p.getDisplayName());
      Font def = displayName.getFont();
      displayName
          .setFont(Font.font(def.getFamily(), FontWeight.BOLD, def.getSize()));
      final Text xmlName = new Text(String.format(" | %s\n", p.getName())); //$NON-NLS-1$
      xmlName.setFill(Color.GRAY);
      final Text description = new Text(
          String.format("%s\n", p.getShortDescription().replace(";", "\n"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      final Text dataType = new Text(String.format("%s: %s\n\r", Messages.getString("dlgParameterHelpDataType"), //$NON-NLS-1$ //$NON-NLS-2$
          p.getPropertyType().getSimpleName()));
      dataType.setFill(Color.GRAY);
      textFlow.getChildren().addAll(displayName, xmlName, description,
          dataType);
    }
  }

  private void initFillingTypeMenu() {
    
    menuFillingType.getItems().clear();
    
    ToggleGroup toggleGroup = new ToggleGroup();
  
    for(FillingType fillingType : FillingTypes.LIST) {
      
       RadioMenuItem item = new RadioMenuItem(fillingType.toString());
       item.setToggleGroup(toggleGroup);
      
      if (fillingType.getClass() == data.getFillingType().getClass()) {
        item.setUserData(data.getFillingType());
        item.setSelected(true);
      } else {
        item.setUserData(fillingType);
        item.setSelected(false);
      }      
            
      menuFillingType.getItems().add(item);
    }
    
    toggleGroup.selectedToggleProperty()
        .addListener((observable, oldValue, newValue) -> {

          if (newValue != null) {
            FillingType fillingType = (FillingType) newValue.getUserData();
            data.setFillingType(fillingType);
            initPropertSheet();
          }

        });
  }

  private void initPropertSheet() {
    ObservableList<Item> liste = BeanPropertyUtils.getProperties(data);
    Collections.sort(liste, propertyComparator);
    propList.getItems().setAll(liste);
    
    liste = BeanPropertyUtils.getProperties(data.getFillingType());
    Collections.sort(liste, propertyComparator);
    propList.getItems().addAll(liste);
  }

  private void clearFigure() {

    final List<XYChart.Data<Number, Number>> dataH = new ArrayList<>();
    final List<XYChart.Data<Number, Number>> dataQ = new ArrayList<>();
    final List<XYChart.Data<Number, Number>> dataI = new ArrayList<>();
    final List<XYChart.Data<Number, Number>> dataO = new ArrayList<>();

    seriesH.setData(FXCollections.observableList(dataH));
    seriesQ.setData(FXCollections.observableList(dataQ));
    seriesF.setData(FXCollections.observableList(dataI));
    seriesO.setData(FXCollections.observableList(dataO));
  }
  
  private void clearComparison() {
    fgChart.getData().remove(2, fgChart.getData().size());
    bgChart.getData().remove(2, bgChart.getData().size());
  }
  
  private String getExportTag() {
    
    final StringBuffer bf = new StringBuffer();
    
    bf.append(String.format("%s, %s %s",  //$NON-NLS-1$
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), //$NON-NLS-1$
        de.baw.lomo.Messages.getString("lomo.name"), //$NON-NLS-1$
        Messages.getString("dlgMessageAboutVersion"))); //$NON-NLS-1$
    
    if (data.getAuthor().length() > 0) {
      bf.append(": ").append(data.getAuthor()); //$NON-NLS-1$
    }
    
    if (data.getDescription().length() > 0) {
      if (data.getAuthor().length() > 0) {
        bf.append(", "); //$NON-NLS-1$
      } else {
        bf.append(": "); //$NON-NLS-1$
      }
      
      bf.append(data.getDescription());
    }
    
    return bf.toString();
  }
  
  private void plotComparison(Results results) {
    
    final Legend fgLegend = (Legend) fgChart.lookup(".chart-legend"); //$NON-NLS-1$
    final int fgLegendSize = fgLegend.getChildren().size();
    
    final Legend bgLegend = (Legend) bgChart.lookup(".chart-legend"); //$NON-NLS-1$
    final int bgLegendSize = bgLegend.getChildren().size();

    final double[] t = results.getTimeline();
    final double[] q = results.getDischargeOverTime();
    final double[] lf = results.getLongitudinalForceOverTime();
    final double[] h = results.getChamberWaterLevelOverTime();
    final double[] o = results.getValveOpeningOverTime();

    final XYChart.Series<Number, Number> seriesFComp = new XYChart.Series<Number, Number>();
    fgChart.getData().add(seriesFComp);
    final XYChart.Series<Number, Number> seriesQComp = new XYChart.Series<Number, Number>();
    fgChart.getData().add(seriesQComp);
    final XYChart.Series<Number, Number> seriesHComp = new XYChart.Series<Number, Number>();
    bgChart.getData().add(seriesHComp);
    final XYChart.Series<Number, Number> seriesOComp = new XYChart.Series<Number, Number>();
    bgChart.getData().add(seriesOComp);

    final List<XYChart.Data<Number, Number>> dataF = new ArrayList<>(t.length);
    final List<XYChart.Data<Number, Number>> dataQ = new ArrayList<>(t.length);
    final List<XYChart.Data<Number, Number>> dataH = new ArrayList<>(t.length);
    final List<XYChart.Data<Number, Number>> dataO = new ArrayList<>(t.length);

    for (int i = 0; i < t.length; i++) {

      if (i > 0 && t[i] == 0) {
        break;
      }

      if (i < q.length) {
        dataQ.add(new XYChart.Data<>(t[i], q[i]));
      }
      if (i < lf.length) {
        dataF.add(new XYChart.Data<>(t[i], lf[i] / 1000.));
      }
      if (i < h.length) {
        dataH.add(new XYChart.Data<>(t[i], h[i]));
      }

      double scale = 1.;

      if (data.getFillingType() instanceof RectangularSluiceGateFillingType) {
        scale = 10.;
      }
      if (i < o.length) {
        dataO.add(new XYChart.Data<>(t[i], o[i] * scale));
      }
    }

    seriesQComp.setData(FXCollections.observableList(dataQ));
    seriesFComp.setData(FXCollections.observableList(dataF));
    seriesHComp.setData(FXCollections.observableList(dataH));
    seriesOComp.setData(FXCollections.observableList(dataO));

   
    fgLegend.getChildren().remove(fgLegendSize,
        fgLegend.getChildren().size());

    bgLegend.getChildren().remove(bgLegendSize,
        bgLegend.getChildren().size());

  }

  private static Comparator<Item> propertyComparator = new Comparator<Item>() {

    @Override
    public int compare(Item o1, Item o2) {
      final PropertyDescriptor p1 = ((BeanProperty) o1).getPropertyDescriptor();
      final PropertyDescriptor p2 = ((BeanProperty) o2).getPropertyDescriptor();

      final int b1 = (int) p1.getValue("order"); //$NON-NLS-1$
      final int b2 = (int) p2.getValue("order"); //$NON-NLS-1$

      return Integer.compare(b1, b2);
    }

  };

}
