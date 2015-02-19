package stacklayout.action;

import android.view.View;

public interface ActionHandler {
    void onStackAction(ActionType action, View view, Runnable onActionEnd);
}
