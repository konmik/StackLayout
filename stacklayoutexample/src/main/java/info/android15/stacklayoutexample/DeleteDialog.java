package info.android15.stacklayoutexample;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import stacklayout.popup.PopupView;
import stacklayout.requirement.RequiredViews;
import stacklayout.view.ViewStack;

import static stacklayout.util.ViewFn.findParent;

@RequiredViews(DeleteDialog.DeleteDialogOwner.class)
public class DeleteDialog extends PopupView {

    @InjectView(R.id.query) TextView query;
    ViewStack viewStack;

    public interface DeleteDialogOwner {
        void onDeleteButtonOK(DeleteDialog dialog);
    }

    public DeleteDialog(Context context) {
        super(context);
    }

    public DeleteDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DeleteDialog(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        viewStack = findParent(this, ViewStack.class);
    }

    public void setQuery(String text) {
        query.setText(text);
    }

    @OnClick(R.id.buttonOK)
    void onButtonOK() {
        DeleteDialogOwner owner = viewStack.findBackView(this, DeleteDialogOwner.class);
        viewStack.pop(this);
        owner.onDeleteButtonOK(this);
    }

    @OnClick(R.id.buttonCancel)
    void onButtonCancel() {
        viewStack.pop(DeleteDialog.this);
    }
}
