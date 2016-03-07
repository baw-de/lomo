package de.baw.lomo.core.data;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import de.baw.lomo.gui.MyPropertyEditor;
import de.baw.lomo.gui.PopOverKeyValueListPropertyEditor;

public class CaseBeanInfo extends SimpleBeanInfo {

	@Override
	public PropertyDescriptor[] getPropertyDescriptors() {
	  		
		PropertyDescriptor[] properties = new PropertyDescriptor[20];
		
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
      
      properties[12] = new PropertyDescriptor("submergenceStart", Case.class); //$NON-NLS-1$
      properties[12].setValue("order", 12); //$NON-NLS-1$
      properties[12].setDisplayName(Messages.getString("nameSubmergenceStart")); //$NON-NLS-1$
      properties[12].setShortDescription(Messages.getString("descrSubmergenceStart")); //$NON-NLS-1$
      properties[12].setPropertyEditorClass(MyPropertyEditor.class);   
      
      properties[13] = new PropertyDescriptor("cfl", Case.class); //$NON-NLS-1$
      properties[13].setValue("order", 13); //$NON-NLS-1$
      properties[13].setDisplayName(Messages.getString("nameCfl")); //$NON-NLS-1$
      properties[13].setShortDescription(Messages.getString("descrCfl")); //$NON-NLS-1$
      properties[13].setPropertyEditorClass(MyPropertyEditor.class);  
      properties[13].setExpert(true);      
      
      properties[14] = new PropertyDescriptor("theta", Case.class); //$NON-NLS-1$
      properties[14].setValue("order", 14); //$NON-NLS-1$
      properties[14].setDisplayName(Messages.getString("nameTheta")); //$NON-NLS-1$
      properties[14].setShortDescription(Messages.getString("descrTheta")); //$NON-NLS-1$
      properties[14].setPropertyEditorClass(MyPropertyEditor.class);  
      properties[14].setExpert(true);    
      
      properties[15] = new PropertyDescriptor("upwind", Case.class); //$NON-NLS-1$
      properties[15].setValue("order", 15); //$NON-NLS-1$
      properties[15].setDisplayName(Messages.getString("nameUpwind")); //$NON-NLS-1$
      properties[15].setShortDescription(Messages.getString("descrUpwind")); //$NON-NLS-1$
      properties[15].setPropertyEditorClass(MyPropertyEditor.class);  
      properties[15].setExpert(true);    
                  
      properties[16] = new PropertyDescriptor("strahlpow", Case.class); //$NON-NLS-1$
      properties[16].setValue("order", 16); //$NON-NLS-1$
      properties[16].setDisplayName(Messages.getString("nameStrahlpow")); //$NON-NLS-1$
      properties[16].setShortDescription(Messages.getString("descrStrahlpow")); //$NON-NLS-1$
      properties[16].setPropertyEditorClass(MyPropertyEditor.class);  
      properties[16].setExpert(true);    
		          
      properties[17] = new PropertyDescriptor("strahlbeiwert", Case.class); //$NON-NLS-1$
      properties[17].setValue("order", 17); //$NON-NLS-1$
      properties[17].setDisplayName(Messages.getString("nameStrahlbeiwert")); //$NON-NLS-1$
      properties[17].setShortDescription(Messages.getString("descrStrahlbeiwert")); //$NON-NLS-1$
      properties[17].setPropertyEditorClass(MyPropertyEditor.class);  
      properties[17].setExpert(true);    
      
      properties[18] = new PropertyDescriptor("shipAreaLookup", Case.class); //$NON-NLS-1$
      properties[18].setValue("order", 18); //$NON-NLS-1$
      properties[18].setDisplayName(Messages.getString("nameShipAreaLookup")); //$NON-NLS-1$
      properties[18].setShortDescription(Messages.getString("descrShipAreaLookup")); //$NON-NLS-1$
      properties[18].setPropertyEditorClass(PopOverKeyValueListPropertyEditor.class);     
      
      properties[19] = new PropertyDescriptor("valveType", Case.class); //$NON-NLS-1$
      properties[19].setValue("order", 19); //$NON-NLS-1$
      properties[19].setDisplayName(Messages.getString("nameValveType")); //$NON-NLS-1$
      properties[19].setShortDescription(Messages.getString("descrValveType")); //$NON-NLS-1$  
      
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}

		return properties;
		
	}

}
