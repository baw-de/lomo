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

@XmlRootElement(name="segmentGateVelocityFillingType")
public class SegmentGateVelocityFillingType extends AbstractSegmentGateFillingType {
  
  private List<KeyValueEntry> segmentGateVelocityLookup = new ArrayList<>();

  public SegmentGateVelocityFillingType() {
    
    
    if (segmentGateVelocityLookup.isEmpty()) {
      segmentGateVelocityLookup.add(new KeyValueEntry(0., 0.025));
      segmentGateVelocityLookup.add(new KeyValueEntry(800., 0.025));
    }
    
    if (segmentGateAMueLookup.isEmpty()) {
      segmentGateAMueLookup.add(new KeyValueEntry(0., 0.0));
      segmentGateAMueLookup.add(new KeyValueEntry(1., 0.03));
      segmentGateAMueLookup.add(new KeyValueEntry(2., 0.09));
      segmentGateAMueLookup.add(new KeyValueEntry(3., 0.16));
      segmentGateAMueLookup.add(new KeyValueEntry(4., 0.29));
      segmentGateAMueLookup.add(new KeyValueEntry(5., 0.45));
      segmentGateAMueLookup.add(new KeyValueEntry(6., 0.69));
      segmentGateAMueLookup.add(new KeyValueEntry(7., 1.02));
      segmentGateAMueLookup.add(new KeyValueEntry(8., 1.45));
      segmentGateAMueLookup.add(new KeyValueEntry(9., 2.06));
      segmentGateAMueLookup.add(new KeyValueEntry(10., 2.7));
      segmentGateAMueLookup.add(new KeyValueEntry(11., 3.41));
      segmentGateAMueLookup.add(new KeyValueEntry(12., 4.15));
      segmentGateAMueLookup.add(new KeyValueEntry(13., 5.0));
      segmentGateAMueLookup.add(new KeyValueEntry(14., 5.88));
      segmentGateAMueLookup.add(new KeyValueEntry(15., 6.5));
      segmentGateAMueLookup.add(new KeyValueEntry(16., 6.96));
      segmentGateAMueLookup.add(new KeyValueEntry(17., 7.43));
      segmentGateAMueLookup.add(new KeyValueEntry(18., 7.6));
      segmentGateAMueLookup.add(new KeyValueEntry(19., 7.8));
      segmentGateAMueLookup.add(new KeyValueEntry(20., 8.0));
      segmentGateAMueLookup.add(new KeyValueEntry(21., 8.23));
      segmentGateAMueLookup.add(new KeyValueEntry(22., 8.36));
    }
    
  }  
  
  @XmlElementWrapper
  @XmlElement(name = "entry")
  public List<KeyValueEntry> getSegmentGateVelocityLookup() {
    return segmentGateVelocityLookup;
  }

  public void setSegmentGateVelocityLookup(List<KeyValueEntry> segmentGateVelocityLookup) {
    this.segmentGateVelocityLookup = segmentGateVelocityLookup;
  }
  
  public double getSegmentGateAngle(double time) {    
    return Utils.linearIntegrate(segmentGateVelocityLookup, time);
  }

  @Override
  public String toString() {
    return Messages.getString("fillingTypeSegmentGateVelocity"); //$NON-NLS-1$
  }

}
