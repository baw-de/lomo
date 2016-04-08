package de.baw.lomo.core.data;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import de.baw.lomo.gui.FilePropertyEditor;

public class PrescribedInflowFillingTypeBeanInfo extends SimpleBeanInfo {
  
  @Override
  public PropertyDescriptor[] getPropertyDescriptors() {
        
    PropertyDescriptor[] properties = new PropertyDescriptor[1];
    
    try {
      
      properties[0] = new PropertyDescriptor("file", PrescribedInflowFillingType.class); //$NON-NLS-1$
      properties[0].setValue("order", 1); //$NON-NLS-1$
      properties[0].setDisplayName(Messages.getString("namePrescribedInflowFile")); //$NON-NLS-1$
      properties[0].setShortDescription(Messages.getString("descrPrescribedInflowFile")); //$NON-NLS-1$
      properties[0].setPropertyEditorClass(FilePropertyEditor.class);
      
    } catch (IntrospectionException e) {
      e.printStackTrace();
    }

    return properties;
    
  }

}
