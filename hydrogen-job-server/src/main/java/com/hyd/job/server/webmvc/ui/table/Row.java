package com.hyd.job.server.webmvc.ui.table;

import java.util.HashMap;

public class Row<K, V> extends HashMap<K, V> {

    public Row<K, V> setAttribute(K k, V v) {
        put(k, v);
        return this;
    }
}
