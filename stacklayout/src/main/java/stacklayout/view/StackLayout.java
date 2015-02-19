package stacklayout.view;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import stacklayout.action.ActionHandler;
import stacklayout.action.ActionType;
import stacklayout.helper.WrappingInflater;
import stacklayout.requirement.RequirementsAnalyzer;
import stacklayout.util.ArrayFn;

import static stacklayout.util.ArrayFn.filter;
import static stacklayout.util.ArrayFn.join;
import static stacklayout.util.ArrayFn.map;

public class StackLayout extends FreezingLayout implements ViewStack { // TODO: check everywhere if wrapped/unwrapped views are correctly used

    public interface OnExitViewListener {
        void onExitView(View view);
        void onFreezeView(View view);
    }

    private RequirementsAnalyzer requirementsAnalyzer;
    private WrappingInflater inflater;
    private ActionHandler actionHandler;
    private OnExitViewListener onExitViewListener;
    private boolean replaceOnTop;

    boolean inTransaction;
    ArrayList<Pair<ActionType, View>> inAction = new ArrayList<>();
    ArrayList<Pair<ActionType, View>> completed = new ArrayList<>();

    public StackLayout(Context context) {
        super(context);
    }

    public StackLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StackLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setRequirementsAnalyzer(RequirementsAnalyzer requirementsAnalyzer) {
        this.requirementsAnalyzer = requirementsAnalyzer;
    }

    public void setInflater(WrappingInflater inflater) {
        this.inflater = inflater;
    }

    public void setActionHandler(ActionHandler actionHandler) {
        this.actionHandler = actionHandler;
    }

    public void setOnExitViewListener(OnExitViewListener onExitViewListener) {
        this.onExitViewListener = onExitViewListener;
    }

    public void setReplaceOnTop(boolean replaceOnTop) {
        this.replaceOnTop = replaceOnTop;
    }

    @Override
    public <T extends View> T push(int layoutId) {
        inTransaction = true;

        List<View> currentChildren = getPermanentChildren();

        View wrap = inflater.inflate(layoutId);
        addView(wrap);
        actionChild(ActionType.PUSH_IN, wrap);

        int count = requirementsAnalyzer.getRequirementCount(getClasses(currentChildren), inflater.unwrap(wrap).getClass());
        for (int i = currentChildren.size() - count - 1; i >= 0; i--)
            actionChild(ActionType.PUSH_OUT, currentChildren.get(i));

        inTransaction = false;
        onActionEnd();
        return (T)inflater.unwrap(wrap);
    }

    @Override
    public void pop(View top) {
        inTransaction = true;
        View wrap = inflater.getWrap(top);

        List<View> currentChildren = getPermanentChildren();
        if (currentChildren.get(currentChildren.size() - 1) != wrap)
            throw new RuntimeException("Trying to pop a view that is not topmost");

        actionChild(ActionType.POP_OUT, wrap);
        currentChildren.remove(currentChildren.size() - 1);

        if (currentChildren.size() == 0) {
            List<Class> frozenClasses = getFrozenClasses();
            List<Class> stackClasses = join(frozenClasses, getClasses(currentChildren));
            if (stackClasses.size() > 0) {
                int required = requirementsAnalyzer.getRequirementCount(stackClasses, stackClasses.remove(stackClasses.size() - 1));
                for (View wrap1 : unfreezeBottom(required + 1))
                    actionChild(ActionType.POP_IN, wrap1);
            }
        }

        inTransaction = false;
        onActionEnd();
    }

