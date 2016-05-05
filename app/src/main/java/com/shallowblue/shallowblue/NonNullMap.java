package com.shallowblue.shallowblue;

import java.util.HashMap;

/**
 * Created by gauch on 5/4/2016.
 */
public class NonNullMap<K,V> extends HashMap<K,V> {
    @Override
    public V put(K key, V value) {
        if(key == null || value == null)
            throw new NullPointerException("Attempted to add a Null object to NonNullMap");
        return super.put(key,value);
    }
}
