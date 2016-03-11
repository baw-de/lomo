package de.baw.lomo.core.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import de.baw.lomo.utils.Utils;

@XmlRootElement(name="segmentGateFillingType")
public class SegmentGateFillingType extends FillingType {
  
  private List<KeyValueEntry> segmentGateAngleLookup = new ArrayList<>();

  private List<KeyValueEntry> segmentGateLossLookup = new ArrayList<>();
  
  public SegmentGateFillingType() {
    
    if (segmentGateAngleLookup.isEmpty()) {
      segmentGateAngleLookup.add(new KeyValueEntry(0., 0.));
      segmentGateAngleLookup.add(new KeyValueEntry(20., 0.));
      segmentGateAngleLookup.add(new KeyValueEntry(248., 1.3));
      segmentGateAngleLookup.add(new KeyValueEntry(1.e99, 1.3));
    }
    
    if (segmentGateLossLookup.isEmpty()) {
      segmentGateLossLookup.add(new KeyValueEntry(0., 0.65));
      segmentGateLossLookup.add(new KeyValueEntry(0.5, 0.8));
      segmentGateLossLookup.add(new KeyValueEntry(0.8, 0.95));
      segmentGateLossLookup.add(new KeyValueEntry(1., 0.95));
      segmentGateLossLookup.add(new KeyValueEntry(1.3, 0.8));
    }
    
  }  

  public List<KeyValueEntry> getSegmentGateAngleLookup() {
    return segmentGateAngleLookup;
  }

  public void setSegmentGateAngleLookup(List<KeyValueEntry> segmentGateAngleLookup) {
    this.segmentGateAngleLookup = segmentGateAngleLookup;
  }

  public List<KeyValueEntry> getSegmentGateLossLookup() {
    return segmentGateLossLookup;
  }

  public void setSegmentGateLossLookup(List<KeyValueEntry> segmentGateLossLookup) {
    this.segmentGateLossLookup = segmentGateLossLookup;
  }
  
  public double getSegmentGateAngle(double time) {
    return Utils.linearInterpolate(segmentGateAngleLookup, time);
  }

  public double getSegmentGateLoss(double angle) {
    return Utils.linearInterpolate(segmentGateLossLookup, angle);
  }

  @Override
  public String toString() {
    return Messages.getString("fillingTypeSegmentGate");
  }
}
