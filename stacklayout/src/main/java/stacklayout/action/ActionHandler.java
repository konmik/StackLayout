package stacklayout.action;

import android.view.View;

/**
 * This interface is used by {@link stacklayout.view.StackLayout} to provide control over
 * animations for stack operations.
 */
public interface ActionHandler {
    /**
     * This method is called by {@link stacklayout.view.StackLayout} when given action is requested.
     * If the action implies that a view will be removed or frozen then this will be delayed until onActionEnd
     * is called for each called onStackAction.
     *
     * In example, if StackLayout has two children, and {@link stacklayout.view.StackLayout#replace} is
     * called then actual child removal will be done after each view's onActionEnd is called.
     *
     * @param action      a type of action that is being executing
     * @param view        a target view of action
     * @param onActionEnd a callback that must be called when animation is done.
     *                    This callback can also can be called immediately if no animation required.
     */
    void onStackAction(ActionType action, View view, Runnable onActionEnd);
}
