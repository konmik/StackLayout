package stacklayout.action;

/**
 * Action is a unit of work that is done by the {@link stacklayout.view.StackLayout} stack on a child view.
 * This enum's single use is for {@link stacklayout.action.ActionHandler} interface.
 */
public enum ActionType {

    /**
     * A view is going to be shown with {@link stacklayout.view.StackLayout#push}.
     */
    PUSH_IN(false, false),

    /**
     * A view is going to be frozen with {@link stacklayout.view.StackLayout#push}.
     */
    PUSH_OUT(true, false),

    /**
     * A view is going to be shown with {@link stacklayout.view.StackLayout#pop} method.
     */
    POP_IN(false, false),

    /**
     * A view is going to be removed with {@link stacklayout.view.StackLayout#pop} method.
     */
    POP_OUT(false, true),

    /**
     * A view is going to be shown with {@link stacklayout.view.StackLayout#replace} method.
     */
    REPLACE_IN(false, false),

    /**
     * A view is going to be removed with {@link stacklayout.view.StackLayout#replace} method.
     */
    REPLACE_OUT(false, true);

    /**
     * @return true if a view is going to be frozen with this action.
     */
    public boolean isFreeze() {
        return out;
    }

    /**
     * @return true if a view is going to be removed with this action.
     */
    public boolean isExit() {
        return exit;
    }

    private boolean out;
    private boolean exit;

    private ActionType(boolean out, boolean exit) {
        this.out = out;
        this.exit = exit;
    }
}
