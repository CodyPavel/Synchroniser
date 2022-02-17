package com.pavell.rickAndMortyApi.backup;

import com.github.ludoviccarretti.services.PostgresqlExportService;
import com.github.ludoviccarretti.services.PostgresqlImportService;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.concurrent.ForkJoinPool;

import static com.pavell.rickAndMortyApi.backup.Backup4jConfig.getProperties;

@Component
public class BackupService {

    public void doBackup() throws SQLException, IOException, ClassNotFoundException {
        PostgresqlExportService postgresqlExportService = new PostgresqlExportService(getProperties());
        postgresqlExportService.export();
    }

    //TODO
    public static boolean doRestore() throws SQLException, ClassNotFoundException, IOException {
        String sql = new String(Files.readAllBytes(Paths.get(System.getProperty("user.home") + "/IdeaProjects/Synchroniser/9_2_2022_16_11_21_rick_and_morty_database_dump.sql")));

        boolean res = PostgresqlImportService.builder()
                .setDatabase("rick_and_morty")
                .setSqlString(sql)
                .setUsername("user")
                .setPassword("password")
                .setDeleteExisting(true)
                .setDropExisting(true)
                .importDatabase();

        return res;
    }

    public static void main(String[] args) {
        System.out.println(ForkJoinPool.getCommonPoolParallelism());
    }
}