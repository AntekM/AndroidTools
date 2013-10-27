package com.mysliborski.tools.data;

import java.io.Serializable;

/**
 * Created by antonimysliborski on 01/10/2013.
 */
public class ValueHolder<T> implements Serializable {

    private T value;

    public ValueHolder() {}

    public ValueHolder(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
