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

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement(name="fillingType")
@XmlSeeAlso({SluiceGateFillingType.class, SegmentGateFillingType.class, 
  SegmentGateVelocityFillingType.class, GenericGateFillingType.class, 
  CustomSourceFillingType.class, CustomSourceFromFileFillingType.class})
public abstract class FillingType {

  /**
   * Returns mass and momentum source at given time and positions.
   * 
   * @param time
   *          Current time in seconds
   * @param positions
   *          Positions in longitudinal direction
   * @param h
   *          Current water depths at positions
   * @param v
   *          Current flow velocity at positions
   * @param data
   *          Current Case object
   * @return first components are mass sources at positions, second components
   *         are momentum sources at positions
   */
  public abstract double[][] getSource(double time, double[] positions, double[] h, double[] v,
      Case data);
  
  /**
   * Returns the cross section area which is effectively used at each
   * computational node. The area is e.g. reduced if a jet is present.
   * 
   * @param time
   *          Current time in seconds
   * @param position
   *          Position in longitudinal direction.
   * @return Effective cross section in m². Returns NaN if not available.
   */
  public abstract double getEffectiveFlowSection(double time, double position);

}
