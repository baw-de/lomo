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

@XmlRootElement(name="segmentGateVelocityFillingType")
public class SegmentGateVelocityFillingType extends AbstractSegmentGateFillingType {
  
  private List<KeyValueEntry> segmentGateVelocityLookup = new ArrayList<>();

  public SegmentGateVelocityFillingType() {
    
    
    if (segmentGateVelocityLookup.isEmpty()) {
      segmentGateVelocityLookup.add(new KeyValueEntry(0., 0.025));
      segmentGateVelocityLookup.add(new KeyValueEntry(800., 0.025));
    }
    
    if (segmentGateAMueLookup.isEmpty()) {
      segmentGateAMueLookup.add(new KeyValueEntry(0., 0.));
      segmentGateAMueLookup.add(new KeyValueEntry(1., 0.));
      segmentGateAMueLookup.add(new KeyValueEntry(2., 0.08));
      segmentGateAMueLookup.add(new KeyValueEntry(3., 0.2));
      segmentGateAMueLookup.add(new KeyValueEntry(4., 0.4));
      segmentGateAMueLookup.add(new KeyValueEntry(5., 0.7));
      segmentGateAMueLookup.add(new KeyValueEntry(6., 1.));
      segmentGateAMueLookup.add(new KeyValueEntry(7., 1.5));
      segmentGateAMueLookup.add(new KeyValueEntry(8., 2.1));
      segmentGateAMueLookup.add(new KeyValueEntry(9., 2.7));
      segmentGateAMueLookup.add(new KeyValueEntry(10., 3.4));
      segmentGateAMueLookup.add(new KeyValueEntry(11., 4.));
      segmentGateAMueLookup.add(new KeyValueEntry(12., 4.5));
      segmentGateAMueLookup.add(new KeyValueEntry(13., 4.9));
      segmentGateAMueLookup.add(new KeyValueEntry(14., 5.2));
      segmentGateAMueLookup.add(new KeyValueEntry(15., 5.4));
      segmentGateAMueLookup.add(new KeyValueEntry(16., 5.5));
      segmentGateAMueLookup.add(new KeyValueEntry(17., 5.6));
      segmentGateAMueLookup.add(new KeyValueEntry(18., 5.5));
      segmentGateAMueLookup.add(new KeyValueEntry(19., 5.5));
      segmentGateAMueLookup.add(new KeyValueEntry(20., 5.4));
    }
    
  }  
  
  public List<KeyValueEntry> getSegmentGateVelocityLookup() {
    return segmentGateVelocityLookup;
  }

  @XmlElementWrapper
  @XmlElement(name = "entry")
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
