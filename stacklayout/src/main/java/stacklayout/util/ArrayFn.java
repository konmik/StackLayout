package stacklayout.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ArrayFn {

    public interface Predicate<T> {
        boolean apply(T t);
    }

    public interface Converter<T1, T2> {
        T2 convert(T1 t1);
    }

    public static <T> ArrayList<T> filter(Iterable<T> iterable, Predicate<T> predicate) {
        ArrayList<T> result = new ArrayList<>();
        for (T t : iterable)
            if (predicate.apply(t))
                result.add(t);
        return result;
    }

    public static <T1, T2> ArrayList<T2> map(Iterable<T1> iterable, Converter<T1, T2> converter) {
        ArrayList<T2> result = new ArrayList<>();
        for (T1 t : iterable)
            result.add(converter.convert(t));
        return result;
    }

    public static <T> ArrayList<T> join(Collection<T> collection1, Collection<T> collection2) {
        ArrayList<T> result = new ArrayList<>(collection1);
        result.addAll(collection2);
        return result;
    }

    public static <T> ArrayList<T> reverse(List<T> list) {
        ArrayList<T> result = new ArrayList<>(list.size());
        for (int i = list.size() - 1; i >= 0; i--)
            result.add(list.get(i));
        return result;
    }

    public static <T> void addDistinct(Collection<T> collection, T item) {
        if (!collection.contains(item))
            collection.add(item);
    }

    public static <T> void addDistinct(Collection<T> collection, Iterable<T> items) {
        for (T item : items)
            addDistinct(collection, item);
    }
}
