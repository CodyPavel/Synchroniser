package com.pavell.rickAndMortyApi.config.amazon;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BucketName {
    RICK_AND_MORTY_FILES("rick-and-morty-files");
    private final String bucketName;
}
