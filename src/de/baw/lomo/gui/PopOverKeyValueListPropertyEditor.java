package de.baw.lomo.gui;

import java.util.List;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

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

public class PopOverKeyValueListPropertyEditor
    implements PropertyEditor<List<KeyValueEntry>> {

  private final Button btnEditor;
  private final PropertySheet.Item item;
  private final ObjectProperty<List<KeyValueEntry>> value = new SimpleObjectProperty<>();
  private PopOver popOver;

  public PopOverKeyValueListPropertyEditor(PropertySheet.Item item) {
    this.item = item;
    if (item.getValue() != null) {
      btnEditor = new Button(". . .");
      value.set((List<KeyValueEntry>) item.getValue());
    } else {
      btnEditor = new Button("<empty>");
    }
    btnEditor.setAlignment(Pos.CENTER);

    initPopupEditor();

    btnEditor.setOnAction((ActionEvent event) -> {
      if (!popOver.isShowing()) {
        popOver.show(btnEditor);
      } else {
        popOver.hide();
      }
    });
  }

  private void initPopupEditor() {

    popOver = new PopOver();

    popOver.setHeaderAlwaysVisible(true);
    popOver.setTitle(item.getName());

    String tbColKey = Messages.getString("tbColKey");
    String tbColValue = Messages.getString("tbColValue");

    final String[] colHeader = item.getDescription().split(";");

    if (colHeader.length == 3) {
      tbColKey = colHeader[1];
      tbColValue = colHeader[2];
    }

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
    keyCol.setCellValueFactory(new PropertyValueFactory<>("key"));
    keyCol.setCellFactory(TextFieldTableCell
        .<KeyValueEntry, Double> forTableColumn(new DoubleStringConverter()));

    keyCol.setOnEditCommit((CellEditEvent<KeyValueEntry, Double> t) -> {
      t.getTableView().getItems().get(t.getTablePosition().getRow())
          .setKey(t.getNewValue());
      visualData.get(t.getTablePosition().getRow()).setXValue(t.getNewValue());
    });

    final TableColumn<KeyValueEntry, Double> valueCol = new TableColumn<>(
        tbColValue);
    valueCol.setPrefWidth(99);
    valueCol.setCellValueFactory(new PropertyValueFactory<>("value"));
    valueCol.setCellFactory(TextFieldTableCell
        .<KeyValueEntry, Double> forTableColumn(new DoubleStringConverter()));
    valueCol.setOnEditCommit((CellEditEvent<KeyValueEntry, Double> t) -> {
      t.getTableView().getItems().get(t.getTablePosition().getRow())
          .setValue(t.getNewValue());
      visualData.get(t.getTablePosition().getRow()).setYValue(t.getNewValue());
    });

    table.getColumns().addAll(keyCol, valueCol);

    final ObservableList<KeyValueEntry> data = FXCollections
        .observableList(value.get());

    table.setItems(data);

    final Button addButton = new Button(Messages.getString("btnTbAdd"));
    addButton.setOnAction((ActionEvent e) -> {
      data.add(new KeyValueEntry());
      visualData.add(new XYChart.Data<Number, Number>(0., 0.));
    });

    final Button delButton = new Button(Messages.getString("btnTbDelete"));
    delButton.setOnAction((ActionEvent e) -> {
      if (table.getSelectionModel().getSelectedIndex() >= 0) {
        data.remove(table.getSelectionModel().getSelectedIndex());
        visualData.remove(table.getSelectionModel().getSelectedIndex());
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
    chart.setPrefSize(table.getPrefWidth() * 1.618, vbox.getHeight());
    final XYChart.Series<Number, Number> series = new XYChart.Series<>();
    series.setData(visualData);
    chart.setId("prop-chart");
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
