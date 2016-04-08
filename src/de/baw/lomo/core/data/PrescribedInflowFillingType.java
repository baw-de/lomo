package de.baw.lomo.core.data;

import java.io.File;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="prescribedInflowFillingType")
public class PrescribedInflowFillingType extends FillingType {
  
  private String file = "";

  public String getFile() {
    return file;
  }

  public void setFile(String file) {
    this.file = file;
    hasFileChanged = true;    
  }
  
  // ***************************************************************************
  
  private boolean hasFileChanged = true;

  @Override
  public double[] getSource(double position, double time, double h, double v,
      Case data) {
    
    if (file.isEmpty()) {      
      throw new IllegalArgumentException("No file given!");      
    }
    
    if (hasFileChanged) {
      
      File f = new File(file);
      
      if (!f.isFile()) {        
        throw new IllegalArgumentException("File does not exist: " + f); 
      }
      
      
      // TODO do stuff
      
      
      hasFileChanged = false;
    }

    return null;
  }
  
  @Override
  public String toString() {
    return Messages.getString("fillingTypePrescribedInflow"); //$NON-NLS-1$
  }

}
