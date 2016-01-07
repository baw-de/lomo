package de.baw.lomo.core.data;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import de.baw.lomo.gui.MyPropertyEditor;
import de.baw.lomo.gui.PopOverKeyValueListPropertyEditor;

public class CaseBeanInfo extends SimpleBeanInfo {

	@Override
	public PropertyDescriptor[] getPropertyDescriptors() {
	  		
		PropertyDescriptor[] properties = new PropertyDescriptor[13];
		
		try {
		  
      properties[0] = new PropertyDescriptor("timeMax", Case.class); //$NON-NLS-1$
      properties[0].setValue("order", 0); //$NON-NLS-1$
      properties[0].setDisplayName(Messages.getString("nameMaxSimTime")); //$NON-NLS-1$
      properties[0].setShortDescription(Messages.getString("descrMaxSimTime")); //$NON-NLS-1$
      properties[0].setPropertyEditorClass(MyPropertyEditor.class);
      properties[0].setExpert(true);
      
      properties[1] = new PropertyDescriptor("numberOfNodes", Case.class); //$NON-NLS-1$
      properties[1].setValue("order", 1); //$NON-NLS-1$
      properties[1].setDisplayName(Messages.getString("nameNbOfNodes")); //$NON-NLS-1$
      properties[1].setShortDescription(Messages.getString("descrNbOfNodes")); //$NON-NLS-1$
      properties[1].setPropertyEditorClass(MyPropertyEditor.class);
      properties[1].setExpert(true);
      
      properties[2] = new PropertyDescriptor("upstreamWaterDepth", Case.class); //$NON-NLS-1$
      properties[2].setValue("order", 2); //$NON-NLS-1$
      properties[2].setDisplayName(Messages.getString("nameUpstreamWaterDepth")); //$NON-NLS-1$
      properties[2].setShortDescription(Messages.getString("descrUpstreamWaterDepth")); //$NON-NLS-1$
      properties[2].setPropertyEditorClass(MyPropertyEditor.class);
      
      properties[3] = new PropertyDescriptor("downstreamWaterDepth", Case.class); //$NON-NLS-1$
      properties[3].setValue("order", 3); //$NON-NLS-1$
      properties[3].setDisplayName(Messages.getString("nameDownstreamWaterDepth")); //$NON-NLS-1$
      properties[3].setShortDescription(Messages.getString("descrDownstreamWaterDepth")); //$NON-NLS-1$
      properties[3].setPropertyEditorClass(MyPropertyEditor.class);
      
      properties[4] = new PropertyDescriptor("deltaWaterDepthStop", Case.class); //$NON-NLS-1$
      properties[4].setValue("order", 4); //$NON-NLS-1$
      properties[4].setDisplayName(Messages.getString("nameDeltaWaterDepthStop")); //$NON-NLS-1$
      properties[4].setShortDescription(Messages.getString("descrDeltaWaterDepthStop")); //$NON-NLS-1$
      properties[4].setPropertyEditorClass(MyPropertyEditor.class);
      properties[4].setExpert(true);
      
      properties[5] = new PropertyDescriptor("chamberLength", Case.class); //$NON-NLS-1$
      properties[5].setValue("order", 5); //$NON-NLS-1$
      properties[5].setDisplayName(Messages.getString("nameChamberLength")); //$NON-NLS-1$
      properties[5].setShortDescription(Messages.getString("descrChamberLength")); //$NON-NLS-1$
      properties[5].setPropertyEditorClass(MyPropertyEditor.class);
      
      properties[6] = new PropertyDescriptor("chamberWidth", Case.class); //$NON-NLS-1$
      properties[6].setValue("order", 6); //$NON-NLS-1$
      properties[6].setDisplayName(Messages.getString("nameChamberWidth")); //$NON-NLS-1$
      properties[6].setShortDescription(Messages.getString("descrChamberWidth")); //$NON-NLS-1$
      properties[6].setPropertyEditorClass(MyPropertyEditor.class);
      
      properties[7] = new PropertyDescriptor("valveHeightLookup", Case.class); //$NON-NLS-1$
      properties[7].setValue("order", 7); //$NON-NLS-1$
      properties[7].setDisplayName(Messages.getString("nameValveHeightLookup")); //$NON-NLS-1$
      properties[7].setShortDescription(Messages.getString("descrValveHeightLookup")); //$NON-NLS-1$
      properties[7].setPropertyEditorClass(PopOverKeyValueListPropertyEditor.class);      
      
      properties[8] = new PropertyDescriptor("valveWidthLookup", Case.class); //$NON-NLS-1$
      properties[8].setValue("order", 8); //$NON-NLS-1$
      properties[8].setDisplayName(Messages.getString("nameValveWidthLookup")); //$NON-NLS-1$
      properties[8].setShortDescription(Messages.getString("descrValveWidthLookup")); //$NON-NLS-1$
      properties[8].setPropertyEditorClass(PopOverKeyValueListPropertyEditor.class);
      
      properties[9] = new PropertyDescriptor("valveLossLookup", Case.class); //$NON-NLS-1$
      properties[9].setValue("order", 9); //$NON-NLS-1$
      properties[9].setDisplayName(Messages.getString("nameValveLossLookup")); //$NON-NLS-1$
      properties[9].setShortDescription(Messages.getString("descrValveLossLookup")); //$NON-NLS-1$
      properties[9].setPropertyEditorClass(PopOverKeyValueListPropertyEditor.class);
      			
      properties[10] = new PropertyDescriptor("culvertCrossSection", Case.class); //$NON-NLS-1$
      properties[10].setValue("order", 10); //$NON-NLS-1$
      properties[10].setDisplayName(Messages.getString("nameCulvertCrossSection")); //$NON-NLS-1$
      properties[10].setShortDescription(Messages.getString("descrCulvertCrossSection")); //$NON-NLS-1$
      properties[10].setPropertyEditorClass(MyPropertyEditor.class);
      
      properties[11] = new PropertyDescriptor("culvertLoss", Case.class); //$NON-NLS-1$
      properties[11].setValue("order", 11); //$NON-NLS-1$
      properties[11].setDisplayName(Messages.getString("nameCulvertLoss")); //$NON-NLS-1$
      properties[11].setShortDescription(Messages.getString("descrCulvertLoss")); //$NON-NLS-1$
      properties[11].setPropertyEditorClass(MyPropertyEditor.class);
      
      properties[12] = new PropertyDescriptor("culvertTopEdge", Case.class); //$NON-NLS-1$
      properties[12].setValue("order", 12); //$NON-NLS-1$
      properties[12].setDisplayName(Messages.getString("nameCulvertTopEdge")); //$NON-NLS-1$
      properties[12].setShortDescription(Messages.getString("descrCulvertTopEdge")); //$NON-NLS-1$
      properties[12].setPropertyEditorClass(MyPropertyEditor.class);    
      
      
      		
		
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}

		return properties;
		
	}

}
