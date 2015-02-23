package stacklayout.helper;

import android.os.Bundle;
import android.view.View;

import java.util.List;

/**
 * This is an interface that provides a helping functionality for StackLayout.
 * Freezer's goal is to freeze views into bundles and unfreeze them back. The freezing
 * goes by packs - i.e. a freezer should freeze all views that are not required
 * for current view hierarchy and unfreeze more than one top view if it will be found that
 * the top view relies on other views to function properly.
 */
public interface Freezer {

    /**
     * Freezes views that are not required by the top view.
     * See {@link stacklayout.requirement.RequirementsAnalyzer#getRequirementCount} for details.
     *
     * @param stack a current stack of views
     * @return a list of frozen views, top to bottom
     */
    List<View> freezeBottom(List<View> stack);

    /**
     * Unfreezes the top view of frozen views.
     * See {@link stacklayout.requirement.RequirementsAnalyzer#getRequirementCount} for details.
     *
     * @return a list of unfrozen views, bottom to top
     */
    List<View> unfreezeBottom();

    /**
     * Saves a freezer's state and a state of additional views (it is implied that they are currently attached to
     * the StackLayout).
     *
     * @param autoFreeze a list of views to sae in addition to frozen views.
     * @return A Bundle which contains Freezer state and states of additional views.
     */
    Bundle save(List<View> autoFreeze);

    /**
     * Restores a freezer state and states of all additional views that has been passed to
     * {@link stacklayout.helper.Freezer#save}.
     *
     * @param bundle a bundle to restore state from.
     * @return restored views that has been passed to {@link stacklayout.helper.Freezer#save}.
     */
    List<View> restore(Bundle bundle);

    /**
     * Resets the state of Freezer.
     */
    void clear();

    /**
     * Returns the number of frozen views.
     *
     * @return The number of frozen views.
     */
    int size();
}
