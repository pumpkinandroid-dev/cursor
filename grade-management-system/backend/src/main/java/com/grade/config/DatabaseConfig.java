package com.grade.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class DatabaseConfig {

    @Value("${spring.datasource.url}")
    private String databaseUrl;

    @Bean
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("database/init.sql"));
        populator.setContinueOnError(true);
        
        initializer.setDatabasePopulator(populator);
        initializer.setEnabled(true);
        
        return initializer;
    }

    @Bean
    public void initializeDatabase() {
        try {
            // 确保数据库文件存在
            String dbPath = databaseUrl.replace("jdbc:sqlite:", "");
            File dbFile = new File(dbPath);
            
            if (!dbFile.exists()) {
                // 创建数据库文件
                Connection connection = DriverManager.getConnection(databaseUrl);
                connection.close();
                System.out.println("数据库文件已创建: " + dbPath);
            }
        } catch (SQLException e) {
            System.err.println("初始化数据库失败: " + e.getMessage());
        }
    }
}
