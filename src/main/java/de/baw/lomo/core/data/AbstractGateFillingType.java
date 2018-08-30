/*******************************************************************************
 * Copyright (C) 2018 Bundesanstalt für Wasserbau
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
 ******************************************************************************/
package de.baw.lomo.core.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import de.baw.lomo.utils.Utils;

public abstract class AbstractGateFillingType extends FillingType {
  
  private static final double GRAVITY = 9.81;
  
  private double jetCoefficientC0 = 0.0;
  private double jetCoefficientC1 = 1.0;
  private double jetCoefficientC2 = 0.1;
  private double jetCoefficientC3 = 2.0;
  protected List<KeyValueEntry> jetOutletLookup = new ArrayList<>();
  protected double maximumPressureHead;

  public abstract double getGateOpening(double time);

  public abstract double getAreaTimesDischargeCoefficient(double time);

  public double getJetCoefficientC0() {
    return jetCoefficientC0;
  }

  public void setJetCoefficientC0(double jetCoefficientC0) {
    this.jetCoefficientC0 = jetCoefficientC0;
  }

  public double getJetCoefficientC1() {
    return jetCoefficientC1;
  }

  public void setJetCoefficientC1(double jetCoefficientC1) {
    this.jetCoefficientC1 = jetCoefficientC1;
  }

  public double getJetCoefficientC2() {
    return jetCoefficientC2;
  }

  public void setJetCoefficientC2(double jetCoefficientC2) {
    this.jetCoefficientC2 = jetCoefficientC2;
  }

  public double getJetCoefficientC3() {
    return jetCoefficientC3;
  }

  public void setJetCoefficientC3(double jetCoefficientC3) {
    this.jetCoefficientC3 = jetCoefficientC3;
  }

  @XmlElementWrapper
  @XmlElement(name = "entry")
  public List<KeyValueEntry> getJetOutletLookup() {
    return jetOutletLookup;
  }

  public void setJetOutletLookup(List<KeyValueEntry> jetOutletLookup) {
    this.jetOutletLookup = jetOutletLookup;
  }

  public double getJetOutlet(double gateOpening) {
    return Utils.linearInterpolate(jetOutletLookup, gateOpening);
  }

  public double getMaximumPressureHead() {
    return maximumPressureHead;
  }

  public void setMaximumPressureHead(double maximumPressureHead) {
    this.maximumPressureHead = maximumPressureHead;
  }

  // ***************************************************************************

  @Override
  public double[][] getSource(double time, double[] positions, double[] h,
      double[] v, Case data) {

    final double[][] source = new double[2][positions.length];

    final double aMue = getAreaTimesDischargeCoefficient(time);
    final double ow = data.getUpstreamWaterLevel();
    final double maxDh = getMaximumPressureHead();

    // Effektive Fallhöhe: Entweder OW bis Schütz oder OW bis UW
    final double dh = Math.min(ow - h[0], maxDh);

    double flowRate =  Math.signum(dh) * aMue * Math.sqrt(2. * GRAVITY * Math.abs(dh));  

    source[0][0] = flowRate;
    source[1][0] = flowRate * 1.;

    return source;
  }

  @Override
  public double getEffectiveFlowSection(double time, double position) {

    final double jetOutlet = getJetOutlet(getGateOpening(time));
    final double c0 = getJetCoefficientC0();
    final double c1 = getJetCoefficientC1();
    final double c2 = getJetCoefficientC2();
    final double c3 = getJetCoefficientC3();

    return c0 + c1 * jetOutlet + c2 * Math.pow(position, c3);
  }

}
