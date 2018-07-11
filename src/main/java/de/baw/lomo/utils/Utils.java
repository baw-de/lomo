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
package de.baw.lomo.utils;

import java.util.List;

import de.baw.lomo.core.data.KeyValueEntry;

public class Utils {

  private Utils() {
    throw new AssertionError();
  }

  public static double linearInterpolate(List<KeyValueEntry> source, double x) {

    final int tbSize = source.size();

    if (x <= source.get(0).getKey()) {
      return source.get(0).getValue();
    }

    if (x >= source.get(tbSize - 1).getKey()) {
      return source.get(tbSize - 1).getValue();
    }

    for (int i = 0; i < tbSize; i++) {
      
      if (source.get(i + 1).getKey() < source.get(i).getKey()) {
        throw new IllegalArgumentException("x values are not monotonic.");
      }

      if ((source.get(i).getKey() <= x) && (x <= source.get(i + 1).getKey())) {

        final double yi = source.get(i).getValue();
        final double yii = source.get(i + 1).getValue();

        final double xi = source.get(i).getKey();
        final double xii = source.get(i + 1).getKey();

        return yi + (yii - yi) / (xii - xi) * (x - xi);
      }
    }
    return Double.NaN;
  }
  
  public static double linearIntegrate(List<KeyValueEntry> source, double x) {

    double integral = 0.0;

    if (x <= source.get(0).getKey()) {
      return integral;
    }

    final int tbSize = source.size();
    
    if (tbSize == 1) {
      return x * source.get(0).getValue();      
    }

    for (int i = 1; i < tbSize; i++) {

      if (source.get(i).getKey() <= x) {

        integral += 0.5
            * (source.get(i).getValue() + source.get(i - 1).getValue())
            * (source.get(i).getKey() - source.get(i - 1).getKey());

      } else {

        integral += (0.5
            * (source.get(i).getValue() - source.get(i - 1).getValue())
            / (source.get(i).getKey() - source.get(i - 1).getKey())
            * (x - source.get(i - 1).getKey()) + source.get(i - 1).getValue())
            * (x - source.get(i - 1).getKey());
        break;

      }
    }

    return integral;
  }
 
}
