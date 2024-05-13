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
package de.baw.lomo.core;

import de.baw.lomo.core.data.AbstractGateFillingType;
import de.baw.lomo.core.data.Case;
import de.baw.lomo.core.data.FillingType;
import de.baw.lomo.core.data.Results;

import java.util.Arrays;
import java.util.List;

public class OneDimensionalModel implements Model {
  
  public boolean isVerbose = true;
  
  private static final double GRAVITY = 9.81;
  private Case data;
  /** Für Laufzeitmessung **/
  private long runtime;
  private double[] timeSeries;

  /** Zeitschritt dt **/
  private double dt;
  /** Max. Zeitschritte **/
  private int maxStep;
  /** Zeitwichtung theta **/
  private double theta;
  /** Upwind-Faktor **/
  private double upwind;
  /** Ortsschritte nx. Q hat einen Wert mehr! **/
  private int nx;
  /** Oberwasserstand **/
  private double ow;
  /** Unterwasserstand **/
  private double uw;
  /** Kammerlaenge **/
  private double kL;
  /** Kammerbreite **/
  private double kB;
  /** Füllorgan **/
  private List<FillingType> fillingTypes;
  /** Aktuelle Zeit **/
  private double time;
  /** Aktueller Zeitschritt **/
  private int step;
  /** Ortsschrittweite **/
  private double dx;
  /** Bisheriges Fuellvolumen **/
  private double chamberVol;
  /** Beta-Beiwerte **/
  private double[] beta;
  /** Ganz alte Zeitebene **/
  private double[] Q00, A00;
  /** Alte Zeitebene **/
  private double[] Q0, A0;
  /** Zeitebene zwischen alt und neu **/
  private double[] Q05, A05;
  /** Neue Zeitebene **/
  private double[] Q1, A1;
  /** Postprozessing **/
  private double[][] v1overT, h1overT;
  private double[] v1, h1;
  /** Schiffsquerschnitte **/
  private double[] aShipNode, aShipCell;
  /** Durch Schiff verdrängtes Volumen **/
  private double shipVol;
  /** Aktuelle Schuetzoeffnungshoehe **/
  private double[][] valveOpening;
  /** Zeitabhaengige Ergebnisse protokollieren **/
  private double[] inflow, h1Mean, I, longitudinalForce;
  /** Strickler Wert **/
  private final double kSt = 100; // Fest verdrahtet
  
  private double[] positions;

