/*
 * Copyright (c) 2019-2024 Bundesanstalt für Wasserbau
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

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="sluiceGateFillingType")
public class SluiceGateFillingType extends AbstractSluiceGateFillingType {

  protected double maximumPressureHead;

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

  public double getMaximumPressureHead() {
    return maximumPressureHead;
  }

  public void setMaximumPressureHead(double maximumPressureHead) {
    this.maximumPressureHead = maximumPressureHead;
  }

  @Override
  public String toString() {
    return Messages.getString("fillingTypeRectangularSluiceGate");
  }

}
