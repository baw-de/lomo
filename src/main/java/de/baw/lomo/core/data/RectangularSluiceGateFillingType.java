package de.baw.lomo.core.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import de.baw.lomo.utils.Utils;

@XmlRootElement(name="rectangularSluiceGateFillingType")
public class RectangularSluiceGateFillingType extends AbstractSluiceGateFillingType {

  private List<KeyValueEntry> sluiceGateHeightLookup = new ArrayList<>();

  private double sluiceGateWidth = 16.2;
  
  public RectangularSluiceGateFillingType() {
    
    super();
    
    maximumPressureHead = 9999;
    
    if (sluiceGateHeightLookup.isEmpty()) {
      sluiceGateHeightLookup.add(new KeyValueEntry(0., 0.));
      sluiceGateHeightLookup.add(new KeyValueEntry(20., 0.));
      sluiceGateHeightLookup.add(new KeyValueEntry(248., 1.3));
      sluiceGateHeightLookup.add(new KeyValueEntry(1000., 1.3));
    }

    if (sluiceGateDischargeCoefficientLookup.isEmpty()) {
      sluiceGateDischargeCoefficientLookup.add(new KeyValueEntry(0., 0.65));
      sluiceGateDischargeCoefficientLookup.add(new KeyValueEntry(0.5, 0.8));
      sluiceGateDischargeCoefficientLookup.add(new KeyValueEntry(0.8, 0.95));
      sluiceGateDischargeCoefficientLookup.add(new KeyValueEntry(1., 0.95));
      sluiceGateDischargeCoefficientLookup.add(new KeyValueEntry(1.3, 0.8));
    }
    
    if (jetOutletLookup.isEmpty()) {
      jetOutletLookup.add(new KeyValueEntry(0.0, 1.3 * 16.2 * 0.6));
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

  public double getSluiceGateHeight(double time) {
    return Utils.linearInterpolate(sluiceGateHeightLookup, time);
  }

  public double getSluiceGateWidth() {
    return sluiceGateWidth;
  }

  public void setSluiceGateWidth(double sluiceGateWidth) {
    this.sluiceGateWidth = sluiceGateWidth;
  }

  @Override
  public double getGateOpening(double time) {    
    return getSluiceGateHeight(time);
  }  

  @Override
  public double getSluiceGateCrossSection(double gateOpening) {
        return gateOpening * getSluiceGateWidth();
  }

  @Override
  public String toString() {
    return Messages.getString("fillingTypeRectangularSluiceGate");
  }

}