  private void init() {

    // *************************************************************************
    // Daten aus Case lesen
    // *************************************************************************

    theta = data.getTheta();
    upwind = data.getUpwind();
    nx = data.getNumberOfNodes();
    ow = data.getUpstreamWaterLevel();
    uw = data.getDownstreamWaterLevel();

    kL = data.getChamberLength();
    kB = data.getChamberWidth();

    fillingTypes = data.getFillingTypes();

    // *************************************************************************
    // Räumliche Diskretisierung
    // *************************************************************************

    beta = new double[nx];
    positions = new double[nx];

    Q00 = new double[nx + 1];
    A00 = new double[nx];

    Q0 = new double[nx + 1];
    A0 = new double[nx];

    Q05 = new double[nx + 1];
    A05 = new double[nx];

    Q1 = new double[nx + 1];
    A1 = new double[nx];

    aShipNode = new double[nx + 1];
    aShipCell = new double[nx];

    // Ortsschrittweite aus Kammerlänge und Knotenanzahl:
    dx = kL / nx;

    // *************************************************************************
    // Anfangsbedingungen
    // *************************************************************************

    step = 0;
    time = 0.;
    chamberVol = 0.;
    shipVol = 0;

    // Zeitschrittweite aus Wellengeschwindigkeit und CFL:
    dt = dx / Math.sqrt(GRAVITY * Math.max(ow,uw)) * data.getCfl();

    // Maximale Anzahl Zeitschritte:
    maxStep = (int) (data.getTimeMax() / dt);

    // Aktuelle Schuetzoeffnungshoehe
    valveOpening = new double[maxStep + 1][(int) data.getFillingTypes().stream().filter(ft -> ft instanceof AbstractGateFillingType).count()];

    // Zeitabhaengige Ergebnisse protokollieren
    inflow = new double[maxStep + 1];
    h1Mean = new double[maxStep + 1];
    I = new double[maxStep + 1];
    longitudinalForce = new double[maxStep + 1];

    timeSeries = new double[maxStep + 1];

    v1overT = new double[maxStep+1][nx + 1];
    h1overT = new double[maxStep+1][nx];

    // Anfangsbedingungen der Felder setzen
    v1 = v1overT[0];
    h1 = h1overT[0];
    h1Mean[0] = uw;

    // Zellwerte
    for (int i = 0; i < nx; i++) {
      aShipCell[i] = data.getShipArea(dx * (i + 0.5));

      A00[i] = uw * kB - aShipCell[i];
      A0[i] = A00[i];
      A05[i] = 0.;
      A1[i] = A0[i];

      beta[i] = 1.;
      h1[i] = uw;
      positions[i] = dx * (i + 0.5);
    }

    final double[] forceComputationBounds = data.getForceComputationBounds().clone();

    if (Double.isNaN(forceComputationBounds[0])) {
      forceComputationBounds[0] = 0.0;
    }

    if (Double.isNaN(forceComputationBounds[1])) {
      forceComputationBounds[1] = kL;
    }

    // Knotenwerte
    for (int i = 0; i < nx + 1; i++) {

      final double pos = dx * i;
      if (forceComputationBounds[0] <= pos && pos <= forceComputationBounds[1]) {
        aShipNode[i] = data.getShipArea(pos);
        shipVol += aShipNode[i] * dx;
      }

      Q00[i] = 0.;
      Q0[i] = 0.;
      Q05[i] = 0.;
      Q1[i] = 0.;

      v1[i] = 0;
    }

  }

