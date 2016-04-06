package de.baw.lomo.core;

import de.baw.lomo.core.data.Case;
import de.baw.lomo.core.data.GateFillingType;
import de.baw.lomo.core.data.Results;
import de.baw.lomo.core.data.SegmentGateFillingType;
import de.baw.lomo.core.data.SluiceGateFillingType;

public class OneDimensionalModel implements Model {
  private static final double GRAVITY = 9.81;
  private Case data;
  /** Für Laufzeitmessung **/
  private long startTime;
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
  /** Kanalflaeche: Austrittsflaeche fuer Strahl **/
  private double aKanal;
  /** Verlustbeiwert am Kanal **/
  private double zetaKanal;
  /** Strahlausbreitung **/
  private double jetExponent;
  /** Strahlausbreitung **/
  private double jetCoefficient;
  /** Beginn Rückstaueindluss **/
  private double maxDh;
  /** Füllorgan **/
  private GateFillingType filling;
  /** Aktuelle Zeit **/
  private double time;
  /** Aktueller Zeitschritt **/
  private int step;
  /** Ortsschrittweite **/
  private double dx;
  /** Bisheriges Fuellvolumen **/
  private double vol;
  /** Beta-Beiwerte **/
  private double[] beta;
  /** Zufluss **/
  private double[] zufluss;
  /** Ganz alte Zeitebene **/
  private double[] Q00, A00;
  /** Alte Zeitebene **/
  private double[] Q0, A0;
  /** Zeitebene zwischen alt und neu **/
  private double[] Q05, A05;
  /** Neue Zeitebene **/
  private double[] Q1, A1;
  /** Postprozessing **/
  private double[] v1, h1;
  /** Schiffsquerschnitte **/
  private double[] A_schiff_node, A_schiff_cell;
  /** Aktuelle Schuetzoeffnungshoehe **/
  private double[] valveOpening;
  /** Zeitabhaengige Ergebnisse protokollieren **/
  private double[] Q, hMean, I, F0, F1, F2;
  /** Strickler Wert **/
  private double kSt = 100; // Fest verdrahtet

  private void init() {

    // *************************************************************************
    // Daten aus Case lesen
    // *************************************************************************

    theta = data.getTheta();
    upwind = data.getUpwind();
    nx = data.getNumberOfNodes();
    ow = data.getUpstreamWaterDepth();
    uw = data.getDownstreamWaterDepth();

    kL = data.getChamberLength();
    kB = data.getChamberWidth();

    jetExponent = data.getJetExponent();
    jetCoefficient = data.getJetCoefficient();

    if (data.getFillingType() instanceof GateFillingType) {

      filling = (GateFillingType) data.getFillingType();

    } else {

      throw new IllegalArgumentException(
          "Unknown filling type: " + data.getFillingType());
    }

    aKanal = filling.getCulvertCrossSection();
    zetaKanal = filling.getCulvertLoss();
    maxDh = filling.getSubmergenceStart();

    // *************************************************************************
    // Räumliche Diskretisierung
    // *************************************************************************

    beta = new double[nx];
    zufluss = new double[nx];

    Q00 = new double[nx + 1];
    A00 = new double[nx];

    Q0 = new double[nx + 1];
    A0 = new double[nx];

    Q05 = new double[nx + 1];
    A05 = new double[nx];

    Q1 = new double[nx + 1];
    A1 = new double[nx];

    v1 = new double[nx + 1];
    h1 = new double[nx];

    A_schiff_node = new double[nx + 1];
    A_schiff_cell = new double[nx];

    // Ortsschrittweite aus Kammerlänge und Knotenanzahl:
    dx = kL / nx;

    // *************************************************************************
    // Anfangsbedingungen
    // *************************************************************************

    step = 0;
    time = 0.;
    vol = 0.;

    // Zeitschrittweite aus Wellengeschwindigkeit und CFL:
    dt = dx / Math.sqrt(GRAVITY * ow) * data.getCfl();

    // Maximale Anzahl Zeitschritte:
    maxStep = (int) (data.getTimeMax() / dt);

    // Aktuelle Schuetzoeffnungshoehe
    valveOpening = new double[maxStep + 1];

    // Zeitabhaengige Ergebnisse protokollieren
    Q = new double[maxStep + 1];
    hMean = new double[maxStep + 1];
    I = new double[maxStep + 1];
    F0 = new double[maxStep + 1];
    F1 = new double[maxStep + 1];
    F2 = new double[maxStep + 1];

    timeSeries = new double[maxStep + 1];

    // Anfangsbedingungen der Felder setzen
    // Zellwerte
    for (int i = 0; i < nx; i++) {
      A_schiff_cell[i] = data.getShipArea(dx * (i + 0.5));

      A00[i] = uw * kB - A_schiff_cell[i];
      A0[i] = A00[i];
      A05[i] = 0.;
      A1[i] = A0[i];
      
      beta[i] = 1.;
      zufluss[i] = 0.;
      h1[i] = 0.;
    }

    // Knotenwerte
    for (int i = 0; i < nx + 1; i++) {
      A_schiff_node[i] = data.getShipArea(dx * i);
      Q00[i] = 0.;
      Q0[i] = 0.;
      Q05[i] = 0.;
      Q1[i] = 0.;
      
      v1[i] = 0;
    }
    
    // Ergebnisse ueber die Zeit loeschen
    for (int i = 0; i < maxStep; i++) {
      hMean[i] = uw;
      Q[i] = 0;
      I[i] = (h1[0] - h1[nx - 2]) / (kL - dx);
      F0[i] = 0;
      F1[i] = 0;
      F2[i] = 0;
    }
  }

