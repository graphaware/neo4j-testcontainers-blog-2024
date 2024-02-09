package com.graphaware.config.plugins;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.*;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import static com.graphaware.Versions.NEO4J_VERSION;
import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@Tag("neo4j-module")
class ChuckNorrisTestContainerIT {

    @Container
    private static final Neo4jContainer<?> neo4jContainer = new Neo4jContainer("neo4j:" + NEO4J_VERSION)
        .withPlugins(MountableFile.forHostPath("target/neo4j-module-basics-all.jar"));

    @Test
    void quote_chuck_norris() {
        String boltUrl = neo4jContainer.getBoltUrl();
        AuthToken authToken = AuthTokens.basic("neo4j", "password");

        try (
            Driver driver = GraphDatabase.driver(boltUrl, authToken);
            Session session = driver.session();
        ) {
            // When
            String result = session.run("RETURN com.graphaware.config.plugins.chuckNorris() AS result").single().get("result").asString();

            // Then
            assertThat(result).containsIgnoringCase("Chuck");
        }
    }
}
