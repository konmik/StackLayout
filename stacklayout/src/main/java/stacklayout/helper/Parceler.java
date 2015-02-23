package stacklayout.helper;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;

/**
 * This is a helper class that is used by {@link stacklayout.helper.DefaultFreezer} internally.
 */
class Parceler {

    private static final String STATE_KEY = "state";
    private static final String LAYOUT_ID_KEY = "layout_id";
    private static final String CLASS_KEY = "class";

    private WrappingInflater inflater;

    public Parceler(WrappingInflater inflater) {
        this.inflater = inflater;
    }

    public Parcelable parcel(View wrap) {
        SparseArray<Parcelable> states = new SparseArray<>();
        wrap.saveHierarchyState(states);
        Bundle bundle = new Bundle();
        bundle.putSparseParcelableArray(STATE_KEY, states);
        bundle.putInt(LAYOUT_ID_KEY, inflater.getLayoutId(wrap));
        bundle.putSerializable(CLASS_KEY, inflater.unwrap(wrap).getClass());
        return bundle;
    }

    public View unparcel(Parcelable parcelable) {
        Bundle bundle = (Bundle)parcelable;
        View wrap = inflater.inflate(bundle.getInt(LAYOUT_ID_KEY));
        wrap.restoreHierarchyState(bundle.getSparseParcelableArray(STATE_KEY));
        return wrap;
    }

    public Class getClass(Parcelable parcelable) {
        return (Class)((Bundle)parcelable).getSerializable(CLASS_KEY);
    }
}
