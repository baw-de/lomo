package de.baw.lomo.core.data;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import de.baw.lomo.gui.MyPropertyEditor;
import de.baw.lomo.gui.PopOverKeyValueListPropertyEditor;

public class SegmentGateFillingTypeBeanInfo extends SimpleBeanInfo {

  @Override
  public PropertyDescriptor[] getPropertyDescriptors() {

    PropertyDescriptor[] properties = new PropertyDescriptor[7];

    try {

      properties[0] = new PropertyDescriptor("segmentGateAngleLookup", SegmentGateFillingType.class); //$NON-NLS-1$
      properties[0].setValue("order", 1); //$NON-NLS-1$
      properties[0].setDisplayName(Messages.getString("nameSegmentGateAngleLookup")); //$NON-NLS-1$
      properties[0].setShortDescription(Messages.getString("descrSegmentGateAngleLookup")); //$NON-NLS-1$
      properties[0].setPropertyEditorClass(PopOverKeyValueListPropertyEditor.class);      

      properties[1] = new PropertyDescriptor("segmentGateAMueLookup", SegmentGateFillingType.class); //$NON-NLS-1$
      properties[1].setValue("order", 2); //$NON-NLS-1$
      properties[1].setDisplayName(Messages.getString("nameSegmentGateAMueLookup")); //$NON-NLS-1$
      properties[1].setShortDescription(Messages.getString("descrSegmentGateAMueLookup")); //$NON-NLS-1$
      properties[1].setPropertyEditorClass(PopOverKeyValueListPropertyEditor.class);

      //Fabian neu:
      properties[2] = new PropertyDescriptor("culvertCrossSection", SegmentGateFillingType.class); //$NON-NLS-1$
      properties[2].setValue("order", 3); //$NON-NLS-1$
      properties[2].setDisplayName(Messages.getString("nameCulvertCrossSection")); //$NON-NLS-1$
      properties[2].setShortDescription(Messages.getString("descrCulvertCrossSection")); //$NON-NLS-1$
      properties[2].setPropertyEditorClass(MyPropertyEditor.class);

      properties[3] = new PropertyDescriptor("culvertLoss", SegmentGateFillingType.class); //$NON-NLS-1$
      properties[3].setValue("order", 4); //$NON-NLS-1$
      properties[3].setDisplayName(Messages.getString("nameCulvertLoss")); //$NON-NLS-1$
      properties[3].setShortDescription(Messages.getString("descrCulvertLoss")); //$NON-NLS-1$
      properties[3].setPropertyEditorClass(MyPropertyEditor.class);

      properties[4] = new PropertyDescriptor("submergenceStart", SegmentGateFillingType.class); //$NON-NLS-1$
      properties[4].setValue("order", 5); //$NON-NLS-1$
      properties[4].setDisplayName(Messages.getString("nameSubmergenceStart")); //$NON-NLS-1$
      properties[4].setShortDescription(Messages.getString("descrSubmergenceStart")); //$NON-NLS-1$
      properties[4].setPropertyEditorClass(MyPropertyEditor.class); 
      
      properties[5] = new PropertyDescriptor("jetCoefficient", SegmentGateFillingType.class); //$NON-NLS-1$
      properties[5].setValue("order", 6); //$NON-NLS-1$
      properties[5].setDisplayName(Messages.getString("nameJetCoefficient")); //$NON-NLS-1$
      properties[5].setShortDescription(Messages.getString("descrJetCoefficient")); //$NON-NLS-1$
      properties[5].setPropertyEditorClass(MyPropertyEditor.class);  
      properties[5].setExpert(true); 
                  
      properties[6] = new PropertyDescriptor("jetExponent", SegmentGateFillingType.class); //$NON-NLS-1$
      properties[6].setValue("order", 7); //$NON-NLS-1$
      properties[6].setDisplayName(Messages.getString("nameJetExponent")); //$NON-NLS-1$
      properties[6].setShortDescription(Messages.getString("descrJetExponent")); //$NON-NLS-1$
      properties[6].setPropertyEditorClass(MyPropertyEditor.class);  
      properties[6].setExpert(true);  
      
    } catch (IntrospectionException e) {
      e.printStackTrace();
    }

    return properties;

  }

}
