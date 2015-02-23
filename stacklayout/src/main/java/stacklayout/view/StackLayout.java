package stacklayout.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Pair;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import stacklayout.action.ActionHandler;
import stacklayout.action.ActionType;
import stacklayout.action.ImmediateActionHandler;
import stacklayout.helper.DefaultFreezer;
import stacklayout.helper.DefaultWrappingInflater;
import stacklayout.helper.Freezer;
import stacklayout.helper.WrappingInflater;
import stacklayout.requirement.DefaultRequirementsAnalyzer;
import stacklayout.requirement.RequirementsAnalyzer;
import stacklayout.util.ArrayFn;

import static stacklayout.util.ArrayFn.filter;
import static stacklayout.util.ArrayFn.filterOut;
import static stacklayout.util.ArrayFn.join;
import static stacklayout.util.ArrayFn.map;
import static stacklayout.util.ViewFn.getChildren;

public class StackLayout extends FrameLayout implements ViewStack {

    private static final String FREEZER_KEY = "freezer";
    private static final String PARENT_KEY = "parent";

    public interface OnDestroyViewListener {
        void onExitView(View view);
        void onFreezeView(View view);
    }

    private Freezer freezer;
    private RequirementsAnalyzer analyzer;
    private WrappingInflater inflater;
    private ActionHandler actionHandler;
    private OnDestroyViewListener onDestroyViewListener;
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

    public class SettingsEditor {
        private Freezer freezer;
        private WrappingInflater inflater;
        private RequirementsAnalyzer requirementsAnalyzer;
        private ActionHandler actionHandler;
        private OnDestroyViewListener onDestroyViewListener;
        private boolean replaceOnTop;

        public SettingsEditor setFreezer(Freezer freezer) {
            this.freezer = freezer;
            return this;
        }

        public SettingsEditor setInflater(WrappingInflater inflater) {
            this.inflater = inflater;
            return this;
        }

        public SettingsEditor setRequirementsAnalyzer(RequirementsAnalyzer requirementsAnalyzer) {
            this.requirementsAnalyzer = requirementsAnalyzer;
            return this;
        }

        public SettingsEditor setActionHandler(ActionHandler actionHandler) {
            this.actionHandler = actionHandler;
            return this;
        }

        public SettingsEditor setOnDestroyViewListener(OnDestroyViewListener onDestroyViewListener) {
            this.onDestroyViewListener = onDestroyViewListener;
            return this;
        }

        public SettingsEditor setReplaceOnTop(boolean replaceOnTop) {
            this.replaceOnTop = replaceOnTop;
            return this;
        }

        public void apply() {
            StackLayout.this.apply(this);
        }
    }

    private void apply(SettingsEditor editor) {
        inflater = editor.inflater != null ? editor.inflater : new DefaultWrappingInflater(LayoutInflater.from(getContext()), StackLayout.this);
        analyzer = editor.requirementsAnalyzer != null ? editor.requirementsAnalyzer : new DefaultRequirementsAnalyzer();

        freezer = editor.freezer != null ? editor.freezer : new DefaultFreezer(inflater, analyzer);

        actionHandler = editor.actionHandler != null ? editor.actionHandler : new ImmediateActionHandler();
        onDestroyViewListener = editor.onDestroyViewListener;
        replaceOnTop = editor.replaceOnTop;
    }

