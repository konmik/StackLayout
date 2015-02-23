package stacklayout.action;

import android.view.View;

/**
 * This is an utility {@link stacklayout.action.ActionHandler} implementation which allows to
 * delegate all actions directly to a view is the view implements {@link stacklayout.action.ActionHandler}.
 */
public class DelegateActionHandler implements ActionHandler {
    @Override
    public void onStackAction(ActionType action, View view, Runnable onActionEnd) {
        if (view instanceof ActionHandler)
            ((ActionHandler)view).onStackAction(action, view, onActionEnd);
        else
            onActionEnd.run();
    }
}
