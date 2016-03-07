package de.baw.lomo.core;

import de.baw.lomo.core.data.Case;
import de.baw.lomo.core.data.Case.ValveType;
import de.baw.lomo.core.data.Results;

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
    // Kammersohle auf NN-10m, Modell um 10 m verschoben
    // Foerde: Oberer Bemessungswasserstand NN + 1,70 m
    // Foerde: Unterer Bemessungswasserstand NN -1,40 m
    // Maximales dH= 1,2 m

    double OW = data.getUpstreamWaterDepth();
    // Unterwasserstand
    // double UW = 15.;
    double UW = data.getDownstreamWaterDepth();
    // Ende der Fuellung
    // double OW_UW = 0.07;
    double OW_UW = data.getDeltaWaterDepthStop();

    // Kammerlaenge
    double KL = data.getChamberLength();
    // Kammerbreite
    double KB = data.getChamberWidth();

    // Kanalflaeche: Austrittsflaeche fÃ¼r Strahl
    double A_kanal = data.getCulvertCrossSection();
    // Verlustbeiwert am Kanal
    double zeta_kanal = data.getCulvertLoss();
    // Strahlausbreitung:
    // A_strahl=A_kanal+Math.pow(A_kanal,strahlpow)*(dx*i)*strahlbeiwert
    double strahlpow = data.getStrahlpow();
    double strahlbeiwert = data.getStrahlbeiwert();

    // Bei freiem Ausfluss: Maximale DruckhÃ¶he, bspw. WSP-OW bis Unterkante
    // Drucksegment

