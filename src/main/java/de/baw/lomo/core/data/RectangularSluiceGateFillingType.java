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
