package de.baw.lomo.core.data;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import de.baw.lomo.gui.MyPropertyEditor;
import de.baw.lomo.gui.PopOverKeyValueListPropertyEditor;

public class RectangularSluiceGateFillingTypeBeanInfo extends SimpleBeanInfo {
  
  @Override
  public PropertyDescriptor[] getPropertyDescriptors() {
        
    PropertyDescriptor[] properties = new PropertyDescriptor[8];
    
    try {
      
      properties[0] = new PropertyDescriptor("sluiceGateHeightLookup", RectangularSluiceGateFillingType.class); //$NON-NLS-1$
      properties[0].setValue("order", 1); //$NON-NLS-1$
      properties[0].setDisplayName(Messages.getString("nameSluiceGateHeightLookup")); //$NON-NLS-1$
      properties[0].setShortDescription(Messages.getString("descrSluiceGateHeightLookup")); //$NON-NLS-1$
      properties[0].setPropertyEditorClass(PopOverKeyValueListPropertyEditor.class);      
      
      properties[1] = new PropertyDescriptor("sluiceGateWidth", RectangularSluiceGateFillingType.class); //$NON-NLS-1$
      properties[1].setValue("order", 2); //$NON-NLS-1$
      properties[1].setDisplayName(Messages.getString("nameSluiceGateWidth")); //$NON-NLS-1$
      properties[1].setShortDescription(Messages.getString("descrSluiceGateWidth")); //$NON-NLS-1$
      properties[1].setPropertyEditorClass(MyPropertyEditor.class);
      
      properties[2] = new PropertyDescriptor("sluiceGateDischargeCoefficientLookup", RectangularSluiceGateFillingType.class); //$NON-NLS-1$
      properties[2].setValue("order", 3); //$NON-NLS-1$
      properties[2].setDisplayName(Messages.getString("nameSluiceGateDischargeCoefficientLookup")); //$NON-NLS-1$
      properties[2].setShortDescription(Messages.getString("descrSluiceGateDischargeCoefficientLookup")); //$NON-NLS-1$
      properties[2].setPropertyEditorClass(PopOverKeyValueListPropertyEditor.class);
            
      properties[3] = new PropertyDescriptor("maximumPressureHead", RectangularSluiceGateFillingType.class); //$NON-NLS-1$
      properties[3].setValue("order", 4); //$NON-NLS-1$
      properties[3].setDisplayName(Messages.getString("nameMaximumPressureHead")); //$NON-NLS-1$
      properties[3].setShortDescription(Messages.getString("descrMaximumPressureHead")); //$NON-NLS-1$
      properties[3].setPropertyEditorClass(MyPropertyEditor.class);
      
      properties[4] = new PropertyDescriptor("prescribedJetOutletEnabled", RectangularSluiceGateFillingType.class); //$NON-NLS-1$
      properties[4].setValue("order", 5); //$NON-NLS-1$
      properties[4].setDisplayName(Messages.getString("namePrescribedJetOutletEnabled")); //$NON-NLS-1$
      properties[4].setShortDescription(Messages.getString("descrPrescribedJetOutletEnabled")); //$NON-NLS-1$
      properties[4].setExpert(true);  
      
      properties[5] = new PropertyDescriptor("jetOutletLookup", RectangularSluiceGateFillingType.class); //$NON-NLS-1$
      properties[5].setValue("order", 6); //$NON-NLS-1$
      properties[5].setDisplayName(Messages.getString("nameJetOutletHeightLookup")); //$NON-NLS-1$
      properties[5].setShortDescription(Messages.getString("descrJetOutletHeightLookup")); //$NON-NLS-1$
      properties[5].setPropertyEditorClass(PopOverKeyValueListPropertyEditor.class);     
      properties[5].setExpert(true);  
                
      properties[6] = new PropertyDescriptor("jetCoefficient", RectangularSluiceGateFillingType.class); //$NON-NLS-1$
      properties[6].setValue("order", 7); //$NON-NLS-1$
      properties[6].setDisplayName(Messages.getString("nameJetCoefficient")); //$NON-NLS-1$
      properties[6].setShortDescription(Messages.getString("descrJetCoefficient")); //$NON-NLS-1$
      properties[6].setPropertyEditorClass(MyPropertyEditor.class);  
      properties[6].setExpert(true); 
                  
      properties[7] = new PropertyDescriptor("jetExponent", RectangularSluiceGateFillingType.class); //$NON-NLS-1$
      properties[7].setValue("order", 8); //$NON-NLS-1$
      properties[7].setDisplayName(Messages.getString("nameJetExponent")); //$NON-NLS-1$
      properties[7].setShortDescription(Messages.getString("descrJetExponent")); //$NON-NLS-1$
      properties[7].setPropertyEditorClass(MyPropertyEditor.class);  
      properties[7].setExpert(true);  

      
    } catch (IntrospectionException e) {
      e.printStackTrace();
    }

    return properties;
    
  }

}
