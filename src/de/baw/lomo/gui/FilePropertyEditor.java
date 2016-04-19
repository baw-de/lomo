package de.baw.lomo.gui;

import java.io.File;

import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.OverrunStyle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class FilePropertyEditor implements PropertyEditor<String> {

  private final Button btnEditor;
  private final PropertySheet.Item item;
  private ObjectProperty<String> value = new SimpleObjectProperty<>();

  public FilePropertyEditor(PropertySheet.Item item) {
    this.item = item;

    btnEditor = new Button(". . ."); //$NON-NLS-1$

    btnEditor.setPrefWidth(177.);
    btnEditor.setMaxWidth(177.);
    btnEditor.setAlignment(Pos.CENTER);
    btnEditor.setTextOverrun(OverrunStyle.LEADING_ELLIPSIS);
    btnEditor.setOnAction((ActionEvent event) -> {
      displayPopupEditor();
    });
  }

  private void displayPopupEditor() {

    final FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle(item.getName()); // $NON-NLS-1$
    fileChooser.getExtensionFilters().add(
        new ExtensionFilter(Messages.getString("descrCsvFileFilter"), "*.txt")); //$NON-NLS-1$ //$NON-NLS-2$

    if (!value.get().isEmpty()) {
      fileChooser.setInitialDirectory((new File(value.get())).getParentFile());
    }

    final File selectedFile = fileChooser
        .showOpenDialog(btnEditor.getScene().getWindow());

    if (selectedFile == null) {
      return;
    }

    value.set(selectedFile.getAbsolutePath());
    item.setValue(value.get());

    btnEditor.setText(value.get());
  }

  @Override
  public Node getEditor() {

    return btnEditor;
  }

  @Override
  public String getValue() {
    return value.get();
  }

  @Override
  public void setValue(String t) {
    value.set(t);
    if (t != null && !t.isEmpty()) {
      btnEditor.setText(t);
    }

  }

}
