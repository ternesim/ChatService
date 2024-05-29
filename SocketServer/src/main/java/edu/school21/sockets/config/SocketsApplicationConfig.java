package edu.school21.sockets.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@ComponentScan("edu.school21.sockets")
@PropertySource("classpath:db.properties")
public class SocketsApplicationConfig {

    @Value("${db.user}")
    String DB_USER;

    @Value("${db.password}")
    String DB_PASS;

    @Value("${db.url}")
    String DB_URL;

    @Value("${db.driver.name}")
    String DB_DRIVE_CLASS_NAME;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DataSource hikariDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(DB_URL);
        config.setUsername(DB_USER);
        config.setPassword(DB_PASS);
        config.setDriverClassName(DB_DRIVE_CLASS_NAME);
        return new HikariDataSource(config);
    }
}
