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
package de.baw.lomo.core.data;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "customSourceFillingType")
public class CustomSourceFillingType extends AbstractCustomSourceFillingType {
  
  public void addSource(CustomSource source) {
    getSources().add(source);
  }
  
  public void clearSources() {
    getSources().clear();
  }

  @Override
  public String toString() {
    return Messages.getString("fillingTypeCustomSource"); //$NON-NLS-1$
  }

}
