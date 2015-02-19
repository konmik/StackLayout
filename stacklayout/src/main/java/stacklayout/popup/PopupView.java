package stacklayout.popup;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import info.android15.stacklayout.R;
import stacklayout.helper.NoPaddingContainer;
import stacklayout.requirement.RequiredViews;
import stacklayout.view.ViewStack;

import static stacklayout.util.ViewFn.findParent;

@NoPaddingContainer
@RequiredViews(View.class)
public class PopupView extends FrameLayout {
    private int dimId = View.NO_ID;
    private int decorId = View.NO_ID;
    private int contentId = View.NO_ID;

    private View dimView;
    private ViewGroup decorView;
    private View contentView;

    public PopupView(Context context) {
        this(context, null);
    }

    public PopupView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PopupView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PopupView);
        dimId = a.getResourceId(R.styleable.PopupView_dim, View.NO_ID);
        decorId = a.getResourceId(R.styleable.PopupView_decor, View.NO_ID);
        contentId = a.getResourceId(R.styleable.PopupView_content, View.NO_ID);
        a.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        dimView = findViewById(dimId);
        decorView = (ViewGroup)findViewById(decorId);
        contentView = findViewById(contentId);
        if (dimView != null)
            dimView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    findParent(PopupView.this, ViewStack.class).pop(PopupView.this);
                }
            });
    }

    @Nullable
    public View getDimView() {
        return dimView;
    }

    @Nullable
    public ViewGroup getDecorView() {
        return decorView;
    }

    public View getContentView() {
        return contentView;
    }
}
