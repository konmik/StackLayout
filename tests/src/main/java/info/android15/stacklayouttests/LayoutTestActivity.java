package info.android15.stacklayouttests;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LayoutTestActivity extends Activity {

    @InjectView(R.id.container) public ViewGroup container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LayoutTestActivity.class.getSimpleName(), "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freezing_layout_test);
        ButterKnife.inject(this);
    }
}
