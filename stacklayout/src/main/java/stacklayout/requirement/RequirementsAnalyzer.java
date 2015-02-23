package stacklayout.requirement;

import java.util.List;

/**
 * This is a requirements analyzer interface for use by StackLayout and Freezer implementation.
 */
public interface RequirementsAnalyzer {
    /**
     * A method that should return a number of views in an underlying stack that are required by the
     * given top view's class. This number should also include views that are between
     * the top view and the last of required views.
     * <p/>
     * Example:
     * ListView is a bottom class
     * ItemView is a middle class, it requires nothing and implements DeleteDialogOwner interface.
     * DeleteDialog is a top class, it requires DeleteDialogOwner interface.
     * <p/>
     * getRequirementCount({ListView.class, ItemView.class}, DeleteDialog.class) should return 1
     *
     * @param stack a list of classes of views in a stack, excluding the top view.
     * @param top   the top view that needs to fill requirements.
     * @return a number of views that is between top (exclusive) and the last (inclusive) of required
     * views.
     */
    int getRequirementCount(List<Class> stack, Class top);
}
