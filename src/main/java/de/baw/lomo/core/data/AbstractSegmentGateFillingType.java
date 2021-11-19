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

public abstract class AbstractSegmentGateFillingType extends AbstractGateFillingType {

  protected List<KeyValueEntry> segmentGateAMueLookup = new ArrayList<>();
    
  public AbstractSegmentGateFillingType() {    
    super();
    maximumPressureHead = 4.1;
    
    if (jetOutletLookup.isEmpty()) {
      jetOutletLookup.add(new KeyValueEntry(0.0, 999999.));
    }
  }

  @XmlElementWrapper
  @XmlElement(name = "entry")
  public List<KeyValueEntry> getSegmentGateAMueLookup() {
    return segmentGateAMueLookup;
  }

  public void setSegmentGateAMueLookup(List<KeyValueEntry> segmentGateAMueLookup) {
    this.segmentGateAMueLookup = segmentGateAMueLookup;
  }

  public double getSegmentGateAMue(double angle) {
    return Utils.linearInterpolate(segmentGateAMueLookup, angle);
  }

  public abstract double getSegmentGateAngle(double time);

  @Override
  public double getAreaTimesDischargeCoefficient(double time) {  
    final double angle = getSegmentGateAngle(time);    
    return getSegmentGateAMue(angle);
  }

  @Override
  public double getGateOpening(double time) {    
    return getSegmentGateAngle(time);
  }

}
