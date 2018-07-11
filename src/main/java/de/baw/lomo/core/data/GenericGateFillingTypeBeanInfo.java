/*******************************************************************************
 * Copyright (C) 2018 Bundesanstalt für Wasserbau
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
 ******************************************************************************/
package de.baw.lomo.core.data;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import de.baw.lomo.gui.NumberPropertyEditor;
import de.baw.lomo.gui.PopOverKeyValueListPropertyEditor;

public class GenericGateFillingTypeBeanInfo extends SimpleBeanInfo {
  
  @Override
  public PropertyDescriptor[] getPropertyDescriptors() {
        
    PropertyDescriptor[] properties = new PropertyDescriptor[7];
    
    try {
      
      properties[0] = new PropertyDescriptor("genericGateAMueLookup", GenericGateFillingType.class); //$NON-NLS-1$
      properties[0].setValue("order", 1); //$NON-NLS-1$
      properties[0].setDisplayName(Messages.getString("nameGenericGateAMueLookup")); //$NON-NLS-1$
      properties[0].setShortDescription(Messages.getString("descrGenericGateAMueLookup")); //$NON-NLS-1$
      properties[0].setPropertyEditorClass(PopOverKeyValueListPropertyEditor.class);
            
      properties[1] = new PropertyDescriptor("maximumPressureHead", GenericGateFillingType.class); //$NON-NLS-1$
      properties[1].setValue("order", 2); //$NON-NLS-1$
      properties[1].setDisplayName(Messages.getString("nameMaximumPressureHead")); //$NON-NLS-1$
      properties[1].setShortDescription(Messages.getString("descrMaximumPressureHead")); //$NON-NLS-1$
      properties[1].setPropertyEditorClass(NumberPropertyEditor.class);
      
      properties[2] = new PropertyDescriptor("jetOutletLookup", GenericGateFillingType.class); //$NON-NLS-1$
      properties[2].setValue("order", 3); //$NON-NLS-1$
      properties[2].setDisplayName(Messages.getString("nameJetOutletAreaLookup")); //$NON-NLS-1$
      properties[2].setShortDescription(Messages.getString("descrJetOutletAreaLookup")); //$NON-NLS-1$
      properties[2].setPropertyEditorClass(PopOverKeyValueListPropertyEditor.class);     
      properties[2].setExpert(true);  
                
      properties[3] = new PropertyDescriptor("jetCoefficientC0", GenericGateFillingType.class); //$NON-NLS-1$
      properties[3].setValue("order", 4); //$NON-NLS-1$
      properties[3].setDisplayName(Messages.getString("nameJetCoefficientC0")); //$NON-NLS-1$
      properties[3].setShortDescription(Messages.getString("descrJetCoefficientC0")); //$NON-NLS-1$
      properties[3].setPropertyEditorClass(NumberPropertyEditor.class);  
      properties[3].setExpert(true); 
                  
      properties[4] = new PropertyDescriptor("jetCoefficientC1", GenericGateFillingType.class); //$NON-NLS-1$
      properties[4].setValue("order", 5); //$NON-NLS-1$
      properties[4].setDisplayName(Messages.getString("nameJetCoefficientC1")); //$NON-NLS-1$
      properties[4].setShortDescription(Messages.getString("descrJetCoefficientC1")); //$NON-NLS-1$
      properties[4].setPropertyEditorClass(NumberPropertyEditor.class);  
      properties[4].setExpert(true); 
      
      properties[5] = new PropertyDescriptor("jetCoefficientC2", GenericGateFillingType.class); //$NON-NLS-1$
      properties[5].setValue("order", 6); //$NON-NLS-1$
      properties[5].setDisplayName(Messages.getString("nameJetCoefficientC2")); //$NON-NLS-1$
      properties[5].setShortDescription(Messages.getString("descrJetCoefficientC2")); //$NON-NLS-1$
      properties[5].setPropertyEditorClass(NumberPropertyEditor.class);  
      properties[5].setExpert(true); 
      
      properties[6] = new PropertyDescriptor("jetCoefficientC3", GenericGateFillingType.class); //$NON-NLS-1$
      properties[6].setValue("order", 7); //$NON-NLS-1$
      properties[6].setDisplayName(Messages.getString("nameJetCoefficientC3")); //$NON-NLS-1$
      properties[6].setShortDescription(Messages.getString("descrJetCoefficientC3")); //$NON-NLS-1$
      properties[6].setPropertyEditorClass(NumberPropertyEditor.class);  
      properties[6].setExpert(true);    

      
    } catch (IntrospectionException e) {
      e.printStackTrace();
    }

    return properties;
    
  }

}
