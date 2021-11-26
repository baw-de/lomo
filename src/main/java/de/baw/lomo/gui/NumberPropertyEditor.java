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

import java.lang.reflect.InvocationTargetException;

import org.controlsfx.control.PropertySheet.Item;
import org.controlsfx.property.editor.AbstractPropertyEditor;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextInputControl;

public class NumberPropertyEditor extends AbstractPropertyEditor<Number, NumericField> {

  private Class<? extends Number> sourceClass; // Double.class;

  // If used correctly, the property type is always of type <? extends Number>
  @SuppressWarnings("unchecked")
  public NumberPropertyEditor(Item property) {
    super(property, new NumericField((Class<? extends Number>) property.getType()));
    this.sourceClass = (Class<? extends Number>) property.getType();
  }

  {
    enableAutoSelectAll(getEditor());
  }

  @Override
  protected ObservableValue<Number> getObservableValue() {
    return getEditor().valueProperty();
  }

  @Override
  public Number getValue() {
    try {
      return sourceClass.getConstructor(String.class).newInstance(getEditor().getText());
    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
        | NoSuchMethodException | SecurityException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public void setValue(Number value) {
    sourceClass = value.getClass();

    String number = value.toString();

    if (number.contains(".")) {
      number = number.replaceAll("\\.?0+$", ""); // TODO
    }

    getEditor().setText(value.toString());
  }

  private static void enableAutoSelectAll(final TextInputControl control) {
    control.focusedProperty()
        .addListener((ObservableValue<? extends Boolean> o, Boolean oldValue, Boolean newValue) -> {
          if (newValue) {
            Platform.runLater(control::selectAll);
          }
        });
  }

}
