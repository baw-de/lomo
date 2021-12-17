/*******************************************************************************
 * Copyright (C) 2021 Bundesanstalt für Wasserbau
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

import de.baw.lomo.gui.PopOverKeyValueListPropertyEditor;

public class SavingBasinFillingTypeBeanInfo extends SimpleBeanInfo {

	@Override
	public PropertyDescriptor[] getPropertyDescriptors() {

		PropertyDescriptor[] properties = new PropertyDescriptor[12];

		try {

			properties[0] = new PropertyDescriptor("floorHeight", SavingBasinFillingType.class); //$NON-NLS-1$
			properties[0].setValue("order", 1); //$NON-NLS-1$
			properties[0].setDisplayName(Messages.getString("nameSavingBasinFloorHeight")); //$NON-NLS-1$
			properties[0].setShortDescription(Messages.getString("descrSavingBasinFloorHeight")); //$NON-NLS-1$
//			properties[0].setPropertyEditorClass(PopOverKeyValueListPropertyEditor.class);
			properties[0].setValue(BeanProperty.CATEGORY_LABEL_KEY, Messages.getString("catNameFilling")); //$NON-NLS-1$

			properties[1] = new PropertyDescriptor("savingBasinSurfaceAreaLookup", SavingBasinFillingType.class); //$NON-NLS-1$
			properties[1].setValue("order", 2); //$NON-NLS-1$
			properties[1].setDisplayName(Messages.getString("nameSavingBasinSurfaceAreaLookup")); //$NON-NLS-1$
			properties[1].setShortDescription(Messages.getString("descrSavingBasinSurfaceAreaLookup")); //$NON-NLS-1$
			properties[1].setPropertyEditorClass(PopOverKeyValueListPropertyEditor.class);
			properties[1].setValue(BeanProperty.CATEGORY_LABEL_KEY, Messages.getString("catNameFilling")); //$NON-NLS-1$

			properties[2] = new PropertyDescriptor("initialFillHeight", SavingBasinFillingType.class); //$NON-NLS-1$
			properties[2].setValue("order", 3); //$NON-NLS-1$
			properties[2].setDisplayName(Messages.getString("nameSavingBasinInitialFillHeight")); //$NON-NLS-1$
			properties[2].setShortDescription(Messages.getString("descrSavingBasinInitialFillHeight")); //$NON-NLS-1$
//			properties[2].setPropertyEditorClass(PopOverKeyValueListPropertyEditor.class);
			properties[2].setValue(BeanProperty.CATEGORY_LABEL_KEY, Messages.getString("catNameFilling")); //$NON-NLS-1$
			
			properties[3] = new PropertyDescriptor("sluiceGateHeightLookup", SavingBasinFillingType.class); //$NON-NLS-1$
			properties[3].setValue("order", 4); //$NON-NLS-1$
			properties[3].setDisplayName(Messages.getString("nameSluiceGateHeightLookup")); //$NON-NLS-1$
			properties[3].setShortDescription(Messages.getString("descrSluiceGateHeightLookup")); //$NON-NLS-1$
			properties[3].setPropertyEditorClass(PopOverKeyValueListPropertyEditor.class);
			properties[3].setValue(BeanProperty.CATEGORY_LABEL_KEY, Messages.getString("catNameFilling")); //$NON-NLS-1$
			
			properties[4] = new PropertyDescriptor("sluiceGateWidthLookup", SavingBasinFillingType.class); //$NON-NLS-1$
			properties[4].setValue("order", 5); //$NON-NLS-1$
			properties[4].setDisplayName(Messages.getString("nameSluiceGateWidthLookup")); //$NON-NLS-1$
			properties[4].setShortDescription(Messages.getString("descrSluiceGateWidthLookup")); //$NON-NLS-1$
			properties[4].setPropertyEditorClass(PopOverKeyValueListPropertyEditor.class);
			properties[4].setValue(BeanProperty.CATEGORY_LABEL_KEY, Messages.getString("catNameFilling")); //$NON-NLS-1$

			properties[5] = new PropertyDescriptor("sluiceGateDischargeCoefficientLookup", SavingBasinFillingType.class); //$NON-NLS-1$
			properties[5].setValue("order", 6); //$NON-NLS-1$
			properties[5].setDisplayName(Messages.getString("nameSluiceGateDischargeCoefficientLookup")); //$NON-NLS-1$
			properties[5].setShortDescription(Messages.getString("descrSluiceGateDischargeCoefficientLookup")); //$NON-NLS-1$
			properties[5].setPropertyEditorClass(PopOverKeyValueListPropertyEditor.class);
			properties[5].setValue(BeanProperty.CATEGORY_LABEL_KEY, Messages.getString("catNameFilling")); //$NON-NLS-1$

			properties[6] = new PropertyDescriptor("prescribedJetOutletEnabled", SavingBasinFillingType.class); //$NON-NLS-1$
			properties[6].setValue("order", 7); //$NON-NLS-1$
			properties[6].setDisplayName(Messages.getString("namePrescribedJetOutletEnabled")); //$NON-NLS-1$
			properties[6].setShortDescription(Messages.getString("descrPrescribedJetOutletEnabled")); //$NON-NLS-1$
			properties[6].setValue(BeanProperty.CATEGORY_LABEL_KEY, Messages.getString("catNameFilling")); //$NON-NLS-1$

			properties[7] = new PropertyDescriptor("jetOutletLookup", SavingBasinFillingType.class); //$NON-NLS-1$
			properties[7].setValue("order", 8); //$NON-NLS-1$
			properties[7].setDisplayName(Messages.getString("nameJetOutletHeightLookup")); //$NON-NLS-1$
			properties[7].setShortDescription(Messages.getString("descrJetOutletHeightLookup")); //$NON-NLS-1$
			properties[7].setPropertyEditorClass(PopOverKeyValueListPropertyEditor.class);
			properties[7].setValue(BeanProperty.CATEGORY_LABEL_KEY, Messages.getString("catNameFilling")); //$NON-NLS-1$

			properties[8] = new PropertyDescriptor("jetCoefficientC0", SavingBasinFillingType.class); //$NON-NLS-1$
			properties[8].setValue("order", 9); //$NON-NLS-1$
			properties[8].setDisplayName(Messages.getString("nameJetCoefficientC0")); //$NON-NLS-1$
			properties[8].setShortDescription(Messages.getString("descrJetCoefficientC0")); //$NON-NLS-1$
			properties[8].setValue(BeanProperty.CATEGORY_LABEL_KEY, Messages.getString("catNameFilling")); //$NON-NLS-1$

			properties[9] = new PropertyDescriptor("jetCoefficientC1", SavingBasinFillingType.class); //$NON-NLS-1$
			properties[9].setValue("order", 10); //$NON-NLS-1$
			properties[9].setDisplayName(Messages.getString("nameJetCoefficientC1")); //$NON-NLS-1$
			properties[9].setShortDescription(Messages.getString("descrJetCoefficientC1")); //$NON-NLS-1$
			properties[9].setValue(BeanProperty.CATEGORY_LABEL_KEY, Messages.getString("catNameFilling")); //$NON-NLS-1$

			properties[10] = new PropertyDescriptor("jetCoefficientC2", SavingBasinFillingType.class); //$NON-NLS-1$
			properties[10].setValue("order", 11); //$NON-NLS-1$
			properties[10].setDisplayName(Messages.getString("nameJetCoefficientC2")); //$NON-NLS-1$
			properties[10].setShortDescription(Messages.getString("descrJetCoefficientC2")); //$NON-NLS-1$
			properties[10].setValue(BeanProperty.CATEGORY_LABEL_KEY, Messages.getString("catNameFilling")); //$NON-NLS-1$

			properties[11] = new PropertyDescriptor("jetCoefficientC3", SavingBasinFillingType.class); //$NON-NLS-1$
			properties[11].setValue("order", 12); //$NON-NLS-1$
			properties[11].setDisplayName(Messages.getString("nameJetCoefficientC3")); //$NON-NLS-1$
			properties[11].setShortDescription(Messages.getString("descrJetCoefficientC3")); //$NON-NLS-1$
			properties[11].setValue(BeanProperty.CATEGORY_LABEL_KEY, Messages.getString("catNameFilling")); //$NON-NLS-1$
			
			
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}

		return properties;

	}

}
