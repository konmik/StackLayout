package stacklayout.util;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.List;

public class ViewFn {
    public static <T> T findParent(View view, Class<T> clazz) {
        for (ViewParent parent = view.getParent(); parent != null; parent = parent.getParent())
            if (clazz.isInstance(parent))
                return clazz.cast(parent);
        return null;
    }

    public static List<View> getChildren(ViewGroup group) {
        List<View> children = new ArrayList<>();
        for (int i = 0, size = group.getChildCount(); i < size; i++)
            children.add(group.getChildAt(i));
        return children;
    }

    private static Rect baseRect = new Rect();
    private static Rect viewRect = new Rect();
    private static Rect resultRect = new Rect();

    // TODO: return rect not as an return value but as an argument
    public static Rect getRelativeRect(View base, View view) {
        base.getGlobalVisibleRect(baseRect);
        view.getGlobalVisibleRect(viewRect);
        resultRect.set(viewRect);
        resultRect.left -= baseRect.left;
        resultRect.top -= baseRect.top;
        resultRect.right -= baseRect.left;
        resultRect.bottom -= baseRect.top;
        return resultRect;
    }

    public static void onMeasured(final View view, final Runnable runnable) {
        if (view.getWidth() != 0 && view.getHeight() != 0) {
            runnable.run();
            return;
        }

        final ViewTreeObserver observer = view.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                runnable.run();
                if (observer.isAlive())
                    observer.removeOnPreDrawListener(this);
                return true;
            }
        });
    }

    public static void onMeasuredLayoutAnAnimate(final View view, final Runnable layout, final Runnable animation) {
        final boolean measured = view.getWidth() != 0 && view.getHeight() != 0;
        if (measured) {
            layout.run();
            view.requestLayout();
        }

        final ViewTreeObserver observer = view.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            boolean done1 = measured;

            @Override
            public boolean onPreDraw() {
                if (!done1) {
                    layout.run();
                    view.requestLayout();
                    done1 = true;
                    return false;
                }

                animation.run();
                if (observer.isAlive())
                    observer.removeOnPreDrawListener(this);
                return true;
            }
        });
        view.invalidate();
    }
}
