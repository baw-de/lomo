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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.DoubleStringConverter;

public class PopOverKeyValueListPropertyEditor implements PropertyEditor<List<KeyValueEntry>> {

  private final Button btnEditor;
  private final PropertySheet.Item item;
  private final ObjectProperty<List<KeyValueEntry>> value = new SimpleObjectProperty<>();

  public PopOverKeyValueListPropertyEditor(PropertySheet.Item item) {
    this.item = item;
    if (item.getValue() != null) {
      btnEditor = new Button(". . .");
      value.set((List<KeyValueEntry>) item.getValue());
    } else {
      btnEditor = new Button("<empty>");
    }
    btnEditor.setAlignment(Pos.CENTER);
    btnEditor.setOnAction((ActionEvent event) -> {
      displayPopupEditor();
    });
  }

  private void displayPopupEditor() {

    PopOver popOver = new PopOver(); // FXMLLoader.load(getClass().getResource("popup.fxml"));

    popOver.setHeaderAlwaysVisible(true);
    popOver.setTitle(item.getName());
            
    String tbColKey = Messages.getString("tbColKey");
    String tbColValue = Messages.getString("tbColValue");
    
    final String[] colHeader = item.getDescription().split(";");
    
    if (colHeader.length == 3) {
      tbColKey = colHeader[1];
      tbColValue = colHeader[2];
    }
    

    VBox vbox = new VBox(5.);
    vbox.setPadding(new Insets(10.));
    TableView<KeyValueEntry> table = new TableView<>();
    table.setPrefSize(200., 200.);
    table.setEditable(true);   
    
    TableColumn<KeyValueEntry, Double> keyCol = new TableColumn<>(tbColKey);
    keyCol.setPrefWidth(99);
    keyCol.setCellValueFactory(new PropertyValueFactory<>("key"));
    keyCol.setCellFactory(TextFieldTableCell.<KeyValueEntry, Double> forTableColumn(new DoubleStringConverter()));

    keyCol.setOnEditCommit((CellEditEvent<KeyValueEntry, Double> t) -> {
      ((KeyValueEntry) t.getTableView().getItems().get(t.getTablePosition().getRow())).setKey(t.getNewValue());
    });

    TableColumn<KeyValueEntry, Double> valueCol = new TableColumn<>(tbColValue);
    valueCol.setPrefWidth(99);
    valueCol.setCellValueFactory(new PropertyValueFactory<>("value"));
    valueCol.setCellFactory(TextFieldTableCell.<KeyValueEntry, Double> forTableColumn(new DoubleStringConverter()));
    valueCol.setOnEditCommit((CellEditEvent<KeyValueEntry, Double> t) -> {
      ((KeyValueEntry) t.getTableView().getItems().get(t.getTablePosition().getRow())).setValue(t.getNewValue());
    });

    table.getColumns().addAll(keyCol, valueCol);

    ObservableList<KeyValueEntry> data = 
        FXCollections.observableList((List<KeyValueEntry>) value.get());

    table.setItems(data);

    final Button addButton = new Button(Messages.getString("btnTbAdd"));
    addButton.setOnAction((ActionEvent e) -> {
      data.add(new KeyValueEntry());
    });

    final Button delButton = new Button(Messages.getString("btnTbDelete"));
    delButton.setOnAction((ActionEvent e) -> {
      if (table.getSelectionModel().getSelectedIndex() >= 0)
        data.remove(table.getSelectionModel().getSelectedIndex());
    });

    HBox hb = new HBox(5.);
    hb.getChildren().addAll(addButton, delButton);

    vbox.getChildren().addAll(table, hb);
    popOver.setContentNode(vbox);

    popOver.show(btnEditor);
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
    // if (t != null) {
    // btnEditor.setText(t.toString());
    // }

  }

}
