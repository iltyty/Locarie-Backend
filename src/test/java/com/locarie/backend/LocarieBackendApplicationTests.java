package com.locarie.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Driver;
import java.sql.SQLException;

@SpringBootTest
class LocarieBackendApplicationTests {

    @Autowired
    DataSource dataSource;

    @Test
    void contextLoads() {
    }

    @Test
    void testConnection() throws SQLException {
        System.out.println(dataSource.getConnection());
    }
}