  private void compute() {

    // Laufzeitmessung
    runtime = System.nanoTime();

    // Ende der Füllung: min_dh
    final double ow_uw = data.getDeltaWaterDepthStop();    

    do {

      // Zeitzaehler erhoehen
      time = time + dt;
      step = step + 1;

      timeSeries[step] = time;

      final double[] volumeSource = new double[positions.length];
      final double[] momentumSource = new double[positions.length];

      int ftIdx = 0;

      for (FillingType ft : fillingTypes) {

        if (ft instanceof AbstractGateFillingType) {
          valveOpening[step][ftIdx] = ((AbstractGateFillingType) ft).getGateOpening(time);
          ftIdx++;
        }

        final double[][] source = ft.getSource(time, positions, h1, v1, data);

        Arrays.setAll(volumeSource, i -> volumeSource[i] + source[0][i]);
        Arrays.setAll(momentumSource, i -> momentumSource[i] + source[1][i]);
      }
      
      // Alte Zeitebene wird ganz alte Zeitebene, neue Zeitebene wird alte
      // Zeitebene:
      for (int i = 0; i < nx; i++) {
        A00[i] = A0[i];
        A0[i] = A1[i];
        // Schaetzung der Werte fuer A05 mit Adams-Bashforth:
        A05[i] = 1.5 * A0[i] - 0.5 * A00[i];
        inflow[step] += volumeSource[i];
      }
      for (int i = 0; i < nx + 1; i++) {
        Q00[i] = Q0[i];
        Q0[i] = Q1[i];
        // Schaetzung der Werte fuer Q05 mit Adams-Bashforth
        Q05[i] = 1.5 * Q0[i] - 0.5 * Q00[i];
      }

      // Gemischte Zeitdiskretisieerung:
      // A mit Adams-Bashforth-Diskretisierung
      // Q mit Adams-Bashforth-Diskretisierung fuer Q und mit
      // Crank-Nicolson fuer A mit implizitem A fuer dh/dx-Term

      // Strahlausbreitung
      final double[] effectiveFlowArea = fillingTypes.get(0).getEffectiveFlowArea(time, positions);

      for (int i = 1; i < fillingTypes.size(); i++) {

        final double[] ftEffectiveFlowArea = fillingTypes.get(i).getEffectiveFlowArea(time, positions);

        for (int j = 0; j < positions.length; j++) {

          if (!Double.isNaN(ftEffectiveFlowArea[j])) {

            if (Double.isNaN(effectiveFlowArea[j])) {
              effectiveFlowArea[j] = ftEffectiveFlowArea[j];
            } else {
              effectiveFlowArea[j] = Math.min(effectiveFlowArea[j], ftEffectiveFlowArea[j]);
            }
          }
        }
      }

      // Schleife fuer Predictor-Corrector
      for (int corrStep = 0; corrStep <= 2; corrStep++) {

        // Strahlbeiwert beta anpassen beta=integral(U*U)dA / (U_mean*Q)
        // = integral(U*U)dA / (integral(U)dA*integral(U)dA / A)
        for (int i = 0; i < nx; i++) {

          beta[i] = 1.;

          if (Double.isNaN(effectiveFlowArea[i])) {
            continue;
          }

          // aEffective cannot be larger than actual wet cross section
          final double aEffective = Math.min(A05[i], effectiveFlowArea[i]);

          // avoid division by zero
          if (aEffective > 1.e-3) {
            beta[i] = A05[i] / aEffective;
          }
        }

        // AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
        // A und Q sind "staggered", darum A nur bis nx-1! A[0] liegt
        // zwischen Q[0] und Q[1].
        for (int i = 1; i < nx - 1; i++) {
          A1[i] = A0[i] - dt * (Q05[i + 1] - Q05[i]) / dx;
        }

        // A berechnen fuer Rand-Volumen
        A1[0] = A0[0] - dt * Q05[1] / dx;
        A1[nx - 1] = A0[nx - 1] + dt * Q05[nx - 1] / dx;

        // Quellterme fuer jedes Volumen
        for (int i = 0; i < nx; i++) {
          A1[i] += dt * volumeSource[i] / dx;           
        }

        // Neues A05 mit variabler Zeitwichtung ermitteln
        for (int i = 0; i < nx; i++) {
          A05[i] = (1. - theta) * A0[i] + theta * A1[i];
        }
        // AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA

        // QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ
        // Q berechnen fuer Knoten im Feld
        for (int i = 1; i < nx; i++) {

          // Wert fuer Q links von Q[i]:
          final double QM = 0.5 * (Q05[i - 1] + Q05[i]);

          // Wert fuer Q rechts von Q[i]
          final double QP = 0.5 * (Q05[i] + Q05[i + 1]);
          final double vM = QM / A05[i - 1]; // Wert fuer v links von Q[i]
          final double vP = QP / A05[i]; // Wert fuer v rechts von Q[i]
          final double rhy = 0.5 * (A05[i] + A05[i - 1]) / (3. * kB);

          // Wasserspiegelgefaelle mit Schiff:
          final double gradH = ((A1[i] + aShipCell[i]) / kB
              - (A1[i - 1] + aShipCell[i - 1]) / kB) / dx;
          // Wasserspiegelgefaelle ohne Schiff:
          // double gradH=( A1[i]/KB -A1[i-1]/KB)/dx;

          Q1[i] = Q0[i]
              - dt * (1. - upwind) * (beta[i] * QP * vP - beta[i - 1] * QM * vM)
                  / dx
              - dt * GRAVITY * 0.5 * (A05[i] + A05[i - 1]) * gradH
              - dt * GRAVITY * 0.5 * (A05[i] + A05[i - 1])
                  * (0.5 * Math.abs(vM + vP)) * (0.5 * (vM + vP)) / kSt / kSt
                  * Math.pow(rhy, -4. / 3.)
              - dt * (vM + vP) * 0.0; // Kuenstliche Daempfung
          // - dt*Q0[i]*0.002; // Kuenstliche Daempfung

          // Upwind
          if (Q0[i] >= 0.) {
            Q1[i] += -dt * upwind
                * (beta[i] * Q05[i] * vP - beta[i - 1] * Q05[i - 1] * vM) / dx;
          } else {
            Q1[i] += -dt * upwind
                * (beta[i] * Q05[i + 1] * vP - beta[i - 1] * Q05[i] * vM) / dx;
          }

          Q1[i] += momentumSource[i];
        }
        // Q fuer RB-Knoten
        Q1[0] = momentumSource[0];        
        Q1[nx] = 0.0; // Impuls am letzten Knoten ist immer Null       
        
        // QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ

        // Neues Q05 mit variabler Zeitwichtung ermitteln:
        for (int i = 0; i < nx + 1; i++) {
          Q05[i] = (1. - theta) * Q0[i] + theta * Q1[i];
        }
      }

      // ***********************************************************************
      // Postprocessing:
      // ***********************************************************************

      h1 = h1overT[step];
      v1 = v1overT[step];
      
      h1Mean[step] = 0.;
      
      for (int i = 0; i < nx; i++) {
        // Übertragen auf h:
        h1[i] = (A1[i] + aShipCell[i]) / kB;
        // Mittleren Wasserstand ausrechnen:
        h1Mean[step] += h1[i] / nx;        
      }

      for (int i = 1; i < nx; i++) {
        // Übertragen auf v:
        v1[i] = Q1[i] / (0.5 * (A1[i - 1] + A1[i]));
      }

      v1[0] = Q1[0] / kB / h1[0];
      v1[nx] = Q1[nx] / kB / h1[nx - 1];

      // Gefaelle ausrechnen: Nur fuer Postprocessing!
      I[step] = (-h1[0] + h1[nx - 2]) / (kL - dx);

      // Volumenbilanz
      chamberVol += dt * inflow[step];

      // Kraft auf Schiff [kN]
      for (int i = 0; i < nx - 1; i++) {

        final double AA = aShipNode[i + 1];
        final double AB = aShipNode[i];

        // Über Flächendifferenzen*Druecke
        longitudinalForce[step] += -(h1[i] - h1[i + 1]) / dx // slope
                * 1000. * 0.5 * (AA + AB) * dx * GRAVITY; // weight force
      }
      
    } while (step < maxStep && Math.abs(h1Mean[step] - ow) > ow_uw);

    runtime = System.nanoTime() - runtime;
  }

