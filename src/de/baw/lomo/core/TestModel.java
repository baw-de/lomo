package de.baw.lomo.core;

import de.baw.lomo.core.data.Case;
import de.baw.lomo.core.data.Results;
import de.baw.lomo.core.data.SluiceGateFillingType;

public class TestModel implements Model {

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
    double theta;

    // Ortsschritte nx. Q hat einen Wert mehr!
    int nx = data.getNumberOfNodes();
    // Ortsschrittweite dx
    double dx;

    // Oberwasserstand
    double OW = data.getUpstreamWaterDepth();
    // Unterwasserstand
    double UW = data.getDownstreamWaterDepth();
    // Ende der Fuellung
    double OW_UW = data.getDeltaWaterDepthStop();

    // Kammerlaenge
    double KL = data.getChamberLength();
    // Kammerbreite
    double KB = data.getChamberWidth();

    SluiceGateFillingType fillingType;
    
    if(data.getFillingType() instanceof SluiceGateFillingType) {
      fillingType = (SluiceGateFillingType) data.getFillingType();
    } else {
      throw new RuntimeException("Not implemented.");
    }    

    // Kanalflaeche: Austrittsflaeche
    double A_kanal = fillingType.getCulvertCrossSection();
    // Verlustbeiwert am Kanal
    double zeta_kanal = fillingType.getCulvertLoss();
    // Oberkante Kanal fuer Rueckstaueinfluss
//    double z_kanal  = data.getSubmergenceStart();
    double z_kanal  = 9999999;

    // Felder fuer Geschwindigkeit und Wasserstand in der Kammer
    double Q00[],A00[]; // Ganz alte Zeitebene
    double Q0[],A0[];     // Alte Zeitebene
    double Q05[],A05[];   // Zeitebene zwischen alt und neu
    double Q1[],A1[];     // Neue Zeitebene
    double v1[],h1[];     // Postprozessing

    // Beta-Beiwerte
    double beta[];

    // Zufluss
    double zufluss[];

    // Sohlhoehe = Schiffseintauchtiefe
    double z[];

    // Zeitabhaengige Ergebnisse protokollieren
    double I[],h_mean[],Q[],F[],Vol;

    // Aktueller Zeitschritt
    int it;
    // Aktuelle Zeit
    double at;
    // Aktuelle Schuetzoeffnungshoehe
    double[] s_s;


    // *************************************************************************************
    // Anfangsbedingungen 
    // *************************************************************************************
    it = 0;    // Anzahl Zeitschritte bisher
    at = 0.;   // Aktuelle Zeit
    Vol=0.; // Bisheriges Fuellvolumen

    // Ortsschrittweite
    dx = KL/nx;   

    // Zeitschrittweite aus Wellengeschwindigkeit
    dt=dx/Math.sqrt(9.81*OW)*data.getCfl();

    // Maximale Anzahl Zeitschritte
    itmax = (int)(tmax/dt);


    z    = new double[nx];
    beta    = new double[nx];
    zufluss = new double[nx];

    Q00 = new double[nx+1]; Q0 = new double[nx+1]; Q05 = new double[nx+1]; Q1 = new double[nx+1]; 
    A00 = new double[nx];   A0 = new double[nx];   A05 = new double[nx];   A1 = new double[nx];

    v1 = new double[nx+1]; 
    h1 = new double[nx];

    s_s = new double[itmax+1];
    Q      = new double[itmax+1];
    h_mean = new double[itmax+1];
    I      = new double[itmax+1];
    F      = new double[itmax+1];
    tResult = new double[itmax+1];
     
    // Anfangsbedingungen der Felder setzen
    for (int i = 0; i < nx; i++){
        A00[i] = UW*KB;
        A0[i] = UW*KB;
        A1[i] = UW*KB;
        z[i] = 0.;
        beta[i] = 1.;
        zufluss[i] = 0.;
    }

    for (int i = 0; i < nx+1; i++){
        Q00[i] = 0.;
        Q0[i] = 0.;
        Q1[i] = 0.;
    }

    // Ergebnisse ueber die Zeit loeschen
    for (int i = 0; i < itmax; i++){
        h_mean[i] = UW;
        Q[i] = 0;
        I[i]=(h1[0]-h1[nx-2])/(KL-dx);
    }


    // *************************************************************************************
    // Zeitschleife
    // *************************************************************************************
    // Laufzeitmessung
    long start_time=System.currentTimeMillis();     

