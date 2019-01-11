/*******************************************************************************
 * Copyright (C) 2019 Bundesanstalt für Wasserbau
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
 ******************************************************************************/
package de.baw.lomo.core.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "customSourceFromFileFillingType")
public class CustomSourceFromFileFillingType extends AbstractCustomSourceFillingType {

  private String file = ""; //$NON-NLS-1$

  public String getFile() {
    return file;
  }

  public void setFile(String file) {
    this.file = file;
    readFile();
  }

  @XmlTransient
  @Override
  public List<CustomSource> getSources() {
    return super.getSources();
  }

  @Override
  public void setSources(List<CustomSource> sources) {
    super.setSources(sources);
  }

  @Override
  public String toString() {
    return Messages.getString("fillingTypeCustomSourceFromFile"); //$NON-NLS-1$
  }
  
  //***************************************************************************

  private void readFile() {
    if (file.isEmpty()) {
      throw new IllegalArgumentException(Messages.getString("customSourceFromFile.customSourceFromFileMissingFileError")); //$NON-NLS-1$
    }

    final File f = new File(file);

    if (!f.isFile()) {
      throw new IllegalArgumentException(Messages.getString("customSourceFromFile.fileDoesNotExistError") + f); //$NON-NLS-1$
    }

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(
        new FileInputStream(f), StandardCharsets.UTF_8))) {

      final NumberFormat format = NumberFormat
          .getNumberInstance(Locale.ENGLISH);

      List<CustomSource> sources = new ArrayList<>();

      String line;

      int headerLine = 0;

      while ((line = reader.readLine()) != null) {

        final String[] lineData = line.replaceAll("[\uFEFF-\uFFFF]", "") //$NON-NLS-1$ //$NON-NLS-2$
            .split("\\s+"); //$NON-NLS-1$

        if (lineData.length == 0) {
          break;
        }

        if (lineData[0].equals("#")) { //$NON-NLS-1$

          if (headerLine == 0) {

            for (int s = 0; s < lineData.length - 1; s++) {
              CustomSource source = new CustomSource();
              source.setPosition(format.parse(lineData[s + 1]).doubleValue());
              sources.add(source);
            }

          } else if (headerLine == 1) {

            for (int s = 0; s < lineData.length - 1; s++) {
              CustomSource source = sources.get(s);
              source.setLengthOfInfluence(
                  format.parse(lineData[s + 1]).doubleValue());
            }

          } else if (headerLine == 2) {

            for (int s = 0; s < lineData.length - 1; s++) {
              CustomSource source = sources.get(s);
              source.setMomentumFactor(
                  format.parse(lineData[s + 1]).doubleValue());
            }

          } else {
            throw new RuntimeException(
                Messages.getString("customSourceFromFile.tooManyHeaderLinesError")); //$NON-NLS-1$
          }
          headerLine++;
        } else {

          if (sources.size() == 0) {
            throw new RuntimeException(
                Messages.getString("customSourceFromFile.missingHeaderLinesError")); //$NON-NLS-1$
          }
          
          if (lineData.length != sources.size() * 2) {
            throw new RuntimeException(
                Messages.getString("customSourceFromFile.missingDataColumnsError")); //$NON-NLS-1$
          }

          for (int i = 0; i < sources.size(); i++) {

            sources.get(i).getSourceLookup()
                .add(new KeyValueEntry(
                    format.parse(lineData[i * 2]).doubleValue(),
                    format.parse(lineData[i * 2 + 1]).doubleValue()));
          }
          
        }
      }

      setSources(sources);

    } catch (final IOException e) {
      e.printStackTrace();
    } catch (final ParseException e) {
      e.printStackTrace();
    }
  }
}
