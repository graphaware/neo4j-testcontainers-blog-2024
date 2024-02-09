package com.graphaware.config.plugins;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.*;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static com.graphaware.Versions.NEO4J_VERSION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@Tag("generic")
class GenericContainerPluginsIT {

    @Container
    private static final GenericContainer genericContainer = new GenericContainer<>(DockerImageName.parse("neo4j:" + NEO4J_VERSION))
        .withEnv("NEO4J_AUTH", "neo4j/password")
        .withEnv("NEO4JLABS_PLUGINS", "[\"apoc\"]")
        .withFileSystemBind("target/neo4j-module-basics-all.jar", "/plugins/neo4j-module-basics-all.jar", BindMode.READ_WRITE)
        .withExposedPorts(7687);

    @Test
    void load_neo4jlabs_plugins() {
        AuthToken authToken = AuthTokens.basic("neo4j", "password");
        String boltUrl = getBoltUrl();
        System.out.println(boltUrl);
        try (
            Driver driver = GraphDatabase.driver(boltUrl, authToken);
            Session session = driver.session();
        ) {
            final List<org.neo4j.driver.Record> result = session.run("SHOW PROCEDURES WHERE name STARTS WITH 'apoc'").list();
            assertThat(result).hasSizeGreaterThan(0);
        }
    }

    @Test
    void load_generic_plugin() {
        AuthToken authToken = AuthTokens.basic("neo4j", "password");
        String boltUrl = getBoltUrl();

        try (
            Driver driver = GraphDatabase.driver(boltUrl, authToken);
            Session session = driver.session();
        ) {
            session.run("CREATE (:Person)-[:INCOMING]->(:Movie {id:1})-[:OUTGOING]->(:Person)");
            var record = session.run("MATCH (u:Movie {id:1}) CALL example.getRelationshipTypes(u) YIELD outgoing, incoming RETURN outgoing, incoming").single();

            assertEquals(1,record.get("outgoing").asInt());
            assertEquals(1, record.get("incoming").asInt());
        }
    }


    @NotNull
    private String getBoltUrl() {
        return "bolt://" + genericContainer.getHost() + ":" + genericContainer.getMappedPort(7687);
    }

}
