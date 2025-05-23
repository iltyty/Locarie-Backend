package com.locarie.backend;

import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LocarieBackendApplicationTests {

  @Autowired DataSource dataSource;

  @Test
  void contextLoads() {}

  @Test
  void testConnection() throws SQLException {
    System.out.println(dataSource.getConnection());
  }
}
