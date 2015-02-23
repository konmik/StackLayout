package stacklayout.action;

import android.view.View;

/**
 * This is a no-op implementation of {@link stacklayout.action.ActionHandler}.
 */
public class ImmediateActionHandler implements ActionHandler {
    @Override
    public void onStackAction(ActionType action, View view, Runnable onActionEnd) {
        onActionEnd.run();
    }
}
