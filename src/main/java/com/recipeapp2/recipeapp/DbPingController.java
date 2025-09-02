package com.recipeapp2.recipeapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.sql.DataSource;
import java.sql.*;

@RestController
public class DbPingController {
    private final DataSource ds;

    public DbPingController(DataSource ds) {
        this.ds = ds;
    }

    @GetMapping("/db-ping")
    public String ping() throws Exception {
        try (Connection c = ds.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT 1")) {
            rs.next();
            return "DB OK: " + rs.getInt(1);
        }
    }
}
