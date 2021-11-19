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

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlValue;

@XmlRootElement
public class KeyValueEntry {

  private double key;
  private double value;
  
  public KeyValueEntry() {
    this(0.0, 0.0);
  }

  public KeyValueEntry(double key, double value) {
    this.key = key;
    this.value = value;
  }

  @XmlAttribute
  public double getKey() {
    return key;
  }

  public void setKey(double key) {
    this.key = key;
  }

  @XmlValue
  public double getValue() {
    return value;    
  }

  public void setValue(double value) {
    this.value = value;
  }
}
