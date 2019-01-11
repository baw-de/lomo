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
