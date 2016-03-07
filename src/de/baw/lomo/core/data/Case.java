package de.baw.lomo.core.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import de.baw.lomo.utils.Utils;

@XmlRootElement(name="BAWLomoCase")
public class Case {

  private final static String VERSION = "0.2";

  private double timeMax = 1000.;

  private int numberOfNodes = 100;

  private double upstreamWaterDepth = 14.;

  private double downstreamWaterDepth = 12.5;

  private double deltaWaterDepthStop = 0.1;

  private double chamberLength = 330.;

  private double chamberWidth = 45.;

  private List<KeyValueEntry> valveHeightLookup = new ArrayList<>();

  private List<KeyValueEntry> valveWidthLookup = new ArrayList<>();

  private List<KeyValueEntry> valveLossLookup = new ArrayList<>();

  private double culvertCrossSection = 1.3 * 16.2 * 0.6;

  private double culvertLoss = 0.;

  private double culvertTopEdge = -999.;

  private double cfl = 0.5;
  
  private ValveType valveType = ValveType.SEGMENT_GATE;

  private double theta = 1.;

  private double upwind = 1.;

  private double strahlpow = 0.5;

  private double strahlbeiwert = 0.1;

  private List<KeyValueEntry> shipAreaLookup = new ArrayList<>();

  public enum ValveType {    
        
    SEGMENT_GATE("valveTypeSegmentGate"), //$NON-NLS-1$
    SLUICE_GATE("valveTypeSluiceGate"); //$NON-NLS-1$
    
    private final String key;
    
    private ValveType(String key) {
      this.key = key;
    }
  
    @Override
    public String toString() {
      return Messages.getString(key);
    }
  }

  public Case() {

    if (valveHeightLookup.size() == 0) {
      valveHeightLookup.add(new KeyValueEntry(0., 0.));
      valveHeightLookup.add(new KeyValueEntry(20., 0.));
      valveHeightLookup.add(new KeyValueEntry(248., 1.3));
      valveHeightLookup.add(new KeyValueEntry(1.e99, 1.3));
      valveHeightLookup.add(new KeyValueEntry(1.e99, 1.3));
      valveHeightLookup.add(new KeyValueEntry(1.e99, 1.3));
    }

    if (valveWidthLookup.size() == 0) {
      valveWidthLookup.add(new KeyValueEntry(0.0, 16.2));
      valveWidthLookup.add(new KeyValueEntry(0.5, 16.2));
      valveWidthLookup.add(new KeyValueEntry(1.0, 16.2));
    }

    if (valveLossLookup.size() == 0) {
      valveLossLookup.add(new KeyValueEntry(0., 0.65));
      valveLossLookup.add(new KeyValueEntry(0.5, 0.8));
      valveLossLookup.add(new KeyValueEntry(0.8, 0.95));
      valveLossLookup.add(new KeyValueEntry(1., 0.95));
      valveLossLookup.add(new KeyValueEntry(1.3, 0.8));
    }

    if (shipAreaLookup.size() == 0) {
      shipAreaLookup.add(new KeyValueEntry(5, 0));
      shipAreaLookup.add(new KeyValueEntry(10, 32.2));
      shipAreaLookup.add(new KeyValueEntry(135, 32.2));
      shipAreaLookup.add(new KeyValueEntry(145, 0));
      shipAreaLookup.add(new KeyValueEntry(145, 0));
      shipAreaLookup.add(new KeyValueEntry(145, 0));
      shipAreaLookup.add(new KeyValueEntry(145, 0));
      shipAreaLookup.add(new KeyValueEntry(145, 0));
      shipAreaLookup.add(new KeyValueEntry(145, 0));
      shipAreaLookup.add(new KeyValueEntry(145, 0));
      shipAreaLookup.add(new KeyValueEntry(145, 0));
      shipAreaLookup.add(new KeyValueEntry(145, 0));
      shipAreaLookup.add(new KeyValueEntry(145, 0));
    }
  }

  @XmlAttribute(required = true)
  public String getVersion() {
    return VERSION;
  }

  public void setVersion(String version) {
    if (!version.equals(VERSION)) {
      throw new IllegalArgumentException("Wrong case version: " + version
          + " Required version is: " + VERSION);
    }
  }

