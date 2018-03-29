package de.baw.lomo.core.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import de.baw.lomo.utils.Utils;

public abstract class AbstractSegmentGateFillingType extends AbstractGateFillingType {

  protected List<KeyValueEntry> segmentGateAMueLookup = new ArrayList<>();
    
  public AbstractSegmentGateFillingType() {    
    super();
    maximumPressureHead = 4.5;
    
    if (jetOutletLookup.isEmpty()) {
      jetOutletLookup.add(new KeyValueEntry(0.0, 999999.));
    }
  }

  @XmlElementWrapper
  @XmlElement(name = "entry")
  public List<KeyValueEntry> getSegmentGateAMueLookup() {
    return segmentGateAMueLookup;
  }

  public void setSegmentGateAMueLookup(List<KeyValueEntry> segmentGateAMueLookup) {
    this.segmentGateAMueLookup = segmentGateAMueLookup;
  }

  public double getSegmentGateAMue(double angle) {
    return Utils.linearInterpolate(segmentGateAMueLookup, angle);
  }

  public abstract double getSegmentGateAngle(double time);

  @Override
  public double getAreaTimesDischargeCoefficient(double time) {  
    final double angle = getSegmentGateAngle(time);    
    return getSegmentGateAMue(angle);
  }

  @Override
  public double getGateOpening(double time) {    
    return getSegmentGateAngle(time);
  }

}