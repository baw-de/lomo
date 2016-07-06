package de.baw.lomo.core.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "prescribedInflowFillingType")
public class PrescribedInflowFillingType extends FillingType {

  private String file = "";

  private List<KeyValueEntry> positionLookup = new ArrayList<>();

  private List<KeyValueEntry> lengthOfInfluenceLookup = new ArrayList<>();
  
  private List<KeyValueEntry> momentumFactorLookup = new ArrayList<>();

  public String getFile() {
    return file;
  }

  public void setFile(String file) {
    this.file = file;    
    readFile();
  }

  @XmlElementWrapper
  @XmlElement(name = "entry")
  public List<KeyValueEntry> getPositionLookup() {
    return positionLookup;
  }

  public void setPositionLookup(List<KeyValueEntry> positionLookup) {
    this.positionLookup = positionLookup;
  }

  @XmlElementWrapper
  @XmlElement(name = "entry")
  public List<KeyValueEntry> getLengthOfInfluenceLookup() {
    return lengthOfInfluenceLookup;
  }

  public void setLengthOfInfluenceLookup(
      List<KeyValueEntry> lengthOfInfluenceLookup) {
    this.lengthOfInfluenceLookup = lengthOfInfluenceLookup;
  }
  
  @XmlElementWrapper
  @XmlElement(name = "entry")
  public List<KeyValueEntry> getMomentumFactorLookup() {
    return momentumFactorLookup;
  }

  public void setMomentumFactorLookup(List<KeyValueEntry> momentumFactorLookup) {
    this.momentumFactorLookup = momentumFactorLookup;
  }
  
  //***************************************************************************

  private final List<Double> timeList = new ArrayList<>();
  private final List<double[]> valuesList = new ArrayList<>();

  @Override
  public double[][] getSource(double time, double[] positions, double[] h,
      double[] v, Case data) {

    // error handling
    if (positionLookup.size() == 0 || lengthOfInfluenceLookup.size() == 0
        || valuesList.size() == 0 
        || (positionLookup.size() != lengthOfInfluenceLookup.size())
        || positionLookup.size() != valuesList.get(0).length) {
      throw new IllegalArgumentException(
          "Not enough data is given to compute source.");
    }

    return computeSource(time, positions, h, v, data);
  }

  private void readFile() {
    if (file.isEmpty()) {
      throw new IllegalArgumentException("No file given!");
    }
  
    final File f = new File(file);
  
    if (!f.isFile()) {
      throw new IllegalArgumentException("File does not exist: " + f);
    }
  
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(
        new FileInputStream(f),StandardCharsets.UTF_8))) {
  
      final NumberFormat format = NumberFormat.getNumberInstance(Locale.GERMAN);
  
      timeList.clear();
      valuesList.clear();
  
      String line;
  
      while ((line = reader.readLine()) != null) {
  
        final String[] lineData = line.replaceAll("[\uFEFF-\uFFFF]", "")
            .split("\\s+");
  
        if (lineData.length == 0) {
          break;
        }
  
        timeList.add(format.parse(lineData[0]).doubleValue());
  
        final double[] values = new double[(int) (lineData.length * 0.5)];
  
        for (int i = 0, j = 1; i < values.length; i++, j = j + 2) {
          values[i] = format.parse(lineData[j]).doubleValue();
  
        }
  
        valuesList.add(values);
      }
      
      if (positionLookup.size() == 0 && lengthOfInfluenceLookup.size() == 0 &&
          momentumFactorLookup.size() == 0) {
        
        for (int i = 0; i < valuesList.get(0).length; i++) {
          positionLookup.add(new KeyValueEntry(i, 0.0));
          lengthOfInfluenceLookup.add(new KeyValueEntry(i, 0.0));
          momentumFactorLookup.add(new KeyValueEntry(i, 0.0));
        }
        
      }
  
    } catch (final IOException e) {
      e.printStackTrace();
    } catch (final ParseException e) {
      e.printStackTrace();
    }
  }

  private double[][] computeSource(double time, double[] positions, double[] h,
      double[] v, Case data) {
    
    final double[][] source = new double[2][positions.length];

    double[] values = new double[positionLookup.size()];

    if (time <= timeList.get(0)) {
      values = valuesList.get(0);

    } else if (time >= timeList.get(timeList.size() - 1)) {
      values = valuesList.get(timeList.size() - 1);

    } else {

      for (int i = 0; i < timeList.size(); i++) {

        if (i + 1 == 801) {
          System.out.println("HEre");
        }

        if (timeList.get(i + 1) < timeList.get(i)) {
          throw new IllegalArgumentException("x values are not monotonic.");
        }

        if ((timeList.get(i) <= time) && (time <= timeList.get(i + 1))) {

          final double[] yi = valuesList.get(i);
          final double[] yii = valuesList.get(i + 1);

          final double xi = timeList.get(i);
          final double xii = timeList.get(i + 1);

          for (int j = 0; j < values.length; j++) {

            values[j] = yi[j] + (yii[j] - yi[j]) / (xii - xi) * (time - xi);
          }

          break;
        }
      }
    }

    for (int f = 0; f < values.length; f++) {

      final List<Integer> influencedNodes = new ArrayList<>();

      for (int i = 0; i < positions.length; i++) {

        final double x = positionLookup.get(f).getValue();
        final double l = lengthOfInfluenceLookup.get(f).getValue();

        if (x - 0.5 * l < positions[i] && positions[i] <= x + 0.5 * l) {

          influencedNodes.add(i);
        }
      }
      
      for (final int node : influencedNodes) {
        source[0][node] = values[f] / influencedNodes.size();
        source[1][node] = source[0][node] * momentumFactorLookup.get(f).getValue();
      }
    }
    return source;
  }

  @Override
  public String toString() {
    return Messages.getString("fillingTypePrescribedInflow"); //$NON-NLS-1$
  }

}
