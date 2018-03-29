package de.baw.lomo.core.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import de.baw.lomo.utils.Utils;

@XmlRootElement(name="segmentGateFillingType")
public class SegmentGateFillingType extends AbstractSegmentGateFillingType {
  
  private List<KeyValueEntry> segmentGateAngleLookup = new ArrayList<>();

  public SegmentGateFillingType() {
    
    
    if (segmentGateAngleLookup.isEmpty()) {
      segmentGateAngleLookup.add(new KeyValueEntry(0., 0.));
      segmentGateAngleLookup.add(new KeyValueEntry(800., 20.));
    }
    
    if (segmentGateAMueLookup.isEmpty()) {
      segmentGateAMueLookup.add(new KeyValueEntry(0., 0.));
      segmentGateAMueLookup.add(new KeyValueEntry(1., 0.));
      segmentGateAMueLookup.add(new KeyValueEntry(2., 0.08));
      segmentGateAMueLookup.add(new KeyValueEntry(3., 0.2));
      segmentGateAMueLookup.add(new KeyValueEntry(4., 0.4));
      segmentGateAMueLookup.add(new KeyValueEntry(5., 0.7));
      segmentGateAMueLookup.add(new KeyValueEntry(6., 1.));
      segmentGateAMueLookup.add(new KeyValueEntry(7., 1.5));
      segmentGateAMueLookup.add(new KeyValueEntry(8., 2.1));
      segmentGateAMueLookup.add(new KeyValueEntry(9., 2.7));
      segmentGateAMueLookup.add(new KeyValueEntry(10., 3.4));
      segmentGateAMueLookup.add(new KeyValueEntry(11., 4.));
      segmentGateAMueLookup.add(new KeyValueEntry(12., 4.5));
      segmentGateAMueLookup.add(new KeyValueEntry(13., 4.9));
      segmentGateAMueLookup.add(new KeyValueEntry(14., 5.2));
      segmentGateAMueLookup.add(new KeyValueEntry(15., 5.4));
      segmentGateAMueLookup.add(new KeyValueEntry(16., 5.5));
      segmentGateAMueLookup.add(new KeyValueEntry(17., 5.6));
      segmentGateAMueLookup.add(new KeyValueEntry(18., 5.5));
      segmentGateAMueLookup.add(new KeyValueEntry(19., 5.5));
      segmentGateAMueLookup.add(new KeyValueEntry(20., 5.4));
    }
    
  }  
  
  public List<KeyValueEntry> getSegmentGateAngleLookup() {
    return segmentGateAngleLookup;
  }

  @XmlElementWrapper
  @XmlElement(name = "entry")
  public void setSegmentGateAngleLookup(List<KeyValueEntry> segmentGateAngleLookup) {
    this.segmentGateAngleLookup = segmentGateAngleLookup;
  }

  public double getSegmentGateAngle(double time) {
    return Utils.linearInterpolate(segmentGateAngleLookup, time);
  }

  @Override
  public String toString() {
    return Messages.getString("fillingTypeSegmentGate");
  }

}
