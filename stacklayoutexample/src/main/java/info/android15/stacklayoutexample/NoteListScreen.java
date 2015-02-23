package info.android15.stacklayoutexample;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import stacklayout.view.ViewStack;

import static stacklayout.util.ViewFn.findParent;

public class NoteListScreen extends FrameLayout {

    @InjectView(R.id.saveCounter) TextView saveCounter;
    @InjectView(R.id.restoreCounter) TextView restoreCounter;
    @InjectView(R.id.listView) ListView listView;
    private ViewStack viewStack;

    public NoteListScreen(Context context) {
        super(context);
    }

    public NoteListScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoteListScreen(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            ButterKnife.inject(this);
            refresh();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        viewStack = findParent(this, ViewStack.class);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        increase(saveCounter);
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        post(new Runnable() {
            @Override
            public void run() {
                increase(restoreCounter);
            }
        });
    }

    public void refresh() {
        listView.setAdapter(new ArrayAdapter<>(getContext(), R.layout.item, R.id.text, Model.getCurrentSet()));
    }

    @OnClick(R.id.buttonDrawer)
    void onButtonDrawer() {
        ((MainActivity)getContext()).onButtonDrawer();
    }

    @OnItemClick(R.id.listView)
    void onItemClick(int position) {
        NoteScreen noteScreen = viewStack.push(R.layout.screen_note);
        noteScreen.setText(Model.getCurrentSet()[position].text);
    }

    private void increase(TextView counter) {
        String str = counter.getText().toString();
        counter.setText(str.length() == 0 ? "1" : Integer.toString(Integer.parseInt(str) + 1));
    }
}
