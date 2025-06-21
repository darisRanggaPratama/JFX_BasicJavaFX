package com.tama.basicjdbc.dao;

import javafx.collections.ObservableList;

public interface DAOInterface<E> {
    int addData(E data);
    int updateData(E data);
    int deleteData(E data);
    ObservableList<E> showData();
}
