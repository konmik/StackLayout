package stacklayout.helper;

import android.view.View;

public interface WrappingInflater {
    View inflate(int layoutId);
    int getLayoutId(View wrap);
    View unwrap(View wrap);
    View getWrap(View view);
}
