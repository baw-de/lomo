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
package de.baw.lomo.core.data;

import de.baw.lomo.utils.Utils;
import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@XmlRootElement(name="BAWLomoCase")
public class Case {

  private final static String VERSION = "1.0";
  
  private String author = "BAW";
  
  private String description = "";

  private double chamberLength = 145.;

  private double chamberWidth = 12.5;

  private double upstreamWaterLevel = 14.;

  private double downstreamWaterLevel = 4.0;

  private List<FillingType> fillingTypes = new ArrayList<>();

  private List<KeyValueEntry> shipAreaLookup = new ArrayList<>();

  private double[] forceComputationBounds = new double[] {Double.NaN, Double.NaN};

  private double timeMax = 3600.;

  private double timeStepMax = Double.POSITIVE_INFINITY;

  private int numberOfNodes = 100;

  private double deltaWaterDepthStop = 0.1;

  private double cfl = 0.5;

  private double theta = 1.;

  private double upwind = 1.;

  public Case() {

    if (shipAreaLookup.isEmpty()) {
      shipAreaLookup.add(new KeyValueEntry(5, 0));
      shipAreaLookup.add(new KeyValueEntry(7, 32.06));
      shipAreaLookup.add(new KeyValueEntry(138, 32.06));
      shipAreaLookup.add(new KeyValueEntry(140, 0));
    }

    if (fillingTypes.isEmpty()) {
      fillingTypes.add(new SluiceGateFillingType());
    }
  }

  @XmlAttribute(required = true)
  public String getVersion() {
    return VERSION;
  }

  public void setVersion(String version) {
    if (!VERSION.equals(version)) {
      throw new IllegalArgumentException("Wrong case version: " + version
          + " Required version is: " + VERSION);
    }
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
  public List<FillingType> getFillingTypes() {
    return fillingTypes;
  }

  public void setFillingTypes(List<FillingType> fillingTypes) {
    this.fillingTypes = fillingTypes;
  }

  public void addFillingType(FillingType fillingType){

    Optional<FillingType> lastFtOfSameType = this.fillingTypes.stream().filter(ft -> ft.getClass().equals(fillingType.getClass())).reduce((first, second) -> second);

    if (lastFtOfSameType.isPresent()) {

      Pattern pattern = Pattern.compile("\\d+");

      Matcher matcher = pattern.matcher(lastFtOfSameType.get().getName());

      if (matcher.find()) {
        String num = matcher.group();
        int inc = Integer.parseInt(num) + 1;
        fillingType.setName(matcher.replaceFirst(String.valueOf(inc)));
      } else if(fillingType.getName().equals(lastFtOfSameType.get().getName())) {
        fillingType.setName(String.format("%s 1", lastFtOfSameType.get().getName())); //$NON-NLS-1$
      }

    }

    this.fillingTypes.add(fillingType);
  }

  public void removeFillingType(FillingType fillingType) {
    this.fillingTypes.remove(fillingType);
  }

  @XmlElementWrapper
  @XmlElement(name = "entry") //$NON-NLS-1$
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

  public double getTimeStepMax() { return timeStepMax; }

  public void setTimeStepMax(double timeStepMax) { this.timeStepMax = timeStepMax; }

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

  @XmlList
  public double[] getForceComputationBounds() {
    return forceComputationBounds;
  }

  public void setForceComputationBounds(double[] forceComputationBounds) {
    if (forceComputationBounds.length != 2) {
      throw new IllegalArgumentException("Force computation bounds must be two values. Using default values.");
    }

    this.forceComputationBounds = forceComputationBounds;
  }

  /**
   * This method is called once in the begining of a new simulation
   * to give the chance for one-time checks and computations.
   */
  public void init() {
    try {
      Utils.validateMonotonicity(shipAreaLookup);
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("shipAreaLookup: " + e.getMessage());
    }

    for (FillingType ft : fillingTypes) {
      ft.init();
    }
  }
}
