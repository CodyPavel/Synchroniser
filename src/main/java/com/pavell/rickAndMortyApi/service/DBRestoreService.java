package com.pavell.rickAndMortyApi.service;

import java.io.IOException;
import java.sql.SQLException;

public interface DBRestoreService {

    public boolean doRestore() throws SQLException, IOException, ClassNotFoundException;

}
