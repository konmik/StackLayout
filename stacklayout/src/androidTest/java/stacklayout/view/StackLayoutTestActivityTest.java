package stacklayout.view;

import android.test.UiThreadTest;
import android.util.Pair;
import android.view.View;

import java.util.ArrayList;

import info.android15.stacklayout.test.R;
import stacklayout.BaseActivityTest;
import stacklayout.action.ActionHandler;
import stacklayout.action.ActionType;
import stacklayout.action.ImmediateActionHandler;
import stacklayout.helper.DefaultWrappingInflater;
import stacklayout.helper.WrappingInflater;
import stacklayout.popup.TestInflater;
import stacklayout.requirement.DefaultRequirementsAnalyzer;
import stacklayout.test.LayoutTestActivity;
import stacklayout.test.View1;

public class StackLayoutTestActivityTest extends BaseActivityTest<LayoutTestActivity> {

    private static final int ITERATIONS_COUNT = 2;
    volatile StackLayout layout;
    volatile WrappingInflater inflater;
    final View[] exit = new View[1];

    ActionHandler delayedHandler = new ActionHandler() {
        @Override
        public void onStackAction(ActionType action, View view, Runnable onActionEnd) {
            view.postDelayed(onActionEnd, 50);
        }
    };

    public StackLayoutTestActivityTest() {
        super(LayoutTestActivity.class);
    }

    int iteration;
    ActionHandler actionHandler;

    @Override
    protected void initActivity() {
        getActivity();
        initLayout();
    }

    private void initLayout() {
        if (iteration == 0) {
            inflater = new TestInflater(getActivity().getLayoutInflater());
        }
        else {
            inflater = new DefaultWrappingInflater(getActivity().getLayoutInflater(), getActivity().container);
        }

        layout = new StackLayout(getActivity());
        layout.set()
            .setInflater(inflater)
            .setRequirementsAnalyzer(new DefaultRequirementsAnalyzer())
            .setActionHandler(actionHandler != null ? actionHandler : new ImmediateActionHandler())
            .setOnDestroyViewListener(new StackLayout.OnDestroyViewListener() {
                @Override
                public void onExitView(View view) {
                    exit[0] = view;
                }

                @Override
                public void onFreezeView(View view) {
                }
            })
            .apply();

        getActivity().container.removeAllViews();
        getActivity().container.addView(layout);
    }

    private void push1121() {
        assertTrue(layout.push(R.layout.view1) instanceof View1);
        assertEquals(0, layout.getFrozenCount());
        assertEquals(1, layout.getChildCount());

        layout.push(R.layout.view1);
        assertEquals(1, layout.getFrozenCount());
        assertEquals(1, layout.getChildCount());

        layout.push(R.layout.view2);
        assertEquals(1, layout.getFrozenCount());
        assertEquals(2, layout.getChildCount());

        layout.push(R.layout.view1);
        assertEquals(3, layout.getFrozenCount());
        assertEquals(1, layout.getChildCount());
    }

