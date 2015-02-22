package stacklayout.view;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import stacklayout.helper.Parceler;
import stacklayout.helper.WrappingInflater;
import stacklayout.requirement.RequirementsAnalyzer;
import stacklayout.util.ArrayFn;

import static stacklayout.util.ArrayFn.map;

public class Freezer {
    private static final String FREEZER_KEY = "parcelables";
    private static final String AUTO_FREEZE_KEY = "auto_freeze";

    private WrappingInflater inflater;
    private Parceler parceler;
    private RequirementsAnalyzer analyzer;

    private ArrayList<Parcelable> parcelables = new ArrayList<>();

    /**
     * @param inflater
     * @param parceler
     * @param analyzer
     */
    public Freezer(WrappingInflater inflater, Parceler parceler, RequirementsAnalyzer analyzer) {
        this.inflater = inflater;
        this.parceler = parceler;
        this.analyzer = analyzer;
    }

    public Bundle save(List<View> autoFreeze) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(FREEZER_KEY, new ArrayList<>(parcelables));
        bundle.putParcelableArrayList(AUTO_FREEZE_KEY, map(autoFreeze, new ArrayFn.Converter<View, Parcelable>() {
            @Override
            public Parcelable convert(View view) {
                return parceler.parcel(view);
            }
        }));
        return bundle;
    }

    /**
     * @param bundle
     * @return a list of views to restore
     */
    public List<View> restore(Bundle bundle) {
        parcelables = bundle.getParcelableArrayList(FREEZER_KEY);
        return map(bundle.getParcelableArrayList(AUTO_FREEZE_KEY), new ArrayFn.Converter<Parcelable, View>() {
            @Override
            public View convert(Parcelable parcelable) {
                return parceler.unparcel(parcelable);
            }
        });
    }

    public void clear() {
        parcelables.clear();
    }

    /**
     * @param children current children
     * @return a list of frozen views, top to bottom
     */
    public List<View> freezeBottom(List<View> children) {
        List<Class> classes = map(children, new ArrayFn.Converter<View, Class>() {
            @Override
            public Class convert(View view) {
                return inflater.unwrap(view).getClass();
            }
        });
        int required = analyzer.getRequirementCount(classes, classes.remove(classes.size() - 1));
        int freeze = classes.size() - required;
        int freezerIndex = parcelables.size();

        ArrayList<View> frozen = new ArrayList<>(freeze);
        while (freeze-- > 0) {
            View view = children.get(freeze);
            frozen.add(view);
            parcelables.add(freezerIndex, parceler.parcel(view));
        }
        return frozen;
    }

    /**
     * @return a list of unfrozen views, bottom to top
     */
    public List<View> unfreezeBottom() {
        ArrayList<View> result = new ArrayList<>();
        List<Class> classes = getFrozenClasses();
        if (classes.size() > 0) {
            int required = analyzer.getRequirementCount(classes, classes.remove(classes.size() - 1)) + 1;
            int freezerIndex = parcelables.size() - required;
            while (required-- > 0)
                result.add(parceler.unparcel(parcelables.remove(freezerIndex)));
        }
        return result;
    }

    public List<Class> getFrozenClasses() {
        return map(parcelables, new ArrayFn.Converter<Parcelable, Class>() {
            @Override
            public Class convert(Parcelable parcelable) {
                return parceler.getClass(parcelable);
            }
        });
    }

    public int size() {
        return parcelables.size();
    }
}
