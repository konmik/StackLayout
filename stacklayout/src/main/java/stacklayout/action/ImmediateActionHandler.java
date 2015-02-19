package stacklayout.action;

import android.view.View;

public class ImmediateActionHandler implements ActionHandler {
    @Override
    public void onStackAction(ActionType action, View view, Runnable onEnd) {
        onEnd.run();
    }
}
