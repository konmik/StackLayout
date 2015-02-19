package info.android15.stacklayouttests.popup;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;

import stacklayout.popup.AnchoredView;

import static stacklayout.util.ViewFn.getRelativeRect;

public class TestAnchoredView extends AnchoredView {

    Rect decorRect;
    Rect contentRect;

    public TestAnchoredView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (contentRect == null) {
            contentRect = new Rect(getRelativeRect(this, getContentView()));
            decorRect = getDecorView() == null ? null : new Rect(getRelativeRect(this, getDecorView()));
        }
    }
}
