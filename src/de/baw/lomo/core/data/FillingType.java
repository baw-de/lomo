package de.baw.lomo.core.data;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement(name="fillingType")
@XmlSeeAlso({SluiceGateFillingType.class, SegmentGateFillingType.class})
public abstract class FillingType {
  
  public final static FillingType[] LIST = new FillingType[] { 
      
      new SluiceGateFillingType(),
      new SegmentGateFillingType()
  }; 

}
