package info.android15.stacklayouttests.util;

import android.view.LayoutInflater;

import info.android15.stacklayouttests.LayoutTestActivity;
import info.android15.stacklayouttests.R;
import info.android15.stacklayouttests.view.BaseActivityTest;
import info.android15.stacklayouttests.views.ViewFnTestView;

public class ViewFnTest extends BaseActivityTest<LayoutTestActivity> {

    ViewFnTestView testView;

    public ViewFnTest() {
        super(LayoutTestActivity.class);
    }

    @Override
    protected void initActivity() {
        super.initActivity();
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        inflater.inflate(R.layout.view_fn_test, getActivity().container);
        testView = (ViewFnTestView)getActivity().container.getChildAt(0);
    }

    public void testOnMeasured1() throws Throwable {
        waitFor(new Condition() {
            @Override
            public boolean isTrue() {
                return testView.isDrawn;
            }
        });
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                assertTrue(testView.onMeasured1BeforeOnDraw);
            }
        });
    }

    public void testOnMeasured2() throws Throwable {
        waitFor(new Condition() {
            @Override
            public boolean isTrue() {
                return testView.isDrawn;
            }
        });
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                assertTrue(testView.onMeasured1BeforeOnDraw); // layout = bottom
                assertTrue(testView.layoutBeforeOnMeasured2); // check if done layout = bottom
                assertTrue(testView.onMeasured2BeforeOnDraw); // animate from top = 0 to bottom
                assertEquals(0, testView.firstDrawSquareTop); // check if animation has started from top + translationY = 0
            }
        });
    }
}
