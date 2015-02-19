package stacklayout.requirement;

import java.util.List;

public interface RequirementsAnalyzer {
    int getRequirementCount(List<Class> stack, Class top);
}
