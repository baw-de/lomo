package de.baw.lomo.core.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
public abstract class AbstractCustomSourceFillingType extends FillingType {

  private List<CustomSource> sources = new ArrayList<>();

  public AbstractCustomSourceFillingType() {

    if (sources.isEmpty()) {
      
      for (int i=1; i < 100; i++) {
      CustomSource c = new CustomSource();
      c.setPosition(i);
      c.setData(new double[] {0.0, 500.0}, new double[] {1.0, 2.0});
      
      sources.add(c);      
      }
    }

  }

  @XmlElement(name = "source")
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

          source[0][cell] += ((sourcePos + 0.5 * sourceLength)
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
  public double getEffectiveFlowSection(double time, double position) {
    return Double.NaN;
  }

}
