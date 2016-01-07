package de.baw.lomo.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
    final double[] q = results.getDischargeOverTime();
    final double[] s = results.getSlopeOverTime();
    final double[] h = results.getChamberWaterDepthOverTime();

    try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {

      for (int i = 0; i < t.length; i++) {

        if (i > 0 && t[i] == 0) {
          break;
        }

        bw.write(t[i] + " ");
        bw.write(h[i] + " ");
        bw.write(q[i] + " ");
        bw.write(s[i] + "");
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
