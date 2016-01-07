package de.baw.lomo.gui;

import java.awt.image.BufferedImage;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
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
import de.baw.lomo.core.data.Results;
import de.baw.lomo.io.IOUtils;
import javafx.application.Platform;
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

  private Model model;
  private Case data;
  private Results lastResults;
//  private ResourceBundle resources;

  private XYChart.Series<Number, Number> seriesQ;
  private XYChart.Series<Number, Number> seriesI;
  private XYChart.Series<Number, Number> seriesH;

  @Override
  public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
    
    bgXaxis.setLabel(Messages.getString("lblXAxis")); //$NON-NLS-1$
    bgXaxis.setAutoRanging(true);
    bgYaxis.setLabel(Messages.getString("lblYAxisLeft")); //$NON-NLS-1$
    fgYaxis.setLabel(Messages.getString("lblYAxisRight")); //$NON-NLS-1$

    seriesQ = new XYChart.Series<Number, Number>();
    seriesQ.setName(Messages.getString("lblSeriesQ")); //$NON-NLS-1$
    fgChart.getData().add(seriesQ);

    seriesI = new XYChart.Series<Number, Number>();
    seriesI.setName(Messages.getString("lblSeriesI")); //$NON-NLS-1$
    fgChart.getData().add(seriesI);
    
    seriesH = new XYChart.Series<Number, Number>();
    seriesH.setName(Messages.getString("lblSeriesH")); //$NON-NLS-1$
    bgChart.getData().add(seriesH);

  }
  
  public void initModel(Model model) {
    
    this.model = model;
    this.data = new Case();
    
    model.init(data);
    
    initPropertSheet();    
  }

  @FXML
  public void processButton(ActionEvent event) {

    if (event.getSource() == btnCalc) {
      System.out.println(Messages.getString("promptStartCalc")); //$NON-NLS-1$
      Results results = model.run();

      final double[] t = results.getTimeline();
      final double[] q = results.getDischargeOverTime();
      final double[] s = results.getSlopeOverTime();
      final double[] h = results.getChamberWaterDepthOverTime();

      final List<XYChart.Data<Number, Number>> dataQ = new ArrayList<>(t.length);
      final List<XYChart.Data<Number, Number>> dataI = new ArrayList<>(t.length);
      final List<XYChart.Data<Number, Number>> dataH = new ArrayList<>(t.length);

      for (int i = 0; i < t.length; i++) {

        if (i > 0 && t[i] == 0) {
          break;
        }

        dataQ.add(new XYChart.Data<>(t[i], q[i]));
        dataI.add(new XYChart.Data<>(t[i], s[i]));
        dataH.add(new XYChart.Data<>(t[i], h[i]));
      }

      seriesQ.setData(FXCollections.observableList(dataQ));
      seriesI.setData(FXCollections.observableList(dataI));
      seriesH.setData(FXCollections.observableList(dataH));
      
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

    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle(Messages.getString("dlgTitleOpenXmlCaseFile")); //$NON-NLS-1$
    fileChooser.getExtensionFilters().add(new ExtensionFilter(Messages.getString("descrXmlCaseFileFilter"), "*.xml")); //$NON-NLS-1$ //$NON-NLS-2$

    File selectedFile = fileChooser.showOpenDialog(rootPane.getScene().getWindow());

    if (selectedFile == null) return;
    
    data = IOUtils.readCaseFromXml(selectedFile);
    clearFigure();
    initPropertSheet();
    model.init(data);
  }

  @FXML
  public void processMenuFileSave(ActionEvent event) {

    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle(Messages.getString("dlgTitleSaveXmlCaseFile")); //$NON-NLS-1$
    fileChooser.getExtensionFilters().add(new ExtensionFilter(Messages.getString("descrXmlCaseFileFilter"), "*.xml")); //$NON-NLS-1$ //$NON-NLS-2$

    File selectedFile = fileChooser.showSaveDialog(rootPane.getScene().getWindow());

    if (selectedFile == null) return;
    
    IOUtils.writeCaseToXml(data, selectedFile);
  }

  @FXML
  public void processMenuExportImage(ActionEvent event) {
    
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle(Messages.getString("dlgTitleExportImage")); //$NON-NLS-1$
    fileChooser.getExtensionFilters().add(new ExtensionFilter(Messages.getString("descrPngFileFilter"), "*.png")); //$NON-NLS-1$ //$NON-NLS-2$

    File selectedFile = fileChooser.showSaveDialog(rootPane.getScene().getWindow());
    
    if (selectedFile == null) return;
    
    WritableImage snapshot = plotPane.snapshot(new SnapshotParameters(), null);
    BufferedImage image = SwingFXUtils.fromFXImage(snapshot, null);
    try {
      ImageIO.write(image, "png", selectedFile); //$NON-NLS-1$
    } catch (IOException e) {
      
      throw new RuntimeException(Messages.getString("errExportImageFailed"), e); //$NON-NLS-1$
    }
  }
  
  @FXML
  public void processMenuExportResults(ActionEvent event) {
    
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle(Messages.getString("dlgTitleExportResults")); //$NON-NLS-1$
    fileChooser.getExtensionFilters().add(new ExtensionFilter(Messages.getString("descrDatFileFilter"), "*.dat")); //$NON-NLS-1$ //$NON-NLS-2$

    File selectedFile = fileChooser.showSaveDialog(rootPane.getScene().getWindow());

    if (selectedFile == null) return;
    
    IOUtils.writeResultsToText(lastResults, selectedFile);    
  }
  
  @FXML
  public void processMenuExit(ActionEvent event) {
    
    Platform.exit();
  }
  
  @FXML
  public void processMenuAbout(ActionEvent event) {
    
    Alert dlg = new Alert(AlertType.INFORMATION);
    dlg.initModality(Modality.APPLICATION_MODAL);
    dlg.initOwner(rootPane.getScene().getWindow());
    dlg.setTitle(Messages.getString("dlgTitleAbout")); //$NON-NLS-1$
    dlg.getDialogPane().setContentText(Messages.getString("dlgMessageAbout") + 
        Messages.getString("dlgMessageAboutVersion")); //$NON-NLS-1$
    dlg.showAndWait();
  }

  private void initPropertSheet() {
    final ObservableList<Item> liste = BeanPropertyUtils.getProperties(data);
    Collections.sort(liste, propertyComparator);
    propList.getItems().setAll(liste);
  }

  private void clearFigure() {

    final List<XYChart.Data<Number, Number>> dataQ = new ArrayList<>();
    final List<XYChart.Data<Number, Number>> dataI = new ArrayList<>();

    seriesQ.setData(FXCollections.observableList(dataQ));
    seriesI.setData(FXCollections.observableList(dataI));
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
