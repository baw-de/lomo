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
import jakarta.xml.bind.annotation.XmlRootElement;

import de.baw.lomo.utils.Utils;

@XmlRootElement(name="genericGateFillingType")
public class GenericGateFillingType extends AbstractGateFillingType {
  
  private List<KeyValueEntry> genericGateOpeningLookup = new ArrayList<>();

  private List<KeyValueEntry> genericGateAMueLookup = new ArrayList<>();

  protected double maximumPressureHead;
  
  public GenericGateFillingType() {
    
    maximumPressureHead = 4.0;
    
    if (jetOutletLookup.isEmpty()) {
      jetOutletLookup.add(new KeyValueEntry(0.0, 0.0));
      jetOutletLookup.add(new KeyValueEntry(100., 4.8));
    }
    
    if (genericGateOpeningLookup.isEmpty()) {
      genericGateOpeningLookup.add(new KeyValueEntry(0., 0.));
      genericGateOpeningLookup.add(new KeyValueEntry(40., 4.));
      genericGateOpeningLookup.add(new KeyValueEntry(540., 100.));
    }
    
    if (genericGateAMueLookup.isEmpty()) {
      genericGateAMueLookup.add(new KeyValueEntry(0., 0.));
      genericGateAMueLookup.add(new KeyValueEntry(4., 0.16));
      genericGateAMueLookup.add(new KeyValueEntry(100., 4.0));
    }
    
  }

  public double getMaximumPressureHead() {
    return maximumPressureHead;
  }

  public void setMaximumPressureHead(double maximumPressureHead) {
    this.maximumPressureHead = maximumPressureHead;
  }

  @XmlElementWrapper
  @XmlElement(name = "entry")
  public List<KeyValueEntry> getGenericGateOpeningLookup() {
    return genericGateOpeningLookup;
  }

  public void setGenericGateOpeningLookup(List<KeyValueEntry> genericGateOpeningLookup) {
    this.genericGateOpeningLookup = genericGateOpeningLookup;
  }

  @XmlElementWrapper
  @XmlElement(name = "entry")
  public List<KeyValueEntry> getGenericGateAMueLookup() {
    return genericGateAMueLookup;
  }

  public void setGenericGateAMueLookup(
      List<KeyValueEntry> genericGateAMueLookup) {
    this.genericGateAMueLookup = genericGateAMueLookup;
  }

  @Override
  public double getGateOpening(double time) {
    return  Utils.linearInterpolate(genericGateOpeningLookup, time);
  }

  @Override
  public double getAreaTimesDischargeCoefficient(double time) {
    return Utils.linearInterpolate(genericGateAMueLookup, getGateOpening(time));
  }

  @Override
  public String toString() {
    return Messages.getString("fillingTypeGenericGate");
  }
  
}