  private void step() {

    // Zeitzaehler erhoehen
    time = time + dt;
    step = step + 1;

    timeSeries[step] = time;

    final double aMue = filling.getAreaTimesLoss(time);
    
    if (filling instanceof SegmentGateFillingType) {
      valveOpening[step] = ((SegmentGateFillingType)filling).getSegmentGateAngle(time);
    } else if (filling instanceof SluiceGateFillingType) {
      valveOpening[step] = ((SluiceGateFillingType)filling).getSluiceGateHeight(time);
    } else {
      throw new IllegalArgumentException(
          "Unknown filling type: " + data.getFillingType());
    }

    // Effektive Fallhöhe: Entweder OW bis Schütz oder OW bis UW
    final double dh = Math.min(ow - h1[0], maxDh);

    Q[step] = aMue * Math.sqrt(2. * GRAVITY * Math.abs(dh)
        / (1. + zetaKanal / aKanal / aKanal * aMue * aMue));

    // TODO: Warum eigentlich?
    // Negative Qs abfangen
    if (dh < 0.) {
      Q[step] = 0.;
    }

    // Vorkopffuellung: Zufluss am nullten Knoten, mit Zeitwichtung:
    zufluss[0] = Q[step];

    // Alte Zeitebene wird ganz alte Zeitebene, neue Zeitebene wird alte
    // Zeitebene:
    for (int i = 0; i < nx; i++) {
      A00[i] = A0[i];
      A0[i] = A1[i];
      // Schaetzung der Werte fuer A05 mit Adams-Bashforth:
      A05[i] = 1.5 * A0[i] - 0.5 * A00[i];
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
    //  Crank-Nicolson fuer A mit implizitem A fuer dh/dx-Term

    // Schleife fuer Predictor-Corrector
    for (int corrStep = 0; corrStep <= 2; corrStep++) {

      // Strahlbeiwert beta anpassen beta=integral(U*U)dA / (U_mean*Q)
      // = integral(U*U)dA / (integral(U)dA*integral(U)dA / A)
      for (int i = 0; i < nx; i++) {

        // Strahlausbreitung
        double aStrahl = aKanal
            + Math.pow(aKanal, jetExponent) * (dx * i) * jetCoefficient;
        aStrahl = Math.min(aStrahl, A05[i]);

        beta[i] = A05[i] / aStrahl;
      }

      // AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
      // A und Q sind "staggered", darum A nur bis nx-1! A[0] liegt
      // zwischen Q[0] und Q[1].
      for (int i = 0; i < nx - 1; i++) {
        A1[i] = A0[i] - dt * (Q05[i + 1] - Q05[i]) / dx;
      }

      // A berechnen fuer Rand-Volumen
      A1[0] = A0[0] - dt * Q05[1] / dx;
      A1[nx - 1] = A0[nx - 1] + dt * Q05[nx - 1] / dx;

      // Quellterme fuer jedes Volumen
      for (int i = 0; i < nx; i++) {
        A1[i] += dt * zufluss[i] / dx;
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
        final double gradH = ((A1[i] + A_schiff_cell[i]) / kB
            - (A1[i - 1] + A_schiff_cell[i - 1]) / kB) / dx;
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
        if (upwind > 0.) {
          if (Q0[i] >= 0.) {
            Q1[i] += -dt * upwind
                * (beta[i] * Q05[i] * vP - beta[i - 1] * Q05[i - 1] * vM) / dx;
          } else {
            Q1[i] += -dt * upwind
                * (beta[i] * Q05[i + 1] * vP - beta[i - 1] * Q05[i] * vM) / dx;
          }
        }
      }
      // Q fuer RB-Knoten
      // Q1[0] = 0.; // Impulsfreies Einleiten
      Q1[0] = 1. * zufluss[0]; // Einleiten mit Impuls am Knoten 0 mit
      // Faktor fuer die Richtung
      Q1[nx] = 0.;
      // QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ

      // Neues Q05 mit variabler Zeitwichtung ermitteln:
      for (int i = 0; i < nx + 1; i++) {
        Q05[i] = (1. - theta) * Q0[i] + theta * Q1[i];
      }
    }

    // Übertragen auf h und v: Nur fuer Postprocessing!
    for (int i = 0; i < nx; i++) {
      h1[i] = (A1[i] + A_schiff_cell[i]) / kB;
    }

    for (int i = 1; i < nx; i++) {
      v1[i] = Q1[i] / (0.5 * (A1[i - 1] + A1[i]));
    }

    v1[0] = Q1[0] / kB / h1[0];
    v1[nx] = Q1[nx] / kB / h1[nx - 1];

    // Gefaelle ausrechnen: Nur fuer Postprocessing!
    I[step] = (-h1[0] + h1[nx - 2]) / (kL - dx);

    // Mittleren Wasserstand ausrechnen: Nur fuer Postprocessing!
    hMean[step] = 0.;
    for (int i = 0; i < nx; i++) {
      hMean[step] += h1[i] / nx;
    }

    // Volumenbilanz
    vol += dt * zufluss[0];

    // Kraft auf Schiff [kN]
    for (int i = 0; i < nx - 1; i++) {

      final double AA = A_schiff_node[i + 1];
      final double AB = A_schiff_node[i];

      // Massenkraft des Schiffes:
      F0[step] += 1000. * GRAVITY * AA * dx;
      // Summe:
      F1[step] += -1000. * GRAVITY * h1[i] * (AA - AB);
      // Über Flächendifferenzen*Druecke
      F2[step] += -(h1[i] - h1[i + 1]) / dx * 1000. * GRAVITY * 0.5 * (AA + AB)
          * dx;
          // F2[it] += -(h1[i] - h1[i + 1]) / dx * 1000. * 9.81 * 0.5 * (AA +
          // AB) * dx / 1000.;// lokales
          // Gefaelle
          // *
          // lokale
          // Masse
          // F2[it] = 1000;

      // System.out.println(i*dx+" "+AA+" "+AB+" "+F[it]+" "+F2[it]);
    }
    // System.out.println("Massenkraft: "+F0[it]/1.e6 +" MN");

    // Hangabtriebskraft des Schiffes:
    F0[step] *= -(h1[0] - h1[nx - 1]) / ((nx - 1) * dx) / 1000.;

    // DEBUG!
    // if (it%100==0) {System.out.println("Time:"+at); for (int i = 0; i
    // < nx ; i ++) {System.out.println(i+" "+A1[i]+" "+Q1[i]+"
    // "+h1[i]+" "+A_schiff_cell[i]);}}

  }

  private Results postprocess() {

    double Qmax = 0., Imax = Double.MIN_VALUE, Imin = Double.MAX_VALUE, 
        Fmax = Double.MIN_VALUE, Fmin = Double.MAX_VALUE,
        schiff = 0., dQ_dt_min = Double.MAX_VALUE, 
        dQ_dt_max = Double.MIN_VALUE;

    for (int i = 0; i < step; i++) {
      Qmax = Math.max(Qmax, Q[i]);
      Imax = Math.max(Imax, I[i]);
      Imin = Math.min(Imin, I[i]);
      Fmax = Math.max(Fmax, F2[i]);
      Fmin = Math.min(Fmin, F2[i]);
      if (i > 0) {
        if ((Q[i] - Q[i - 1]) / dt < dQ_dt_min) {
          dQ_dt_min = (Q[i] - Q[i - 1]) / dt;
        }
        if ((Q[i] - Q[i - 1]) / dt > dQ_dt_max) {
          dQ_dt_max = (Q[i] - Q[i - 1]) / dt;
        }
      }
    }

    // Schiffsverdraengung [m^3]
    for (int i = 0; i < nx; i++) {
      schiff += data.getShipArea(dx * i) * dx;
    }
    
    System.out.println("*******************************************************");
    System.out.printf("Zeitschritte: %d \n", step);
    System.out.printf("Füllzeit: %f s \n", time);
    System.out.printf("Füllvolumen: %f m³/s \n", vol);
    System.out.printf("Schiffsvolumen: %f m³/s \n", schiff);
    System.out.printf("Qmax: %f m³/s \n", Qmax);
    System.out.printf("Fx_min: %f kN  Fx_max: %f kN \n", Fmin, Fmax);
    System.out.printf("Fx/G: %f  \n",
        Math.max(Fmax, Math.abs(Fmin)) / schiff / GRAVITY * 1000.);
    System.out.printf("Imin: %f ‰  Imax: %f ‰ \n",
        Imin * 1000., Imax * 1000.);
    System.out.printf("dQ/dt_min: %f m³/s²  dQ/dt_max: %f m³/s² \n",
            dQ_dt_min, dQ_dt_max);
    System.out.println("*******************************************************");
    System.out.printf("Gesamtlaufzeit: %f s \n",
        (System.nanoTime() - startTime) * 1.e-9);

    return new Results() {

      @Override
      public double[] getTimeline() {
        return timeSeries;
      }

      @Override
      public double[] getSlopeOverTime() {
        return I;
      }

      @Override
      public double[] getDischargeOverTime() {
        return Q;
      }

      @Override
      public double[] getChamberWaterDepthOverTime() {
        return hMean;
      }

      @Override
      public double[] getLongitudinalForceOverTime() {
        return F2;
      }

      @Override
      public double[] getValveOpeningOverTime() {
        return valveOpening;
      }
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

    // Laufzeitmessung
    startTime = System.nanoTime();

    // Ende der Füllung: min_dh
    final double ow_uw = data.getDeltaWaterDepthStop();

    do {

      step();

    } while ((step < maxStep) && (hMean[step] < ow - ow_uw));

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
