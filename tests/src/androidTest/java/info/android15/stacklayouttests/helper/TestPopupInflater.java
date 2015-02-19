package info.android15.stacklayouttests.helper;

import android.test.UiThreadTest;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import info.android15.stacklayouttests.LayoutTestActivity;
import info.android15.stacklayouttests.R;
import info.android15.stacklayouttests.view.BaseActivityTest;
import info.android15.stacklayouttests.views.View1;
import stacklayout.helper.DefaultWrappingInflater;

public class TestPopupInflater extends BaseActivityTest<LayoutTestActivity> {
    public TestPopupInflater() {
        super(LayoutTestActivity.class);
    }

    ViewGroup container;

    @Override
    protected void initActivity() {
        container = getActivity().container;
    }

    @UiThreadTest
    public void testAll() throws Throwable {
        DefaultWrappingInflater inflater = new DefaultWrappingInflater(LayoutInflater.from(getActivity()), container);
        ViewGroup c = (ViewGroup)inflater.inflate(R.layout.view1);
        assertEquals(View1.class, c.getChildAt(0).getClass());
        assertEquals(View1.class, inflater.unwrap(c).getClass());
        assertEquals(R.layout.view1, inflater.getLayoutId(c));

        container.setPadding(1, 2, 3, 4);
        ViewGroup npc = (ViewGroup)inflater.inflate(R.layout.no_padding_view1);
        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams)npc.getLayoutParams();
        assertEquals(-1, mlp.leftMargin);
        assertEquals(-2, mlp.topMargin);
        assertEquals(-3, mlp.rightMargin);
        assertEquals(-4, mlp.bottomMargin);

        c = (ViewGroup)inflater.inflate(R.layout.view1);
        mlp = (ViewGroup.MarginLayoutParams)c.getLayoutParams();
        assertEquals(0, mlp.leftMargin);
        assertEquals(0, mlp.topMargin);
        assertEquals(0, mlp.rightMargin);
        assertEquals(0, mlp.bottomMargin);
    }
}
