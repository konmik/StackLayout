package stacklayout.test;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import stacklayout.util.ViewFn;

public class ViewFnTestView extends FrameLayout {
    public boolean isDrawn;
    public boolean onMeasured1BeforeOnDraw;
    public boolean layoutBeforeOnMeasured2;
    public boolean onMeasured2BeforeOnDraw;
    public int firstDrawSquareTop;
    private View square;

    public ViewFnTestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isDrawn)
            isDrawn = true;
        firstDrawSquareTop = (int)(square.getTop() + square.getTranslationY());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        square = getChildAt(0);
        ViewFn.onMeasured(this, new Runnable() {
            @Override
            public void run() {
                if (!isDrawn)
                    onMeasured1BeforeOnDraw = true;
            }
        });
        ViewFn.onMeasuredLayoutAnAnimate(this, new Runnable() {
            @Override
            public void run() {
                if (!isDrawn)
                    onMeasured1BeforeOnDraw = true;

                ((LayoutParams)square.getLayoutParams()).gravity = Gravity.BOTTOM;
                square.requestLayout();
            }
        }, new Runnable() {
            @Override
            public void run() {
                if (!isDrawn)
                    onMeasured2BeforeOnDraw = true;
                layoutBeforeOnMeasured2 = square.getTop() > 0;
                int top = square.getTop();
                square.setTranslationY(-top);
                square.animate().translationY(0).setDuration(10).start();
            }
        });
    }
}
