/*
 * Copyright (c) 2019-2021 Bundesanstalt für Wasserbau
 *
 * This file is part of LoMo.
 *
 * LoMo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * LoMo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LoMo.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.baw.lomo.core.data;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;

import de.baw.lomo.utils.Utils;

public abstract class AbstractSluiceGateFillingType
    extends AbstractGateFillingType {

  protected List<KeyValueEntry> sluiceGateDischargeCoefficientLookup = new ArrayList<>();
  protected List<KeyValueEntry> sluiceGateHeightLookup = new ArrayList<>();
  protected List<KeyValueEntry> sluiceGateWidthLookup = new ArrayList<>();

  private boolean prescribedJetOutletEnabled = false;

  public AbstractSluiceGateFillingType() {
    super();
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

  public void setSluiceGateDischargeCoefficientLookup(
      List<KeyValueEntry> sluiceGateDischargeCoeffcientLookup) {
    this.sluiceGateDischargeCoefficientLookup = sluiceGateDischargeCoeffcientLookup;
  }

  public boolean isPrescribedJetOutletEnabled() {
    return prescribedJetOutletEnabled;
  }

  public void setPrescribedJetOutletEnabled(
      boolean prescribedJetOutletEnabled) {
    this.prescribedJetOutletEnabled = prescribedJetOutletEnabled;
  }

  public double getSluiceGateHeight(double time) {
    return Utils.linearInterpolate(sluiceGateHeightLookup, time);
  }

  public double getSluiceGateWidth(double height) {
    return Utils.linearInterpolate(sluiceGateWidthLookup, height);
  }

  @Override
  public double getGateOpening(double time) {
    return getSluiceGateHeight(time);
  }

  public double getSluiceGateDischargeCoefficient(double gateOpening) {
    return Utils.linearInterpolate(sluiceGateDischargeCoefficientLookup,
            gateOpening);
  }

  public double getSluiceGateCrossSection(double gateOpening) {
    return Utils.linearIntegrate(sluiceGateWidthLookup, gateOpening);
  }

  @Override
  public double getAreaTimesDischargeCoefficient(double time) {

    final double gateOpening = getGateOpening(time);
    final double crossSection = getSluiceGateCrossSection(gateOpening);
    final double loss = getSluiceGateDischargeCoefficient(gateOpening);

    return crossSection * loss;
  }

  @Override
  public double getJetOutlet(double gateOpening) {

    if (isPrescribedJetOutletEnabled()) {
      return super.getJetOutlet(gateOpening);
    } else {      
      return getSluiceGateCrossSection(gateOpening);
    }
  }

}
