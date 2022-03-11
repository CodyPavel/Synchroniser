package com.pavell.rickAndMortyApi.backup;

import com.github.ludoviccarretti.options.PropertiesOptions;
import com.github.ludoviccarretti.services.PostgresqlExportService;
import com.github.ludoviccarretti.services.PostgresqlImportService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Properties;

@Component
public class BackupService {
    @Value("${db.password}")
    private String dbPassword;

    @Value("${db.name}")
    private String dbName;

    @Value("${db.user}")
    private String dbUser;


    public void doBackup() throws SQLException, IOException, ClassNotFoundException {
        PostgresqlExportService postgresqlExportService = new PostgresqlExportService(getProperties());
        postgresqlExportService.export();
    }

    //TODO
    public boolean doRestore(String fileName) throws SQLException, ClassNotFoundException, IOException {
        String sql = new String(Files.readAllBytes(Paths.get(fileName)));

        boolean res = PostgresqlImportService.builder()
                .setDatabase(dbName)
                .setSqlString(sql)
                .setUsername(dbUser)
                .setPassword(dbPassword)
                .setDeleteExisting(true)
                .setDropExisting(true)
                .importDatabase();

        return res;
    }

    private Properties getProperties() {
        Properties properties = new Properties();
        properties.setProperty(PropertiesOptions.DB_NAME, dbName);
        properties.setProperty(PropertiesOptions.DB_USERNAME, dbUser);
        properties.setProperty(PropertiesOptions.DB_PASSWORD, dbPassword);

        properties.setProperty(PropertiesOptions.TEMP_DIR, new File(System.getProperty("user.home") + "/IdeaProjects/Synchroniser").getPath());

        properties.setProperty(PropertiesOptions.PRESERVE_GENERATED_ZIP, "true");

        return properties;
    }
}