package com.tama.collectjfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ObservableListFX {
    public static void main(String[] args) {
        ObservableList<String> list = FXCollections.observableArrayList();
        ObservableList<String> item = FXCollections.observableArrayList();
        ObservableList<String> member = FXCollections.observableArrayList();
        ObservableList<String> data;
        list.add("Toshiba");
        list.add("Lenovo");
        list.add("Axioo");
        System.out.println("\nList item: " + list);
        list.addAll("Asus", "Acer", "Dell", "Apple", "Zyrex");
        System.out.println("\nUpdate item: " + list);

        // Show all item in list and its index
        for (int i = 0; i < list.size(); i++) {
            System.out.println("Index " + i + ": " + list.get(i));
        }

        // Remove item by value
        list.remove("Asus");
        System.out.println("\nRemove by value: " + list);

        // Remove item by index
        list.remove(5);
        System.out.println("\nRemove by index: " + list);

        // Remove item from index 0 to 2
        list.remove(0, 2);
        System.out.println("\nRemove from index 0 to 2: " + list);

        // New List
        item.addAll("Nokia", "Samsung", "Apple", "Xiaomi", "Infinix", "Oppo");

        // Concat/Merge two list
        member.addAll(item);
        member.addAll(list);
        System.out.println("\nMerge two list: " + member);

        data = FXCollections.concat(item, list);
        System.out.println("\nConcat two list: " + data);



    }
}
