/*
 * Copyright (c) 2019-2026 Bundesanstalt für Wasserbau
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
package de.baw.lomo.core.data;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@XmlRootElement(name = "customSourceFromFileFillingType")
public class CustomSourceFromFileFillingType extends AbstractCustomSourceFillingType {

  private String file = ""; //$NON-NLS-1$

  public String getFile() {
    return file;
  }

  public void setFile(String file) {
    this.file = file;
  }

  @XmlTransient
  @Override
  public List<CustomSource> getSources() {
    return super.getSources();
  }

  @Override
  public String toString() {
    return Messages.getString("fillingTypeCustomSourceFromFile"); //$NON-NLS-1$
  }
  
  //***************************************************************************

  private void readFile() {
    if (file.isEmpty()) {
      System.out.println(Messages.getString("customSourceFromFile.customSourceFromFileMissingFileError")); //$NON-NLS-1$
      return;
    }

    final File f = new File(file);

    if (!f.isFile()) {
      System.out.format(Messages.getString("customSourceFromFile.fileDoesNotExistError") + "\n", f.getName()); //$NON-NLS-1$
      return;
    }

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(
        new FileInputStream(f), StandardCharsets.UTF_8))) {

      final NumberFormat format = NumberFormat
          .getNumberInstance(Locale.ENGLISH);

      List<CustomSource> sources = new ArrayList<>();

      String line;
      int lineNb = 1;

      while ((line = reader.readLine()) != null) {

        final String[] lineData = line.replaceAll("[\uFEFF-\uFFFF]", "") //$NON-NLS-1$ //$NON-NLS-2$
            .split("\\s+"); //$NON-NLS-1$

        if (lineData.length == 0) {
          continue;
        }

        if ("#".equals(lineData[0])) { //$NON-NLS-1$
          
          if (lineData.length == 4) {

            try {
              CustomSource source = new CustomSource();
              source.setPosition(format.parse(lineData[1]).doubleValue());
              source.setLengthOfInfluence(
                  format.parse(lineData[2]).doubleValue());
              source.setMomentumFactor(format.parse(lineData[3]).doubleValue());
              sources.add(source);

            } catch (final ParseException e) {
              System.out.format(Messages
                  .getString("customSourceFromFile.cannotReadSourceInLineError") //$NON-NLS-1$
                  + "\n", lineNb);//$NON-NLS-1$
              return;
            }

          } else {
            System.out.format(
                Messages.getString("customSourceFromFile.cannotReadSourceInLineError") //$NON-NLS-1$
                    + "\n", lineNb);//$NON-NLS-1$                
            return;
          }

        } else {

          if (sources.isEmpty()) {
            System.out.format(
                Messages.getString("customSourceFromFile.missingHeaderLinesError")); //$NON-NLS-1$
            return;
          }
          
          if (lineData.length != sources.size() * 2) {
            System.out.format(
                Messages.getString("customSourceFromFile.missingDataColumnsError")); //$NON-NLS-1$
            return;
          }

          for (int i = 0; i < sources.size(); i++) {

            try {
            sources.get(i).getSourceLookup()
                .add(new KeyValueEntry(
                    format.parse(lineData[i * 2]).doubleValue(),
                    format.parse(lineData[i * 2 + 1]).doubleValue()));
            } catch (final ParseException e) {
              System.out.format(Messages
                  .getString("customSourceFromFile.cannotReadDataInLineError") //$NON-NLS-1$
                  + "\n", lineNb);//$NON-NLS-1$
            }
          }
          
        }
        
        lineNb++;
      }

      setSources(sources);

    } catch (final IOException e) {
      System.out.println(Messages.getString("customSourceFromFile.cannotReadFile"));
    }
  }

  @Override
  public void init() {
    readFile();
    super.init();
  }
}
