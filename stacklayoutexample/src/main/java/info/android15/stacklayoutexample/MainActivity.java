package info.android15.stacklayoutexample;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;
import stacklayout.action.ActionHandler;
import stacklayout.action.ActionType;
import stacklayout.view.StackLayout;
import stacklayout.view.ViewStack;

public class MainActivity extends Activity {

    private static final int ANIMATION_DURATION = 400;

    @Optional @InjectView(R.id.drawerLayout) DrawerLayout drawerLayout;
    @InjectView(R.id.stackLayout) StackLayout stackLayout;
    private boolean isDestroyed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        stackLayout.set().setActionHandler(new ActionHandler() {
            @Override
            public void onStackAction(ActionType action, View view, final Runnable onActionEnd) {
                if (action == ActionType.POP_OUT || action == ActionType.REPLACE_OUT) {
                    view.animate().alpha(0).setDuration(ANIMATION_DURATION).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            onActionEnd.run();
                        }
                    }).start();
                }
                else if (action == ActionType.PUSH_IN) {
                    view.setAlpha(0);
                    view.animate().alpha(1).setDuration(ANIMATION_DURATION).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            onActionEnd.run();
                        }
                    }).start();
                }
                else
                    onActionEnd.run();
            }
        }).apply();

        if (savedInstanceState == null)
            stackLayout.push(R.layout.screen_note_list);

//        viewStack.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (isDestroyed)
//                    return;
//                Log.i(MainActivity.class.getSimpleName(), "-------------");
//                viewStack.report(new Printer() {
//                    @Override
//                    public void println(String s) {
//                        Log.i(MainActivity.class.getSimpleName(), "Stack - " + s);
//                    }
//                });
//                viewStack.postDelayed(this, 5000);
//            }
//        }, 5000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroyed = true;
    }

    public ViewStack getStackLayout() {
        return stackLayout;
    }

    public void onButtonDrawer() {
        if (drawerLayout != null)
            drawerLayout.openDrawer(Gravity.START);
    }

    public void closeDrawer() {
        if (drawerLayout != null)
            drawerLayout.closeDrawer(Gravity.START);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(Gravity.START))
            drawerLayout.closeDrawers();
        else if (!stackLayout.back())
            super.onBackPressed();
    }
}
