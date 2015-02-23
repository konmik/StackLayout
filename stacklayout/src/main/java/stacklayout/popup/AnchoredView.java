package stacklayout.popup;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;

import info.android15.stacklayout.R;
import stacklayout.requirement.RequiredViews;
import stacklayout.view.ViewStack;

import static stacklayout.util.ViewFn.findParent;
import static stacklayout.util.ViewFn.getRelativeRect;

/**
 * This is a {@link stacklayout.popup.PopupView} extension that aligns its content to
 * a given view that is a part of an underlying view in {@link stacklayout.view.StackLayout}.
 */
@RequiredViews(AnchoredView.AnchorProvider.class)
public class AnchoredView extends PopupView implements ViewTreeObserver.OnPreDrawListener {

    private static final Rect INVALID_RECT = new Rect(1, 1, -1, -1);

    public interface AnchorProvider {
        View getAnchor(AnchoredView anchoredView);
    }

    private View anchor;
    private Rect anchorRect = new Rect(INVALID_RECT);
    private int anchorCornerGravity;
    private int contentCornerGravity;

    public AnchoredView(Context context) {
        this(context, null);
    }

    public AnchoredView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnchoredView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AnchoredView);
        anchorCornerGravity = a.getInt(R.styleable.AnchoredView_anchor_corner, 0);
        contentCornerGravity = a.getInt(R.styleable.AnchoredView_popup_corner, 0);
        a.recycle();
    }

    public void setAnchorCornerGravity(int anchorCornerGravity) {
        this.anchorCornerGravity = anchorCornerGravity;
        anchorRect.set(INVALID_RECT);
        requestLayout();
    }

    public void setContentCornerGravity(int contentCornerGravity) {
        this.contentCornerGravity = contentCornerGravity;
        anchorRect.set(INVALID_RECT);
        requestLayout();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        anchor = findParent(this, ViewStack.class).findBackView(this, AnchorProvider.class).getAnchor(this);
        getViewTreeObserver().addOnPreDrawListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnPreDrawListener(this);
    }

    @Override
    public boolean onPreDraw() {
        Rect rect = getRelativeRect(this, anchor);
        if (anchorRect.equals(rect))
            return true;
        anchorRect.set(rect);

        onUpdateLayoutParams();
        return true;
    }

    /**
     * TODO: make a "same size" mode with FILL_VERTICAL or something.
     * TODO: ability to rely on START & END in addition to LEFT & RIGHT (starting with Android 4.2).
     * TODO: a flag that will prevent view's content to spread out of a StackLayout's size + a flag that will prevent view's content to spread out of a wrap's size.
     * TODO: an option to show on the other side if there is not enough room on the current (show at the top in there is no room at the bottom, show on the left if there is no room at the right).
     */
    protected void onUpdateLayoutParams() {
        int anchorX = (anchorCornerGravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.LEFT ? anchorRect.left : anchorRect.right;
        int anchorY = (anchorCornerGravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.TOP ? anchorRect.top : anchorRect.bottom;

        View decorView = getDecorView();
        View contentView = getContentView();
        int paddingLeft = decorView == null ? 0 : contentView.getLeft();
        int paddingTop = decorView == null ? 0 : contentView.getTop();
        int paddingRight = decorView == null ? 0 : decorView.getWidth() - contentView.getWidth() - contentView.getLeft();
        int paddingBottom = decorView == null ? 0 : decorView.getHeight() - contentView.getHeight() - contentView.getTop();

        View alignView = decorView == null ? contentView : decorView;
        int width = alignView.getWidth();
        int height = alignView.getHeight();

        LayoutParams lp = (LayoutParams)alignView.getLayoutParams();

        if ((contentCornerGravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.LEFT)
            lp.leftMargin = anchorX - paddingLeft;
        else
            lp.leftMargin = anchorX - width + paddingRight;

        if ((contentCornerGravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.TOP)
            lp.topMargin = anchorY - paddingTop;
        else
            lp.topMargin = anchorY - height + paddingBottom;

        lp.rightMargin = getWidth() - lp.leftMargin - width;
        lp.bottomMargin = getHeight() - lp.topMargin - height;

        alignView.requestLayout();
    }
}
