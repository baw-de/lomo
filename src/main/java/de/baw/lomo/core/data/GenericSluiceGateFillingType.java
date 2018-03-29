package de.baw.lomo.core.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import de.baw.lomo.utils.Utils;

@XmlRootElement(name="genericSluiceGateFillingType")
public class GenericSluiceGateFillingType extends AbstractSluiceGateFillingType {

  private List<KeyValueEntry> sluiceGateCrossSectionLookup = new ArrayList<>();

  public GenericSluiceGateFillingType() {
    
    super();
    maximumPressureHead = 9999;
    
    if (sluiceGateCrossSectionLookup.isEmpty()) {
      sluiceGateCrossSectionLookup.add(new KeyValueEntry(0., 0.));
      sluiceGateCrossSectionLookup.add(new KeyValueEntry(20., 0.));
      sluiceGateCrossSectionLookup.add(new KeyValueEntry(248., 21.06));
      sluiceGateCrossSectionLookup.add(new KeyValueEntry(1000., 21.06));
    }

    if (sluiceGateDischargeCoefficientLookup.isEmpty()) {
      sluiceGateDischargeCoefficientLookup.add(new KeyValueEntry(0., 0.65));
      sluiceGateDischargeCoefficientLookup.add(new KeyValueEntry(8.1, 0.8));
      sluiceGateDischargeCoefficientLookup.add(new KeyValueEntry(12.96, 0.95));
      sluiceGateDischargeCoefficientLookup.add(new KeyValueEntry(16.2, 0.95));
      sluiceGateDischargeCoefficientLookup.add(new KeyValueEntry(21.06, 0.8));
    }
    
    if (jetOutletLookup.isEmpty()) {
      jetOutletLookup.add(new KeyValueEntry(0.0, 1.3 * 16.2 * 0.6));
    }
    
  }

  @XmlElementWrapper
  @XmlElement(name = "entry")
  public List<KeyValueEntry> getSluiceGateCrossSectionLookup() {
    return sluiceGateCrossSectionLookup;
  }

  public void setSluiceGateCrossSectionLookup(List<KeyValueEntry> sluiceGateCrossSectionLookup) {
    this.sluiceGateCrossSectionLookup = sluiceGateCrossSectionLookup;
  }

  public double getSluiceGateCrossSection(double gateOpening) {
    return gateOpening;
  }

  @Override
  public double getGateOpening(double time) {    
    return Utils.linearInterpolate(sluiceGateCrossSectionLookup, time);
  }
  
  @Override
  public String toString() {
    return Messages.getString("fillingTypeGenericSluiceGate");
  }

}
