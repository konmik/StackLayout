package info.android15.stacklayouttests.popup;

import android.test.UiThreadTest;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import info.android15.stacklayouttests.LayoutTestActivity;
import info.android15.stacklayouttests.R;
import info.android15.stacklayouttests.TestInflater;
import info.android15.stacklayouttests.TestParceler;
import info.android15.stacklayouttests.view.BaseActivityTest;
import stacklayout.action.ImmediateActionHandler;
import stacklayout.helper.Parceler;
import stacklayout.helper.WrappingInflater;
import stacklayout.requirement.DefaultRequirementsAnalyzer;
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
        Parceler parceler = new TestParceler(inflater);

        stackLayout.setInflater(inflater);
        stackLayout.setParceler(parceler);
        stackLayout.setRequirementsAnalyzer(new DefaultRequirementsAnalyzer());
        stackLayout.setActionHandler(new ImmediateActionHandler());

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
