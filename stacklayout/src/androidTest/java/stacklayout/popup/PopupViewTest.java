package stacklayout.popup;

import android.test.UiThreadTest;
import android.view.ViewGroup;

import info.android15.stacklayout.test.R;
import stacklayout.BaseActivityTest;
import stacklayout.action.ImmediateActionHandler;
import stacklayout.helper.WrappingInflater;
import stacklayout.requirement.DefaultRequirementsAnalyzer;
import stacklayout.test.LayoutTestActivity;
import stacklayout.view.StackLayout;

public class PopupViewTest extends BaseActivityTest<LayoutTestActivity> {

    ViewGroup container;
    TestPopupView testPopupView;
    StackLayout stackLayout;

    public PopupViewTest() {
        super(LayoutTestActivity.class);
    }

    @Override
    protected void initActivity() {
        super.initActivity();
        container = getActivity().container;
        container.addView(stackLayout = new StackLayout(getActivity()));

        WrappingInflater inflater = new TestInflater(getActivity().getLayoutInflater());

        stackLayout.set()
            .setInflater(inflater)
            .setRequirementsAnalyzer(new DefaultRequirementsAnalyzer())
            .setActionHandler(new ImmediateActionHandler())
            .apply();

        stackLayout.push(R.layout.view1);
        testPopupView = stackLayout.push(R.layout.view_popup);
    }

    @UiThreadTest
    public void testInit() {
        assertNotNull(testPopupView.getDimView());
        assertNotNull(testPopupView.getDecorView());
        assertNotNull(testPopupView.getContentView());
    }
}
