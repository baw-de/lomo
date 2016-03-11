package de.baw.lomo.core.data;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import de.baw.lomo.gui.PopOverKeyValueListPropertyEditor;

public class SegmentGateFillingTypeBeanInfo extends SimpleBeanInfo {
  
  @Override
  public PropertyDescriptor[] getPropertyDescriptors() {
        
    PropertyDescriptor[] properties = new PropertyDescriptor[2];
    
    try {
      
      properties[0] = new PropertyDescriptor("segmentGateAngleLookup", SegmentGateFillingType.class); //$NON-NLS-1$
      properties[0].setValue("order", 1); //$NON-NLS-1$
      properties[0].setDisplayName(Messages.getString("nameSegmentGateAngleLookup")); //$NON-NLS-1$
      properties[0].setShortDescription(Messages.getString("descrSegmentGateAngleLookup")); //$NON-NLS-1$
      properties[0].setPropertyEditorClass(PopOverKeyValueListPropertyEditor.class);      
      
      properties[1] = new PropertyDescriptor("segmentGateLossLookup", SegmentGateFillingType.class); //$NON-NLS-1$
      properties[1].setValue("order", 2); //$NON-NLS-1$
      properties[1].setDisplayName(Messages.getString("nameSegmentGateLossLookup")); //$NON-NLS-1$
      properties[1].setShortDescription(Messages.getString("descrSegmentGateLossLookup")); //$NON-NLS-1$
      properties[1].setPropertyEditorClass(PopOverKeyValueListPropertyEditor.class);
      
    } catch (IntrospectionException e) {
      e.printStackTrace();
    }

    return properties;
    
  }

}
