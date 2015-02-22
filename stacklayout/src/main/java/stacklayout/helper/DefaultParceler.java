package stacklayout.helper;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

public class DefaultParceler implements Parceler {

    private static final String STATE_KEY = "state";
    private static final String LAYOUT_ID_KEY = "layout_id";
    private static final String CLASS_KEY = "class";

    private WrappingInflater inflater;

    public DefaultParceler(WrappingInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    public Parcelable parcel(View container) {
        View view = inflater.unwrap(container);
        SparseArray<Parcelable> states = new SparseArray<>();
        view.saveHierarchyState(states);
        Bundle bundle = new Bundle();
        bundle.putSparseParcelableArray(STATE_KEY, states);
        bundle.putInt(LAYOUT_ID_KEY, inflater.getLayoutId(container));
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
