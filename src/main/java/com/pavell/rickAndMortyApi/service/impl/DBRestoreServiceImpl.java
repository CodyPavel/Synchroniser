package com.pavell.rickAndMortyApi.service.impl;

import com.pavell.rickAndMortyApi.backup.BackupService;
import com.pavell.rickAndMortyApi.service.DBRestoreService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;

@Service
@Slf4j
@RequiredArgsConstructor
public class DBRestoreServiceImpl implements DBRestoreService {

    private BackupService backupService;

    @Override
    public boolean doRestore() throws SQLException, IOException, ClassNotFoundException {
//        "/IdeaProjects/Synchroniser/9_2_2022_16_11_21_rick_and_morty_database_dump.sql"
        //TODO find newest file reZip and restore
        String fileName  = "/IdeaProjects/Synchroniser/9_2_2022_16_11_21_rick_and_morty_database_dump.sql";

        return backupService.doRestore(fileName);
    }

}
