package de.baw.lomo.gui;

import java.awt.image.BufferedImage;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import org.controlsfx.control.PropertySheet;
import org.controlsfx.control.PropertySheet.Item;
import org.controlsfx.property.BeanProperty;
import org.controlsfx.property.BeanPropertyUtils;

import de.baw.lomo.core.Model;
import de.baw.lomo.core.data.Case;
import de.baw.lomo.core.data.FillingType;
import de.baw.lomo.core.data.Results;
import de.baw.lomo.io.IOUtils;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.Menu;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
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
  private Button btnStep;
  @FXML
  private Button btnReset;
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

    fgYaxis.needsLayoutProperty().addListener(new ChangeListener<Boolean>() {

      @Override
      public void changed(ObservableValue<? extends Boolean> observable,
          Boolean oldValue, Boolean newValue) {

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

        throw new RuntimeException("Could not fit axis.");
      }

    });

//    initConsole();    
    
  }
  
  public void initConsole() {

    final OutputStream my = new OutputStream() {

      @Override
      public void write(int b) throws IOException {
        Platform.runLater(new Runnable() {
          public void run() {
            console.appendText(String.valueOf((char) b));
            
            // limit text length
            if (console.getText().length() > 10000) {
              console.deleteText(0, 5000);
            }
            // set cursor to end for scroll down
            console.positionCaret(console.getText().length()-1);
          }
        });
      }
    };

    final PrintStream out = new PrintStream(my, true);

    System.setOut(out);
    System.setErr(out);
  }

  public void initModel(Model model) {

    this.model = model;
    this.data = new Case();

    model.init(data);

    initPropertSheet();
    initFillingTypeMenu();
  }

  @FXML
  public void processButton(ActionEvent event) {

    if (event.getSource() == btnCalc) {
      System.out.println(Messages.getString("promptStartCalc")); //$NON-NLS-1$
      final Results results = model.run();

      final double[] t = results.getTimeline();
      final double[] q = results.getDischargeOverTime();
      final double[] lf = results.getLongitudinalForceOverTime();
      final double[] h = results.getChamberWaterDepthOverTime();
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
        dataO.add(new XYChart.Data<>(t[i], o[i] * 10.));

        bgYmax = Math.max(bgYmax, Math.max(h[i],o[i] * 10.));
        bgYmin = Math.min(bgYmin, Math.min(h[i],o[i] * 10.));
      }

      seriesQ.setData(FXCollections.observableList(dataQ));
      seriesF.setData(FXCollections.observableList(dataF));
      seriesH.setData(FXCollections.observableList(dataH));
      seriesO.setData(FXCollections.observableList(dataO));

      lastResults = results;

    } else if (event.getSource() == btnStep) {
      // model.step();
      System.out.println(Messages.getString("promptStepCalc")); //$NON-NLS-1$

    } else if (event.getSource() == btnReset) {
      // model.init(caseData);

      System.out.println(Messages.getString("promptReset")); //$NON-NLS-1$
    }

  }

  @FXML
  public void processMenuNew(ActionEvent event) {
    data = new Case();
    clearFigure();
    initPropertSheet();
    model.init(data);
  }

  @FXML
  public void processMenuFileOpen(ActionEvent event) {

    final FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle(Messages.getString("dlgTitleOpenXmlCaseFile")); //$NON-NLS-1$
    fileChooser.getExtensionFilters().add(new ExtensionFilter(
        Messages.getString("descrXmlCaseFileFilter"), "*.xml")); //$NON-NLS-1$ //$NON-NLS-2$

    final File selectedFile = fileChooser
        .showOpenDialog(rootPane.getScene().getWindow());

    if (selectedFile == null) {
      return;
    }

    data = IOUtils.readCaseFromXml(selectedFile);
    clearFigure();
    initPropertSheet();
    initFillingTypeMenu();
    model.init(data);
  }

  @FXML
  public void processMenuFileSave(ActionEvent event) {

    final FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle(Messages.getString("dlgTitleSaveXmlCaseFile")); //$NON-NLS-1$
    fileChooser.getExtensionFilters().add(new ExtensionFilter(
        Messages.getString("descrXmlCaseFileFilter"), "*.xml")); //$NON-NLS-1$ //$NON-NLS-2$

    final File selectedFile = fileChooser
        .showSaveDialog(rootPane.getScene().getWindow());

    if (selectedFile == null) {
      return;
    }

    IOUtils.writeCaseToXml(data, selectedFile);
  }

  @FXML
  public void processMenuExportImage(ActionEvent event) {

    final FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle(Messages.getString("dlgTitleExportImage")); //$NON-NLS-1$
    fileChooser.getExtensionFilters().add(
        new ExtensionFilter(Messages.getString("descrPngFileFilter"), "*.png")); //$NON-NLS-1$ //$NON-NLS-2$

    final File selectedFile = fileChooser
        .showSaveDialog(rootPane.getScene().getWindow());

    if (selectedFile == null) {
      return;
    }

    final WritableImage snapshot = plotPane.snapshot(new SnapshotParameters(),
        null);
    final BufferedImage image = SwingFXUtils.fromFXImage(snapshot, null);
    try {
      ImageIO.write(image, "png", selectedFile); //$NON-NLS-1$
    } catch (final IOException e) {

      throw new RuntimeException(Messages.getString("errExportImageFailed"), e); //$NON-NLS-1$
    }
  }

  @FXML
  public void processMenuExportResults(ActionEvent event) {

    final FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle(Messages.getString("dlgTitleExportResults")); //$NON-NLS-1$
    fileChooser.getExtensionFilters().add(
        new ExtensionFilter(Messages.getString("descrDatFileFilter"), "*.dat")); //$NON-NLS-1$ //$NON-NLS-2$

    final File selectedFile = fileChooser
        .showSaveDialog(rootPane.getScene().getWindow());

    if (selectedFile == null) {
      return;
    }

    IOUtils.writeResultsToText(lastResults, selectedFile);
  }

  @FXML
  public void processMenuExit(ActionEvent event) {

    Platform.exit();
  }

  @FXML
  public void processMenuAbout(ActionEvent event) {

    final Alert dlg = new Alert(AlertType.INFORMATION);
    dlg.initModality(Modality.APPLICATION_MODAL);
    dlg.initOwner(rootPane.getScene().getWindow());
    dlg.setTitle(Messages.getString("dlgTitleAbout")); //$NON-NLS-1$
    dlg.getDialogPane().setContentText(Messages.getString("dlgMessageAbout") //$NON-NLS-1$
        + Messages.getString("dlgMessageAboutVersion")); //$NON-NLS-1$
    dlg.showAndWait();
  }

  private void initFillingTypeMenu() {
    
    menuFillingType.getItems().clear();
    
    ToggleGroup toggleGroup = new ToggleGroup();
  
    for(FillingType fillingType : FillingType.LIST) {
      
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
    
    toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
  
      @Override
      public void changed(ObservableValue<? extends Toggle> observable,
          Toggle oldValue, Toggle newValue) {
  
        if(newValue != null) {
          FillingType fillingType = (FillingType) newValue.getUserData();        
          data.setFillingType(fillingType);
          initPropertSheet();
        }        
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

    seriesH.setData(FXCollections.observableList(dataH));
    seriesQ.setData(FXCollections.observableList(dataQ));
    seriesF.setData(FXCollections.observableList(dataI));
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
