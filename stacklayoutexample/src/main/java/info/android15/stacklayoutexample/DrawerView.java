package info.android15.stacklayoutexample;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import butterknife.ButterKnife;
import butterknife.OnClick;
import stacklayout.view.ViewStack;

public class DrawerView extends FrameLayout {

    public DrawerView(Context context) {
        super(context);
    }

    public DrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);
    }

    @OnClick({R.id.set0, R.id.set1})
    void onButtonSet(View view) {
        Model.setCurrentSet(view.getId() == R.id.set0 ? 0 : 1);

        ViewStack stackLayout = ((MainActivity)getContext()).getStackLayout();
        stackLayout.replace(R.layout.screen_note_list);

        ((MainActivity)getContext()).closeDrawer();
    }
}
