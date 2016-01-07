package de.baw.lomo.core.data;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

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