    do {
      double mueA, s, A_strahl,A_schuetz,mue_schuetz, dh, QM, QP, AIm1, AI;

      // Zeitzaehler erhoehen
      at = at + dt;
      it = it +1;

      // Aktuelle Schützöffnungsweite ermitteln
      s_s[it]=fillingType.getSluiceGateHeight(at);   

      // Aktuelle Schützöffnungsflaeche ermitteln
      A_schuetz = s_s[it] * fillingType.getSluiceGateWidth(s_s[it]);

      // mue-Beiwert fuer Schuetz
      mue_schuetz = fillingType.getSluiceGateLoss(s_s[it]);
      mueA = A_schuetz * mue_schuetz;

      // Vorkopffuellung: Wirksame Fallhoehe am Knoten 0
      dh = OW - Math.max(h1[0], z_kanal);

      // Volumenstrom in die Kammer ausrechnen
      Q[it]=mueA*Math.sqrt(2.*9.81*Math.abs(dh)/(1+zeta_kanal/A_kanal/A_kanal*A_schuetz*A_schuetz*mue_schuetz*mue_schuetz));
      Q[it]=Q[it]*Math.signum(dh);

      // Vorkopffuellung: Zufluss am nullten Knoten, mit Zeitwichtung
      zufluss[0]=Q[it];

      // Alte Zeitebene wird ganz alte Zeitebene, neue Zeitebene wird alte Zeitebene
      for (int i = 0; i < nx  ; i ++) {
        A00[i] = A0[i]; 
        A0[i]  = A1[i]; 
      } 
      for (int i = 0; i < nx+1; i ++) {
        Q00[i] = Q0[i]; 
        Q0[i]  = Q1[i]; 
        Q05[i] = 1.5*Q0[i] - 0.5*Q00[i];       // Schaetzung der Werte fuer Q05 mit Adams-Bashforth
      }

      // Gemischte Zeitdiskretisieerung: 
      // A mit Adams-Bashforth-Diskretisierung
      // Q mit Adams-Bashforth-Diskretisierung fuer Q und mit Crank-Nicolson fuer A mit implizitem A fuer dh/dx-Term

      // Schleife fuer Predictor-Corrector
      for (int corrStep=0; corrStep<1; corrStep++) {
        // AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA         
        // A und Q sind "staggered", darum A nur bis nx-1! A[0] liegt zwischen Q[0] und Q[1].
        for (int i = 0; i < nx-1; i ++)
          A1[i] = A0[i] - dt*(Q05[i+1]-Q05[i])/dx; 

        // A berechnen fuer Rand-Volumen
        A1[0]    = A0[0]   -dt*Q05[1]   /dx;
        A1[nx-1] = A0[nx-1]+dt*Q05[nx-1]/dx;

        // Quellterme fuer jedes Volumen
        for (int i = 0; i < nx; i ++)  A1[i] += dt*zufluss[i]/dx;
        // AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA         

        // Neues A05 mit Crank-Nicolson ermitteln
        for (int i = 0; i <  nx; i ++) A05[i] = 0.5*A0[i] + 0.5*A1[i]; 

        // QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ 
        // Q berechnen fuer Knoten im Feld
        for (int i = 1; i < nx; i ++) {
          QM=0.5*(Q05[i-1]+Q05[i  ]); // Wert fuer Q links  von Q[i]
          QP=0.5*(Q05[i]  +Q05[i+1]); // Wert fuer Q rechts von Q[i]
          Q1[i] = Q0[i] - dt*((beta[i]*QP*QP/A05[i]-beta[i-1]*QM*QM/A05[i-1])/dx + 9.81*0.5*(A05[i]+A05[i-1])*(A1[i]-A1[i-1])/KB/dx );
        } 
        // Q fuer RB-Knoten
        // Q1[0]    = 0.;  // Impulsfreies Einleiten
        Q1[0]    = 0;//1.*zufluss[0];   // Einleiten mit Impuls am Knoten 0 mit Faktor fuer die Richtung
        Q1[nx]   = 1.*zufluss[0];
        // QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ 

        // Neues Q05 mit Crank-Nicolson ermitteln
        for (int i = 0; i <  nx+1; i ++) Q05[i] = 0.5*Q0[i] + 0.5*Q1[i]; 
      } 

      // Strahlbeiwert beta anpassen
      for (int i = 0; i < nx; i++){
        A_strahl=Math.min(A_kanal+dx*i*0.1*KB,A1[i]);  // Strahlausbreitung waere anzupassen ...
        beta[i] = A1[i]/A_strahl;  
      }


      // Übertragen auf h und v: Nur fuer Postprocessing!
      for (int i = 0; i < nx  ; i ++)  h1[i] = A1[i]/KB; 
      for (int i = 1; i < nx  ; i ++)  v1[i] = Q1[i]/KB/(0.5*(h1[i-1]+h1[i])); 
      v1[0] = Q1[0] /KB/h1[0]; 
      v1[nx]= Q1[nx]/KB/h1[nx-1]; 

      // Gefaelle ausrechnen: Nur fuer Postprocessing!
      I[it]=(h1[0]-h1[nx-2])/(KL-dx);
      F[it]=3300.e3*9.81*I[it];

      // Mittleren Wasserstand ausrechnen: Nur fuer Postprocessing! 
      h_mean[it]=0.;
      for (int i = 0; i < nx  ; i ++)  h_mean[it] += h1[i]/nx; 

      // Volumenbilanz
      Vol+=dt*zufluss[0];
    }
    while((it<itmax)&&(h_mean[it]<OW-OW_UW));
    itmax=it;
    
    // *************************************************************************************
    // Postprozessing
    // *************************************************************************************
    double Qmax=0., Imax=-1.e99, Imin=1.e99;

  
    for (int i = 0; i < itmax  ; i ++) {
      Qmax = Math.max(Qmax,Q[i]); 
      Imax = Math.max(Imax,I[i]); 
      Imin = Math.min(Imin,I[i]); 
    }
    
    for (int i = 0; i < itmax  ; i ++) {
      tResult[i] = i * dt;      
    }

    System.out.println("***************************************************************");     
    System.out.println("Zeitschritte: "+it);     
    System.out.println("Fuellzeit: "+at+" s");     
    System.out.println("Fuellvolumen: "+Vol+" m^3");     
    System.out.println("Qmax: "+Qmax +" m**3/s");     
    System.out.println("Imin: "+Imin*1000. +" o/oo");     
    System.out.println("Imax: "+Imax*1000. +" o/oo");     
    System.out.println("***************************************************************");     
    System.out.println("Gesamtlaufzeit: " + (double)(System.currentTimeMillis()-start_time)/1000.);   
    
    
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
        return F;
      }

      @Override
      public double[] getValveOpeningOverTime() {
        return s_s;
      }
    };
  }

}
