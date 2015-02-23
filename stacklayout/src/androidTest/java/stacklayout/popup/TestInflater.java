package stacklayout.popup;

import android.view.LayoutInflater;
import android.view.View;

import info.android15.stacklayout.R;
import stacklayout.helper.WrappingInflater;

public class TestInflater implements WrappingInflater {
    private LayoutInflater layoutInflater;

    public TestInflater(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    @Override
    public View inflate(int layoutId) {
        View wrap = layoutInflater.inflate(layoutId, null);
        wrap.setTag(R.id.view_layout_id, layoutId);
        return wrap;
    }

    @Override
    public int getLayoutId(View wrap) {
        return (int)wrap.getTag(R.id.view_layout_id);
    }

    @Override
    public View unwrap(View wrap) {
        return wrap;
    }

    @Override
    public View getWrap(View view) {
        return view;
    }
}
