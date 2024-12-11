package whpu.gomoku.gomokuserver.netty.runner;

import lombok.extern.slf4j.Slf4j;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.Server;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import whpu.gomoku.gomokuserver.data.common.GomokuConstant;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;

@Component
@EnableConfigurationProperties
@Slf4j
public class H2DatabaseRunner implements ApplicationRunner, ApplicationListener<ContextClosedEvent> {

    @Value("${spring.h2.console.port}")
    private int port;

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String username;

    private Server h2Server;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        h2Server = Server.createWebServer("-web", "-webAllowOthers", "-webPort", String.valueOf(port));
        h2Server.start();
        log.info("H2 database server started on port：{}", port);
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL(jdbcUrl);
        dataSource.setUser(username);
        try (Connection connection = dataSource.getConnection()) {
            ClassPathResource resource = new ClassPathResource(GomokuConstant.SCHEMA_SQL_POSITION);
            String schemaSql = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            connection.createStatement().execute(schemaSql);
            log.info("H2 database schema initialized.");
        } catch (Exception e) {
            log.error("Failed to initialize H2 database schema.", e);
        }
    }

    @Override
    public void onApplicationEvent(@NotNull ContextClosedEvent event) {
        if (h2Server != null) {
            try {
                h2Server.stop();
                log.info("H2 database server stopped.");
            } catch (Exception e) {
                log.error("Failed to stop H2 database server.", e);
            }
        }
    }
}
