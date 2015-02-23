package stacklayout.helper;

import android.view.View;

/**
 * This interface describes a helper class for {@link stacklayout.view.StackLayout}.
 * The purpose of this class is to provide StackLayout with inflating capabilities.
 * Wrapping/unwrapping is used to control the general layout of StackLayout children.
 * Currently wrapping is used to layout PopupView in a fullscreen mode despite
 * a normal view will occupy only a part of the screen in the landscape mode.
 */
public interface WrappingInflater {
    /**
     * Inflate a given layout and return a root view from it.
     *
     * @param layoutId id of the layout to inflate,
     * @return the wrapped root view of the inflated layout.
     */
    View inflate(int layoutId);

    /**
     * Extracts a layout id from previously inflated view. There could be used several strategies for
     * saving and restoring of layout id, so this is out of scope for StackLayout's sanity.
     *
     * @param wrap a view that has been returned by {@link stacklayout.helper.WrappingInflater#inflate}.
     * @return an id of a layout that has been used for inflation.
     */
    int getLayoutId(View wrap);

    /**
     * Returns a View itself from a given wrap. There could be cases when someone will want to wrap a view inside
     * of several wraps, so wrapping/unwrapping is done inside of WrappingInflater.
     *
     * @param wrap a view that has been returned by {@link stacklayout.helper.WrappingInflater#inflate}.
     * @return an inflated view.
     */
    View unwrap(View wrap);

    /**
     * Returns a wrapping view of a given view.
     *
     * @param view a reference to a view that has been inflated with {@link stacklayout.helper.WrappingInflater}.
     * @return a wrapping view of the given view.
     */
    View getWrap(View view);
}
