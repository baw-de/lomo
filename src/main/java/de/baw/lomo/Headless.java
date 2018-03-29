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
