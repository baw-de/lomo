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
    sourceClass = (Class<? extends Number>) value.getClass();

    String number = value.toString();

    if (number.contains(".")) {
      number = number.replaceAll("\\.?0+$", "");
    }

    getEditor().setText(value.toString());
  }

  private static void enableAutoSelectAll(final TextInputControl control) {
    control.focusedProperty()
        .addListener((ObservableValue<? extends Boolean> o, Boolean oldValue, Boolean newValue) -> {
          if (newValue) {
            Platform.runLater(() -> {
              control.selectAll();
            });
          }
        });
  }

}
