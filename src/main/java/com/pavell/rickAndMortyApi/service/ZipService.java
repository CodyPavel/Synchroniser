package com.pavell.rickAndMortyApi.service;

import net.lingala.zip4j.exception.ZipException;

public interface ZipService {

    public void unZipFile(String zipFile) throws ZipException;

    public String getLatestZipDump ();
}
