package stacklayout.popup;

import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import info.android15.stacklayout.test.R;
import stacklayout.BaseActivityTest;
import stacklayout.action.ImmediateActionHandler;
import stacklayout.helper.WrappingInflater;
import stacklayout.requirement.DefaultRequirementsAnalyzer;
import stacklayout.test.LayoutTestActivity;
import stacklayout.view.StackLayout;

public class AnchoredViewTest extends BaseActivityTest<LayoutTestActivity> {

    ViewGroup container;
    StackLayout stackLayout;
    TestAnchoredView testView;
    View anchor;
    View content;
    View decorView;

    public AnchoredViewTest() {
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
    }

    private void layoutTest(final int anchorCornerGravity, final int contentCornerGravity, final Runnable test) throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                testView.setAnchorCornerGravity(anchorCornerGravity);
                testView.setContentCornerGravity(contentCornerGravity);
                testView.contentRect = null;
            }
        });
        waitFor(new Condition() {
            @Override
            public boolean isTrue() {
                return testView.contentRect != null;
            }
        });
        runTestOnUiThread(test);
    }

    public void testLayout() throws Throwable {
//        final AtomicInteger counter = new AtomicInteger();

        for (int decor = 0; decor < 2; decor++) {

            final int finalDecor = decor;

            for (int iteration = 0; iteration < 2; iteration++) {

                final int size = iteration == 0 ? FrameLayout.LayoutParams.MATCH_PARENT : (int)(getActivity().getResources().getDisplayMetrics().density * 50);

                runTestOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TestAnchorProvider anchorProvider = stackLayout.push(R.layout.anchor_provider);
                        testView = stackLayout.push(finalDecor == 0 ? R.layout.anchored_view : R.layout.anchored_view_with_decor);
                        anchor = anchorProvider.getAnchor(testView);

                        content = testView.getContentView();
                        content.getLayoutParams().width = content.getLayoutParams().height = size;

                        decorView = testView.getDecorView();
                        if (decorView != null) {
                            if (size == FrameLayout.LayoutParams.MATCH_PARENT)
                                decorView.getLayoutParams().width = decorView.getLayoutParams().height = size;
                            else
                                decorView.getLayoutParams().width = decorView.getLayoutParams().height = FrameLayout.LayoutParams.WRAP_CONTENT;
                        }
                    }
                });

                int[] corners = {
                    Gravity.TOP | Gravity.LEFT,
                    Gravity.TOP | Gravity.RIGHT,
                    Gravity.BOTTOM | Gravity.RIGHT,
                    Gravity.BOTTOM | Gravity.LEFT
                };

                for (final int anchorGravity : corners) {
                    for (final int contentGravity : corners) {
//                        Log.d("TEST_PASS", "" + counter.incrementAndGet());
                        layoutTest(anchorGravity, contentGravity, new Runnable() {
                            @Override
                            public void run() {

                                Rect anchorRect = getRect(anchor);
                                Rect contentRect = getRect(content);

                                boolean anchorLeft = (anchorGravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.LEFT;
                                int anchorX = anchorLeft ? anchorRect.left : anchorRect.right;
                                boolean anchorTop = (anchorGravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.TOP;
                                int anchorY = anchorTop ? anchorRect.top : anchorRect.bottom;
                                boolean contentLeft = (contentGravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.LEFT;
                                int contentX = contentLeft ? contentRect.left : contentRect.right;
                                boolean contentTop = (contentGravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.TOP;
                                int contentY = contentTop ? contentRect.top : contentRect.bottom;

                                assertEquals(anchorX, contentX);
                                assertEquals(anchorY, contentY);

                                if (size == FrameLayout.LayoutParams.MATCH_PARENT) {
                                    assertEquals(testView.getWidth(), finalDecor == 1 ? decorView.getWidth() : content.getWidth());
                                    assertEquals(testView.getHeight(), finalDecor == 1 ? decorView.getHeight() : content.getHeight());
                                }
                                else {
                                    assertEquals(size, content.getWidth());
                                    assertEquals(size, content.getHeight());
                                }
                            }
                        });
                    }
                }
            }
        }
    }

    private static Rect getRect(View view) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        return rect;
    }
}
