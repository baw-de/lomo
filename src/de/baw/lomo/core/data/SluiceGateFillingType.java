package de.baw.lomo.core.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import de.baw.lomo.utils.Utils;

@XmlRootElement(name="sluiceGateFillingType")
public class SluiceGateFillingType extends GateFillingType {

  private List<KeyValueEntry> sluiceGateHeightLookup = new ArrayList<>();

  private List<KeyValueEntry> sluiceGateWidthLookup = new ArrayList<>();

  private List<KeyValueEntry> sluiceGateDischargeCoefficientLookup = new ArrayList<>();

  private double culvertCrossSection = 1.3 * 16.2 * 0.6;

  private double culvertLoss = 0.;
  
  private double maximumPressureHead = 9999;
  
  public SluiceGateFillingType() {
    
    if (sluiceGateHeightLookup.isEmpty()) {
      sluiceGateHeightLookup.add(new KeyValueEntry(0., 0.));
      sluiceGateHeightLookup.add(new KeyValueEntry(20., 0.));
      sluiceGateHeightLookup.add(new KeyValueEntry(248., 1.3));
      sluiceGateHeightLookup.add(new KeyValueEntry(1000., 1.3));
    }

    if (sluiceGateWidthLookup.isEmpty()) {
      sluiceGateWidthLookup.add(new KeyValueEntry(0.0, 16.2));
      sluiceGateWidthLookup.add(new KeyValueEntry(0.5, 16.2));
      sluiceGateWidthLookup.add(new KeyValueEntry(1.0, 16.2));
    }

    if (sluiceGateDischargeCoefficientLookup.isEmpty()) {
      sluiceGateDischargeCoefficientLookup.add(new KeyValueEntry(0., 0.65));
      sluiceGateDischargeCoefficientLookup.add(new KeyValueEntry(0.5, 0.8));
      sluiceGateDischargeCoefficientLookup.add(new KeyValueEntry(0.8, 0.95));
      sluiceGateDischargeCoefficientLookup.add(new KeyValueEntry(1., 0.95));
      sluiceGateDischargeCoefficientLookup.add(new KeyValueEntry(1.3, 0.8));
    }
    
  }

  @XmlElementWrapper
  @XmlElement(name = "entry")
  public List<KeyValueEntry> getSluiceGateHeightLookup() {
    return sluiceGateHeightLookup;
  }

  public void setSluiceGateHeightLookup(List<KeyValueEntry> sluiceGateHeightLookup) {
    this.sluiceGateHeightLookup = sluiceGateHeightLookup;
  }

  @XmlElementWrapper
  @XmlElement(name = "entry")
  public List<KeyValueEntry> getSluiceGateWidthLookup() {
    return sluiceGateWidthLookup;
  }

  public void setSluiceGateWidthLookup(List<KeyValueEntry> sluiceGateWidthLookup) {
    this.sluiceGateWidthLookup = sluiceGateWidthLookup;
  }

  @XmlElementWrapper
  @XmlElement(name = "entry")
  public List<KeyValueEntry> getSluiceGateDischargeCoefficientLookup() {
    return sluiceGateDischargeCoefficientLookup;
  }

  public void setSluiceGateDischargeCoefficientLookup(List<KeyValueEntry> sluiceGateDischargeCoeffcientLookup) {
    this.sluiceGateDischargeCoefficientLookup = sluiceGateDischargeCoeffcientLookup;
  }
  
  public double getSluiceGateHeight(double time) {
    return Utils.linearInterpolate(sluiceGateHeightLookup, time);
  }

  public double getSluiceGateWidth(double height) {
    return Utils.linearInterpolate(sluiceGateWidthLookup, height);
  }

  public double getSluiceGateDischargeCoefficient(double height) {
    return Utils.linearInterpolate(sluiceGateDischargeCoefficientLookup, height);
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
    
    final double height = getSluiceGateHeight(time);
    final double width = getSluiceGateWidth(height);
    final double loss = getSluiceGateDischargeCoefficient(height);
    
    return height * width * loss;
  }

  @Override
  public double getGateOpening(double time) {
    
    return getSluiceGateHeight(time);
  }

  @Override
  public String toString() {
    return Messages.getString("fillingTypeSluiceGate");
  }

}
