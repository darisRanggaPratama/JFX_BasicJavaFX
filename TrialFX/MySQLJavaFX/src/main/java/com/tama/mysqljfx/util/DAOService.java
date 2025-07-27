package com.tama.mysqljfx.util;

import java.sql.SQLException;
import java.util.List;

public interface DAOService<T> {
    List<T> fetchAll() throws SQLException, ClassNotFoundException;

    int addData(T object) throws SQLException, ClassNotFoundException;

}
