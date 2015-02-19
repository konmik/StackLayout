package stacklayout.view;

import android.support.annotation.Nullable;
import android.view.View;

public interface ViewStack {
    <T extends View> T push(int layoutId);
    void pop(View top);
    <T extends View> T replace(int layoutId);
    @Nullable <T> T findBackView(View view, Class<T> backViewClass);
}
