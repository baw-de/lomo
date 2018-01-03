package de.baw.lomo.core.data;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement(name="fillingType")
@XmlSeeAlso({SluiceGateFillingType.class, SegmentGateFillingType.class, 
  SegmentGateVelocityFillingType.class, PrescribedInflowFillingType.class})
public abstract class FillingType {
  
  public final static FillingType[] LIST = new FillingType[] { 
      
      new SluiceGateFillingType(),
      new SegmentGateVelocityFillingType(),
      new SegmentGateFillingType(),
      new PrescribedInflowFillingType()
  }; 
  
  public abstract double[][] getSource(double time, double[] positions, double[] h, 
      double[] v, Case data);

}
