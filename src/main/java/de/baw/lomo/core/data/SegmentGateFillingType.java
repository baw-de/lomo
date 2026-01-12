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

import de.baw.lomo.utils.Utils;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="segmentGateFillingType")
public class SegmentGateFillingType extends AbstractSegmentGateFillingType {
  
  private List<KeyValueEntry> segmentGateAngleLookup = new ArrayList<>();

  public SegmentGateFillingType() {
    
    
    if (segmentGateAngleLookup.isEmpty()) {
      segmentGateAngleLookup.add(new KeyValueEntry(0., 0.));
      segmentGateAngleLookup.add(new KeyValueEntry(800., 20.));
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
  @XmlElement(name = "entry") //$NON-NLS-1$
  public List<KeyValueEntry> getSegmentGateAngleLookup() {
    return segmentGateAngleLookup;
  }

  public void setSegmentGateAngleLookup(List<KeyValueEntry> segmentGateAngleLookup) {
    this.segmentGateAngleLookup = segmentGateAngleLookup;
  }

  public double getSegmentGateAngle(double time) {
    return Utils.linearInterpolate(segmentGateAngleLookup, time);
  }

  @Override
  public void init() {
    try {
      Utils.validateMonotonicity(segmentGateAngleLookup);
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("segmentGateAngleLookup: " + e.getMessage());
    }
    super.init();
  }

  @Override
  public String toString() {
    return Messages.getString("fillingTypeSegmentGate");
  }

}
