package stacklayout.helper;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import stacklayout.requirement.RequirementsAnalyzer;
import stacklayout.util.ArrayFn;

import static stacklayout.util.ArrayFn.map;

/**
 * This is a default implementation of {@link stacklayout.helper.Freezer} interface.
 * It uses {@link stacklayout.helper.WrappingInflater} to instantiate views and
 * {@link stacklayout.requirement.RequirementsAnalyzer} to analyze view's dependencies.
 * {@link stacklayout.helper.Parceler} is used to parcel and unparcel views.
 */
public class DefaultFreezer implements Freezer {
    private static final String FREEZER_KEY = "parcelables";
    private static final String AUTO_FREEZE_KEY = "auto_freeze";

    private WrappingInflater inflater;
    private RequirementsAnalyzer analyzer;
    private Parceler parceler;

    private ArrayList<Parcelable> parcelables = new ArrayList<>();

    public DefaultFreezer(WrappingInflater inflater, RequirementsAnalyzer analyzer) {
        this.inflater = inflater;
        this.analyzer = analyzer;
        parceler = new Parceler(inflater);
    }

    @Override
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

    @Override
    public List<View> restore(Bundle bundle) {
        parcelables = bundle.getParcelableArrayList(FREEZER_KEY);
        return map(bundle.getParcelableArrayList(AUTO_FREEZE_KEY), new ArrayFn.Converter<Parcelable, View>() {
            @Override
            public View convert(Parcelable parcelable) {
                return parceler.unparcel(parcelable);
            }
        });
    }

    @Override
    public void clear() {
        parcelables.clear();
    }

    @Override
    public List<View> freezeBottom(List<View> stack) {
        List<Class> classes = map(stack, new ArrayFn.Converter<View, Class>() {
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
            View view = stack.get(freeze);
            frozen.add(view);
            parcelables.add(freezerIndex, parceler.parcel(view));
        }
        return frozen;
    }

    @Override
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

    private List<Class> getFrozenClasses() {
        return map(parcelables, new ArrayFn.Converter<Parcelable, Class>() {
            @Override
            public Class convert(Parcelable parcelable) {
                return parceler.getClass(parcelable);
            }
        });
    }

    @Override
    public int size() {
        return parcelables.size();
    }
}
