package com.pavell.rickAndMortyApi.service.impl;

import com.pavell.rickAndMortyApi.backup.BackupService;
import com.pavell.rickAndMortyApi.service.DBRestoreService;
import com.pavell.rickAndMortyApi.service.ZipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.exception.ZipException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;

@Service
@Slf4j
@RequiredArgsConstructor
public class DBRestoreServiceImpl implements DBRestoreService {

    private final BackupService backupService;
    private final ZipService zipService;
    private final FileStore fileStore;

    @Override
    public boolean doRestore() throws SQLException, IOException, ClassNotFoundException, ZipException {
        String latestZipDump = zipService.getLatestZipDumpFileName();
        zipService.unZipFile(latestZipDump);
        try {
            return backupService.doRestore(latestZipDump.replace("zip", "sql"));
        } catch (Exception e) {
            // TODO do restore from Amazon S3
//            fileStore.download()
        }
        return false;
    }
}
