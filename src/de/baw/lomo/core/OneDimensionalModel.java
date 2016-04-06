package de.baw.lomo.core;

import de.baw.lomo.core.data.Case;
import de.baw.lomo.core.data.Results;
import de.baw.lomo.core.data.SegmentGateFillingType;
import de.baw.lomo.core.data.SluiceGateFillingType;

public class OneDimensionalModel implements Model {
  private Case data;
  private double[] tResult;

  @Override
  public void init(Case caseData) {
    this.data = caseData;
  }

  @Override
  public Results run() {

    // Zeitschritt dt
    double dt;
    
    // Endzeit
    double tmax = data.getTimeMax();
    
    // Max. Zeitschritte
    int itmax;
    
    // Zeitwichtung theta
    double theta = data.getTheta();
    
    // Upwind-Faktor
    double upwind = data.getUpwind();

    // Ortsschritte nx. Q hat einen Wert mehr!
    int nx = data.getNumberOfNodes();;
    
    // Ortsschrittweite dx
    double dx;


    // Oberwasserstand
    double OW = data.getUpstreamWaterDepth();
    
    // Unterwasserstand
    double UW = data.getDownstreamWaterDepth();
    
    //Ende der Füllung: min_dh
    double OW_UW = data.getDeltaWaterDepthStop();

    // Kammerlaenge
    double KL = data.getChamberLength();
    
    // Kammerbreite
    double KB = data.getChamberWidth();


    double A_kanal, zeta_kanal, jetExponent, jetCoefficient, max_dh;

    
    //TODO: Drucksehment und Tafelschütz zusammenlegen, if-Abfrage hier wegwerfen!
    // Spezifische Angaben für Tafelschütze:
    if(data.getFillingType() instanceof SluiceGateFillingType) {

      SluiceGateFillingType sluice = (SluiceGateFillingType) data.getFillingType();

      // Kanalflaeche: Austrittsflaeche fuer Strahl
      A_kanal = sluice.getCulvertCrossSection();

      // Verlustbeiwert am Kanal
      zeta_kanal = sluice.getCulvertLoss();
      

      // Strahlausbreitung:
      // A_strahl=A_kanal+Math.pow(A_kanal,strahlpow)*(dx*i)*strahlbeiwert
      jetExponent = data.getJetExponent();
      jetCoefficient = data.getJetCoefficient();      
      
      // Beginn Rückstaueindluss
      max_dh = sluice.getSubmergenceStart();

      // Spezifische Angaben für Drucksegnent
    } else if (data.getFillingType() instanceof SegmentGateFillingType) {

      SegmentGateFillingType segment = (SegmentGateFillingType) data.getFillingType();

      // Kanalflaeche: Austrittsflaeche fuer Strahl
      A_kanal = segment.getCulvertCrossSection();

      // Verlustbeiwert am Kanal
      zeta_kanal = segment.getCulvertLoss();

      // Strahlausbreitung:
      // A_strahl=A_kanal+Math.pow(A_kanal,strahlpow)*(dx*i)*strahlbeiwert
      jetExponent = data.getJetExponent(); 
      jetCoefficient = data.getJetCoefficient(); //evtl. gleich 0 für Segment?  

      // Beginn Rückstaueindluss
      max_dh = segment.getSubmergenceStart();

    } else {
      throw new IllegalArgumentException("Unknown filling type: " + data.getFillingType());
    }

    
    // Felder fuer Geschwindigkeit und Wasserstand in der Kammer
    double Q00[], A00[]; // Ganz alte Zeitebene
    double Q0[], A0[]; // Alte Zeitebene
    double Q05[], A05[]; // Zeitebene zwischen alt und neu
    double Q1[], A1[]; // Neue Zeitebene
    double v1[], h1[]; // Postprozessing
    double A_schiff_node[], A_schiff_cell[]; // Schiffsquerschnitte
    
    // Beta-Beiwerte
    double beta[];

    // Zufluss
    double zufluss[];

    // Sohlhoehe = Schiffseintauchtiefe
    double z[];

    // Zeitabhaengige Ergebnisse protokollieren
    double I[], h_mean[], Q[], F0[], F1[], F2[], Vol;
    double Amue;

    // Aktueller Zeitschritt
    int it;
    
    // Aktuelle Zeit
    double at;
    
    // Aktuelle Schuetzoeffnungshoehe
    double[] s_s;

    // *************************************************************************************
    // Anfangsbedingungen
    // *************************************************************************************
    it = 0; // Anzahl Zeitschritte bisher
    at = 0.; // Aktuelle Zeit
    Vol = 0.; // Bisheriges Fuellvolumen

    // Ortsschrittweite
    dx = KL / nx;

    // Zeitschrittweite aus Wellengeschwindigkeit und CFL
    dt = dx / Math.sqrt(9.81 * OW) * data.getCfl();

    // Maximale Anzahl Zeitschritte
    itmax = (int) (tmax / dt);

    z = new double[nx];
    beta = new double[nx];
    zufluss = new double[nx];

    Q00 = new double[nx + 1];
    Q0 = new double[nx + 1];
    Q05 = new double[nx + 1];
    Q1 = new double[nx + 1];
    A00 = new double[nx];
    A0 = new double[nx];
    A05 = new double[nx];
    A1 = new double[nx];

    v1 = new double[nx + 1];
    h1 = new double[nx];

    A_schiff_node = new double[nx + 1];
    A_schiff_cell = new double[nx];

    s_s = new double[itmax + 1];
    Q = new double[itmax + 1];
    h_mean = new double[itmax + 1];
    I = new double[itmax + 1];
    F0 = new double[itmax + 1];
    F1 = new double[itmax + 1];
    F2 = new double[itmax + 1];

    tResult = new double[itmax+1];



    // Anfangsbedingungen der Felder setzen
    // Zellwerte
    for (int i = 0; i < nx; i++) {
      A_schiff_cell[i] = data.getShipArea(dx * (i + 0.5));
      A00[i] = UW * KB - A_schiff_cell[i];
      A0[i] = A00[i];
      A1[i] = A0[i];
      z[i] = 0.;
      beta[i] = 1.;
      zufluss[i] = 0.;
    }

    // Knotenwerte
    for (int i = 0; i < nx + 1; i++) {
      A_schiff_node[i] = data.getShipArea(dx * i);
      Q00[i] = 0.;
      Q0[i] = 0.;
      Q1[i] = 0.;
    }

    // Ergebnisse ueber die Zeit loeschen
    for (int i = 0; i < itmax; i++) {
      h_mean[i] = UW;
      Q[i] = 0;
      I[i] = (h1[0] - h1[nx - 2]) / (KL - dx);
      F0[i] = 0;
      F1[i] = 0;
      F2[i] = 0;
    }

    // *************************************************************************************
    // Zeitschleife
    // *************************************************************************************
    // Laufzeitmessung
    long start_time = System.currentTimeMillis();

    do {
      double s, A_strahl, A_schuetz, mue_schuetz, dh = 0;

      // Zeitzaehler erhoehen
      at = at + dt;
      it = it + 1;

      // Spezifische Werte für Tafelschütze:
      if(data.getFillingType() instanceof SluiceGateFillingType) {

        SluiceGateFillingType sluice = (SluiceGateFillingType) data.getFillingType();

        // Aktuelle SchÃ¼tzÃ¶ffnungsweite ermitteln
        s_s[it] = sluice.getSluiceGateHeight(at);

        // Aktuelle Schuetzoeffnungsflaeche ermitteln
        A_schuetz = s_s[it] *  sluice.getSluiceGateWidth(s_s[it]);

        // mue-Beiwert fuer Schuetz
        mue_schuetz = sluice.getSluiceGateLoss(s_s[it]);

        //A*mue für Tafelschütz
        Amue = A_schuetz * mue_schuetz;


     // Spezifische Angaben für Drucksegmente:
      } else if (data.getFillingType() instanceof SegmentGateFillingType) {

        SegmentGateFillingType segment = (SegmentGateFillingType) data.getFillingType();

        //Oeffnungswinkel
        double segmentAngle = segment.getSegmentGateAngle(at);
        
        //TODO:
        s_s[it] = segmentAngle/10; //Dreckige Loesung fuer Plot solange Franz in Urlaub
        
        //A * mue
        Amue = segment.getSegmentGateLoss(segmentAngle);



      } else {
        throw new IllegalArgumentException("Unknown filling type: " + data.getFillingType());
      }

      
      // Effektive Fallhöhe: Entwerden OW bis Schütz oder OW bis UW
      dh = Math.min(OW - h1[0]       , max_dh);
      
      Q[it] = Amue * Math.sqrt(2. * 9.81 * Math.abs(dh) / (1 + zeta_kanal / A_kanal / A_kanal * Amue * Amue));
      
      //TODO: Warum eigentlich?
      // Negative Qs abfangen
      if (dh < 0.)
        Q[it] = 0.;

      // Vorkopffuellung: Zufluss am nullten Knoten, mit Zeitwichtung
      zufluss[0] = Q[it];

      // Alte Zeitebene wird ganz alte Zeitebene, neue Zeitebene wird alte
      // Zeitebene
      for (int i = 0; i < nx; i++) {
        A00[i] = A0[i];
        A0[i] = A1[i];
        A05[i] = 1.5 * A0[i] - 0.5 * A00[i]; // Schaetzung der Werte
        // fuer A05 mit
        // Adams-Bashforth
      }
      for (int i = 0; i < nx + 1; i++) {
        Q00[i] = Q0[i];
        Q0[i] = Q1[i];
        Q05[i] = 1.5 * Q0[i] - 0.5 * Q00[i]; // Schaetzung der Werte
        // fuer Q05 mit Adams-Bashforth
      }

      // Gemischte Zeitdiskretisieerung:
      // A mit Adams-Bashforth-Diskretisierung
      // Q mit Adams-Bashforth-Diskretisierung fuer Q und mit
      // Crank-Nicolson fuer A mit implizitem A fuer dh/dx-Term

      // Schleife fuer Predictor-Corrector
      for (int corrStep = 0; corrStep <= 2; corrStep++) {

        // Strahlbeiwert beta anpassen beta=integral(U*U)dA / (U_mean*Q)
        // = integral(U*U)dA / (integral(U)dA*integral(U)dA / A)
        for (int i = 0; i < nx; i++) {

            A_strahl = A_kanal + Math.pow(A_kanal, jetExponent) * (dx * i) * jetCoefficient; // Strahlausbreitung
            A_strahl = Math.min(A_strahl, A05[i]);
            beta[i] = A05[i] / A_strahl;
                      }

        // AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
        // A und Q sind "staggered", darum A nur bis nx-1! A[0] liegt
        // zwischen Q[0] und Q[1].
        for (int i = 0; i < nx - 1; i++)
          A1[i] = A0[i] - dt * (Q05[i + 1] - Q05[i]) / dx;

        // A berechnen fuer Rand-Volumen
        A1[0] = A0[0] - dt * Q05[1] / dx;
        A1[nx - 1] = A0[nx - 1] + dt * Q05[nx - 1] / dx;

        // Quellterme fuer jedes Volumen
        for (int i = 0; i < nx; i++)
          A1[i] += dt * zufluss[i] / dx;
        // AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA

        // Neues A05 mit variabler Zeitwichtung ermitteln
        for (int i = 0; i < nx; i++)
          A05[i] = (1. - theta) * A0[i] + theta * A1[i];

        // QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ
        // Q berechnen fuer Knoten im Feld
        for (int i = 1; i < nx; i++) {
          double QM, QP, vM, vP, gradH, rhy, kSt = 100.;
          QM = 0.5 * (Q05[i - 1] + Q05[i]); // Wert fuer Q links von
          // Q[i]
          QP = 0.5 * (Q05[i] + Q05[i + 1]); // Wert fuer Q rechts von
          // Q[i]
          vM = QM / A05[i - 1]; // Wert fuer v links von Q[i]
          vP = QP / A05[i]; // Wert fuer v rechts von Q[i]
          rhy = 0.5 * (A05[i] + A05[i - 1]) / (3. * KB);
          gradH = ((A1[i] + A_schiff_cell[i]) / KB - (A1[i - 1] + A_schiff_cell[i - 1]) / KB) / dx; // Wasserspiegelgefaelle
          // mit
          // Schiff
          // gradH=( A1[i]/KB -A1[i-1]/KB)/dx ; //
          // Wasserspiegelgefaelle ohne Schiff

          Q1[i] = Q0[i] - dt * (1. - upwind) * (beta[i] * QP * vP - beta[i - 1] * QM * vM) / dx
              - dt * 9.81 * 0.5 * (A05[i] + A05[i - 1]) * gradH
              - dt * 9.81 * 0.5 * (A05[i] + A05[i - 1]) * (0.5 * Math.abs(vM + vP)) * (0.5 * (vM + vP))
              / kSt / kSt * Math.pow(rhy, -4. / 3.)
              - dt * (vM + vP) * 0.0; // Kuenstliche Daempfung
          // - dt*Q0[i]*0.002; // Kuenstliche Daempfung

          // Upwind
          if (upwind > 0.) {
            if (Q0[i] >= 0.)
              Q1[i] += -dt * upwind * (beta[i] * Q05[i] * vP - beta[i - 1] * Q05[i - 1] * vM) / dx;
            else
              Q1[i] += -dt * upwind * (beta[i] * Q05[i + 1] * vP - beta[i - 1] * Q05[i] * vM) / dx;
          }
        }
        // Q fuer RB-Knoten
        // Q1[0] = 0.; // Impulsfreies Einleiten
        Q1[0] = 1. * zufluss[0]; // Einleiten mit Impuls am Knoten 0 mit
        // Faktor fuer die Richtung
        Q1[nx] = 0.;
        // QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ

        // Neues Q05 mit variabler Zeitwichtung ermitteln
        for (int i = 0; i < nx + 1; i++)
          Q05[i] = (1. - theta) * Q0[i] + theta * Q1[i];
      }

      // Ãœbertragen auf h und v: Nur fuer Postprocessing!
      for (int i = 0; i < nx; i++)
        h1[i] = (A1[i] + A_schiff_cell[i]) / KB;
      for (int i = 1; i < nx; i++)
        v1[i] = Q1[i] / (0.5 * (A1[i - 1] + A1[i]));
      v1[0] = Q1[0] / KB / h1[0];
      v1[nx] = Q1[nx] / KB / h1[nx - 1];

      // Gefaelle ausrechnen: Nur fuer Postprocessing!
      I[it] = (-h1[0] + h1[nx - 2]) / (KL - dx);

      // Mittleren Wasserstand ausrechnen: Nur fuer Postprocessing!
      h_mean[it] = 0.;
      for (int i = 0; i < nx; i++)
        h_mean[it] += h1[i] / nx;

      // Volumenbilanz
      Vol += dt * zufluss[0];

      // Kraft auf Schiff [kN]
      for (int i = 0; i < nx - 1; i++) {
        double AA = A_schiff_node[i + 1];
        double AB = A_schiff_node[i];

        F0[it] += 1000. * 9.81 * AA * dx; // Massenkraft des Schiffes
        F1[it] += -1000. * 9.81 * h1[i] * (AA - AB); // Summe
        // Ã¼ber
        // FlÃ¤chendifferenzen*Druecke
        F2[it] += -(h1[i] - h1[i + 1]) / dx * 1000. * 9.81 * 0.5 * (AA + AB) * dx;
        //				F2[it] += -(h1[i] - h1[i + 1]) / dx * 1000. * 9.81 * 0.5 * (AA + AB) * dx / 1000.;// lokales
        // Gefaelle
        // *
        // lokale
        // Masse
        //				F2[it] = 1000;

        // System.out.println(i*dx+" "+AA+" "+AB+" "+F[it]+" "+F2[it]);
      }
      // System.out.println("Massenkraft: "+F0[it]/1.e6 +" MN");

      F0[it] *= -(h1[0] - h1[nx - 1]) / ((nx - 1) * dx) / 1000.; // Hangabtriebskraft
      // des
      // Schiffes

      // DEBUG!
      // if (it%100==0) {System.out.println("Time:"+at); for (int i = 0; i
      // < nx ; i ++) {System.out.println(i+" "+A1[i]+" "+Q1[i]+"
      // "+h1[i]+" "+A_schiff_cell[i]);}}

    } while ((it < itmax) && (h_mean[it] < OW - OW_UW));
    // while((it<itmax));
    itmax = it;

    // for (int i = 0; i < nx-1; i++){System.out.println(i*dx + "
    // "+beta[i]);}

    // *************************************************************************************
    // Postprozessing
    // *************************************************************************************
    double Qmax = 0., Imax = -1.e99, Imin = 1.e99, Fmax = -1.e99, Fmin = 1.e99, schiff = 0., dQ_dt_min = 1.e99,
        dQ_dt_max = -1.e99;

    // Ausgabe in Textfile
    //		try {
    //			FileWriter fw = new FileWriter("Schleuse.dat");
    //			BufferedWriter bw = new BufferedWriter(fw);
    //
    //			for (int i = 0; i < itmax; i++) {
    //				bw.write(Double.toString(i * dt) + " ");
    //				//bw.write(Double.toString(Amue) + " ");
    //				bw.write(Double.toString(h_mean[i]) + " ");
    //				bw.write(Double.toString(Q[i]) + " ");
    //				bw.write(Double.toString(I[i]) + " ");
    //				bw.write(Double.toString(F0[i]) + " ");
    //				bw.write(Double.toString(F1[i]) + " ");
    //				bw.write(Double.toString(F2[i]));
    //				bw.newLine();
    //			}
    //			bw.close();
    //		} catch (IOException e) {
    //			System.out.println("IOException : " + e);
    //		}

    for (int i = 0; i < itmax; i++) {
      Qmax = Math.max(Qmax, Q[i]);
      Imax = Math.max(Imax, I[i]);
      Imin = Math.min(Imin, I[i]);
      Fmax = Math.max(Fmax, F2[i]);
      Fmin = Math.min(Fmin, F2[i]);
      if (i > 0) {
        if ((Q[i] - Q[i - 1]) / dt < dQ_dt_min)
          dQ_dt_min = (Q[i] - Q[i - 1]) / dt;
        if ((Q[i] - Q[i - 1]) / dt > dQ_dt_max)
          dQ_dt_max = (Q[i] - Q[i - 1]) / dt;
      }
    }

    // Schiffsverdraengung [m^3]
    for (int i = 0; i < nx; i++)
      //schiff += twoVectorsLinearInterpolate(A_schiff[0], A_schiff[1], dx * i) * dx;
      schiff += data.getShipArea( dx * i) * dx;


    System.out.println("***************************************************************");
    System.out.println("Zeitschritte: " + it);
    System.out.println("Fuellzeit: " + at + " s");
    System.out.println("Fuellvolumen: " + Vol + " m^3");
    System.out.println("Schiffsvolumen: " + schiff + " m^3");
    System.out.println("Qmax: " + Qmax + " m**3/s");
    System.out.println("Fx_min: " + Fmin + " kN");
    System.out.println("Fx_max: " + Fmax + " kN");
    System.out.println("Fx/G: " + Math.max(Fmax, Math.abs(Fmin)) / schiff / 9.81 * 1000. + " o/oo");
    System.out.println("Imin: " + Imin * 1000. + " o/oo");
    System.out.println("Imax: " + Imax * 1000. + " o/oo");
    System.out.println("dQ/dt_min: " + dQ_dt_min + " m**3/s/s");
    System.out.println("dQ/dt_max: " + dQ_dt_max + " m**3/s/s");
    System.out.println("***************************************************************");
    System.out.println("Gesamtlaufzeit: " + (double) (System.currentTimeMillis() - start_time) / 1000.);

    for (int i = 0; i < itmax  ; i ++) {
      tResult[i] = i * dt;      
    }

    return new Results() {

      @Override
      public double[] getTimeline() {
        return tResult;
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
        return h_mean;
      }

      @Override
      public double[] getLongitudinalForceOverTime() {
        return F2;
      }

      @Override
      public double[] getValveOpeningOverTime() {
        return s_s;
      }
    };

  }


  }

}
