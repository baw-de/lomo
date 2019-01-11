package de.baw.lomo.core.data;

import javax.xml.bind.annotation.XmlRootElement;

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
