package info.android15.stacklayouttests.helper;

import android.os.Parcelable;
import android.test.UiThreadTest;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import info.android15.stacklayouttests.LayoutTestActivity;
import info.android15.stacklayouttests.R;
import info.android15.stacklayouttests.view.BaseActivityTest;
import stacklayout.helper.DefaultParceler;
import stacklayout.helper.DefaultWrappingInflater;

public class TestPopupParceler extends BaseActivityTest<LayoutTestActivity> {

    ViewGroup box;
    DefaultWrappingInflater inflater;
    DefaultParceler parceler;

    public TestPopupParceler() {
        super(LayoutTestActivity.class);
    }

    @Override
    protected void initActivity() {
        box = getActivity().container;
        inflater = new DefaultWrappingInflater(LayoutInflater.from(getActivity()), box);
        parceler = new DefaultParceler(inflater);
    }

    @UiThreadTest
    public void testAll() throws Throwable {
        View view1 = inflater.inflate(R.layout.textview);
        ((TextView)inflater.unwrap(view1)).setText("1");
        Parcelable parcelable = parceler.parcel(view1);
        assertEquals(TextView.class, parceler.getClass(parcelable));
        view1 = parceler.unparcel(parcelable, box);
        assertEquals("1", ((TextView)inflater.unwrap(view1)).getText().toString());
    }
}
