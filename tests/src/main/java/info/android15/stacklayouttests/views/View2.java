package info.android15.stacklayouttests.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import stacklayout.requirement.RequiredViews;

@RequiredViews(View1.class)
public class View2 extends View {
    public View2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
