/*
 * Copyright (c) 2019-2026 Bundesanstalt für Wasserbau
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
package de.baw.lomo.core.data;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import org.controlsfx.property.BeanProperty;

import de.baw.lomo.gui.FilePropertyEditor;

public class CustomSourceFromFileFillingTypeBeanInfo extends SimpleBeanInfo {
  
  @Override
  public PropertyDescriptor[] getPropertyDescriptors() {
        
    PropertyDescriptor[] properties = new PropertyDescriptor[1];
    
    try {
      
      properties[0] = new PropertyDescriptor("file", CustomSourceFromFileFillingType.class); //$NON-NLS-1$
      properties[0].setValue("order", 1); //$NON-NLS-1$
      properties[0].setDisplayName(Messages.getString("customSourceFromFile.nameFile")); //$NON-NLS-1$
      properties[0].setShortDescription(Messages.getString("customSourceFromFile.descrFile")); //$NON-NLS-1$
      properties[0].setPropertyEditorClass(FilePropertyEditor.class);
      properties[0].setValue(BeanProperty.CATEGORY_LABEL_KEY, Messages.getString("catNameFilling")); //$NON-NLS-1$
      
    } catch (IntrospectionException e) {
      e.printStackTrace();
    }

    return properties;
    
  }

}
