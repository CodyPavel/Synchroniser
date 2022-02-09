package com.pavell.rickAndMortyApi.backup;

import com.github.ludoviccarretti.options.PropertiesOptions;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Properties;


@Component
public class Backup4jConfig {

    public static Properties getProperties() {
        //required properties for exporting of db
        Properties properties = new Properties();
        properties.setProperty(PropertiesOptions.DB_NAME, "rick_and_morty");
        properties.setProperty(PropertiesOptions.DB_USERNAME, "user");
        properties.setProperty(PropertiesOptions.DB_PASSWORD, "password");

        //set the outputs temp dir
        properties.setProperty(PropertiesOptions.TEMP_DIR, new File(System.getProperty("user.home") + "/IdeaProjects/Synchroniser").getPath());

        properties.setProperty(PropertiesOptions.PRESERVE_GENERATED_ZIP, "true");

        return properties;
    }
}
