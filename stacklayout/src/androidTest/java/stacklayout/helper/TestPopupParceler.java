package stacklayout.helper;

import android.os.Parcelable;
import android.test.UiThreadTest;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import info.android15.stacklayout.test.R;
import stacklayout.BaseActivityTest;
import stacklayout.test.LayoutTestActivity;

public class TestPopupParceler extends BaseActivityTest<LayoutTestActivity> {

    ViewGroup box;
    DefaultWrappingInflater inflater;
    Parceler parceler;

    public TestPopupParceler() {
        super(LayoutTestActivity.class);
    }

    @Override
    protected void initActivity() {
        box = getActivity().container;
        inflater = new DefaultWrappingInflater(LayoutInflater.from(getActivity()), box);
        parceler = new Parceler(inflater);
    }

    @UiThreadTest
    public void testAll() throws Throwable {
        View view1 = inflater.inflate(R.layout.textview);
        ((TextView)inflater.unwrap(view1)).setText("1");
        Parcelable parcelable = parceler.parcel(view1);
        assertEquals(TextView.class, parceler.getClass(parcelable));
        view1 = parceler.unparcel(parcelable);
        assertEquals("1", ((TextView)inflater.unwrap(view1)).getText().toString());
    }
}
