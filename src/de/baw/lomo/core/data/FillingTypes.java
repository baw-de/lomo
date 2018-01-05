package de.baw.lomo.core.data;

public final class FillingTypes {
  
  public final static FillingType[] LIST = new FillingType[] {

      new RectangularSluiceGateFillingType(), new GenericSluiceGateFillingType(), 
      new SegmentGateVelocityFillingType(), new SegmentGateFillingType(), 
      new PrescribedInflowFillingType() };

}
