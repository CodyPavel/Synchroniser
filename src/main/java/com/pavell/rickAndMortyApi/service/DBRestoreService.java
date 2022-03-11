package com.pavell.rickAndMortyApi.service;

import net.lingala.zip4j.exception.ZipException;

import java.io.IOException;
import java.sql.SQLException;

public interface DBRestoreService {

    public boolean doRestore() throws SQLException, IOException, ClassNotFoundException, ZipException;

}
