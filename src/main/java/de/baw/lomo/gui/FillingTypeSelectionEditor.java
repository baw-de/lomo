/*
 * Copyright (c) 2021 Bundesanstalt für Wasserbau
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

import de.baw.lomo.core.data.FillingType;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.AbstractPropertyEditor;

public class FillingTypeSelectionEditor extends AbstractPropertyEditor<FillingType, ComboBox<FillingType>> {

    private boolean isValid = false;

    public FillingTypeSelectionEditor(PropertySheet.Item property) {
        super(property, new ComboBox<>());

        getEditor().setId("ftSelector");

        getEditor().setEditable(true);
        getEditor().getEditor().textProperty()
                .addListener((observable, oldValue, newValue) -> {

                    getEditor().getEditor().getStyleClass().remove("warning"); //$NON-NLS-1$

                    if (getEditor().getItems().size() > 1 &&
                            getEditor().getItems().stream()
                                    .anyMatch(ft -> ft.getName().equals(newValue.strip()))) {
                      getEditor().getEditor().getStyleClass().add("warning"); //$NON-NLS-1$
                        isValid = false;
                    } else {
                        isValid = true;
                    }
                });

        getEditor().focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                if (!isValid) {
                    getEditor().cancelEdit();
                    getEditor().getEditor().getStyleClass().remove("warning"); //$NON-NLS-1$
                    isValid = true;
                } else {
                    final String editorText = getEditor().getEditor().getText();
                    if (!editorText.equals(getValue().getName())) {
                        getValue().setName(editorText);
                        getEditor().setItems(FXCollections.observableArrayList(getEditor().getItems()));
                        getEditor().getSelectionModel().select(getValue());
                    }
                }
            }
        });

        getEditor().setConverter(new StringConverter<>() {
            @Override
            public String toString(FillingType ft) {
                return ft.getName();
            }

            @Override
            public FillingType fromString(String string) {
                if (!isValid) { return getValue(); }

                return getEditor().getItems().stream()
                        .filter(ft -> ft.getName().equals(string))
                        .findFirst().orElse(getValue());
            }
        });
    }

    @Override
    protected ObservableValue<FillingType> getObservableValue() {
        return getEditor().getSelectionModel().selectedItemProperty();
    }

    @Override
    public void setValue(FillingType value) {
        getEditor().getSelectionModel().select(value);
    }
}
