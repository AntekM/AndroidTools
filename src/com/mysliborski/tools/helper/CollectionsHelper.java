package com.mysliborski.tools.helper;

import com.mysliborski.tools.data.HasId;

import java.util.*;

public class CollectionsHelper {

    /**
     * Filters list (only element that pass will be in resulting collection)
     *
     * @param originalList
     * @param filter
     * @return
     */
    public static <T> List<T> filter(List<T> originalList, Filter<T> filter) {
        List<T> filteredList = new ArrayList<T>();
        for (T element : originalList) {
            if (filter.pass(element)) {
                filteredList.add(element);
            }
        }
        return filteredList;
    }

    /**
     * Changes List into Map
     *
     * @param list
     * @param mapper
     * @return
     */
    public static <K, V> Map<K, V> map(List<V> list, Mapper<K, V> mapper) {
        Map<K, V> map = new HashMap<K, V>();
        for (V value : list) {
            map.put(mapper.keyFor(value), value);
        }
        return map;
    }

    /**
     * Changes List into Map
     *
     * @param list
     * @return
     */
    public static <V extends HasId> Map<Long, V> idMap(Collection<V> list) {
        Map<Long, V> map = new HashMap<Long, V>();
        if (list != null)
            for (V value : list) {
                map.put(value.getId(), value);
            }
        return map;
    }

    /**
     * Converts List of el. O into list of el. R
     *
     * @param original
     * @param mapper
     * @return
     */
    public static <O, R> List<R> convert(List<O> original, Mapper<R, O> mapper) {
        if (original != null) {
            List<R> resultList = new ArrayList<R>(original.size());
            for (O el : original) {
                resultList.add(mapper.keyFor(el));
            }
            return resultList;
        }
        return null;
    }

    /**
     * Returns first element of the list if list is not empty
     *
     * @param list
     * @return
     */
    public static <T> T getFirst(List<T> list) {
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public static <O, R> void fill(Collection<R> target, Collection<O> source, Converter<O, R> converter) {
        for (O el : source) {
            target.add(converter.convert(el));
        }
    }

    /**
     * Returns the first element from the list that passes a filter condition
     *
     * @param col
     * @param cond
     * @return
     */
    public static <X> X find(Collection<X> col, Filter<X> cond) {
        if (col != null) {
            for (X el : col) {
                if (cond.pass(el)) {
                    return el;
                }
            }
        }
        return null;
    }

    public interface Filter<T> {
        boolean pass(T element);
    }

    public interface Mapper<K, V> {
        K keyFor(V entry);
    }

    public interface Converter<O, R> {
        R convert(O orign);
    }
}
