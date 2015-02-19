package stacklayout.helper;

import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;

public interface Parceler {
    Parcelable parcel(View view);
    View unparcel(Parcelable parcelable, ViewGroup parent);
    Class getClass(Parcelable parcelable);
}
