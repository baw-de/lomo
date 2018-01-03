package de.baw.lomo.core.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import de.baw.lomo.utils.Utils;

public abstract class AbstractSegmentGateFillingType extends GateFillingType {

  private double culvertCrossSection = 999999.;
  private double culvertLoss = 0.;
  private double maximumPressureHead = 4.5;
  protected List<KeyValueEntry> segmentGateAMueLookup = new ArrayList<>();

  public AbstractSegmentGateFillingType() {
    super();
  }

  public double getSegmentGateAMue(double angle) {
    return Utils.linearInterpolate(segmentGateAMueLookup, angle);
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

  public double getMaximumPressureHead() {
    return maximumPressureHead;
  }

  public void setMaximumPressureHead(double maximumPressureHead) {
    this.maximumPressureHead = maximumPressureHead;
  }

  @Override
  public double getAreaTimesDischargeCoefficient(double time) {
  
    final double angle = getSegmentGateAngle(time);
    
    return getSegmentGateAMue(angle);
  }

  public abstract double getSegmentGateAngle(double time);

  @Override
  public double getGateOpening(double time) {
    
    return getSegmentGateAngle(time);
  }

  @XmlElementWrapper
  @XmlElement(name = "entry")
  public List<KeyValueEntry> getSegmentGateAMueLookup() {
    return segmentGateAMueLookup;
  }

  public void setSegmentGateAMueLookup(List<KeyValueEntry> segmentGateAMueLookup) {
    this.segmentGateAMueLookup = segmentGateAMueLookup;
  }

}