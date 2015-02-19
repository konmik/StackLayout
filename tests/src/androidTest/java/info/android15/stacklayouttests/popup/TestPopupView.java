package info.android15.stacklayouttests.popup;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import stacklayout.popup.PopupView;

public class TestPopupView extends PopupView {

    public boolean onDrawDone;
    public Rect dimRect;
    public Rect decorRect;
    public Rect contentRect;

    public TestPopupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!onDrawDone) {
            dimRect = getViewRect(getDimView());
            decorRect = getViewRect(getDecorView());
            contentRect = getViewRect(getContentView());
            onDrawDone = true;
        }
    }

    private Rect getViewRect(View view) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        return rect;
    }
}
