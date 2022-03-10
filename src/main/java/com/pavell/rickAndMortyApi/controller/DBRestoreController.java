package com.pavell.rickAndMortyApi.controller;

import com.pavell.rickAndMortyApi.service.DBRestoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping("/restoreDB")
@RequiredArgsConstructor
public class DBRestoreController {

    private final DBRestoreService dbRestoreService;

    @GetMapping
    public ResponseEntity<Boolean> doRestore() throws SQLException, IOException, ClassNotFoundException {
        boolean dbRestoredSuccessful = dbRestoreService.doRestore();

        return new ResponseEntity<>(dbRestoredSuccessful, HttpStatus.OK);
    }

}
