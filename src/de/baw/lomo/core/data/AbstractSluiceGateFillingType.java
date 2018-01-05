package de.baw.lomo.core.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import de.baw.lomo.utils.Utils;

public abstract class AbstractSluiceGateFillingType
    extends AbstractGateFillingType {

  protected List<KeyValueEntry> sluiceGateDischargeCoefficientLookup = new ArrayList<>();

  private boolean prescribedJetOutletEnabled = false;

  public AbstractSluiceGateFillingType() {
    super();
  }

  @XmlElementWrapper
  @XmlElement(name = "entry")
  public List<KeyValueEntry> getSluiceGateDischargeCoefficientLookup() {
    return sluiceGateDischargeCoefficientLookup;
  }

  public void setSluiceGateDischargeCoefficientLookup(
      List<KeyValueEntry> sluiceGateDischargeCoeffcientLookup) {
    this.sluiceGateDischargeCoefficientLookup = sluiceGateDischargeCoeffcientLookup;
  }

  public double getSluiceGateDischargeCoefficient(double gateOpening) {
    return Utils.linearInterpolate(sluiceGateDischargeCoefficientLookup,
        gateOpening);
  }

  public boolean isPrescribedJetOutletEnabled() {
    return prescribedJetOutletEnabled;
  }

  public void setPrescribedJetOutletEnabled(
      boolean prescribedJetOutletEnabled) {
    this.prescribedJetOutletEnabled = prescribedJetOutletEnabled;
  }

  public abstract double getSluiceGateCrossSection(double gateOpening);

  @Override
  public double getAreaTimesDischargeCoefficient(double time) {

    final double gateOpening = getGateOpening(time);
    final double crossSection = getSluiceGateCrossSection(gateOpening);
    final double loss = getSluiceGateDischargeCoefficient(gateOpening);

    return crossSection * loss;
  }

  @Override
  public double getJetOutlet(double gateOpening) {

    if (prescribedJetOutletEnabled) {
      return super.getJetOutlet(gateOpening);
    } else {
      // Zero outlet is not allowed!
      return Math.max(0.01, getSluiceGateCrossSection(gateOpening));
    }
  }

}