package de.baw.lomo.core;

import de.baw.lomo.core.data.Case;
import de.baw.lomo.core.data.Results;

public interface Model {
  
  void setCaseData(Case caseData);
  
  Results run();

}
