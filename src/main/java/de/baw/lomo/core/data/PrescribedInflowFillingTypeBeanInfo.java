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

import de.baw.lomo.gui.FilePropertyEditor;
import de.baw.lomo.gui.PopOverKeyValueListPropertyEditor;

public class PrescribedInflowFillingTypeBeanInfo extends SimpleBeanInfo {
  
  @Override
  public PropertyDescriptor[] getPropertyDescriptors() {
        
    PropertyDescriptor[] properties = new PropertyDescriptor[4];
    
    try {
      
      properties[0] = new PropertyDescriptor("file", PrescribedInflowFillingType.class); //$NON-NLS-1$
      properties[0].setValue("order", 1); //$NON-NLS-1$
      properties[0].setDisplayName(Messages.getString("namePrescribedInflowFile")); //$NON-NLS-1$
      properties[0].setShortDescription(Messages.getString("descrPrescribedInflowFile")); //$NON-NLS-1$
      properties[0].setPropertyEditorClass(FilePropertyEditor.class);
      
      properties[1] = new PropertyDescriptor("positionLookup", PrescribedInflowFillingType.class); //$NON-NLS-1$
      properties[1].setValue("order", 2); //$NON-NLS-1$
      properties[1].setDisplayName(Messages.getString("namePrescribedInflowPositionLookup")); //$NON-NLS-1$
      properties[1].setShortDescription(Messages.getString("descrPrescribedInflowPositionLookup")); //$NON-NLS-1$
      properties[1].setPropertyEditorClass(PopOverKeyValueListPropertyEditor.class);
      
      properties[2] = new PropertyDescriptor("lengthOfInfluenceLookup", PrescribedInflowFillingType.class); //$NON-NLS-1$
      properties[2].setValue("order", 3); //$NON-NLS-1$
      properties[2].setDisplayName(Messages.getString("namePrescribedInflowLengthOfInfluenceLookup")); //$NON-NLS-1$
      properties[2].setShortDescription(Messages.getString("descrPrescribedInflowLengthOfInfluenceLookup")); //$NON-NLS-1$
      properties[2].setPropertyEditorClass(PopOverKeyValueListPropertyEditor.class);
      
      properties[3] = new PropertyDescriptor("momentumFactorLookup", PrescribedInflowFillingType.class); //$NON-NLS-1$
      properties[3].setValue("order", 4); //$NON-NLS-1$
      properties[3].setDisplayName(Messages.getString("namePrescribedInflowMomentumFactorLookup")); //$NON-NLS-1$
      properties[3].setShortDescription(Messages.getString("descrPrescribedInflowMomentumFactorLookup")); //$NON-NLS-1$
      properties[3].setPropertyEditorClass(PopOverKeyValueListPropertyEditor.class);
      
    } catch (IntrospectionException e) {
      e.printStackTrace();
    }

    return properties;
    
  }

}
