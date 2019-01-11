package de.baw.lomo.core.data;

import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class CustomSourceFillingTypeBeanInfo extends SimpleBeanInfo {
  
  @Override
  public PropertyDescriptor[] getPropertyDescriptors() {
        
    PropertyDescriptor[] properties = new PropertyDescriptor[0];
    
//    try {
//      
//      properties[0] = new PropertyDescriptor("sources", CustomSourceFillingType.class); //$NON-NLS-1$
//      properties[0].setValue("order", 1); //$NON-NLS-1$
//      properties[0].setDisplayName(Messages.getString("nameCustomSourceSources")); //$NON-NLS-1$
//      properties[0].setShortDescription(Messages.getString("descrCustomSourceSources")); //$NON-NLS-1$
////      properties[0].setPropertyEditorClass(PopOverKeyValueListPropertyEditor.class);
//      properties[0].setValue(BeanProperty.CATEGORY_LABEL_KEY, Messages.getString("catNameFilling")); //$NON-NLS-1$
//      
//    } catch (IntrospectionException e) {
//      e.printStackTrace();
//    }

    return properties;
    
  }

}