    @Override
    public <T extends View> T replace(int layoutId) {
        inTransaction = true;

        clearFrozen();

        List<View> children = getPermanentChildren();
        Collections.reverse(children);
        for (View wrap : children)
            actionChild(ActionType.REPLACE_OUT, wrap);

        for (Pair<ActionType, View> pair : join(inAction, completed)) {
            if (pair.first == ActionType.PUSH_OUT) {
                completed.remove(pair);
                inAction.remove(pair);
                completed.add(new Pair<>(ActionType.REPLACE_OUT, pair.second));
            }
        }

        View wrap = inflater.inflate(layoutId);
        addView(wrap, replaceOnTop ? getChildCount() : 0);
        actionChild(ActionType.REPLACE_IN, wrap);

        inTransaction = false;
        onActionEnd();

        return (T)inflater.unwrap(wrap);
    }

    @Override
    public <T> T findBackView(View frontView, Class<T> backViewClass) {
        List<View> unwrappedStack = getUnwrappedPermanentChildren();
        for (int i = unwrappedStack.indexOf(frontView) - 1; i >= 0; i--) {
            View view = unwrappedStack.get(i);
            if (backViewClass.isInstance(view))
                return (T)view;
        }
        return null;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        onActionEnd();
    }

    @Override
    protected List<View> getAutoFreezeChildren() {
        return getPermanentChildren();
    }

    private void actionChild(ActionType action, final View wrap) {
        final Pair<ActionType, View> entry = new Pair<>(action, wrap);
        inAction.add(entry);
        actionHandler.onStackAction(action, inflater.unwrap(wrap), new Runnable() {
            @Override
            public void run() {
                if (inAction.contains(entry)) {
                    inAction.remove(entry);
                    completed.add(entry);
                    onActionEnd();
                }
            }
        });
    }

    private void onActionEnd() {
        if (!inTransaction && inAction.size() == 0) {
            exitOnActionsEnd();
            freezeOnActionsEnd();
        }
    }

    private void exitOnActionsEnd() {
        for (int p = completed.size() - 1; p >= 0; p--) {
            Pair<ActionType, View> pair = completed.get(p);
            if (pair.first.isExit()) {
                completed.remove(p);
                removeView(pair.second);
                if (onExitViewListener != null)
                    onExitViewListener.onExitView(inflater.unwrap(pair.second));
            }
        }
        completed.clear();
    }

    private void freezeOnActionsEnd() {
        List<View> wraps = getPermanentChildren();
        List<Class> classes = getClasses(wraps);
        if (classes.size() > 0) {
            int required = requirementsAnalyzer.getRequirementCount(classes, classes.remove(classes.size() - 1));
            int freeze = classes.size() - required;
            freezeBottom(freeze);
            if (onExitViewListener != null) {
                for (int w = freeze - 1; w >= 0; w++)
                    onExitViewListener.onFreezeView(inflater.unwrap(wraps.get(w)));
            }
        }
    }

    private List<View> getChildren() {
        List<View> children = new ArrayList<>();
        for (int i = 0, size = getChildCount(); i < size; i++)
            children.add(getChildAt(i));
        return children;
    }

    private List<View> getPermanentChildren() {
        final List<View> out = map(filter(join(inAction, completed), new ArrayFn.Predicate<Pair<ActionType, View>>() {
            @Override
            public boolean apply(Pair<ActionType, View> pair) {
                return pair.first.isOut();
            }
        }), new ArrayFn.Converter<Pair<ActionType, View>, View>() {
            @Override
            public View convert(Pair<ActionType, View> pair) {
                return pair.second;
            }
        });
        return filter(getChildren(), new ArrayFn.Predicate<View>() {
            @Override
            public boolean apply(View wrap) {
                return !out.contains(wrap);
            }
        });
    }

    private ArrayList<View> getUnwrappedPermanentChildren() {
        return map(getPermanentChildren(), new ArrayFn.Converter<View, View>() {
            @Override
            public View convert(View wrap) {
                return inflater.unwrap(wrap);
            }
        });
    }

    private List<Class> getClasses(List<View> wraps) {
        return map(wraps, new ArrayFn.Converter<View, Class>() {
            @Override
            public Class convert(View wrap) {
                return inflater.unwrap(wrap).getClass();
            }
        });
    }
}
