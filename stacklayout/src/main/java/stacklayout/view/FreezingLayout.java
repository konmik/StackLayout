package stacklayout.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import stacklayout.util.ArrayFn;
import stacklayout.helper.Parceler;

import static stacklayout.util.ArrayFn.map;

public class FreezingLayout extends FrameLayout {

    private static final String FREEZER_KEY = "freezer";
    private static final String PARENT_KEY = "parent";
    private static final String CHILDREN_KEY = "children";

    private Parceler parceler;
    private ArrayList<Parcelable> freezer = new ArrayList<>();

    public FreezingLayout(Context context) {
        super(context);
    }

    public FreezingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FreezingLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setParceler(Parceler parceler) {
        this.parceler = parceler;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARENT_KEY, super.onSaveInstanceState());
        bundle.putParcelableArrayList(FREEZER_KEY, new ArrayList<>(freezer));
        ArrayList<Parcelable> children = new ArrayList<>(getChildCount());
        for (View view : getAutoFreezeChildren())
            children.add(parceler.parcel(view));
        bundle.putParcelableArrayList(CHILDREN_KEY, children);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle)state;
        super.onRestoreInstanceState(bundle.getParcelable(PARENT_KEY));
        freezer = bundle.getParcelableArrayList(FREEZER_KEY);
        for (Parcelable child : bundle.getParcelableArrayList(CHILDREN_KEY))
            addView(parceler.unparcel(child, this));
    }

    @Override
    protected void dispatchSaveInstanceState(@NonNull SparseArray<Parcelable> container) {
        container.put(getId(), onSaveInstanceState());
    }

    @Override
    protected void dispatchRestoreInstanceState(@NonNull SparseArray<Parcelable> container) {
        onRestoreInstanceState(container.get(getId()));
    }

    protected List<View> getAutoFreezeChildren() {
        ArrayList<View> children = new ArrayList<>(getChildCount());
        for (int i = 0; i < getChildCount(); i++)
            children.add(getChildAt(i));
        return children;
    }

    public int getFrozenCount() {
        return freezer.size();
    }

    public Parcelable getFrozen(int index) {
        return freezer.get(index);
    }

    public void clearFrozen() {
        freezer.clear();
    }

    public void freeze(int freezerIndex, int viewIndex) {
        View view = getChildAt(viewIndex);
        freezer.add(freezerIndex, parceler.parcel(view));
        removeView(view);
    }

    public View unfreeze(int freezerIndex, int viewIndex) {
        View view = parceler.unparcel(freezer.remove(freezerIndex), this);
        addView(view, viewIndex);
        return view;
    }

    public void freezeBottom(int count) {
        int freezerIndex = getFrozenCount();
        while (count-- > 0)
            freeze(freezerIndex, count);
    }

    public List<View> unfreezeBottom(int count) {
        ArrayList<View> result = new ArrayList<>();
        int viewIndex = 0;
        int freezerIndex = getFrozenCount() - count;
        while (count-- > 0)
            result.add(unfreeze(freezerIndex, viewIndex++));
        return result;
    }

    public List<Class> getFrozenClasses() {
        return map(freezer, new ArrayFn.Converter<Parcelable, Class>() {
            @Override
            public Class convert(Parcelable parcelable) {
                return parceler.getClass(parcelable);
            }
        });
    }
}
