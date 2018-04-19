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

public abstract class AbstractGateFillingType extends GateFillingType {
  
  private static final double GRAVITY = 9.81; 
  
  private double jetCoefficient = 0.05;
  
  private double jetExponent = 1.0;
  
  protected List<KeyValueEntry> jetOutletLookup = new ArrayList<>();
  
  protected double maximumPressureHead;
  
  public double getJetCoefficient() {
    return jetCoefficient;
  }

  public void setJetCoefficient(double jetCoeffcient) {
    this.jetCoefficient = jetCoeffcient;
  }

  public double getJetExponent() {
    return jetExponent;
  }

  public void setJetExponent(double jetExponent) {
    this.jetExponent = jetExponent;
  }

  public double getMaximumPressureHead() {
    return maximumPressureHead;
  }

  public void setMaximumPressureHead(double maximumPressureHead) {
    this.maximumPressureHead = maximumPressureHead;
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

  public abstract double getAreaTimesDischargeCoefficient(double time);

  
  // ***************************************************************************
  
  @Override
  public double getJetCrossSection(double position, double time) {

    final double jetOutlet = getJetOutlet(getGateOpening(time));
    final double jetCoefficient = getJetCoefficient();
    final double jetExponent = getJetExponent();

    return jetOutlet + jetCoefficient * Math.pow(position, jetExponent) * jetOutlet;
  }

  @Override
  public double[][] getSource(double time, double[] positions, double[] h,
      double[] v, Case data) {
    
    final double[][] source = new double[2][positions.length];
    
    final double aMue = getAreaTimesDischargeCoefficient(time);
    final double ow = data.getUpstreamWaterLevel();
    final double maxDh = getMaximumPressureHead();

    // Effektive Fallhöhe: Entweder OW bis Schütz oder OW bis UW
    final double dh = Math.min(ow - h[0], maxDh);

    double flowRate = aMue * Math.sqrt(2. * GRAVITY * Math.abs(dh));

    // TODO: Warum eigentlich?
    // Negative Qs abfangen
    if (dh < 0.) {
      flowRate = 0.;
    }    
    
    source[0][0] = flowRate;
    source[1][0] = flowRate * 1.;
    
    return source;
  }  
  
}
