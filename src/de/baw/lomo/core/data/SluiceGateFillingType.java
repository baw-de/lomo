package de.baw.lomo.core.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import de.baw.lomo.utils.Utils;

@XmlRootElement(name="sluiceGateFillingType")
public class SluiceGateFillingType extends FillingType {

  private List<KeyValueEntry> sluiceGateHeightLookup = new ArrayList<>();

  private List<KeyValueEntry> sluiceGateWidthLookup = new ArrayList<>();

  private List<KeyValueEntry> sluiceGateLossLookup = new ArrayList<>();

  private double culvertCrossSection = 1.3 * 16.2 * 0.6;

  private double culvertLoss = 0.;
  
  public SluiceGateFillingType() {
    
    if (sluiceGateHeightLookup.isEmpty()) {
      sluiceGateHeightLookup.add(new KeyValueEntry(0., 0.));
      sluiceGateHeightLookup.add(new KeyValueEntry(20., 0.));
      sluiceGateHeightLookup.add(new KeyValueEntry(248., 1.3));
      sluiceGateHeightLookup.add(new KeyValueEntry(1.e99, 1.3));
    }

    if (sluiceGateWidthLookup.isEmpty()) {
      sluiceGateWidthLookup.add(new KeyValueEntry(0.0, 16.2));
      sluiceGateWidthLookup.add(new KeyValueEntry(0.5, 16.2));
      sluiceGateWidthLookup.add(new KeyValueEntry(1.0, 16.2));
    }

    if (sluiceGateLossLookup.isEmpty()) {
      sluiceGateLossLookup.add(new KeyValueEntry(0., 0.65));
      sluiceGateLossLookup.add(new KeyValueEntry(0.5, 0.8));
      sluiceGateLossLookup.add(new KeyValueEntry(0.8, 0.95));
      sluiceGateLossLookup.add(new KeyValueEntry(1., 0.95));
      sluiceGateLossLookup.add(new KeyValueEntry(1.3, 0.8));
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
  public List<KeyValueEntry> getSluiceGateLossLookup() {
    return sluiceGateLossLookup;
  }

  public void setSluiceGateLossLookup(List<KeyValueEntry> sluiceGateLossLookup) {
    this.sluiceGateLossLookup = sluiceGateLossLookup;
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
  
  public double getSluiceGateHeight(double time) {
    return Utils.linearInterpolate(sluiceGateHeightLookup, time);
  }

  public double getSluiceGateWidth(double height) {
    return Utils.linearInterpolate(sluiceGateWidthLookup, height);
  }

  public double getSluiceGateLoss(double height) {
    return Utils.linearInterpolate(sluiceGateLossLookup, height);
  }

  @Override
  public String toString() {
    return Messages.getString("fillingTypeSluiceGate");
  }

  public double getValveLoss(double d) {
    // TODO Auto-generated method stub
    return 0;
  }
}
