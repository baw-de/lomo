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

import de.baw.lomo.gui.PopOverKeyValueListPropertyEditor;

public class GenericGateFillingTypeBeanInfo extends SimpleBeanInfo {
  
  @Override
  public PropertyDescriptor[] getPropertyDescriptors() {
        
    PropertyDescriptor[] properties = new PropertyDescriptor[8];
    
    try {
      
      properties[0] = new PropertyDescriptor("genericGateOpeningLookup", GenericGateFillingType.class); //$NON-NLS-1$
      properties[0].setValue("order", 1); //$NON-NLS-1$
      properties[0].setDisplayName(Messages.getString("nameGenericGateOpeningLookup")); //$NON-NLS-1$
      properties[0].setShortDescription(Messages.getString("descrGenericGateOpeningLookup")); //$NON-NLS-1$
      properties[0].setPropertyEditorClass(PopOverKeyValueListPropertyEditor.class);
      properties[0].setValue(BeanProperty.CATEGORY_LABEL_KEY, Messages.getString("catNameFilling")); //$NON-NLS-1$
      
      properties[1] = new PropertyDescriptor("genericGateAMueLookup", GenericGateFillingType.class); //$NON-NLS-1$
      properties[1].setValue("order", 2); //$NON-NLS-1$
      properties[1].setDisplayName(Messages.getString("nameGenericGateAMueLookup")); //$NON-NLS-1$
      properties[1].setShortDescription(Messages.getString("descrGenericGateAMueLookup")); //$NON-NLS-1$
      properties[1].setPropertyEditorClass(PopOverKeyValueListPropertyEditor.class);
      properties[1].setValue(BeanProperty.CATEGORY_LABEL_KEY, Messages.getString("catNameFilling")); //$NON-NLS-1$
            
      properties[2] = new PropertyDescriptor("maximumPressureHead", GenericGateFillingType.class); //$NON-NLS-1$
      properties[2].setValue("order", 3); //$NON-NLS-1$
      properties[2].setDisplayName(Messages.getString("nameMaximumPressureHead")); //$NON-NLS-1$
      properties[2].setShortDescription(Messages.getString("descrMaximumPressureHead")); //$NON-NLS-1$
      properties[2].setValue(BeanProperty.CATEGORY_LABEL_KEY, Messages.getString("catNameFilling")); //$NON-NLS-1$
      
      properties[3] = new PropertyDescriptor("jetOutletLookup", GenericGateFillingType.class); //$NON-NLS-1$
      properties[3].setValue("order", 4); //$NON-NLS-1$
      properties[3].setDisplayName(Messages.getString("nameJetOutletPercentageLookup")); //$NON-NLS-1$
      properties[3].setShortDescription(Messages.getString("descrJetOutletPercentageLookup")); //$NON-NLS-1$
      properties[3].setPropertyEditorClass(PopOverKeyValueListPropertyEditor.class);     
      properties[3].setValue(BeanProperty.CATEGORY_LABEL_KEY, Messages.getString("catNameFilling")); //$NON-NLS-1$
                
      properties[4] = new PropertyDescriptor("jetCoefficientC0", GenericGateFillingType.class); //$NON-NLS-1$
      properties[4].setValue("order", 5); //$NON-NLS-1$
      properties[4].setDisplayName(Messages.getString("nameJetCoefficientC0")); //$NON-NLS-1$
      properties[4].setShortDescription(Messages.getString("descrJetCoefficientC0")); //$NON-NLS-1$
      properties[4].setValue(BeanProperty.CATEGORY_LABEL_KEY, Messages.getString("catNameFilling")); //$NON-NLS-1$
                  
      properties[5] = new PropertyDescriptor("jetCoefficientC1", GenericGateFillingType.class); //$NON-NLS-1$
      properties[5].setValue("order", 6); //$NON-NLS-1$
      properties[5].setDisplayName(Messages.getString("nameJetCoefficientC1")); //$NON-NLS-1$
      properties[5].setShortDescription(Messages.getString("descrJetCoefficientC1")); //$NON-NLS-1$
      properties[5].setValue(BeanProperty.CATEGORY_LABEL_KEY, Messages.getString("catNameFilling")); //$NON-NLS-1$
      
      properties[6] = new PropertyDescriptor("jetCoefficientC2", GenericGateFillingType.class); //$NON-NLS-1$
      properties[6].setValue("order", 7); //$NON-NLS-1$
      properties[6].setDisplayName(Messages.getString("nameJetCoefficientC2")); //$NON-NLS-1$
      properties[6].setShortDescription(Messages.getString("descrJetCoefficientC2")); //$NON-NLS-1$
      properties[6].setValue(BeanProperty.CATEGORY_LABEL_KEY, Messages.getString("catNameFilling")); //$NON-NLS-1$
      
      properties[7] = new PropertyDescriptor("jetCoefficientC3", GenericGateFillingType.class); //$NON-NLS-1$
      properties[7].setValue("order", 8); //$NON-NLS-1$
      properties[7].setDisplayName(Messages.getString("nameJetCoefficientC3")); //$NON-NLS-1$
      properties[7].setShortDescription(Messages.getString("descrJetCoefficientC3")); //$NON-NLS-1$
      properties[7].setValue(BeanProperty.CATEGORY_LABEL_KEY, Messages.getString("catNameFilling")); //$NON-NLS-1$

      
    } catch (IntrospectionException e) {
      e.printStackTrace();
    }

    return properties;
    
  }

}
