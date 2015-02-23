package stacklayout.helper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import info.android15.stacklayout.R;

public class DefaultWrappingInflater implements WrappingInflater {

    private LayoutInflater layoutInflater;
    private ViewGroup parent;

    public DefaultWrappingInflater(LayoutInflater layoutInflater, ViewGroup parent) {
        this.layoutInflater = layoutInflater;
        this.parent = parent;
    }

    @Override
    public View inflate(int layoutId) {
        ViewGroup container = createContainer();
        View view = layoutInflater.inflate(layoutId, container, false);
        view.setTag(R.id.view_layout_id, layoutId);
        return addToContainer(container, view);
    }

    @Override
    public View unwrap(View wrap) {
        return ((ViewGroup)wrap).getChildAt(0);
    }

    @Override
    public View getWrap(View view) {
        return (View)view.getParent();
    }

    @Override
    public int getLayoutId(View container) {
        return (int)unwrap(container).getTag(R.id.view_layout_id);
    }

    private View addToContainer(ViewGroup container, View view) {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams)container.getLayoutParams();
        if (view.getClass().isAnnotationPresent(NoPaddingContainer.class)) {
            lp.topMargin = -parent.getPaddingTop();
            lp.rightMargin = -parent.getPaddingRight();
            lp.bottomMargin = -parent.getPaddingBottom();
            lp.leftMargin = -parent.getPaddingLeft();
        }
        container.addView(view);
        return container;
    }

    private FrameLayout createContainer() {
        FrameLayout container = new FrameLayout(parent.getContext());
        parent.addView(container);
        parent.removeView(container); // a hack to create default layout params
        ViewGroup.LayoutParams lp = container.getLayoutParams();
        lp.width = FrameLayout.LayoutParams.MATCH_PARENT;
        lp.height = FrameLayout.LayoutParams.MATCH_PARENT;
        container.setLayoutParams(lp);
        return container;
    }
}
