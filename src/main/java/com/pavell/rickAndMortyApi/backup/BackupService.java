package com.pavell.rickAndMortyApi.backup;

import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BackupService {
//    private final static String PATH =
//    private final static String DUMP =
//            "docker exec -i postgres_db /bin/bash -c \"PGPASSWORD=password pg_dump --username user rick_and_morty \" > /home/user1/IdeaProjects/Synchroniser/src/main/resources/backup/dump.sql\n";
//    private final static String RESTORE = "";

    public void doBackup() throws IOException {
        Runtime.getRuntime().exec("docker exec -i postgres_db /bin/bash -c \"PGPASSWORD=password pg_dump --username user rick_and_morty \" > /home/user1/IdeaProjects/Synchroniser/src/main/resources/backup/dump.sql\n");
    }

}
