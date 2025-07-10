package com.tama.collectjfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

import java.util.Arrays;

public class ObservableSetFX {
    public static void main(String[] args) {
        ObservableSet<String> set = FXCollections.observableSet();
        ObservableSet<String> item = FXCollections.observableSet();
        ObservableSet<String> member = FXCollections.observableSet();

        // Adding elements to set
        set.add("Toshiba");
        set.add("Lenovo");
        set.add("Axioo");
        System.out.println("\nSet item: " + set);

        // Adding multiple elements
        set.addAll(Arrays.asList("Toshiba", "Asus", "Acer", "Dell", "Apple", "Zyrex"));
        System.out.println("\nUpdate item: " + set);

        // Show all items in set
        for (String element : set) {
            System.out.println("Element: " + element);
        }

        // Remove item by value
        set.remove("Asus");
        System.out.println("\nRemove Asus: " + set);

        // New Set
        item.addAll(Arrays.asList("Nokia", "Samsung", "Apple", "Xiaomi", "Infinix", "Oppo"));

        // Merge two sets
        member.addAll(item);
        member.addAll(set);
        System.out.println("\nMerge two sets: " + member);
    }
}