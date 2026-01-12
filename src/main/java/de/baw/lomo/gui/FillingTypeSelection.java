/*
 * Copyright (c) 2021-2026 Bundesanstalt für Wasserbau
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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class FillingTypeSelection {

    private FillingType fillingElement;
    private final ObjectProperty<FillingType> fillingType;

    public FillingTypeSelection() {
        fillingType = new SimpleObjectProperty<>();
    }

    public FillingType getFillingElement() {
        return fillingElement;
    }

    public void setFillingElement(FillingType fillingElement) {
        this.fillingElement = fillingElement;
        this.fillingType.set(fillingElement);
    }

    public FillingType getFillingType() {
        return fillingType.getValue();
    }

    public void setFillingType(FillingType ft) {
        fillingType.set(ft);
    }

    public ObjectProperty<FillingType> fillingTypeProperty() {
        return fillingType;
    }
}
