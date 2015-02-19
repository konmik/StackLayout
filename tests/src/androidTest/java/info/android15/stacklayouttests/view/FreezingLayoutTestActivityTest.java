package info.android15.stacklayouttests.view;

import android.test.UiThreadTest;
import android.util.Log;
import android.widget.TextView;

import info.android15.stacklayouttests.LayoutTestActivity;
import info.android15.stacklayouttests.R;
import info.android15.stacklayouttests.TestInflater;
import info.android15.stacklayouttests.TestParceler;
import info.android15.stacklayouttests.views.View1;
import info.android15.stacklayouttests.views.View2;
import info.android15.stacklayouttests.views.View3;
import info.android15.stacklayouttests.views.View4;
import stacklayout.view.FreezingLayout;
import stacklayout.helper.WrappingInflater;
import stacklayout.helper.Parceler;

public class FreezingLayoutTestActivityTest extends BaseActivityTest<LayoutTestActivity> {

    FreezingLayout layout;
    WrappingInflater inflater;
    Parceler parceler;

    public FreezingLayoutTestActivityTest() {
        super(LayoutTestActivity.class);
    }

    @Override
    protected void initActivity() {
        inflater = new TestInflater(getActivity().getLayoutInflater());
        parceler = new TestParceler(inflater);
        layout = new FreezingLayout(getActivity());
        layout.setParceler(parceler);
        getActivity().container.addView(layout);
        layout.addView(inflater.inflate(R.layout.textview));
        ((TextView)layout.getChildAt(0)).setText("1");
        layout.freeze(0, 0);
        layout.addView(inflater.inflate(R.layout.textview));
        ((TextView)layout.getChildAt(0)).setText("2");
    }

    public void testSaveRestore() throws Throwable {
        Log.i(LayoutTestActivity.class.getSimpleName(), "testSaveRestore");
        restartActivity();
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(LayoutTestActivity.class.getSimpleName(), "testSaveRestore-runTestOnUiThread");
                assertEquals("2", ((TextView)layout.getChildAt(0)).getText().toString());
                layout.unfreeze(0, 0);
                assertEquals("1", ((TextView)layout.getChildAt(0)).getText().toString());
            }
        });
    }

    @UiThreadTest
    public void testCounters() throws Throwable {
        Log.i(LayoutTestActivity.class.getSimpleName(), "testCounters");
        assertEquals(1, layout.getFrozenCount());
        assertEquals(1, layout.getChildCount());
        layout.freeze(layout.getFrozenCount(), 0);
        assertEquals(2, layout.getFrozenCount());
        assertEquals(0, layout.getChildCount());
        layout.unfreeze(layout.getFrozenCount() - 1, 0);
        layout.unfreeze(layout.getFrozenCount() - 1, 0);
        assertEquals(0, layout.getFrozenCount());
        assertEquals(2, layout.getChildCount());
        layout.freeze(0, 0);
        layout.clearFrozen();
        layout.removeAllViews();
        assertEquals(0, layout.getFrozenCount());
        assertEquals(0, layout.getChildCount());
    }

    @UiThreadTest
    public void testFrozen() throws Throwable {
        assertEquals(TextView.class, TestParceler.extractClass(layout.getFrozen(0)));
    }

    @UiThreadTest
    public void testFrozenCounter() {
        assertEquals(1, layout.getFrozenCount());
        assertEquals(1, layout.getChildCount());
        layout.clearFrozen();
        assertEquals(0, layout.getFrozenCount());
        assertEquals(1, layout.getChildCount());
    }

    @UiThreadTest
    public void testPushPopChunk() {
        layout.clearFrozen();
        layout.removeAllViews();
        layout.addView(inflater.inflate(R.layout.view1));
        layout.addView(inflater.inflate(R.layout.view2));
        layout.addView(inflater.inflate(R.layout.view3));
        layout.addView(inflater.inflate(R.layout.view4));
        assertEquals(0, layout.getFrozenCount());
        assertEquals(4, layout.getChildCount());
        layout.freezeBottom(2);
        assertEquals(2, layout.getFrozenCount());
        assertEquals(2, layout.getChildCount());
        assertEquals(View1.class, layout.getFrozenClasses().get(0));
        assertEquals(View2.class, layout.getFrozenClasses().get(1));
        layout.freezeBottom(2);
        assertEquals(View1.class, layout.getFrozenClasses().get(0));
        assertEquals(View2.class, layout.getFrozenClasses().get(1));
        assertEquals(View3.class, layout.getFrozenClasses().get(2));
        assertEquals(View4.class, layout.getFrozenClasses().get(3));
        layout.unfreezeBottom(3);
        assertEquals(View1.class, layout.getFrozenClasses().get(0));
        assertEquals(View2.class, inflater.unwrap(layout.getChildAt(0)).getClass());
        assertEquals(View3.class, inflater.unwrap(layout.getChildAt(1)).getClass());
        assertEquals(View4.class, inflater.unwrap(layout.getChildAt(2)).getClass());
        layout.unfreezeBottom(1);
        assertEquals(View1.class, inflater.unwrap(layout.getChildAt(0)).getClass());
        assertEquals(0, layout.getFrozenCount());
        assertEquals(4, layout.getChildCount());
    }
}
