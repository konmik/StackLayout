package stacklayout.view;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;

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

    private ViewGroup layout;
    private WrappingInflater inflater;
    private Parceler parceler;
    private RequirementsAnalyzer analyzer;

    private ArrayList<Parcelable> parcelables = new ArrayList<>();

    public Freezer(ViewGroup layout, WrappingInflater inflater, Parceler parceler, RequirementsAnalyzer analyzer) {
        this.layout = layout;
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

    public void restore(Bundle bundle) {
        parcelables = bundle.getParcelableArrayList(FREEZER_KEY);
        for (Parcelable child : bundle.getParcelableArrayList(AUTO_FREEZE_KEY))
            layout.addView(parceler.unparcel(child, layout));
    }

    public void clear() {
        parcelables.clear();
    }

    public void freeze(int freezerIndex, int viewIndex) {
        View view = layout.getChildAt(viewIndex); // TODO: wrap/unwrap here?
        parcelables.add(freezerIndex, parceler.parcel(view));
        layout.removeView(view);
    }

    public View unfreeze(int freezerIndex, int viewIndex) {
        View view = parceler.unparcel(parcelables.remove(freezerIndex), layout);
        layout.addView(view, viewIndex);
        return view;
    }

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
            frozen.add(0, layout.getChildAt(freeze));
            freeze(freezerIndex, freeze);
        }
        return frozen;
    }

    public List<View> unfreezeBottom() {
        ArrayList<View> result = new ArrayList<>();
        List<Class> classes = getFrozenClasses();
        if (classes.size() > 0) {
            int required = analyzer.getRequirementCount(classes, classes.remove(classes.size() - 1)) + 1;
            int viewIndex = 0;
            int freezerIndex = parcelables.size() - required;
            while (required-- > 0)
                result.add(unfreeze(freezerIndex, viewIndex++));
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
