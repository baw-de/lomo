package de.baw.lomo.core.data;

public abstract class GateFillingType extends FillingType {
  
  private double jetCoefficient = 0.1;
  
  private double jetExponent = 0.5;
  
  public double getJetCoefficient() {
    return jetCoefficient;
  }

  public void setJetCoefficient(double jetCoeffcient) {
    this.jetCoefficient = jetCoeffcient;
  }

  public double getJetExponent() {
    return jetExponent;
  }

  public void setJetExponent(double jetExponent) {
    this.jetExponent = jetExponent;
  }

  public abstract double getSubmergenceStart();

  public abstract void setSubmergenceStart(double submergenceStart);

  public abstract double getCulvertCrossSection();

  public abstract void setCulvertCrossSection(double culvertCrossSection);

  public abstract double getCulvertLoss();

  public abstract void setCulvertLoss(double culvertLoss);

  public abstract double getAreaTimesLoss(double time);

  public abstract double getGateOpening(double time);
  
  // ***************************************************************************

  private static final double GRAVITY = 9.81;  
  private final double[] nullInflow = new double[] {0.0,0.0};
  
  public double getJetCrossSection(double position, double time) {

    final double aKanal = getCulvertCrossSection();
    final double jetCoefficient = getJetCoefficient();
    final double jetExponent = getJetExponent();

    return aKanal + Math.pow(aKanal, jetExponent) * position * jetCoefficient;
  }

  @Override
  public double[] getSource(double position, double time, double h, double v,
      Case data) {    
    
    if (position == 0) {

      final double aMue = getAreaTimesLoss(time);
      final double ow = data.getUpstreamWaterDepth();
      final double maxDh = getSubmergenceStart();
      final double zetaKanal = getCulvertLoss();
      final double aKanal = getCulvertCrossSection();
  
      // Effektive Fallhöhe: Entweder OW bis Schütz oder OW bis UW
      final double dh = Math.min(ow - h, maxDh);
  
      double inflow = aMue * Math.sqrt(2. * GRAVITY * Math.abs(dh)
          / (1. + zetaKanal / aKanal / aKanal * aMue * aMue));
  
      // TODO: Warum eigentlich?
      // Negative Qs abfangen
      if (dh < 0.) {
        inflow = 0.;
      }
      
      return new double[] { inflow, inflow * 1. };
    }    
    
    return nullInflow;
  }
  
}
