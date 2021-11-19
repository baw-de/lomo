/*******************************************************************************
 * Copyright (C) 2019 Bundesanstalt für Wasserbau
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

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import de.baw.lomo.utils.Utils;

@XmlRootElement(name="sluiceGateFillingType")
public class SluiceGateFillingType extends AbstractSluiceGateFillingType {

  private List<KeyValueEntry> sluiceGateHeightLookup = new ArrayList<>();
  
  private List<KeyValueEntry> sluiceGateWidthLookup = new ArrayList<>();
  
  public SluiceGateFillingType() {
    
    super();
    
    maximumPressureHead = 4.0;
    
    if (sluiceGateHeightLookup.isEmpty()) {
      sluiceGateHeightLookup.add(new KeyValueEntry(0.0, 0.0));
      sluiceGateHeightLookup.add(new KeyValueEntry(40.0, 0.04));
      sluiceGateHeightLookup.add(new KeyValueEntry(540.0, 1.0));
    }
    
    if (sluiceGateWidthLookup.isEmpty()) {
      sluiceGateWidthLookup.add(new KeyValueEntry(0., 8.0));
    }

    if (sluiceGateDischargeCoefficientLookup.isEmpty()) {
      sluiceGateDischargeCoefficientLookup.add(new KeyValueEntry(0., 0.5));
    }
    
    if (jetOutletLookup.isEmpty()) {
      jetOutletLookup.add(new KeyValueEntry(0.0, 0.0));
      jetOutletLookup.add(new KeyValueEntry(1.0, 1.0 * 8.0 * 0.6));
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

  @XmlElementWrapper
  @XmlElement(name = "entry")
  public List<KeyValueEntry> getSluiceGateWidthLookup() {
    return sluiceGateWidthLookup;
  }

  public void setSluiceGateWidthLookup(List<KeyValueEntry> sluiceGateWidthLookup) {
    this.sluiceGateWidthLookup = sluiceGateWidthLookup;
  }
  
  public double getSluiceGateWidth(double height) {
    return Utils.linearInterpolate(sluiceGateWidthLookup, height);
  }

  @Override
  public double getGateOpening(double time) {    
    return getSluiceGateHeight(time);
  }  

  @Override
  public double getSluiceGateCrossSection(double gateOpening) {
    return Utils.linearIntegrate(sluiceGateWidthLookup, gateOpening);
  }

  @Override
  public String toString() {
    return Messages.getString("fillingTypeRectangularSluiceGate");
  }

}
