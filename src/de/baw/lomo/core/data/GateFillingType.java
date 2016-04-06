package de.baw.lomo.core.data;

public abstract class GateFillingType extends FillingType {
  
  public abstract double getAreaTimesLoss(double time);

  public abstract double getSubmergenceStart();  
  public abstract void setSubmergenceStart(double submergenceStart);

  public abstract double getCulvertCrossSection();
  public abstract void setCulvertCrossSection(double culvertCrossSection);

  public abstract double getCulvertLoss();
  public abstract void setCulvertLoss(double culvertLoss);

}
