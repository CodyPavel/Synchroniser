package com.pavell.rickAndMortyApi.backup;

import com.github.ludoviccarretti.services.PostgresqlExportService;
import com.github.ludoviccarretti.services.PostgresqlImportService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

import static com.pavell.rickAndMortyApi.backup.Backup4jConfig.getProperties;

@Component
public class BackupService {

    @Scheduled(cron = "0 0 8-10 * * *")
    public void doBackup() throws SQLException, IOException, ClassNotFoundException {
        PostgresqlExportService postgresqlExportService = new PostgresqlExportService(getProperties());
        postgresqlExportService.export();
    }

    public boolean doRestore() throws SQLException, ClassNotFoundException, IOException {
        String sql = new String(Files.readAllBytes(Paths.get(System.getProperty("user.home") + "/IdeaProjects/Synchroniser/9_2_2022_16_11_21_rick_and_morty_database_dump.sql")));

        boolean res = PostgresqlImportService.builder()
                .setSqlString(sql)
                .setJdbcConnString("jdbc:postgresql://localhost:5432/rick_and_morty")
                .setUsername("user")
                .setPassword("password")
                .setDeleteExisting(true)
                .setDropExisting(true)
                .importDatabase();

        return res;
    }
}