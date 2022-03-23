package com.pavell.rickAndMortyApi.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
@RequiredArgsConstructor
class SystemRepository {

    private final JdbcTemplate jdbcTemplate;


    public String getCurrentDate() {
        String result = jdbcTemplate.queryForObject(
                "SELECT CURRENT_DATE FROM DUAL", new RowMapper<String>() {

                    @Override
                    public String mapRow(ResultSet rs, int rowNum)
                            throws SQLException {
                        return rs.getString(1);
                    }
                });
        return result;
    }

}

