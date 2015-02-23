package stacklayout.requirement;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import static stacklayout.util.ArrayFn.addDistinct;

public class DefaultRequirementsAnalyzer implements RequirementsAnalyzer {
    @Override
    public int getRequirementCount(List<Class> stack, Class top) {
        List<Class<?>> requirements = getRequiredViewClasses(top);
        int index = stack.size();
        while (requirements.size() > 0) {
            Class clazz = stack.get(--index);
            removeSatisfiedDependencies(requirements, clazz);
            for (Class c : getRequiredViewClasses(clazz))
                addDistinct(requirements, c);
        }
        return stack.size() - index;
    }

    /**
     * This algorithm is used instead of {@link java.lang.annotation.Inherited} annotation because
     * there can exist more than one {@link stacklayout.requirement.RequiredViews} in the views
     * hierarchy.
     */
    private static List<Class<?>> getRequiredViewClasses(Class clazz) {
        List<Class<?>> list = new ArrayList<>();
        while (clazz != null && !clazz.equals(View.class)) {
            if (clazz.isAnnotationPresent(RequiredViews.class)) {
                for (Class c : ((RequiredViews)clazz.getAnnotation(RequiredViews.class)).value())
                    addDistinct(list, c);
            }
            clazz = clazz.getSuperclass();
        }
        return list;
    }

    private void removeSatisfiedDependencies(List<Class<?>> requirements, Class clazz) {
        for (int i = requirements.size() - 1; i >= 0; i--) {
            if (requirements.get(i).isAssignableFrom(clazz))
                requirements.remove(i);
        }
    }
}
