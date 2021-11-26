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
  private final ObjectProperty<String> value = new SimpleObjectProperty<>();

  public FilePropertyEditor(PropertySheet.Item item) {
    this.item = item;

    btnEditor = new Button(". . ."); //$NON-NLS-1$

    btnEditor.setPrefWidth(177.);
    btnEditor.setMaxWidth(177.);
    btnEditor.setAlignment(Pos.CENTER);
    btnEditor.setTextOverrun(OverrunStyle.LEADING_ELLIPSIS);
    btnEditor.setOnAction((ActionEvent event) -> displayPopupEditor());
  }

  private void displayPopupEditor() {

    final FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle(item.getName()); // $NON-NLS-1$
    fileChooser.getExtensionFilters().addAll(
        new ExtensionFilter(Messages.getString("descrCsvFileFilter"), "*.txt"), //$NON-NLS-1$ //$NON-NLS-2$
        new ExtensionFilter(Messages.getString("descrAllFileFilter"), "*.*")); //$NON-NLS-1$ //$NON-NLS-2$

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
