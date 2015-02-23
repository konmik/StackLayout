package info.android15.stacklayoutexample;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import stacklayout.view.ViewStack;

import static stacklayout.util.ViewFn.findParent;

public class NoteScreen extends FrameLayout implements DeleteDialog.DeleteDialogOwner {

    @InjectView(R.id.text) TextView textView;

    ViewStack viewStack;

    public NoteScreen(Context context) {
        super(context);
    }

    public NoteScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoteScreen(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ButterKnife.inject(this);
        viewStack = findParent(this, ViewStack.class);
    }

    public void setText(String text) {
        textView.setText(text);
    }

    @OnClick(R.id.buttonBack)
    void onButtonBack() {
        viewStack.pop(this);
    }

    @OnClick(R.id.buttonDelete)
    void onButtonDelete() {
        DeleteDialog dialog = viewStack.push(R.layout.dialog_delete);
        dialog.setQuery("Note\n" + textView.getText().toString() + "\nwill be deleted.");
    }

    @Override
    public void onDeleteButtonOK(DeleteDialog dialog) {
        viewStack.pop(this);
    }
}
