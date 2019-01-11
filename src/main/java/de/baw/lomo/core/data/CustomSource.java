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