//     OW-max_dh = Beginn Rueckstau
//    double max_dh = OW - data.getSubmergenceStart();
//    System.out.print("max_dh= " + max_dh); 

    // Oeffnungscharakteristik
    // Komplettfuellung
    // double schuetzoeffnung [][] ={{0., 100, 1000.},
    // {0., 5., 25. }}; // Oeffnung ueber Zeit
    // double schuetzoeffnung [][] ={{0., 500., 800, 1100.},
    // {0., 12., 15, 20.}}; // Oeffnung ueber Zeit
    // Restfuellung
    // double schuetzoeffnung[][] = { { 0., 200, 400., 99999. }, { 0., 7, 16., 16. } }; // Oeffnung
    // ueber
    // Zeit

    //double schuetzbreite[][] = { { 0., 0.5, 1. }, { 8., 8., 8. } }; // Breite
    // ueber
    // Oeffnung

    //double schuetzmue[][] = { { 0., 0.8, 1.5, 2. }, { 0.6, 0.8, 0.8, 0.6 } }; // Mue
    // ueber
    // Oeffnung

    // Nur fuer DruckSegmente: A*mue ueber schuetzoeffnung
    boolean drucksegment = false; //FABIAN TO DO!!!!
    // Passend hinkalibriert fuer max_dh = 4.5;
    double schuetzAmue[][] = { { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 },
        { 0., 0., 0., 0.28, 0.58, 1.1, 1.6, 2.2, 2.80, 3.5, 4.2, 4.90, 5.5, 6.0, 6.5, 6.8, 7.3, 7.6, 7.8, 7.9,
      7.7 } };

    // Beginn Bolzum
    // Aus
    // ...\Bolzum\10064_neue_Schleuse\5_Untersuchungen\Modelluntersuchungen\4_Segmentkalibrierung
    // double schuetzAmue [][] ={{0 , 1 , 2 , 3, 4, 5, 6, 7, 8, 9, 10, 11,
    // 12, 13, 14, 15, 16, 17, 18, 19, 20},
    // {0.00,0.00,0.08,0.2,0.4 ,0.7 ,1.,1.5,2.1,2.7, 3.4, 4. , 4.5, 4.9
    // ,5.2,5.4,5.5, 5.6, 5.5, 5.5, 5.4}};
    // for (int i = 0; i < schuetzAmue [1].length; i++) schuetzAmue [1][i]
    // *= Math.sqrt(5./max_dh);
    // Ende Bolzum

    // Beginn Gleesen Numerik
    // double schuetzAmue [][] ={{0, 1, 2 , 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
    // 13, 14, 15, 16, 17, 18, 19, 20},
    // {0., 0.03, 0.09,0.17,0.29, 0.46,0.71,1.04,1.50,2.08,2.7,
    // 3.30,3.97,4.95,6.04,6.72,6.96,7.11,7.3,7.5,7.7}};
    // Ende Gleesen Numerik

    // Benetzte Schiffsquerschnittsflaeche: A_schiff ueber x
    //double A_schiff[][] = { { 5., 10., 135., 145. }, { 0., 11.5 * 2.8, 11.5 * 2.8, 0. } };

    /*
     * Bolzum Winkel A*mue_Versuch_Bolzum A*mue_Versuch_Bolzum_geglaettet
     * A*mue_Numerik_Gleesen 0 0,00 0,00 0,00 0,00 1 0,03 0,00 0,00 0,03 2
     * 0,09 0,00 0,07 0,09 3 0,17 0,23 0,28 0,17 4 0,29 0,63 0,58 0,29 5
     * 0,46 0,90 0,89 0,46 6 0,71 1,16 1,23 0,71 7 1,04 1,68 1,66 1,04 8
     * 1,50 2,20 2,20 1,50 9 2,08 2,79 2,75 2,08 10 2,70 3,37 3,36 2,70 11
     * 3,30 4,05 4,00 3,30 12 3,97 4,73 4,75 3,97 13 4,95 5,66 5,50 4,95 14
     * 6,04 6,31 6,15 6,04 15 6,72 6,70 6,53 6,72 16 6,96 6,81 6,85 6,96 17
     * 7,11 7,28 7,17 7,11 18 7,28 7,69 7,47 7,28 19 7,50 7,73 7,53 7,50 20
     * 7,50 7,44 7,45 7,74 21 7,50 7,46 7,41
     */

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
    double Amue = 0;

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

    // Zeitschrittweite aus Wellengeschwindigkeit
    dt = dx / Math.sqrt(9.81 * OW) * 0.5;

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
    //Amue = new double[];

    tResult = new double[itmax+1];



    // Anfangsbedingungen der Felder setzen
    // Zellwerte
    for (int i = 0; i < nx; i++) {
      //A_schiff_cell[i] = twoVectorsLinearInterpolate(A_schiff[0], A_schiff[1], dx * (i + 0.5));
      A_schiff_cell[i] = data.getShipArea(dx * (i + 0.5));
      A00[i] = UW * KB - A_schiff_cell[i];
      // A00[i] = UW*KB;
      A0[i] = A00[i];
      A1[i] = A0[i];
      z[i] = 0.;
      beta[i] = 1.;
      zufluss[i] = 0.;
    }

    // Knotenwerte
    for (int i = 0; i < nx + 1; i++) {
      //A_schiff_node[i] = twoVectorsLinearInterpolate(A_schiff[0], A_schiff[1], dx * i);
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

      // Aktuelle SchÃ¼tzÃ¶ffnungsweite ermitteln
      s_s[it] = data.getValveHeight(at);

      switch (data.getValveType() ) { 

      case SLUICE_GATE: //TAFELSCHUETZ
        // Aktuelle Schuetzoeffnungsflaeche ermitteln
        A_schuetz = s_s[it] *  data.getValveWidth(s_s[it]);
        // mue-Beiwert fuer Schuetz
        mue_schuetz = data.getValveLoss(s_s[it]);
        //System.out.println("DEBUG: mue_schuetz: " + mue_schuetz);
        Amue = A_schuetz * mue_schuetz;
        //System.out.println("DEBUG:Amue: " + Amue);
        break;

      case SEGMENT_GATE: //DRUCKSEGMENT
        
        //TODO: Bisher gleich wie bei Tafelschütz, muss noch verbessert werden.
        // Aktuelle Schuetzoeffnungsflaeche ermitteln
        A_schuetz = s_s[it] *  data.getValveWidth(s_s[it]);
        // mue-Beiwert fuer Schuetz
        mue_schuetz = data.getValveLoss(s_s[it]);
        //System.out.println("DEBUG: mue_schuetz: " + mue_schuetz);
        Amue = A_schuetz * mue_schuetz;
        //System.out.println("DEBUG:Amue: " + Amue);

        break;

      default:
        System.out.print("Kein Bekannter Verschlusstyp defniert!"); 
        break;

      }



      // Fuer Tafelschuetze etc.:
      //			if (!drucksegment) {
      //				// Aktuelle SchÃ¼tzÃ¶ffnungsflaeche ermitteln
      //				A_schuetz = s_s[it] *  data.getValveWidth(s_s[it]);
      //				//System.out.println("DEBUG: A_schuetz: " + A_schuetz);
      //				// mue-Beiwert fuer Schuetz
      //				mue_schuetz = data.getValveLoss(s_s[it]);
      //				//System.out.println("DEBUG: mue_schuetz: " + mue_schuetz);
      //				Amue = A_schuetz * mue_schuetz;
      //				//System.out.println("DEBUG:Amue: " + Amue);
      //			}

      // Fuer Drucksegmente:
      //			if (drucksegment)
      //				Amue[it] = twoVectorsLinearInterpolate(schuetzAmue[0], schuetzAmue[1], s_s);
      //
      // Vorkopffuellung: Wirksame Fallhoehe am Knoten 0
      // dh = Math.min(OW - h_mean[it-1], max_dh);
      
//    dh = Math.min(OW - h_mean[it-1], OW - data.getSubmergenceStart());

      //TODO: Fabian mit CT diskutieren!
      if (data.getSubmergenceStart() > OW){
        dh = OW - h_mean[it-1]; // nur falls sinnloser Wert;
      }
      else {
        if (h_mean[it-1] <= data.getSubmergenceStart()){
          dh = OW - data.getSubmergenceStart();
          //          System.out.println("h < z_kanal, dh = " +dh);
        }
        else if (h_mean[it-1] > data.getSubmergenceStart()){
          dh = OW - h_mean[it-1];
          //          System.out.println("h > z_kanal, dh = " +dh);
        }

      }
      

 
      
      //
      //			// Volumenstrom in die Kammer ausrechnen
      Q[it] = Amue * Math.sqrt(2. * 9.81 * Math.abs(dh) / (1 + zeta_kanal / A_kanal / A_kanal * Amue * Amue));

      if (dh < 0.)
        Q[it] = 0.;

      // Q[it]=-(Math.cos(at/97.5)-1.)*5.;

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
        // fuer Q05 mit
        // Adams-Bashforth
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
          // A_strahl=A_kanal+dx*i*0.015*KB; // Strahlausbreitung
          // waere anzupassen ...
          // A_strahl=A_kanal+dx*i*0.1*dx*i*0.1; // Strahlausbreitung
          // waere anzupassen ...
          A_strahl = A_kanal + Math.pow(A_kanal, strahlpow) * (dx * i) * strahlbeiwert; // Strahlausbreitung
          // waere
          // anzupassen
          // ...
          // A_strahl=A_kanal+(dx*i)*0.2; // Strahlausbreitung waere
          // anzupassen ...
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
        return F1;
      }

      @Override
      public double[] getValveOpeningOverTime() {
        return s_s;
      }
    };

  }

  //	public static double twoVectorsLinearInterpolate(double[] x, double[] y, double xin) {
  //		int nx = x.length;
  //
  //		if (x.length != y.length)
  //			throw new IllegalArgumentException("x and y have different length!");
  //
  //		if (xin <= x[0])
  //			return y[0];
  //		if (xin >= x[nx - 1])
  //			return y[nx - 1];
  //
  //		for (int i = 0; i < nx - 1; i++) {
  //			if (x[i + 1] < x[i])
  //				throw new IllegalArgumentException("x is not monotonic!");
  //
  //			if ((xin >= x[i]) && (xin <= x[i + 1]))
  //				return y[i] + (y[i + 1] - y[i]) / (x[i + 1] - x[i]) * (xin - x[i]);
  //		}
  //
  //		return -1.e99;
  //	}

  @Override
  public Results step() {
    // TODO Auto-generated method stub
    return null;
  }

}
