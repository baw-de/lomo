/*
 * Copyright (c) 2019-2021 Bundesanstalt für Wasserbau
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
package de.baw.lomo.io;

import de.baw.lomo.core.data.Case;
import de.baw.lomo.core.data.FillingType;
import de.baw.lomo.core.data.Results;
import jakarta.xml.bind.*;
import jakarta.xml.bind.util.JAXBSource;

import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

public final class IOUtils {

  private IOUtils() {
    throw new AssertionError();
  }

  @SuppressWarnings("unchecked") // we have just marshalled the object before
  public static <T extends FillingType> T deepCopyDataObjects(T obj) {

    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
      Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

      return (T) jaxbUnmarshaller.unmarshal(new JAXBSource(jaxbContext,obj));

    } catch (JAXBException e) {

      throw new RuntimeException(Messages.getString("errorDeepCopy"), e); //$NON-NLS-1$
    }
  }

  public static Case readCaseFromXml(File xmlFile) {

    try {

      JAXBContext jaxbContext = JAXBContext.newInstance(Case.class);
      Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      return (Case) jaxbUnmarshaller.unmarshal(xmlFile);

    } catch (JAXBException e) {

      throw new RuntimeException(Messages.getString("xmlFileInvalid"), e); //$NON-NLS-1$
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

      throw new RuntimeException(Messages.getString("errorWritingXml"), e); //$NON-NLS-1$
    }
  }
  
  public static void writeResultsToText(Results results, File file) {
    writeResultsToText(results, file, null);
  }

  public static void writeResultsToText(Results results, File file, String comment) {

    final double[] timeResults = results.getTimeline();
    final double[][] valeOpeningResults = results.getValveOpeningOverTime();
    final double[] waterLevelResults = results.getMeanChamberWaterLevelOverTime();
    final double[] dischargeResults = results.getDischargeOverTime();
    final double[] slopeResults = results.getSlopeOverTime();   
    final double[] forceResults = results.getLongitudinalForceOverTime();  
    
    
    DecimalFormatSymbols dSep = new DecimalFormatSymbols();
    dSep.setDecimalSeparator('.');
    
    DecimalFormat df = new DecimalFormat("#.######", dSep); //$NON-NLS-1$

    try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
        new FileOutputStream(file), StandardCharsets.UTF_8))) {
      
      if (comment != null) {
        bw.write(comment);
        bw.newLine();
      }
      
      bw.write("TIME[s] "); //$NON-NLS-1$
      for (int j = 0; j < valeOpeningResults[0].length; j++) {
        String valveStr = "";
        if (valeOpeningResults[0].length > 1) {
          valveStr = "_" + j;
        }
        bw.write("VALVE_OPENING" + valveStr + "[m,°,%] "); //$NON-NLS-1$  //$NON-NLS-2$
      }
      bw.write("CHAMBER_WATER_LEVEL[m] FLOW_RATE[m^3/s] SLOPE[-] LONGITUDINAL_FORCE[N]"); //$NON-NLS-1$
      bw.newLine();

      for (int i = 0; i < timeResults.length; i++) {

        bw.write(df.format(timeResults[i]) + " "); //$NON-NLS-1$

        for (int j = 0; j < valeOpeningResults[0].length; j++) {
          bw.write(df.format(valeOpeningResults[i][j]) + " "); //$NON-NLS-1$
        }
        bw.write(df.format(waterLevelResults[i]) + " "); //$NON-NLS-1$
        bw.write(df.format(dischargeResults[i]) + " "); //$NON-NLS-1$
        bw.write(df.format(slopeResults[i]) + " "); //$NON-NLS-1$
        bw.write(df.format(forceResults[i]) + ""); //$NON-NLS-1$
        bw.newLine();
      }

    } catch (IOException e) {
      
      throw new RuntimeException(Messages.getString("errorWritingResults"), e); //$NON-NLS-1$
    }

  }
  
  public static void writeResultsToOpenFoam(Results results, File file) {
    writeResultsToOpenFoam(results, file, null);
  }
  
  public static void writeResultsToOpenFoam(Results results, File file, String comment) {
    
    final double[] timeResults = results.getTimeline();
    final double[] dischargeResults = results.getDischargeOverTime();
    
    DecimalFormatSymbols dSep = new DecimalFormatSymbols();
    dSep.setDecimalSeparator('.');
    
    DecimalFormat df = new DecimalFormat("#.######", dSep); //$NON-NLS-1$

    try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
        new FileOutputStream(file), StandardCharsets.UTF_8))) {
      
      if (comment != null) {
        bw.write("// " + comment); //$NON-NLS-1$
        bw.newLine();
      }
      
      bw.write("// TIME[s] FLOW_RATE[m^3/s]"); //$NON-NLS-1$
      bw.newLine();
      
      bw.write(Integer.toUnsignedString(timeResults.length));
      bw.newLine();
      bw.write('(');
      bw.newLine();

      for (int i = 0; i < timeResults.length; i++) {

        bw.write('(');
        bw.write(df.format(timeResults[i]) + " "); //$NON-NLS-1$
        // We have to multiply by one as the filling flow rate will leave 
        // the OpenFOAM domain.
        bw.write(df.format(dischargeResults[i] * -1.)); //$NON-NLS-1$
        bw.write(')');
        bw.newLine();
      }
      bw.write(')');
      bw.newLine();

    } catch (IOException e) {
      
      throw new RuntimeException(Messages.getString("errorWritingResults"), e); //$NON-NLS-1$
    }
  }

  public static void generateCaseSchema() {

    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(Case.class);
      jaxbContext.generateSchema(new SchemaOutputResolver() {
        @Override
        public Result createOutput(String namespaceUri, String suggestedFileName) {
          return new StreamResult("BAWLomoCase.xsd"); //$NON-NLS-1$
        }
      });

    } catch (JAXBException | IOException e) {
      throw new RuntimeException(Messages.getString("errorWritingSchema"), e); //$NON-NLS-1$
    }
  }
  
  public static Results readResultsFromFile(File file) {
    
    final List<Double> timeList = new ArrayList<>();
    final List<List<Double>> openingLists = new ArrayList<>();
    final List<Double> waterLevelList = new ArrayList<>();
    final List<Double> dischargeList = new ArrayList<>();
    final List<Double> slopeList = new ArrayList<>();
    final List<Double> forceList = new ArrayList<>();
    
    final List<List<Double>> listList = new ArrayList<>();
    
    final NumberFormat format = NumberFormat.getNumberInstance(Locale.ENGLISH);
    
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(
        new FileInputStream(file),StandardCharsets.UTF_8))) {
      
      String line;
      boolean readData = false;
      
      while ((line = reader.readLine()) != null) {
  
        final String[] lineData = line.replaceAll("[\uFEFF-\uFFFF]", "").trim() //$NON-NLS-1$ //$NON-NLS-2$
            .split("\\s+"); //$NON-NLS-1$
  
        if (lineData.length == 0) {
          break;
        }
        
        if (lineData[0].toLowerCase(Locale.ENGLISH).startsWith("time")) { //$NON-NLS-1$

          for (String lineDatum : lineData) {

            final String columnHeader = lineDatum.toLowerCase(Locale.ENGLISH);

            if (columnHeader.startsWith("time")) { //$NON-NLS-1$
              listList.add(timeList);
            } else if (columnHeader.startsWith("valve_opening")) { //$NON-NLS-1$
              final List<Double> vList = new ArrayList<>();
              listList.add(vList);
              openingLists.add(vList);
            } else if (columnHeader.startsWith("chamber_water_level")) { //$NON-NLS-1$
              listList.add(waterLevelList);
            } else if (columnHeader.startsWith("flow_rate")) { //$NON-NLS-1$
              listList.add(dischargeList);
            } else if (columnHeader.startsWith("slope")) { //$NON-NLS-1$
              listList.add(slopeList);
            } else if (columnHeader.startsWith("longitudinal_force")) { //$NON-NLS-1$
              listList.add(forceList);
            } else {
              throw new RuntimeException(String.format(Messages.getString("errorLoadingResults"), columnHeader)); //$NON-NLS-1$
            }

          }
          
          readData = true;       
          continue;
        }
        
        if (readData) {
          
          for (int i = 0; i < lineData.length; i++) {
            try {
              listList.get(i).add(format.parse(lineData[i]).doubleValue());
            } catch (ParseException e) {
              listList.get(i).add(Double.NaN);
            }
          }          
        }        
      }
      
      
    } catch (final IOException e) {
      e.printStackTrace();
    }     
    
    return new Results() {
      
      @Override
      public double[][] getValveOpeningOverTime() {
        return IntStream.range(0, timeList.size()).mapToObj(
                i -> openingLists.stream().mapToDouble(l -> l.get(i)).toArray()).toArray(double[][]::new);
      }

      @Override
      public double[] getTimeline() {
        return timeList.stream().mapToDouble(d -> d).toArray();
      }

      @Override
      public double[] getPositions() {
        throw new UnsupportedOperationException(Messages.getString("resultsNotSupportPositions")); //$NON-NLS-1$
      }

      @Override
      public double[] getSlopeOverTime() {
        return slopeList.stream().mapToDouble(d -> d).toArray();
      }
      
      @Override
      public double[] getLongitudinalForceOverTime() {
        return forceList.stream().mapToDouble(d -> d).toArray();
      }
      
      @Override
      public double[] getDischargeOverTime() {
        return dischargeList.stream().mapToDouble(d -> d).toArray();
      }
      
      @Override
      public double[] getMeanChamberWaterLevelOverTime() {
        return waterLevelList.stream().mapToDouble(d -> d).toArray();
      }

      @Override
      public double[][] getChamberWaterLevelOverTime() {
        return new double[timeList.size()][];
      }

      @Override
      public double[][] getFlowVelocityOverTime() {
        return new double[timeList.size()][];
      }
    };    
  }

  public static void main(String[] args) {
    IOUtils.generateCaseSchema();
  }
}
