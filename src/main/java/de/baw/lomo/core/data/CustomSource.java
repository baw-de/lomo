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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import de.baw.lomo.utils.Utils;

@XmlRootElement(name = "source")
public class CustomSource {
  
  private double position = 0.0;
  
  private double lengthOfInfluence = 0.0;
  
  private double momentumFactor = 0.0;
  
  private List<KeyValueEntry> sourceLookup = new ArrayList<>();

  public double getPosition() {
    return position;
  }

  public void setPosition(double position) {
    this.position = position;
  }

  public double getLengthOfInfluence() {
    return lengthOfInfluence;
  }

  public void setLengthOfInfluence(double lengthOfInfluence) {
    this.lengthOfInfluence = lengthOfInfluence;
  }

  public double getMomentumFactor() {
    return momentumFactor;
  }

  public void setMomentumFactor(double momentumFactor) {
    this.momentumFactor = momentumFactor;
  }

  @XmlElementWrapper
  @XmlElement(name = "entry")
  public List<KeyValueEntry> getSourceLookup() {
    return sourceLookup;
  }

  public void setSourceLookup(List<KeyValueEntry> sourceLookup) {
    this.sourceLookup = sourceLookup;
  }
  
  public double getSource(double time) {
    return Utils.linearInterpolate(sourceLookup, time);
  }

  public void setData(double[] time, double[] source) {
    
    if (time.length != source.length) {
      throw new IllegalArgumentException(Messages.getString("customSourceArrayLengthError")); //$NON-NLS-1$
    }
    
    sourceLookup.clear();
    
    for (int i = 0; i < time.length; i++) {
      sourceLookup.add(new KeyValueEntry(time[i], source[i]));
    }
  }

}
