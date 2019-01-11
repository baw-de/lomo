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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import de.baw.lomo.utils.Utils;

@XmlRootElement(name="BAWLomoCase")
public class Case {

  private final static String VERSION = "0.9";
  
  private String author = "BAW";
  
  private String description = "";

  private double chamberLength = 145.;

  private double chamberWidth = 12.5;

  private double upstreamWaterLevel = 14.;

  private double downstreamWaterLevel = 4.0;

  private FillingType fillingType = new SluiceGateFillingType();

  private List<KeyValueEntry> shipAreaLookup = new ArrayList<>();

  private double timeMax = 1000.;

  private int numberOfNodes = 100;

  private double deltaWaterDepthStop = 0.1;

  private double cfl = 0.5;

  private double theta = 1.;

  private double upwind = 1.;

  public Case() {

    if (shipAreaLookup.isEmpty()) {
      shipAreaLookup.add(new KeyValueEntry(5, 0));
      shipAreaLookup.add(new KeyValueEntry(7, 32.06));
      shipAreaLookup.add(new KeyValueEntry(133, 32.06));
      shipAreaLookup.add(new KeyValueEntry(135, 0));
    }
  }

  @XmlAttribute(required = true)
  public String getVersion() {
    return VERSION;
  }

  public void setVersion(String version) {
    if (!version.equals(VERSION)) {
      throw new IllegalArgumentException("Wrong case version: " + version
          + " Required version is: " + VERSION);
    }
  }

  public double getChamberLength() {
    return chamberLength;
  }

  public void setChamberLength(double chamberLength) {
    this.chamberLength = chamberLength;
  }

  public double getChamberWidth() {
    return chamberWidth;
  }

  public void setChamberWidth(double chamberWidth) {
    this.chamberWidth = chamberWidth;
  }

  public double getUpstreamWaterLevel() {
    return upstreamWaterLevel;
  }

  public void setUpstreamWaterLevel(double upstreamWaterLevel) {
    this.upstreamWaterLevel = upstreamWaterLevel;
  }

  public double getDownstreamWaterLevel() {
    return downstreamWaterLevel;
  }

  public void setDownstreamWaterLevel(double downstreamWaterLevel) {
    this.downstreamWaterLevel = downstreamWaterLevel;
  }

  @XmlElementRef
  public FillingType getFillingType() {
    return fillingType;
  }

  public void setFillingType(FillingType fillingType) {
    this.fillingType = fillingType;
  }

  @XmlElementWrapper
  @XmlElement(name = "entry")
  public List<KeyValueEntry> getShipAreaLookup() {
    return shipAreaLookup;
  }

  public void setShipAreaLookup(List<KeyValueEntry> shipAreaLookup) {
    this.shipAreaLookup = shipAreaLookup;
  }

  public double getTimeMax() {
    return timeMax;
  }

  public void setTimeMax(double timeMax) {
    this.timeMax = timeMax;
  }

  public int getNumberOfNodes() {
    return numberOfNodes;
  }

  public void setNumberOfNodes(int numberOfNodes) {
    this.numberOfNodes = numberOfNodes;
  }

  public double getDeltaWaterDepthStop() {
    return deltaWaterDepthStop;
  }

  public void setDeltaWaterDepthStop(double deltaWaterDepthStop) {
    this.deltaWaterDepthStop = deltaWaterDepthStop;
  }

  public double getCfl() {
    return cfl;
  }

  public void setCfl(double cfl) {
    this.cfl = cfl;
  }

  public double getTheta() {
    return theta;
  }

  public void setTheta(double theta) {
    this.theta = theta;
  }

  public double getUpwind() {
    return upwind;
  }

  public void setUpwind(double upwind) {
    this.upwind = upwind;
  }

  public double getShipArea(double x) {
    return Utils.linearInterpolate(shipAreaLookup, x);
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

}
