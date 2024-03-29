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
module de.baw.lomo {
  requires java.desktop;
  requires javafx.fxml;
  requires javafx.swing;
  requires jakarta.xml.bind;
  requires org.glassfish.jaxb.runtime;
  requires org.controlsfx.controls;
  
  exports de.baw.lomo.utils;
  exports de.baw.lomo.io;
  exports de.baw.lomo.gui;
  exports de.baw.lomo.core.data;
  exports de.baw.lomo.core;

  opens de.baw.lomo.gui to javafx.fxml;
  opens de.baw.lomo.core.data to jakarta.xml.bind;

  uses de.baw.lomo.core.data.FillingType;

  provides de.baw.lomo.core.data.FillingType with
          de.baw.lomo.core.data.SluiceGateFillingType,
          de.baw.lomo.core.data.SegmentGateFillingType,
          de.baw.lomo.core.data.SegmentGateVelocityFillingType,
          de.baw.lomo.core.data.GenericGateFillingType,
          de.baw.lomo.core.data.SavingBasinFillingType,
          de.baw.lomo.core.data.CustomSourceFromFileFillingType;
}
