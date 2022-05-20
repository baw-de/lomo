/*
 * Copyright (c) 2022 Bundesanstalt für Wasserbau
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

import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.converter.DoubleStringConverter;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class RangeEditor implements PropertyEditor<double[]> {
    
    private final HBox rangeEditor;
    private final TextField low;
    private final TextField high;

    public RangeEditor(PropertySheet.Item property) {
        
        rangeEditor = new HBox(5);

        Pattern validEditingState = Pattern.compile("(-?(([1-9][0-9]*)|0)?(\\.[0-9]*)?)|NaN");
        final UnaryOperator<TextFormatter.Change> filter =
                change -> {
                    if (validEditingState.matcher(change.getControlNewText()).matches()) {

                        if (change.getControlNewText().isEmpty()) {
                            change.setText("NaN");
                        }

                        return change; //if change is a number
                    } else {
                        return null;
                    }
                };

        low = new TextField();
        low.setTextFormatter(new TextFormatter<Double>(new DoubleStringConverter(), 1.0, filter));
        low.setPrefColumnCount(5);
        HBox.setHgrow(low, Priority.ALWAYS);
        high = new TextField();
        high.setPrefColumnCount(5);
        HBox.setHgrow(high, Priority.ALWAYS);
        high.setTextFormatter(new TextFormatter<Double>(new DoubleStringConverter(), 1.0, filter));

        rangeEditor.getChildren().addAll(low, high);
    }
    
    @Override
    public Node getEditor() {
        return rangeEditor;
    }

    @Override
    public double[] getValue() {
        return new double[] { (double)low.getTextFormatter().getValue(), (double)high.getTextFormatter().getValue() };
    }

    @Override
    public void setValue(double[] value) {
        ((TextFormatter<Double>)low.getTextFormatter()).setValue(value[0]);
        ((TextFormatter<Double>)high.getTextFormatter()).setValue(value[1]);
    }
}
