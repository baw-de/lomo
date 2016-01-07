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

}
