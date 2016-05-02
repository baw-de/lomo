package de.baw.lomo.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import de.baw.lomo.core.data.Case;
import de.baw.lomo.core.data.Results;

public class IOUtils {

  private IOUtils() {
    throw new AssertionError();
  }

  public static Case readCaseFromXml(File xmlFile) {

    try {

      JAXBContext jaxbContext = JAXBContext.newInstance(Case.class);
      Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      return (Case) jaxbUnmarshaller.unmarshal(xmlFile);

    } catch (JAXBException e) {

      throw new RuntimeException("XML case file is no valid.", e);
    }
  }

  public static void writeCaseToXml(Case data, File xmlFile) {

    try {

      JAXBContext jaxbContext = JAXBContext.newInstance(Case.class);
      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

      // output pretty printed
      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

      jaxbMarshaller.marshal(data, xmlFile);

    } catch (JAXBException e) {

      throw new RuntimeException("There was an error when writing XML case file.", e);
    }
  }

  public static void writeResultsToText(Results results, File file) {

    final double[] t = results.getTimeline();
    final double[] o = results.getValveOpeningOverTime();
    final double[] h = results.getChamberWaterDepthOverTime();
    final double[] q = results.getDischargeOverTime();
    final double[] s = results.getSlopeOverTime();   
    final double[] lf = results.getLongitudinalForceOverTime();  
    
    
    DecimalFormatSymbols dSep = new DecimalFormatSymbols();
    dSep.setDecimalSeparator('.');
    
    DecimalFormat df = new DecimalFormat("#.######", dSep);

    try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
        new FileOutputStream(file), StandardCharsets.UTF_8))) {
      
      bw.write("t[s] s[m] H[m] Q[m^3/s] I[-] Fl[N]");
      bw.newLine();

      for (int i = 0; i < t.length; i++) {

        if (i > 0 && t[i] == 0) {
          break;
        }

        bw.write(df.format(t[i]) + " ");
        bw.write(df.format(o[i]) + " ");
        bw.write(df.format(h[i]) + " ");
        bw.write(df.format(q[i]) + " ");
        bw.write(df.format(s[i]) + " ");
        bw.write(df.format(lf[i]) + "");
        bw.newLine();
      }

      bw.close();

    } catch (IOException e) {
      
      throw new RuntimeException("There was an error when writing results text file.", e);
    }

  }
  
//  public static void writeResultsToXml(Results results, File xmlFile) {
//
//    try {
//
//      JAXBContext jaxbContext = JAXBContext.newInstance(Results.class);
//      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
//
//      // output pretty printed
//      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//
//      jaxbMarshaller.marshal(results, xmlFile);
//
//    } catch (JAXBException e) {
//
//      throw new RuntimeException("There was an error when writing XML results file.", e);
//    }
//  }
}
