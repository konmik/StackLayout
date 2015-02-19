package info.android15.stacklayouttests.view;

import android.app.Activity;
import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class BaseActivityTest<ActivityType extends Activity> extends ActivityInstrumentationTestCase2<ActivityType> {

    public BaseActivityTest(Class<ActivityType> activityClass) {
        super(activityClass);
    }

    protected void initActivity() {
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getActivity();
        try {
            runTestOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initActivity();
                }
            });
        }
        catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    protected void sleep(int time) {
        try {
            Thread.sleep(time);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected void restartActivity() throws Throwable {
        final Bundle state = new Bundle();
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                getInstrumentation().callActivityOnSaveInstanceState(getActivity(), state);
                getActivity().finish();
            }
        });
        setActivity(null);
        getActivity();
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                initActivity();
                getInstrumentation().callActivityOnRestoreInstanceState(getActivity(), state);
            }
        });
    }

    protected interface Condition {
        boolean isTrue();
    }

    /**
     * Waits for condition to become true.
     * @param condition to run on UI thread
     * @throws Throwable
     */
    protected void waitFor(final Condition condition) throws Throwable {
        long time1 = System.nanoTime();
        final AtomicBoolean conditionIsTrue = new AtomicBoolean(false);
        final AtomicInteger counter = new AtomicInteger();
        while (!conditionIsTrue.get() && System.nanoTime() < time1 + 1000 * 1000000) {
            runTestOnUiThread(new Runnable() {
                @Override
                public void run() {
                    counter.incrementAndGet();
                    conditionIsTrue.set(condition.isTrue());
                }
            });
            sleep(50);
        }
        Log.e(getClass().getSimpleName(), String.format("waitFor %s: %d, counter: %d", condition.getClass(), (System.nanoTime() - time1) / 1000000, counter.get()));
        assertTrue(conditionIsTrue.get());
    }
}
