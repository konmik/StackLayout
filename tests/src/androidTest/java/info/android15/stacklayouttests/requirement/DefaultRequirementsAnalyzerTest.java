package info.android15.stacklayouttests.requirement;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

import stacklayout.requirement.DefaultRequirementsAnalyzer;
import stacklayout.requirement.RequiredViews;

public class DefaultRequirementsAnalyzerTest extends InstrumentationTestCase {
    DefaultRequirementsAnalyzer analyzer = new DefaultRequirementsAnalyzer();

    class View1 extends View {
        public View1(Context context) {
            super(context);
        }
    }

    @RequiredViews(View1.class)
    class View2 extends View{
        public View2(Context context) {
            super(context);
        }
    }

    @RequiredViews(View2.class)
    class View3 extends View {
        public View3(Context context) {
            super(context);
        }
    }

    @RequiredViews(View.class)
    class ViewDependentOnBaseViewX extends View {
        ViewDependentOnBaseViewX(Context context) {
            super(context);
        }
    }

    @RequiredViews({View1.class, ViewDependentOnBaseViewX.class})
    class ViewDoubleDependenciesXY extends View {
        ViewDoubleDependenciesXY(Context context) {
            super(context);
        }
    }

    public void testGetDependencyCount() throws Exception {
        assertEquals(0, analyzer.getRequirementCount(new ArrayList<Class>(), Object.class));
        assertEquals(1, analyzer.getRequirementCount(Arrays.asList(new Class[]{View1.class}), View2.class));
        assertEquals(2, analyzer.getRequirementCount(Arrays.asList(new Class[]{View1.class, View1.class, View2.class}), View3.class));
        assertEquals(3, analyzer.getRequirementCount(Arrays.asList(new Class[]{View1.class, View1.class, View2.class, View1.class}), View3.class));
        assertEquals(1, analyzer.getRequirementCount(Arrays.asList(new Class[]{View1.class}), ViewDependentOnBaseViewX.class));
        assertEquals(2, analyzer.getRequirementCount(Arrays.asList(new Class[]{View1.class, ViewDependentOnBaseViewX.class}), ViewDoubleDependenciesXY.class));
    }
}
