package com.pavell.rickAndMortyApi.service.impl;

import com.pavell.rickAndMortyApi.service.ZipService;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ZipServiceImpl implements ZipService {

    public static final String ZIP_DIRECTORY = System.getProperty("user.home") + "/IdeaProjects/Synchroniser/";
    public static final String ZIP_FILE_NAME = "rick_and_morty_database_dump.zip";


    @Override
    public void unZipFile(String zipFile) throws ZipException {
        ZipFile nameOfZipFile = new ZipFile(ZIP_DIRECTORY + zipFile);
        nameOfZipFile.extractAll(ZIP_DIRECTORY);
    }

    @Override
    public String getLatestZipDump() {
        File folder = new File(ZIP_DIRECTORY);
        List<File> files = Arrays.stream(Objects.requireNonNull(folder.listFiles()))
                .filter(file -> file.getName().contains(ZIP_FILE_NAME))
                .filter(File::isFile)
                .collect(Collectors.toList());

        List<LocalDateTime> localDateTimes = new ArrayList<>();

        files.forEach(file -> {
            List<Integer> fileDateTime = Arrays.stream(file.getName().replace("_" + ZIP_FILE_NAME, "").split("_"))
                    .map(Integer::parseInt).collect(Collectors.toList());
            localDateTimes.add(LocalDateTime.of(fileDateTime.get(2),
                    fileDateTime.get(1),
                    fileDateTime.get(0),
                    fileDateTime.get(3),
                    fileDateTime.get(4),
                    fileDateTime.get(5))
            );
        });
        Optional<LocalDateTime> max = localDateTimes.stream().max(LocalDateTime::compareTo);
        LocalDateTime localDateTimeMax = max.orElse(null);

        String nameOfZipFile = localDateTimeMax.getDayOfMonth() + "_" +
                localDateTimeMax.getMonth().getValue() + "_" +
                localDateTimeMax.getYear() + "_" +
                (localDateTimeMax.getHour() < 10 ? "0" + localDateTimeMax.getHour() : localDateTimeMax.getHour()) + "_" +
                (localDateTimeMax.getMinute() < 10 ? "0" + localDateTimeMax.getMinute() : localDateTimeMax.getMinute()) + "_" +
                (localDateTimeMax.getSecond() < 10 ? "0" + localDateTimeMax.getSecond() : localDateTimeMax.getSecond()) + "_" +
                ZIP_FILE_NAME;

        return nameOfZipFile;
    }
}