  public double getChamberLength() {
    return chamberLength;
  }

  public void setChamberLength(double chamberLength) {
    this.chamberLength = chamberLength;
  }

  public double getTimeMax() {
    return timeMax;
  }

  public void setTimeMax(double timeMax) {
    this.timeMax = timeMax;
  }

  public int getNumberOfNodes() {
    return numberOfNodes;
  }

  public void setNumberOfNodes(int numberOfNodes) {
    this.numberOfNodes = numberOfNodes;
  }

  public double getUpstreamWaterDepth() {
    return upstreamWaterDepth;
  }

  public void setUpstreamWaterDepth(double upstreamWaterDepth) {
    this.upstreamWaterDepth = upstreamWaterDepth;
  }

  public double getDownstreamWaterDepth() {
    return downstreamWaterDepth;
  }

  public void setDownstreamWaterDepth(double downstreamWaterDepth) {
    this.downstreamWaterDepth = downstreamWaterDepth;
  }

  public double getDeltaWaterDepthStop() {
    return deltaWaterDepthStop;
  }

  public void setDeltaWaterDepthStop(double deltaWaterDepthStop) {
    this.deltaWaterDepthStop = deltaWaterDepthStop;
  }

  public double getChamberWidth() {
    return chamberWidth;
  }

  public void setChamberWidth(double chamberWidth) {
    this.chamberWidth = chamberWidth;
  }

  @XmlElementWrapper
  @XmlElement(name = "entry")
  public List<KeyValueEntry> getValveHeightLookup() {
    return valveHeightLookup;
  }

  public void setValveHeightLookup(List<KeyValueEntry> valveHeight) {
    this.valveHeightLookup = valveHeight;
  }

  @XmlElementWrapper
  @XmlElement(name = "entry")
  public List<KeyValueEntry> getValveWidthLookup() {
    return valveWidthLookup;
  }

  public void setValveWidthLookup(List<KeyValueEntry> valveWidth) {
    this.valveWidthLookup = valveWidth;
  }

  @XmlElementWrapper
  @XmlElement(name = "entry")
  public List<KeyValueEntry> getValveLossLookup() {
    return valveLossLookup;
  }

  public void setValveLossLookup(List<KeyValueEntry> valveLoss) {
    this.valveLossLookup = valveLoss;
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

  public double getCulvertTopEdge() {
    return culvertTopEdge;
  }

  public void setCulvertTopEdge(double culvertlTopEdge) {
    this.culvertTopEdge = culvertlTopEdge;
  }

  public double getCfl() {
    return cfl;
  }

  public void setCfl(double cfl) {
    this.cfl = cfl;
  }

  public double getValveHeight(double time) {
    return Utils.linearInterpolate(valveHeightLookup, time);
  }

  public double getValveWidth(double height) {
    return Utils.linearInterpolate(valveWidthLookup, height);
  }

  public double getValveLoss(double height) {
    return Utils.linearInterpolate(valveLossLookup, height);
  }
  
  public ValveType getValveType() {
    return valveType;
  }

  public void setValveType(ValveType valveType) {
    this.valveType = valveType;
  }

  public double getTheta() {
    return theta;
  }

  public void setTheta(double theta) {
    this.theta = theta;
  }

  public double getUpwind() {
    return upwind;
  }

  public void setUpwind(double upwind) {
    this.upwind = upwind;
  }

  public double getStrahlpow() {
    return strahlpow;
  }

  public void setStrahlpow(double strahlpow) {
    this.strahlpow = strahlpow;
  }

  public double getStrahlbeiwert() {
    return strahlbeiwert;
  }

  public void setStrahlbeiwert(double strahlbeiwert) {
    this.strahlbeiwert = strahlbeiwert;
  }

  public double getShipArea(double x) {
    return Utils.linearInterpolate(shipAreaLookup, x);
  }

  public List<KeyValueEntry> getShipAreaLookup() {
    return shipAreaLookup;
  }

  public void setShipAreaLookup(List<KeyValueEntry> shipAreaLookup) {
    this.shipAreaLookup = shipAreaLookup;
  }

}
