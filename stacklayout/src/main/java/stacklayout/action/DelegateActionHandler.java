package stacklayout.action;

import android.view.View;

public class DelegateActionHandler implements ActionHandler {
    @Override
    public void onStackAction(ActionType action, View view, Runnable onActionEnd) {
        if (view instanceof ActionHandler)
            ((ActionHandler)view).onStackAction(action, view, onActionEnd);
        else
            onActionEnd.run();
    }
}
