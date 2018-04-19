/*******************************************************************************
 * Copyright (C) 2018 Bundesanstalt für Wasserbau
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
package de.baw.lomo;

import java.io.File;

import de.baw.lomo.core.Model;
import de.baw.lomo.core.OneDimensionalModel;
import de.baw.lomo.core.data.Case;
import de.baw.lomo.core.data.Results;
import de.baw.lomo.io.IOUtils;

public class Headless {

  public static void main(String[] args) {
        
    Case initCase = new Case();
    
    IOUtils.writeCaseToXml(initCase, new File("D:\\testlomo.xml"));    
    
    Case data = IOUtils.readCaseFromXml(new File("D:\\testlomo.xml"));
    
    Model model = new OneDimensionalModel();
    
    model.setCaseData(data);
    
    Results results = model.run();
    
    IOUtils.writeResultsToText(results, new File("D:\\results.dat"));

  }

}