    public SettingsEditor set() {
        return new SettingsEditor();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putBundle(FREEZER_KEY, freezer.save(getNotExitChildren()));
        bundle.putParcelable(PARENT_KEY, super.onSaveInstanceState());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle)state;
        super.onRestoreInstanceState(bundle.getParcelable(PARENT_KEY));
        int viewIndex = 0;
        for (View view : freezer.restore(bundle.getBundle(FREEZER_KEY)))
            addView(view, viewIndex++);
        onActionEnd();
    }

    @Override
    protected void dispatchSaveInstanceState(@NonNull SparseArray<Parcelable> container) {
        container.put(getId(), onSaveInstanceState());
    }

    @Override
    protected void dispatchRestoreInstanceState(@NonNull SparseArray<Parcelable> container) {
        onRestoreInstanceState(container.get(getId()));
    }

    @Override
    public <T extends View> T push(int layoutId) {
        inTransaction = true;

        List<View> currentChildren = getPermanentChildren();

        View wrap = inflater.inflate(layoutId);
        addView(wrap);
        actionChild(ActionType.PUSH_IN, wrap);

        int count = analyzer.getRequirementCount(getClasses(currentChildren), inflater.unwrap(wrap).getClass());
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

        if (currentChildren.size() == 1) {
            int viewIndex = 0;
            for (View wrap1 : freezer.unfreezeBottom()) {
                addView(wrap1, viewIndex++);
                actionChild(ActionType.POP_IN, wrap1);
            }
        }

        inTransaction = false;
        onActionEnd();
    }

    @Override
    public <T extends View> T replace(int layoutId) {
        inTransaction = true;

        freezer.clear();

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
    public boolean back() {
        List<View> children = getPermanentChildren();
        if (freezer.size() + children.size() <= 1)
            return false;
        pop(inflater.unwrap(children.get(children.size() - 1)));
        return true;
    }

    @Override
    public <T> T findBackView(View frontView, Class<T> backViewClass) {
        List<View> unwrappedStack = map(getPermanentChildren(), new ArrayFn.Converter<View, View>() {
            @Override
            public View convert(View wrap) {
                return inflater.unwrap(wrap);
            }
        });
        for (int i = unwrappedStack.indexOf(frontView) - 1; i >= 0; i--) {
            View view = unwrappedStack.get(i);
            if (backViewClass.isInstance(view))
                return (T)view;
        }
        throw new RuntimeException("Trying to find a view that does not exist");
    }

    public int getFrozenCount() {
        return freezer.size();
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
            destroyOnActionsEnd();
            freezeOnActionsEnd();
        }
    }

    private void destroyOnActionsEnd() {
        for (int p = completed.size() - 1; p >= 0; p--) {
            Pair<ActionType, View> pair = completed.get(p);
            if (pair.first.isExit()) {
                completed.remove(p);
                removeView(pair.second);
                if (onDestroyViewListener != null)
                    onDestroyViewListener.onExitView(inflater.unwrap(pair.second));
            }
        }
        completed.clear();
    }

    private void freezeOnActionsEnd() {
        List<View> wraps = getPermanentChildren();
        if (wraps.size() > 0) {
            for (View wrap : freezer.freezeBottom(wraps)) {
                removeView(wrap);
                if (onDestroyViewListener != null)
                    onDestroyViewListener.onFreezeView(inflater.unwrap(wrap));
            }
        }
    }

    /**
     * @return A list of children that are not going to exit
     */
    private List<View> getNotExitChildren() {
        return filterOut(getChildren(this), getDynamicViews(new ArrayFn.Predicate<Pair<ActionType, View>>() {
            @Override
            public boolean apply(Pair<ActionType, View> pair) { return pair.first.isExit(); }
        }));
    }

    /**
     * @return A list of children that are not going to freeze of exit
     */
    private List<View> getPermanentChildren() {
        return filterOut(getChildren(this), getDynamicViews(new ArrayFn.Predicate<Pair<ActionType, View>>() {
            @Override
            public boolean apply(Pair<ActionType, View> pair) { return pair.first.isFreeze() || pair.first.isExit(); }
        }));
    }

    private List<View> getDynamicViews(ArrayFn.Predicate<Pair<ActionType, View>> predicate) {
        return map(filter(join(inAction, completed), predicate), new ArrayFn.Converter<Pair<ActionType, View>, View>() {
            @Override
            public View convert(Pair<ActionType, View> pair) { return pair.second; }
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
