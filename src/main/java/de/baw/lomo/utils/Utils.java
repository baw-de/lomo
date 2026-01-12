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
package de.baw.lomo.utils;

import de.baw.lomo.core.data.KeyValueEntry;

import java.util.List;

public final class Utils {

  private Utils() {
    throw new AssertionError();
  }

  public static double linearInterpolate(List<KeyValueEntry> source, double x) {
    final int tbSize = source.size();

    if (tbSize == 0) {
      throw new IllegalArgumentException(Messages.getString("errorEmptySourceList")); //$NON-NLS-1$
    }

    if (tbSize == 1) {
      return source.get(0).getValue();
    }

    // Cache first and last entries
    final KeyValueEntry first = source.get(0);
    final KeyValueEntry last = source.get(tbSize - 1);

    if (x <= first.getKey()) {
      return first.getValue();
    }

    if (x >= last.getKey()) {
      return last.getValue();
    }

    // Binary search for the interpolation range
    int left = 0;
    int right = tbSize - 1;

    while (left < right - 1) {
      int mid = left + (right - left) / 2;
      double midKey = source.get(mid).getKey();

      if (x < midKey) {
        right = mid;
      } else {
        left = mid;
      }
    }

    // Perform interpolation
    final KeyValueEntry lower = source.get(left);
    final KeyValueEntry upper = source.get(left + 1);

    final double xi = lower.getKey();
    final double xii = upper.getKey();
    final double yi = lower.getValue();
    final double yii = upper.getValue();

    return yi + (yii - yi) / (xii - xi) * (x - xi);
  }

  public static double linearIntegrate(List<KeyValueEntry> source, double x) {
    final int tbSize = source.size();

    if (tbSize == 0) {
      throw new IllegalArgumentException(Messages.getString("errorEmptySourceList")); //$NON-NLS-1$
    }

    if (tbSize == 1) {
      return x * source.get(0).getValue();      
    }

    // Cache first entry
    final KeyValueEntry first = source.get(0);

    if (x <= first.getKey()) {
      return 0.0;
    }

    double integral = 0.0;
    KeyValueEntry prev = first;

    for (int i = 1; i < tbSize; i++) {
      final KeyValueEntry curr = source.get(i);
      final double currKey = curr.getKey();

      if (currKey <= x) {
        // Complete segment: trapezoidal rule
        final double width = currKey - prev.getKey();
        final double avgHeight = 0.5 * (curr.getValue() + prev.getValue());
        integral += width * avgHeight;

        prev = curr;
      } else {
        // Partial segment: x falls between prev and curr
        final double width = x - prev.getKey();

        // Linear interpolation for y-value at x
        final double slope = (curr.getValue() - prev.getValue()) / (currKey - prev.getKey());
        final double yAtX = prev.getValue() + slope * width;

        // Trapezoidal rule for partial segment
        final double avgHeight = 0.5 * (prev.getValue() + yAtX);
        integral += width * avgHeight;

        break;
      }
    }

    // Handle case where x is beyond all points
    if (x > source.get(tbSize - 1).getKey()) {
      final KeyValueEntry last = source.get(tbSize - 1);
      final double extraWidth = x - last.getKey();
      integral += extraWidth * last.getValue();
    }

    return integral;
  }

  public static void validateMonotonicity(List<KeyValueEntry> source) throws IllegalArgumentException {
    if (source.size() < 2) {
      return; // Single point or empty is trivially monotonic
    }

    for (int i = 0; i < source.size() - 1; i++) {
      if (source.get(i + 1).getKey() <= source.get(i).getKey()) {
        throw new IllegalArgumentException(String.format(
                Messages.getString("errorNotMonotonic"), //$NON-NLS-1$
                i,source.get(i).getKey(),source.get(i + 1).getKey())
        );
      }
    }
  }
}
