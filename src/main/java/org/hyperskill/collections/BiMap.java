package org.hyperskill.collections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BiMap<K, V> {

    private final Map<K, V> map = new HashMap<>();
    private final Map<V, K> inverseMap = new HashMap<>();

    public V put(K key, V value) {
        if (map.containsKey(key) || inverseMap.containsKey(value)) {
            throw new IllegalArgumentException("No duplicated Keys or Values allowed");
        }
        map.put(key, value);
        inverseMap.put(value, key);

        return value;
    }

    public void putAll(Map<K, V> map) {
        map.forEach(this::put);
    }

    public Set<V> values() {
        return inverseMap.keySet();
    }

    public V forcePut(K key, V value) {

        if (map.containsKey(key)) {
            V v = map.get(key);
            inverseMap.remove(v);
            map.remove(key);
        }

        if (inverseMap.containsKey(value)) {
            K k = inverseMap.get(value);
            map.remove(k);
            inverseMap.remove(value);
        }

        return put(key, value);
    }

    public BiMap<V, K> inverse() {
        BiMap<V, K> inverseBiMap = new BiMap<>();
        inverseBiMap.putAll(inverseMap);
        return inverseBiMap;
    }

    @Override
    public String toString() {
        return map.toString();
    }
}

