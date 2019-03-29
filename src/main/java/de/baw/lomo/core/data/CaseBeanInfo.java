/*******************************************************************************
 * Copyright (C) 2019 Bundesanstalt für Wasserbau
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

import org.controlsfx.property.BeanProperty;

import de.baw.lomo.gui.NumberPropertyEditor;
import de.baw.lomo.gui.PopOverKeyValueListPropertyEditor;

public class CaseBeanInfo extends SimpleBeanInfo {

	@Override
	public PropertyDescriptor[] getPropertyDescriptors() {
	  		
		PropertyDescriptor[] properties = new PropertyDescriptor[14];
		
		try {
		  
      properties[0] = new PropertyDescriptor("chamberLength", Case.class); //$NON-NLS-1$
      properties[0].setValue("order", 1); //$NON-NLS-1$
      properties[0].setDisplayName(Messages.getString("nameChamberLength")); //$NON-NLS-1$
      properties[0].setShortDescription(Messages.getString("descrChamberLength")); //$NON-NLS-1$
      properties[0].setPropertyEditorClass(NumberPropertyEditor.class);
      properties[0].setValue(BeanProperty.CATEGORY_LABEL_KEY, Messages.getString("catNameGeometry")); //$NON-NLS-1$
      
      properties[1] = new PropertyDescriptor("chamberWidth", Case.class); //$NON-NLS-1$
      properties[1].setValue("order", 2); //$NON-NLS-1$
      properties[1].setDisplayName(Messages.getString("nameChamberWidth")); //$NON-NLS-1$
      properties[1].setShortDescription(Messages.getString("descrChamberWidth")); //$NON-NLS-1$
      properties[1].setPropertyEditorClass(NumberPropertyEditor.class);
      properties[1].setValue(BeanProperty.CATEGORY_LABEL_KEY, Messages.getString("catNameGeometry")); //$NON-NLS-1$
		  
		  properties[2] = new PropertyDescriptor("upstreamWaterLevel", Case.class); //$NON-NLS-1$
      properties[2].setValue("order", 3); //$NON-NLS-1$
      properties[2].setDisplayName(Messages.getString("nameUpstreamWaterLevel")); //$NON-NLS-1$
      properties[2].setShortDescription(Messages.getString("descrUpstreamWaterLevel")); //$NON-NLS-1$
      properties[2].setPropertyEditorClass(NumberPropertyEditor.class);
      properties[2].setValue(BeanProperty.CATEGORY_LABEL_KEY, Messages.getString("catNameGeometry")); //$NON-NLS-1$
      
      properties[3] = new PropertyDescriptor("downstreamWaterLevel", Case.class); //$NON-NLS-1$
      properties[3].setValue("order", 4); //$NON-NLS-1$
      properties[3].setDisplayName(Messages.getString("nameDownstreamWaterLevel")); //$NON-NLS-1$
      properties[3].setShortDescription(Messages.getString("descrDownstreamWaterLevel")); //$NON-NLS-1$
      properties[3].setPropertyEditorClass(NumberPropertyEditor.class);
      properties[3].setValue(BeanProperty.CATEGORY_LABEL_KEY, Messages.getString("catNameGeometry")); //$NON-NLS-1$
      
      properties[4] = new PropertyDescriptor("fillingType", Case.class); //$NON-NLS-1$
      properties[4].setValue("order", 99); //$NON-NLS-1$
      properties[4].setDisplayName(Messages.getString("nameFillingType")); //$NON-NLS-1$
      properties[4].setShortDescription(Messages.getString("descrFillingType")); //$NON-NLS-1$
      properties[4].setValue(BeanProperty.CATEGORY_LABEL_KEY, Messages.getString("catNameFilling")); //$NON-NLS-1$
      
      properties[5] = new PropertyDescriptor("shipAreaLookup", Case.class); //$NON-NLS-1$
      properties[5].setValue("order", 6); //$NON-NLS-1$
      properties[5].setDisplayName(Messages.getString("nameShipAreaLookup")); //$NON-NLS-1$
      properties[5].setShortDescription(Messages.getString("descrShipAreaLookup")); //$NON-NLS-1$
      properties[5].setPropertyEditorClass(PopOverKeyValueListPropertyEditor.class); 
      properties[5].setValue(BeanProperty.CATEGORY_LABEL_KEY, Messages.getString("catNameGeometry")); //$NON-NLS-1$
		  
      properties[6] = new PropertyDescriptor("timeMax", Case.class); //$NON-NLS-1$
      properties[6].setValue("order", 7); //$NON-NLS-1$
      properties[6].setDisplayName(Messages.getString("nameMaxSimTime")); //$NON-NLS-1$
      properties[6].setShortDescription(Messages.getString("descrMaxSimTime")); //$NON-NLS-1$
      properties[6].setPropertyEditorClass(NumberPropertyEditor.class);
      properties[6].setValue(BeanProperty.CATEGORY_LABEL_KEY, Messages.getString("catNameNumerics")); //$NON-NLS-1$
      
      properties[7] = new PropertyDescriptor("numberOfNodes", Case.class); //$NON-NLS-1$
      properties[7].setValue("order", 8); //$NON-NLS-1$
      properties[7].setDisplayName(Messages.getString("nameNbOfNodes")); //$NON-NLS-1$
      properties[7].setShortDescription(Messages.getString("descrNbOfNodes")); //$NON-NLS-1$
      properties[7].setPropertyEditorClass(NumberPropertyEditor.class);
      properties[7].setValue(BeanProperty.CATEGORY_LABEL_KEY, Messages.getString("catNameNumerics")); //$NON-NLS-1$
      
      properties[8] = new PropertyDescriptor("deltaWaterDepthStop", Case.class); //$NON-NLS-1$
      properties[8].setValue("order", 5); //$NON-NLS-1$
      properties[8].setDisplayName(Messages.getString("nameDeltaWaterDepthStop")); //$NON-NLS-1$
      properties[8].setShortDescription(Messages.getString("descrDeltaWaterDepthStop")); //$NON-NLS-1$
      properties[8].setPropertyEditorClass(NumberPropertyEditor.class);
      properties[8].setValue(BeanProperty.CATEGORY_LABEL_KEY, Messages.getString("catNameGeometry")); //$NON-NLS-1$
      
      properties[9] = new PropertyDescriptor("cfl", Case.class); //$NON-NLS-1$
      properties[9].setValue("order", 10); //$NON-NLS-1$
      properties[9].setDisplayName(Messages.getString("nameCfl")); //$NON-NLS-1$
      properties[9].setShortDescription(Messages.getString("descrCfl")); //$NON-NLS-1$
      properties[9].setPropertyEditorClass(NumberPropertyEditor.class);  
      properties[9].setValue(BeanProperty.CATEGORY_LABEL_KEY, Messages.getString("catNameNumerics")); //$NON-NLS-1$    
      
      properties[10] = new PropertyDescriptor("theta", Case.class); //$NON-NLS-1$
      properties[10].setValue("order", 11); //$NON-NLS-1$
      properties[10].setDisplayName(Messages.getString("nameTheta")); //$NON-NLS-1$
      properties[10].setShortDescription(Messages.getString("descrTheta")); //$NON-NLS-1$
      properties[10].setPropertyEditorClass(NumberPropertyEditor.class);  
      properties[10].setValue(BeanProperty.CATEGORY_LABEL_KEY, Messages.getString("catNameNumerics")); //$NON-NLS-1$
                 
      properties[11] = new PropertyDescriptor("upwind", Case.class); //$NON-NLS-1$
      properties[11].setValue("order", 12); //$NON-NLS-1$
      properties[11].setDisplayName(Messages.getString("nameUpwind")); //$NON-NLS-1$
      properties[11].setShortDescription(Messages.getString("descrUpwind")); //$NON-NLS-1$
      properties[11].setPropertyEditorClass(NumberPropertyEditor.class);  
      properties[11].setValue(BeanProperty.CATEGORY_LABEL_KEY, Messages.getString("catNameNumerics")); //$NON-NLS-1$
      
      properties[12] = new PropertyDescriptor("author", Case.class); //$NON-NLS-1$
      properties[12].setValue("order", 13); //$NON-NLS-1$
      properties[12].setDisplayName(Messages.getString("nameAuthor")); //$NON-NLS-1$
      properties[12].setShortDescription(Messages.getString("descrAuthor")); //$NON-NLS-1$
      properties[12].setValue(BeanProperty.CATEGORY_LABEL_KEY, Messages.getString("catNameMisc")); //$NON-NLS-1$ 
      
      properties[13] = new PropertyDescriptor("description", Case.class); //$NON-NLS-1$
      properties[13].setValue("order", 14); //$NON-NLS-1$
      properties[13].setDisplayName(Messages.getString("nameDescription")); //$NON-NLS-1$
      properties[13].setShortDescription(Messages.getString("descrDescription")); //$NON-NLS-1$
      properties[13].setValue(BeanProperty.CATEGORY_LABEL_KEY, Messages.getString("catNameMisc")); //$NON-NLS-1$ 
      
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}

		return properties;
		
	}

}
