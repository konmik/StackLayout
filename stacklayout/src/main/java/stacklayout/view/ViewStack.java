package stacklayout.view;

import android.view.View;

/**
 * This interface is a generalization for manipulating a stack of views.
 * An activity normally should share this interface rather
 * than providing a direct reference to the underlying StackLayout.
 * It also is a good practice to search ViewStack.class instead of StackLayout.class
 * with {@link stacklayout.util.ViewFn#findParent}.
 */
public interface ViewStack {

    /**
     * Pushes a view on top of the stack.
     *
     * @param layoutId layout to inflate.
     * @param <T>      returning view type.
     * @return the root view of an inflated layout.
     */
    <T extends View> T push(int layoutId);

    /**
     * Pops a view from the stack.
     *
     * @param top the view to pop. This parameter is required for a stack integrity check.
     *            If this parameter is not equal to the top of the stack then an runtime
     *            exception will be thrown.
     */
    void pop(View top);

    /**
     * Replaces the current stack of views with one view.
     *
     * @param layoutId layout to inflate.
     * @param <T>      returning view type.
     * @return the root view of an inflated layout.
     */
    <T extends View> T replace(int layoutId);

    /**
     * Finds a view of a given type that is below of given view in the stack.
     * This is a typical operation when returning a result from a popup view.
     * If no view is found then a runtime exception will be thrown.
     *
     * @param view          A view to count back from.
     * @param backViewClass A view type to find.
     * @param <T>           A type of a view to return.
     * @return a view that has been found.
     */
    <T> T findBackView(View view, Class<T> backViewClass);

    /**
     * This is a shortcut for checking a stack size and popping of the first view that is not temporary.
     * This method should not be called from a view because it does not provide integrity check. Use this
     * method for a general activity back navigation, in example inside of overridden
     * {@link android.app.Activity#onBackPressed}. When going back from a view, use {@link ViewStack#pop} instead.
     *
     * @return True if a pop operation has been made.
     */
    boolean back();
}