    public void testPush() throws Throwable {
        for (iteration = 0; iteration < ITERATIONS_COUNT; iteration++) {
            runTestOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initLayout();
                    push1121();
                }
            });
            restartActivity();
            runTestOnUiThread(new Runnable() {
                @Override
                public void run() {
                    assertEquals(3, layout.getFrozenCount());
                    assertEquals(1, layout.getChildCount());
                }
            });
        }
    }

    @UiThreadTest
    public void testPop() throws Throwable {
        for (iteration = 0; iteration < ITERATIONS_COUNT; iteration++) {
            initLayout();
            push1121();

            layout.pop(inflater.unwrap(layout.getChildAt(layout.getChildCount() - 1)));
            assertEquals(1, layout.getFrozenCount());
            assertEquals(2, layout.getChildCount());

            layout.pop(inflater.unwrap(layout.getChildAt(layout.getChildCount() - 1)));
            assertEquals(1, layout.getFrozenCount());
            assertEquals(1, layout.getChildCount());

            layout.pop(inflater.unwrap(layout.getChildAt(layout.getChildCount() - 1)));
            assertEquals(0, layout.getFrozenCount());
            assertEquals(1, layout.getChildCount());

            layout.pop(inflater.unwrap(layout.getChildAt(layout.getChildCount() - 1)));
            assertEquals(0, layout.getFrozenCount());
            assertEquals(0, layout.getChildCount());
        }
    }

    @UiThreadTest
    public void testReplace() throws Throwable {
        for (iteration = 0; iteration < ITERATIONS_COUNT; iteration++) {
            initLayout();
            push1121();

            layout.pop(inflater.unwrap(layout.getChildAt(layout.getChildCount() - 1)));
            assertEquals(1, layout.getFrozenCount());
            assertEquals(2, layout.getChildCount());

            assertTrue(layout.replace(R.layout.view1) instanceof View1);
            assertEquals(0, layout.getFrozenCount());
            assertEquals(1, layout.getChildCount());
        }
    }

    public void testDelayedPush() throws Throwable {
        for (iteration = 0; iteration < ITERATIONS_COUNT; iteration++) {
            runTestOnUiThread(new Runnable() {
                @Override
                public void run() {
                    actionHandler = delayedHandler;
                    initLayout();
                    layout.push(R.layout.view1);
                    layout.push(R.layout.view1);
                    layout.push(R.layout.view2);
                    layout.push(R.layout.view1);
                    assertEquals(0, layout.getFrozenCount());
                    assertEquals(4, layout.getChildCount());
                }
            });
            sleep(100);
            runTestOnUiThread(new Runnable() {
                @Override
                public void run() {
                    assertEquals(3, layout.getFrozenCount());
                    assertEquals(1, layout.getChildCount());
                }
            });
        }
    }

    public void testDelayedPop() throws Throwable {
        for (iteration = 0; iteration < ITERATIONS_COUNT; iteration++) {
            runTestOnUiThread(new Runnable() {
                @Override
                public void run() {
                    actionHandler = delayedHandler;
                    initLayout();
                    layout.push(R.layout.view1);
                    layout.push(R.layout.view1);
                    layout.push(R.layout.view2);
                    View top = layout.push(R.layout.view1);
                    assertEquals(0, layout.getFrozenCount());
                    assertEquals(4, layout.getChildCount());
                    layout.pop(top);
                    assertEquals(0, layout.getFrozenCount());
                    assertEquals(4, layout.getChildCount());
                }
            });
            sleep(100);
            runTestOnUiThread(new Runnable() {
                @Override
                public void run() {
                    assertEquals(1, layout.getFrozenCount());
                    assertEquals(2, layout.getChildCount());
                }
            });
        }
    }

    public void testDelayedReplace() throws Throwable {
        for (iteration = 0; iteration < ITERATIONS_COUNT; iteration++) {
            runTestOnUiThread(new Runnable() {
                @Override
                public void run() {
                    actionHandler = delayedHandler;
                    initLayout();
                    layout.push(R.layout.view1);
                    layout.push(R.layout.view1);
                    layout.push(R.layout.view2);
                    View top = layout.push(R.layout.view1);
                    assertEquals(0, layout.getFrozenCount());
                    assertEquals(4, layout.getChildCount());
                    layout.pop(top);
                    assertEquals(0, layout.getFrozenCount());
                    assertEquals(4, layout.getChildCount());
                    layout.replace(R.layout.view1);
                    assertEquals(0, layout.getFrozenCount());
                    assertEquals(5, layout.getChildCount());
                }
            });
            sleep(1000);
            runTestOnUiThread(new Runnable() {
                @Override
                public void run() {
                    assertEquals(0, layout.getFrozenCount());
                    assertEquals(1, layout.getChildCount());
                }
            });
        }
    }

    @UiThreadTest
    public void testDependencies() {
        for (iteration = 0; iteration < ITERATIONS_COUNT; iteration++) {
            initLayout();
            layout.push(R.layout.view1);
            layout.push(R.layout.view2);
            layout.push(R.layout.view2);
            assertEquals(0, layout.getFrozenCount());
            assertEquals(3, layout.getChildCount());
            try {
                layout.push(R.layout.view4);
                assertEquals(true, false);
            }
            catch (Throwable ignored) {
            }
        }
    }

    public void testFlipDuringPop() throws Throwable {
        for (iteration = 0; iteration < ITERATIONS_COUNT; iteration++) {
            runTestOnUiThread(new Runnable() {
                @Override
                public void run() {
                    actionHandler = delayedHandler;
                    initLayout();
                    layout.push(R.layout.view1);
                    layout.push(R.layout.view1);
                    layout.push(R.layout.view2);
                    layout.pop(layout.push(R.layout.view2));
                    assertEquals(0, layout.getFrozenCount());
                    assertEquals(4, layout.getChildCount());
                }
            });
            restartActivity();
            runTestOnUiThread(new Runnable() {
                @Override
                public void run() {
                    assertEquals(1, layout.getFrozenCount());
                    assertEquals(2, layout.getChildCount());
                }
            });
        }
    }

    @UiThreadTest
    public void testOnExit() throws Throwable {
        for (iteration = 0; iteration < ITERATIONS_COUNT; iteration++) {
            initLayout();
            layout.push(R.layout.view1);
            View view2 = layout.push(R.layout.view2);
            layout.pop(view2);
            assertEquals(view2, exit[0]);
        }
    }

    @UiThreadTest
    public void testActionExecution() throws Throwable {
        for (iteration = 0; iteration < ITERATIONS_COUNT; iteration++) {
            final ArrayList<Pair<ActionType, View>> actions = new ArrayList<>();
            actionHandler = new ActionHandler() {
                @Override
                public void onStackAction(ActionType action1, View view1, Runnable onActionEnd) {
                    actions.add(new Pair<>(action1, view1));
                    onActionEnd.run();
                }
            };
            initLayout();
            View view1 = layout.push(R.layout.view1);
            // stack: view1
            assertTrue(actions.contains(new Pair<>(ActionType.PUSH_IN, view1)));
            assertEquals(1, actions.size());

            actions.clear();
            View view1_2 = layout.push(R.layout.view1);
            // stack: view1, view1_2
            assertTrue(actions.contains(new Pair<>(ActionType.PUSH_OUT, view1)));
            assertTrue(actions.contains(new Pair<>(ActionType.PUSH_IN, view1_2)));
            assertEquals(2, actions.size());

            actions.clear();
            layout.pop(view1_2);
            // stack: view1
            assertTrue(actions.contains(new Pair<>(ActionType.POP_OUT, view1_2)));
            view1 = findViewByAction(actions, ActionType.POP_IN);
            assertTrue(actions.contains(new Pair<>(ActionType.POP_IN, view1)));
            assertEquals(2, actions.size());

            actions.clear();
            View view1_3 = layout.replace(R.layout.view1);
            // stack: view1_3
            assertTrue(actions.contains(new Pair<>(ActionType.REPLACE_OUT, view1)));
            assertTrue(actions.contains(new Pair<>(ActionType.REPLACE_IN, view1_3)));
        }
    }

    private View findViewByAction(ArrayList<Pair<ActionType, View>> actions, ActionType popIn) {
        for (Pair<ActionType, View> pair : actions) {
            if (pair.first == popIn)
                return pair.second;
        }
        return null;
    }
}
