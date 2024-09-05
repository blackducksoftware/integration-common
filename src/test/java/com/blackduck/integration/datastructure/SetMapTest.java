package com.blackduck.integration.datastructure;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.blackduck.integration.datastructure.SetMap;

public class SetMapTest {
    @Test
    public void testLinkedMap() {
        String[] values = {"value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9", "value10"};
        SetMap<String, String> setMap = SetMap.createLinked();
        String key1 = "key_1";
        String key2 = "key_2";
        String key3 = "key_3";
        String key4 = "key_4";
        Set<String> key2_set = new LinkedHashSet<>();
        key2_set.add(values[1]);
        key2_set.add(values[2]);

        Set<String> key3_set = new LinkedHashSet<>();
        key3_set.add(values[3]);
        key3_set.add(values[4]);
        key3_set.add(values[5]);
        Map<String, Set<String>> key4_map = new HashMap<>();
        LinkedHashSet<String> key4_set = new LinkedHashSet<>();
        key4_set.add(values[6]);
        key4_set.add(values[7]);
        key4_set.add(values[8]);
        key4_set.add(values[9]);
        key4_map.put(key4, key4_set);

        setMap.add(key1, values[0]);
        setMap.put(key2, key2_set);
        setMap.addAll(key3, key3_set);
        setMap.putAll(key4_map);

        String[] actualValues = setMap.entrySet().stream()
                .map(Map.Entry::getValue)
                .flatMap(Set::stream)
                .collect(Collectors.toList())
                .toArray(new String[values.length]);

        assertTrue(Arrays.equals(values, actualValues));
    }

    @Test
    public void testGetMap() {
        Map<String, Set<String>> expectedMap = new HashMap<>();
        LinkedHashSet<String> set1 = new LinkedHashSet<>();
        LinkedHashSet<String> set2 = new LinkedHashSet<>();
        expectedMap.put("key1", set1);
        expectedMap.put("key2", set2);
        SetMap<String, String> setMap = new SetMap<>(expectedMap);
        assertEquals(expectedMap, setMap.getMap());
    }

    @Test
    public void testCombine() {
        SetMap<String, String> setMap1 = SetMap.createDefault();
        String key1 = "key1";
        String key2 = "key2";
        setMap1.add(key1, "value1");
        SetMap<String, String> setMap2 = SetMap.createDefault();
        setMap2.add(key2, "value2");
        setMap1.combine(setMap2);

        assertEquals(2, setMap1.size());
        assertEquals(setMap1.getValue(key1), setMap1.get(key1));
        assertEquals(setMap1.getValue(key2), setMap1.get(key2));
    }

}
