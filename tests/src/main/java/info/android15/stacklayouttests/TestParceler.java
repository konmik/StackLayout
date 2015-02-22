package info.android15.stacklayouttests;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import stacklayout.helper.WrappingInflater;
import stacklayout.helper.Parceler;

public class TestParceler implements Parceler {

    public static final String STATE_KEY = "state";
    public static final String LAYOUT_ID_KEY = "layout_id";
    public static final String CLASS_KEY = "class";

    private WrappingInflater inflater;

    public TestParceler(WrappingInflater inflater) {
        this.inflater = inflater;
    }

    public static Class extractClass(Parcelable parcelable) {
        Bundle bundle = (Bundle)parcelable;
        return (Class)bundle.getSerializable(CLASS_KEY);
    }

    @Override
    public Parcelable parcel(View view) {
        SparseArray<Parcelable> container = new SparseArray<>();
        view.saveHierarchyState(container);
        Bundle bundle = new Bundle();
        bundle.putSparseParcelableArray(STATE_KEY, container);
        bundle.putInt(LAYOUT_ID_KEY, (int)view.getTag(R.id.view_layout_id));
        bundle.putSerializable(CLASS_KEY, view.getClass());
        return bundle;
    }

    @Override
    public View unparcel(Parcelable parcelable) {
        Bundle bundle = (Bundle)parcelable;
        View view = inflater.inflate(bundle.getInt(LAYOUT_ID_KEY));
        view.restoreHierarchyState(bundle.getSparseParcelableArray(STATE_KEY));
        return view;
    }

    @Override
    public Class getClass(Parcelable parcelable) {
        return (Class)((Bundle)parcelable).getSerializable(CLASS_KEY);
    }
}
