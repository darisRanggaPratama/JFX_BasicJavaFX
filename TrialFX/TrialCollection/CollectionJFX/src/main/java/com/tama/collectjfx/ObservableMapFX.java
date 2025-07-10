package com.tama.collectjfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import java.util.HashMap;
import java.util.Map;

public class ObservableMapFX {
    public static void main(String[] args) {
        ObservableMap<String, Integer> map = FXCollections.observableHashMap();
        ObservableMap<String, Integer> item = FXCollections.observableHashMap();
        ObservableMap<String, Integer> member = FXCollections.observableHashMap();

        // Adding elements to map
        map.put("Toshiba", 1_5000_000);
        map.put("Lenovo", 12_000_000);
        map.put("Axioo", 8_000_000);
        System.out.println("\nMap item: " + map);

        // Adding multiple elements
        Map<String, Integer> tempMap = new HashMap<>();
        tempMap.put("Asus", 13000000);
        tempMap.put("Acer", 11000000);
        tempMap.put("Dell", 14000000);
        tempMap.put("Apple", 25000000);
        tempMap.put("Zyrex", 7000000);
        map.putAll(tempMap);
        System.out.println("\nUpdate item: " + map);

        // Show all items in map
        for (String key : map.keySet()) {
            System.out.println("Brand: " + key + ", Price: " + map.get(key));
        }

        // Remove item by key
        map.remove("Asus");
        System.out.println("\nRemove Asus: " + map);

        // New Map
        Map<String, Integer> phoneMap = new HashMap<>();
        phoneMap.put("Nokia", 3000000);
        phoneMap.put("Samsung", 8000000);
        phoneMap.put("Xiaomi", 4000000);
        phoneMap.put("Infinix", 2500000);
        phoneMap.put("Oppo", 5000000);
        item.putAll(phoneMap);

        // Merge two maps
        member.putAll(item);
        member.putAll(map);
        System.out.println("\nMerge two maps: " + member);
    }
}