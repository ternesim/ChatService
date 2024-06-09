package edu.school21.spring.service.config;

import edu.school21.sockets.server.Rest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
@ComponentScan("edu.school21")
public class TestApplicationConfig {

    @Value("${db.user}")
    String DB_USER;

    @Value("${db.password}")
    String DB_PASS;

    @Value("${db.url}")
    String DB_URL;

    @Value("${db.driver.name}")
    String DB_DRIVE_CLASS_NAME;

    @Bean
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        builder.setType(EmbeddedDatabaseType.HSQL);
        builder.addScript("/schema.sql");
        builder.addScript("/data.sql");
        return builder.build();
    }

    @Bean
    public Rest server() {
        return new Rest();
    }

}
