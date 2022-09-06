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

import de.baw.lomo.core.data.KeyValueEntry;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.converter.DoubleStringConverter;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

import java.util.Comparator;
import java.util.List;

public class PopOverKeyValueListPropertyEditor
    implements PropertyEditor<List<KeyValueEntry>> {

  private final Button btnEditor;
  private final PropertySheet.Item item;
  private final ObjectProperty<List<KeyValueEntry>> value = new SimpleObjectProperty<>();
  private PopOver popOver;

  // If used correctly the value is always of type List<KeyValueEntry>
  @SuppressWarnings("unchecked")
  public PopOverKeyValueListPropertyEditor(PropertySheet.Item item) {
    this.item = item;
    if (item.getValue() != null) {
      btnEditor = new Button(". . .");
      value.set((List<KeyValueEntry>) item.getValue());
    } else {
      btnEditor = new Button("<empty>"); //$NON-NLS-1$
    }
    btnEditor.setAlignment(Pos.CENTER);

    initPopupEditor();

    btnEditor.setOnAction((ActionEvent event) -> {
      if (!popOver.isShowing()) {
        popOver.show(btnEditor);
      } else {
        popOver.hide();
        popOver.setDetached(false); // Guarantee it's attached again the next time        
      }
    });
  }

  // If used correctly, we are always dealing with KeyValueEntry here
  @SuppressWarnings("unchecked")
  private void initPopupEditor() {

    popOver = new PopOver();

    popOver.setHeaderAlwaysVisible(true);
    popOver.setTitle(item.getName());
    popOver.setAutoFix(false); // so we can drag the popover to second screen

    String tbColKey = Messages.getString("tbColKey");
    String tbColValue = Messages.getString("tbColValue");

    final String[] colHeader = item.getDescription().split(";");

    if (colHeader.length == 3) {
      tbColKey = colHeader[1];
      tbColValue = colHeader[2];
    }

    final ObservableList<KeyValueEntry> data = FXCollections
        .observableList(value.get());

    final ObservableList<XYChart.Data<Number, Number>> visualData = FXCollections
        .observableArrayList();

    for (final KeyValueEntry entry : value.get()) {
      visualData.add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
    }

    final TableView<KeyValueEntry> table = new TableView<>();
    table.setPrefSize(200., 200.);
    table.setEditable(true);

    final TableColumn<KeyValueEntry, Double> keyCol = new TableColumn<>(
        tbColKey);
    keyCol.setPrefWidth(99);
    keyCol.setCellValueFactory(new PropertyValueFactory<>("key")); //$NON-NLS-1$
    keyCol.setCellFactory(TextFieldTableCell
        .forTableColumn(new DoubleStringConverter() {

          @Override
          public Double fromString(String value) {

            value = value.replace(",", ".");
            value = value.replaceAll("[^0-9.]+", "");

            return super.fromString(value);
          }

        }));

    keyCol.setOnEditCommit((CellEditEvent<KeyValueEntry, Double> t) -> {
      
      if (t.getNewValue() == null) {
        
        t.getTableView().getColumns().get(0).setVisible(false);
        t.getTableView().getColumns().get(0).setVisible(true);
        return;
      }
      
      t.getTableView().getItems().get(t.getTablePosition().getRow())
          .setKey(t.getNewValue());
            
      data.sort(Comparator.comparingDouble(KeyValueEntry::getKey));
      
      syncVisualData(visualData);
    });

    final TableColumn<KeyValueEntry, Double> valueCol = new TableColumn<>(
        tbColValue);
    valueCol.setPrefWidth(99);
    valueCol.setCellValueFactory(new PropertyValueFactory<>("value")); //$NON-NLS-1$
    valueCol.setCellFactory(TextFieldTableCell
        .forTableColumn(new DoubleStringConverter() {

          @Override
          public Double fromString(String value) {

            value = value.replace(",", ".");
            value = value.replaceAll("[^0-9.]+", "");

            return super.fromString(value);
          }

        }));

    valueCol.setOnEditCommit((CellEditEvent<KeyValueEntry, Double> t) -> {
      
      if (t.getNewValue() == null) {
        
        t.getTableView().getColumns().get(0).setVisible(false);
        t.getTableView().getColumns().get(0).setVisible(true);
        return;
      }

      t.getTableView().getItems().get(t.getTablePosition().getRow())
          .setValue(t.getNewValue());
      syncVisualData(visualData);
    });

    table.getColumns().addAll(keyCol, valueCol);

    table.setItems(data);

    final Button addButton = new Button(Messages.getString("btnTbAdd"));
    addButton.setOnAction((ActionEvent e) -> {

      data.add(new KeyValueEntry(data.get(data.size()-1).getKey(), 0.0));
      syncVisualData(visualData);
    });

    final Button delButton = new Button(Messages.getString("btnTbDelete"));
    delButton.setOnAction((ActionEvent e) -> {
      if (table.getSelectionModel().getSelectedIndex() >= 0) {
        
        if (table.getItems().size() <= 1) {
          table.getSelectionModel().clearSelection();
          return;
        }
        
        data.remove(table.getSelectionModel().getSelectedIndex());
        syncVisualData(visualData);
      }
    });

    // Create content:

    final HBox hb = new HBox(5., addButton, delButton);

    final VBox vbox = new VBox(5., table, hb);
    vbox.setPadding(new Insets(10.));

    final NumberAxis xAxis = new NumberAxis();
    xAxis.setLabel(tbColKey);

    final NumberAxis yAxis = new NumberAxis();
    yAxis.setLabel(tbColValue);

    final LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
    chart.setLegendVisible(false);
    chart.setAnimated(false);
    chart.setPrefSize(table.getPrefWidth() * 1.618, vbox.getHeight());
    final XYChart.Series<Number, Number> series = new XYChart.Series<>();
    series.setData(visualData);
    chart.setId("prop-chart"); //$NON-NLS-1$
    chart.getData().add(series);

    chart.visibleProperty().bind(Controller.SHOW_PROP_CHARTS);
    chart.managedProperty().bind(chart.visibleProperty());
    chart.setPadding(new Insets(10., 10., 10., 0.));

    final GridPane pane = new GridPane();
    pane.add(vbox, 0, 0);
    pane.add(chart, 1, 0);
    GridPane.setHgrow(vbox, Priority.ALWAYS);

    popOver.setContentNode(pane);
  }

  private void syncVisualData(ObservableList<XYChart.Data<Number, Number>> visualData) {

      visualData.clear();

      for (final KeyValueEntry entry : value.get()) {
        visualData.add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
      }    
  }

  @Override
  public Node getEditor() {

    return btnEditor;
  }

  @Override
  public List<KeyValueEntry> getValue() {
    return value.get();
  }

  @Override
  public void setValue(List<KeyValueEntry> t) {
    value.set(t);
  }

}
