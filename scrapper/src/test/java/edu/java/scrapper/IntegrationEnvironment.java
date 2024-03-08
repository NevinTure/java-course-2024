package edu.java.scrapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.sql.SQLException;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.DirectoryResourceAccessor;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class IntegrationEnvironment {

    public static PostgreSQLContainer<?> POSTGRES;

    static {
        POSTGRES = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("scrapper")
            .withUsername("postgres")
            .withPassword("postgres");
        POSTGRES.start();

        runMigrations(POSTGRES);
    }

    private static void runMigrations(JdbcDatabaseContainer<?> c) {
        Path pathToChangeLog = new File(".")
            .toPath()
            .toAbsolutePath()
            .getParent()
            .getParent()
            .resolve("migrations");
        try (Database database = DatabaseFactory
            .getInstance()
            .findCorrectDatabaseImplementation(
                new JdbcConnection(c.createConnection(""))
            )) {
            Liquibase liquibase = new Liquibase(
                "master.xml",
                new DirectoryResourceAccessor(pathToChangeLog),
                database
            );
            liquibase.update(
                "",
                new OutputStreamWriter(System.out, StandardCharsets.UTF_8)
            );
        } catch (LiquibaseException | SQLException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }
}
