package de.baw.lomo.core.data;

public abstract class GateFillingType extends FillingType {

  public abstract double getGateOpening(double time);

  public abstract double getJetCrossSection(double position, double time);

}