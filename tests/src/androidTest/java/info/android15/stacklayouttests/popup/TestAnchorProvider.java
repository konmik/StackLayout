package info.android15.stacklayouttests.popup;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import stacklayout.popup.AnchoredView;

import static stacklayout.util.ViewFn.getRelativeRect;

public class TestAnchorProvider extends FrameLayout implements AnchoredView.AnchorProvider {

    Rect anchorRect;

    public TestAnchorProvider(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (anchorRect == null)
            anchorRect = new Rect(getRelativeRect(this, getChildAt(0)));
    }

    @Override
    public View getAnchor(AnchoredView anchoredView) {
        return getChildAt(0);
    }
}
