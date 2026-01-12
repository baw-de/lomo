/*
 * Copyright (c) 2019-2026 Bundesanstalt für Wasserbau
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

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@XmlTransient
public abstract class AbstractCustomSourceFillingType extends FillingType {

  private List<CustomSource> sources = new ArrayList<>();

  @XmlElement(name = "source") //$NON-NLS-1$
  public List<CustomSource> getSources() {
    return sources;
  }

  public void setSources(List<CustomSource> sources) {
    this.sources = sources;
  }
  
  //***************************************************************************

  @Override
  public double[][] getSource(double time, double[] positions, double[] h,
      double[] v, Case data) {

    final double[][] source = new double[2][positions.length];

    for (int cell = 0; cell < positions.length; cell++) {

      final double cellMinPos;

      if (cell == 0) {
        cellMinPos = 0;
      } else {
        cellMinPos = 0.5 * (positions[cell - 1] + positions[cell]);
      }

      final double cellMaxPos;

      if (cell == positions.length - 1) {
        cellMaxPos = data.getChamberLength();
      } else {
        cellMaxPos = 0.5 * (positions[cell] + positions[cell + 1]);
      }

      for (CustomSource s : sources) {

        final double sourcePos = s.getPosition();
        final double sourceLength = s.getLengthOfInfluence();

        if (cellMinPos <= sourcePos - 0.5 * sourceLength
            && sourcePos - 0.5 * sourceLength < cellMaxPos) {

          source[0][cell] += (Math.min(cellMaxPos,
              sourcePos + 0.5 * sourceLength)
              - (sourcePos - 0.5 * sourceLength)) / sourceLength
              * s.getSource(time);
          source[1][cell] += source[0][cell] * s.getMomentumFactor();

        } else if (cellMinPos < sourcePos + 0.5 * sourceLength
            && sourcePos + 0.5 * sourceLength <= cellMaxPos) {

          source[0][cell] += (sourcePos + 0.5 * sourceLength
              - Math.max(cellMinPos, sourcePos - 0.5 * sourceLength))
              / sourceLength * s.getSource(time);

          source[1][cell] += source[0][cell] * s.getMomentumFactor();

        } else if (sourcePos - 0.5 * sourceLength <= cellMinPos
            && cellMaxPos <= sourcePos + 0.5 * sourceLength) {

          source[0][cell] += (cellMaxPos - cellMinPos) / sourceLength
              * s.getSource(time);
          source[1][cell] += source[0][cell] * s.getMomentumFactor();

        }

      }

    }

    return source;
  }

  @Override
  public double[] getEffectiveFlowArea(double time, double[] positions) {
    final double[] arr = new double[positions.length];
    Arrays.fill(arr, Double.NaN);
    return arr;
  }

  @Override
  public void init() {
    for (CustomSource s : sources) {
      s.init();
    }
    super.init();
  }
}
