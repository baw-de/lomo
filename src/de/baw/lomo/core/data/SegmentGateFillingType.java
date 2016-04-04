package de.baw.lomo.core.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import de.baw.lomo.utils.Utils;

@XmlRootElement(name="segmentGateFillingType")
public class SegmentGateFillingType extends FillingType {
  
  private List<KeyValueEntry> segmentGateAngleLookup = new ArrayList<>();

  private List<KeyValueEntry> segmentGateLossLookup = new ArrayList<>();
  
  private double culvertCrossSection = 12.*666;

  private double culvertLoss = 0.;
  
  private double submergenceStart = 4.5;
  
  public SegmentGateFillingType() {
    
    if (segmentGateAngleLookup.isEmpty()) {
      segmentGateAngleLookup.add(new KeyValueEntry(0., 0.));
      segmentGateAngleLookup.add(new KeyValueEntry(800., 20.));
    }
    
    if (segmentGateLossLookup.isEmpty()) {
      segmentGateLossLookup.add(new KeyValueEntry(0., 0.));
      segmentGateLossLookup.add(new KeyValueEntry(1., 0.));
      segmentGateLossLookup.add(new KeyValueEntry(2., 0.08));
      segmentGateLossLookup.add(new KeyValueEntry(3., 0.2));
      segmentGateLossLookup.add(new KeyValueEntry(4., 0.4));
      segmentGateLossLookup.add(new KeyValueEntry(5., 0.7));
      segmentGateLossLookup.add(new KeyValueEntry(6., 1.));
      segmentGateLossLookup.add(new KeyValueEntry(7., 1.5));
      segmentGateLossLookup.add(new KeyValueEntry(8., 2.1));
      segmentGateLossLookup.add(new KeyValueEntry(9., 2.7));
      segmentGateLossLookup.add(new KeyValueEntry(10., 3.4));
      segmentGateLossLookup.add(new KeyValueEntry(11., 4.));
      segmentGateLossLookup.add(new KeyValueEntry(12., 4.5));
      segmentGateLossLookup.add(new KeyValueEntry(13., 4.9));
      segmentGateLossLookup.add(new KeyValueEntry(14., 5.2));
      segmentGateLossLookup.add(new KeyValueEntry(15., 5.4));
      segmentGateLossLookup.add(new KeyValueEntry(16., 5.5));
      segmentGateLossLookup.add(new KeyValueEntry(17., 5.6));
      segmentGateLossLookup.add(new KeyValueEntry(18., 5.5));
      segmentGateLossLookup.add(new KeyValueEntry(19., 5.5));
      segmentGateLossLookup.add(new KeyValueEntry(20., 5.4));
    }
    
  }  
  
  public double getSubmergenceStart() {
    return submergenceStart;
  }

  public void setSubmergenceStart(double submergenceStart) {
    this.submergenceStart = submergenceStart;
  }

  public List<KeyValueEntry> getSegmentGateAngleLookup() {
    return segmentGateAngleLookup;
  }

  @XmlElementWrapper
  @XmlElement(name = "entry")
  public void setSegmentGateAngleLookup(List<KeyValueEntry> segmentGateAngleLookup) {
    this.segmentGateAngleLookup = segmentGateAngleLookup;
  }

  @XmlElementWrapper
  @XmlElement(name = "entry")
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
  
  public double getCulvertCrossSection() {
    return culvertCrossSection;
  }

  public void setCulvertCrossSection(double culvertCrossSection) {
    this.culvertCrossSection = culvertCrossSection;
  }

  public double getCulvertLoss() {
    return culvertLoss;
  }

  public void setCulvertLoss(double culvertLoss) {
    this.culvertLoss = culvertLoss;
  }

  @Override
  public String toString() {
    return Messages.getString("fillingTypeSegmentGate");
  }


}
