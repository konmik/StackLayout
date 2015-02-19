package info.android15.stacklayouttests.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import stacklayout.requirement.RequiredViews;

@RequiredViews(View2.class)
public class View3 extends View {
    public View3(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
