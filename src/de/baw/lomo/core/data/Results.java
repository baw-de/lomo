package de.baw.lomo.core.data;

import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public interface Results {
  
  @XmlList
  double[] getTimeline();
  
  @XmlList
  double[] getDischargeOverTime();
  
  @XmlList
  double[] getSlopeOverTime();
  
  @XmlList
  double[] getChamberWaterLevelOverTime();

  @XmlList
  double[] getLongitudinalForceOverTime();
  
  @XmlList
  double[] getValveOpeningOverTime();

}
