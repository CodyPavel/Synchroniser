package com.pavell.rickAndMortyApi.config;

import com.pavell.rickAndMortyApi.backup.BackupService;
import com.pavell.rickAndMortyApi.service.CharacterService;
import com.pavell.rickAndMortyApi.service.EpisodeService;
import com.pavell.rickAndMortyApi.service.LocationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.sql.SQLException;

@Configuration
@EnableScheduling
public class SpringConfig {

    private LocationService locationService;
    private EpisodeService episodeService;
    private CharacterService characterService;
    private BackupService backupService;

    public SpringConfig(BackupService backupService,
                        LocationService locationService,
                        EpisodeService episodeService,
                        CharacterService characterService) {
        this.backupService = backupService;
        this.locationService = locationService;
        this.episodeService = episodeService;
        this.characterService = characterService;
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Scheduled(cron = "0 0 8-10 * * *")
    private void getBackupJob() throws SQLException, IOException, ClassNotFoundException {
        backupService.doBackup();
    }

    @Scheduled(cron = "0 0 8-10 * * *")
    public void getSynchronizationRickApiJob() {
        locationService.loadData();
        episodeService.loadData();
        characterService.loadData();
    }

}
