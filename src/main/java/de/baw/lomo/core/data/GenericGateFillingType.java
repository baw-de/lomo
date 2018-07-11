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
import javax.xml.bind.annotation.XmlRootElement;

import de.baw.lomo.utils.Utils;

@XmlRootElement(name="genericGateFillingType")
public class GenericGateFillingType extends AbstractGateFillingType {

  private List<KeyValueEntry> genericGateAMueLookup = new ArrayList<>();
  
  public GenericGateFillingType() {
    
    maximumPressureHead = 9999;
    
    if (jetOutletLookup.isEmpty()) {
      jetOutletLookup.add(new KeyValueEntry(0.0, 12.636));
    }
    
    if (genericGateAMueLookup.isEmpty()) {
      genericGateAMueLookup.add(new KeyValueEntry(0., 0.));
      genericGateAMueLookup.add(new KeyValueEntry(20., 0.));
      genericGateAMueLookup.add(new KeyValueEntry(107.7, 6.48));
      genericGateAMueLookup.add(new KeyValueEntry(160.31, 12.312));
      genericGateAMueLookup.add(new KeyValueEntry(195.4, 15.39));
      genericGateAMueLookup.add(new KeyValueEntry(248., 16.848));
      genericGateAMueLookup.add(new KeyValueEntry(1000., 16.848));
    }
    
  }

  @Override
  public double getGateOpening(double time) {
    return getAreaTimesDischargeCoefficient(time);
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
  public double getAreaTimesDischargeCoefficient(double time) {
    return Utils.linearInterpolate(genericGateAMueLookup, time);
  }

  @Override
  public String toString() {
    return Messages.getString("fillingTypeGenericGate");
  }
  
}