  private Results postprocess() {
    
    if (isVerbose) {

      double Qmax = Double.MIN_VALUE, Qmin = Double.MAX_VALUE, 
          Imax = Double.MIN_VALUE, Imin = Double.MAX_VALUE,
          Fmax = Double.MIN_VALUE, Fmin = Double.MAX_VALUE,
          dQ_dt_min = Double.MAX_VALUE, dQ_dt_max = Double.MIN_VALUE;

      for (int i = 0; i < step; i++) {
        Qmax = Math.max(Qmax, inflow[i]);
        Qmin = Math.min(Qmin, inflow[i]);
        Imax = Math.max(Imax, I[i]);
        Imin = Math.min(Imin, I[i]);
        Fmax = Math.max(Fmax, longitudinalForce[i]);
        Fmin = Math.min(Fmin, longitudinalForce[i]);
        if (i > 0) {
          if ((inflow[i] - inflow[i - 1]) / dt < dQ_dt_min) {
            dQ_dt_min = (inflow[i] - inflow[i - 1]) / dt;
          }
          if ((inflow[i] - inflow[i - 1]) / dt > dQ_dt_max) {
            dQ_dt_max = (inflow[i] - inflow[i - 1]) / dt;
          }
        }
      }

      @SuppressWarnings("StringBufferReplaceableByString")
      final StringBuffer bf = new StringBuffer();

      bf.append("* * * * * * * * * * * * * * * * * * * * * * * * * * * *\n"); //$NON-NLS-1$

      bf.append(String.format(Messages.getString("resultTimeSteps") + " \n", //$NON-NLS-1$ //$NON-NLS-2$
          step));

      bf.append(String.format(Messages.getString("resultFillingTime") + " \n", //$NON-NLS-1$ //$NON-NLS-2$
          time));

      bf.append(String.format(Messages.getString("resultFillingVolume") + " \n", //$NON-NLS-1$ //$NON-NLS-2$
          chamberVol));

      bf.append(String.format(Messages.getString("resultMinMaxFlowRate") + " \n", //$NON-NLS-1$ //$NON-NLS-2$
          Qmin, Qmax));

      bf.append(String.format(
              Messages.getString("resultMinMaxFlowRateChange") + " \n", //$NON-NLS-1$ //$NON-NLS-2$
              dQ_dt_min, dQ_dt_max));

      bf.append(String.format(Messages.getString("resultMinMaxSlope") + " \n", //$NON-NLS-1$ //$NON-NLS-2$
              Imin * 1000., Imax * 1000.));

      bf.append(String.format(Messages.getString("resultShipVolume") + " \n", //$NON-NLS-1$ //$NON-NLS-2$
              shipVol));

      bf.append(String.format(
              Messages.getString("resultMinMaxLongitudinalForces") + " \n", //$NON-NLS-1$ //$NON-NLS-2$
              Fmin * 1.e-3, Fmax * 1.e-3));

      bf.append(String.format(
          Messages.getString("resultMinMaxLongitudinalForceToGravityForce") //$NON-NLS-1$
              + " \n", //$NON-NLS-1$
          // Ratios given in per mille, Fg=shipVol*1000*g:
          Fmin / (shipVol * GRAVITY),
          Fmax / (shipVol * GRAVITY)));

      bf.append("* * * * * * * * * * * * * * * * * * * * * * * * * * * *\n"); //$NON-NLS-1$

      bf.append(String.format(Messages.getString("resultTotalRuntime") + " \n", //$NON-NLS-1$ //$NON-NLS-2$
          runtime * 1.e-9));

      System.out.println(bf);

    }

    return new Results() {

      @Override
      public double[] getTimeline() {
        return Arrays.copyOf(timeSeries, step);
      }

      @Override
      public double[] getPositions() { return Arrays.copyOf(positions, positions.length); }

      @Override
      public double[] getSlopeOverTime() {
        return Arrays.copyOf(I, step);
      }

      @Override
      public double[] getDischargeOverTime() {
        return Arrays.copyOf(inflow, step);
      }

      @Override
      public double[] getMeanChamberWaterLevelOverTime() {
        return Arrays.copyOf(h1Mean, step);
      }

      @Override
      public double[] getLongitudinalForceOverTime() {
        return Arrays.copyOf(longitudinalForce, step);
      }

      @Override
      public double[][] getValveOpeningOverTime() { return Arrays.stream(valveOpening,0, step).map(double[]::clone).toArray(double[][]::new); }

      @Override
      public double[][] getChamberWaterLevelOverTime() { return Arrays.stream(h1overT,0, step).map(double[]::clone).toArray(double[][]::new); }

      @Override
      public double[][] getFlowVelocityOverTime() { return Arrays.stream(v1overT,0, step).map(double[]::clone).toArray(double[][]::new); }
    };
  }

  @Override
  public Results run() {

    // *************************************************************************
    // Initialisierung
    // *************************************************************************

    init();

    // *************************************************************************
    // Zeitschleife
    // *************************************************************************

    compute();

    // *************************************************************************
    // Postprozessing
    // *************************************************************************

    return postprocess();
  }

  @Override
  public void setCaseData(Case caseData) {
    this.data = caseData;
  }

}
