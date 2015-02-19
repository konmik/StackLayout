package stacklayout.action;

public enum ActionType {
    PUSH_IN(false, false),
    PUSH_OUT(true, false),
    POP_IN(false, false),
    POP_OUT(true, true),
    REPLACE_IN(false, false),
    REPLACE_OUT(true, true);

    private boolean out;
    private boolean exit;

    ActionType(boolean out, boolean exit) {
        this.out = out;
        this.exit = exit;
    }

    public boolean isOut() {
        return out;
    }

    public boolean isExit() {
        return exit;
    }
}
