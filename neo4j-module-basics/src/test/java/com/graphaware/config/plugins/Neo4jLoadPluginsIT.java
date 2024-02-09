package com.graphaware.config.plugins;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.*;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.containers.Neo4jLabsPlugin;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.lang.Record;
import java.util.List;

import static com.graphaware.Versions.NEO4J_VERSION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@Testcontainers
@Tag("neo4j-module")
class Neo4jLoadPluginsIT {

    @Container
    private static final Neo4jContainer neo4jContainer =
        new Neo4jContainer(DockerImageName.parse("neo4j:" + NEO4J_VERSION))
            .withLabsPlugins(Neo4jLabsPlugin.APOC, Neo4jLabsPlugin.GRAPH_DATA_SCIENCE)
            .withPlugins(MountableFile.forHostPath("target/neo4j-module-basics-all.jar"))
        ;

    @Test
    void load_neo4jlabs_plugins() {
        AuthToken authToken = AuthTokens.basic("neo4j", "password");
        String boltUrl = neo4jContainer.getBoltUrl();
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
        String boltUrl = neo4jContainer.getBoltUrl();
        System.out.println(boltUrl);
        try (
            Driver driver = GraphDatabase.driver(boltUrl, authToken);
            Session session = driver.session();
        ) {
            session.run("CREATE (:Person)-[:INCOMING]->(:Movie {id:1})-[:OUTGOING]->(:Person)");
            var record = session.run("MATCH (u:Movie {id:1}) CALL example.getRelationshipTypes(u) YIELD outgoing, incoming RETURN outgoing, incoming").single();

            assertEquals(1, record.get("outgoing").asInt());
            assertEquals(1, record.get("incoming").asInt());
        }
    }
}
