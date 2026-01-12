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

import org.controlsfx.property.BeanProperty;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class FillingTypeSelectionBeanInfo extends SimpleBeanInfo {

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {

        PropertyDescriptor[] properties = new PropertyDescriptor[2];

        try {
            properties[0] = new PropertyDescriptor("fillingElement", FillingTypeSelection.class); //$NON-NLS-1$
            properties[0].setValue("order", 0); //$NON-NLS-1$
            properties[0].setDisplayName(Messages.getString("nameSelectedFillingElement")); //$NON-NLS-1$
            properties[0].setShortDescription(Messages.getString("descrSelectedFillingElement")); //$NON-NLS-1$
            properties[0].setPropertyEditorClass(FillingTypeSelectionEditor.class);
            properties[0].setValue(BeanProperty.CATEGORY_LABEL_KEY, de.baw.lomo.core.data.Messages.getString("catNameFilling")); //$NON-NLS-1$

            properties[1] = new PropertyDescriptor("fillingType", FillingTypeSelection.class); //$NON-NLS-1$
            properties[1].setValue("order", 1); //$NON-NLS-1$
            properties[1].setDisplayName(de.baw.lomo.core.data.Messages.getString("nameFillingType")); //$NON-NLS-1$
            properties[1].setShortDescription(de.baw.lomo.core.data.Messages.getString("descrFillingType")); //$NON-NLS-1$
            properties[1].setValue(BeanProperty.CATEGORY_LABEL_KEY, de.baw.lomo.core.data.Messages.getString("catNameFilling")); //$NON-NLS-1$
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }

        return properties;
    }
}