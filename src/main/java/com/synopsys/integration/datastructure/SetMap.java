/*
 * integration-common
 *
 * Copyright (c) 2024 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.datastructure;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class SetMap<K, S> extends AbstractMap<K, Set<S>> {
    private final Map<K, Set<S>> map;

    public SetMap(final Map<K, Set<S>> map) {
        this.map = map;
    }

    private SetMap() {
        this(new HashMap<>());
    }

    public static final <K, S> SetMap<K, S> createDefault() {
        return new SetMap<>();
    }

    public static final <K, S> SetMap<K, S> createLinked() {
        LinkedHashMap<K, Set<S>> initializer = new LinkedHashMap<>();
        return new SetMap<>(initializer);
    }

    public Set<S> getValue(K key) {
        return map.get(key);
    }

    public Set<S> add(K key, S value) {
        Set<S> set = this.computeIfAbsent(key, ignoredKey -> new LinkedHashSet<>());
        set.add(value);
        return set;
    }

    public Set<S> addAll(K key, Set<S> value) {
        Set<S> set = this.computeIfAbsent(key, ignoredKey -> new LinkedHashSet<>());
        set.addAll(value);
        return set;
    }

    @Override
    public Set<S> put(final K key, final Set<S> value) {
        return map.put(key, value);
    }

    @Override
    public void putAll(final Map<? extends K, ? extends Set<S>> mapToAdd) {
        map.putAll(mapToAdd);
    }

    @Override
    public Set<Entry<K, Set<S>>> entrySet() {
        return map.entrySet();
    }

    public Map<K, Set<S>> getMap() {
        return map;
    }

    public void combine(SetMap<K, S> other) {
        other.forEach(this::addAll);
    }
}
